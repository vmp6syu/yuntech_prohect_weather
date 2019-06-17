package com.example.speak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;




public class MainActivity extends AppCompatActivity {
    RequestQueue mQueue;
    TextView t ;
    Button b;
    Button b2;
    String placenaem[] =new String[22];
    String weamode[][][] =new String[22][15][15];  // 放狀態  開始時間~結束時間   職
    String weatherM[] =new String[15];  //天氣報導
    String unit[]=new String[15];
    String startTime[] =new String[15];
    int index_time;
    int index_place;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t=(TextView)findViewById(R.id.textView);
        b=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button2);
        index_place=0;
        index_time=0;
        mQueue = Volley.newRequestQueue( this);

        jsonParse();

        setweatherText();
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setplace();
                }
            }
        );

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
    }

    private void setweatherText(){

        t.setText("");
        t.append("時間範圍:"+startTime[index_time]+"\n\n");
        for(int i = 0;i<15;i++)
        {
            t.append(weatherM[i]+":"+weamode[index_place][i][index_time]+unit[i]+"\n\n");
        }

    }
    private  void  setTime(){

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
        dialog_list.setTitle("時間選擇");
        dialog_list.setItems(startTime, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "你選的時間範圍是" + startTime[which], Toast.LENGTH_SHORT).show();
                index_time=which;
                setweatherText();

            }
        });
        dialog_list.show();

    }

    private  void setplace(){

        AlertDialog.Builder dialog_list = new AlertDialog.Builder(MainActivity.this);
        dialog_list.setTitle("地點設定");
        dialog_list.setItems(placenaem, new DialogInterface.OnClickListener(){
            @Override

            //只要你在onClick處理事件內，使用which參數，就可以知道按下陣列裡的哪一個了
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "你想要看的地方是" + placenaem[which], Toast.LENGTH_SHORT).show();
                index_place=which;
                setweatherText();
            }
        });
        dialog_list.show();

//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//        dialog.setTitle("基本訊息對話按鈕");
//        dialog.setMessage("基本訊息對話功能介紹");
//        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "我還尚未了解",Toast.LENGTH_SHORT).show();
//            }
//
//        });
//        dialog.setPositiveButton("YES",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "我了解了",Toast.LENGTH_SHORT).show();
//            }
//
//        });
//        dialog.setNeutralButton("取消",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                // TODO Auto-generated method stub
//                Toast.makeText(MainActivity.this, "取消",Toast.LENGTH_SHORT).show();
//            }
//
//        });
//        dialog.show();

    }

    private void jsonParse() {

        String url = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-D0047-091?Authorization=CWB-00B52FCF-6271-4D9A-BE3F-9DCEDD770CE5";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject j1 =response.getJSONObject("records");
                            JSONArray j2 = j1.getJSONArray("locations");
                            JSONObject j3= j2.getJSONObject(0);
                            JSONArray jsonArray = j3.getJSONArray("location");

                            // i place
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject employee = jsonArray.getJSONObject(i);
                                String placename = employee.getString("locationName");
                                placenaem[i]=placename;
                                JSONArray weather = employee.getJSONArray("weatherElement");

                                //  j weather mode
                                for (int j=0 ;j< weather.length();j++)
                                {
                                    JSONObject weathermode = weather.getJSONObject(j);
                                    String tt = weathermode.getString("description");
                                    if((i==0))
                                    {
                                        weatherM[j]=tt;
                                    }

                                    JSONArray time =weathermode.getJSONArray("time");
                                    for (int z=0;z<time.length();z++)
                                    {
                                        JSONObject daytime = time.getJSONObject(z);
                                        JSONArray elementValue = daytime.getJSONArray("elementValue");
                                        JSONObject Value =elementValue.getJSONObject(0);

                                        String value =Value.getString("value");
                                        weamode[i][j][z]=value;

                                        if (i==0&j==0) {
                                            String start =daytime.getString("startTime");
                                            String end = daytime.getString("endTime");
                                            startTime[z] = start+"~"+end;
                                        }
                                        if (i==0&z==0)
                                        {
                                            String measures=Value.getString("measures");
                                            if (measures.equals("NA")||measures.equals("自定義 Wx 文字")||measures.equals("NA "))
                                            {
                                                unit[j] = " ";
                                            }
                                            else
                                            {
                                                unit[j] = measures;
                                            }

                                        }

                                      //  t.append("index:"+i+" we:"+j+" "+z+" "+ start+" "+end+"  "+value+measures+"\n\n");
                                    }
                                }
                            }
                        t.setText("OK");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    public void  speake(View view) throws IOException {
//        Intent intemt =new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intemt.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intemt.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        if(intemt.resolveActivity(getPackageManager())!=null){
//            startActivityForResult(intemt ,10);
//        }
//        else {
//            Toast.makeText(this,"Your Device Do not support speeck input",Toast.LENGTH_SHORT).show();
//        }
//
//        mQueue = Volley.newRequestQueue(MainActivity.this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        t.setText(response);
//                        mQueue.stop();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                t.setText("Something have problem");
//                error.printStackTrace();
//                mQueue.stop();
//            }
//        }
//
//        );
//        mQueue.add(stringRequest);
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case 10:
//                if (resultCode==RESULT_OK && data != null )
//                {
//                    ArrayList<String> result =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    t.setText(result.get(0));
//
//                }
//                //t.setText("1222222");
//                break;
//        }
//


      }
}




