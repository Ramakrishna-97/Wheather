package com.example.wheatherapitask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class City_Country_State_CodeActivity extends AppCompatActivity {
    TextView textview2;
    EditText cityname,countrycode,statecode;
    String citynameStr,countrycodeStr,statecodeStr;
    String key;
    String statecodeUrl;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_country_state_code);

        textview2=findViewById(R.id.text_response);
        cityname=findViewById(R.id.cityname);
        countrycode=findViewById(R.id.countrycode);
        statecode=findViewById(R.id.Statecode);

        requestQueue= Volley.newRequestQueue(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);

    }
    private void dataMethod()
    {
        statecodeUrl="https://api.openweathermap.org/data/2.5/weather?q={city name},{state code},{country code}&appid={API key}";
    }

    public void submit(View view) {
        progressDialog.show();
        citynameStr=cityname.getText().toString();
        countrycodeStr=countrycode.getText().toString();
        statecodeStr=statecode.getText().toString();
        key=getResources().getString(R.string.weather_key);

        statecodeUrl="https://api.openweathermap.org/data/2.5/weather?q="+citynameStr+","+statecodeStr+","+countrycodeStr+"&appid="+key;

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, statecodeUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                try {

                    JSONObject jsonObject=response.getJSONObject("coord");
                    double lon= jsonObject.getDouble("lon");
                    double lat= jsonObject.getDouble("lat");

                    JSONArray jsonArray=response.getJSONArray("weather");
                    JSONObject weatherObj=jsonArray.getJSONObject(0);

                    int id=weatherObj.getInt("id");
                    String main=weatherObj.getString("main");
                    String description=weatherObj.getString("description");

                    long visibility=response.getLong("visibility");

                    JSONObject wind=response.getJSONObject("wind");
                    double speed=wind.getDouble("speed");
                    //  double gust=wind.getDouble("gust");

                    String name=response.getString("name");

                    JSONObject sys=response.getJSONObject("sys");
                    String countryname=sys.getString("country");
                    double sunrise=sys.getDouble("sunrise");
                    double sunset=sys.getDouble("sunset");


                    textview2.setText(""+lat+"\n"+lon+"\n"+id+
                            "\n"+main+"\n"+description+"\n"+visibility+"\n"+speed+"\n"+name+"\n"+countryname+"\n"+sunrise+"\n"+sunset);

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(City_Country_State_CodeActivity.this, "kk"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                    Toast.makeText(getApplicationContext(), "Time out or no connection  error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "AuthFailureError error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ServerError error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "NetworkError error", Toast.LENGTH_SHORT).show();

                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ParseError error", Toast.LENGTH_SHORT).show();

                }

            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}