package com.example.speak;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    String placenaem[] =new String[22];
    String weamode[][][][] =new String[22][15][15][3];  // 放狀態  開始時間~結束時間   職
    String weatherM[] =new String[15];  //天氣報導
    String unit[]=new String[15];
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        t=(TextView)findViewById(R.id.textView);
        b=(Button)findViewById(R.id.button);

        mQueue = Volley.newRequestQueue( this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonParse(); }}

        );
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
                                        String start =daytime.getString("startTime");
                                        String end = daytime.getString("endTime");

                                        weamode[i][j][z][0]=start;
                                        weamode[i][j][z][1]=end;

                                        JSONArray elementValue = daytime.getJSONArray("elementValue");
                                        JSONObject Value =elementValue.getJSONObject(0);
                                        String value =Value.getString("value");
                                        String measures=Value.getString("measures");

                                        weamode[i][j][z][2]=value;
                                        if (i==0&j==0)
                                            unit[z]=measures;

                                     //   t.append("index:"+i+" we:"+j+" "+z+" "+ start+" "+end+"  "+value+measures+"\n\n");
                                    }
                                }
                            }

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




