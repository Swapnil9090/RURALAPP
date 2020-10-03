package com.example.swapnil.googlemaps;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.swapnil.googlemaps.MapActivity.First_location;

public class weatherforcast extends AppCompatActivity {
    private static double p, l;
    static double abc = 0;
    static TextView t1;
    static TextView t2, t3,t4,t5;
    static ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weatherforcast);
        t1 = (TextView) findViewById(R.id.textView3);//temprature
        t2 = (TextView) findViewById(R.id.textView4);//Humidity
        t3 = (TextView) findViewById(R.id.textView5);//discription
        t4=(TextView) findViewById(R.id.textView2);//name of city
        t5=(TextView)findViewById(R.id.textView7);//for date
         img=(ImageView)findViewById(R.id.image1);//weather condition icon
        // getDeviceLocation();
        LocationManager locationmanger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationmanger.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationmanger.getLastKnownLocation(provider);
        p = location.getLatitude();
        l = location.getLongitude();

        DecimalFormat p1=new DecimalFormat("##.##");
        p=Double.valueOf(p1.format(p));

        DecimalFormat l1=new DecimalFormat("##.##");
        l=Double.valueOf(l1.format(l));
        find_weather();

    }

    public void find_weather() {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + p + "&lon=" + l + "&appid=88253201288488fa042dffa01d7f0cd9";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object=response.getJSONObject("main");
                    JSONArray array= response.getJSONArray("weather");
                    Double temp= main_object.getDouble("temp");
                    Double humidity=main_object.getDouble("humidity");
                    temp-=273;
                    DecimalFormat temp1=new DecimalFormat("##.##");
                    temp=Double.valueOf(temp1.format(temp));

                    t1.setText(String.valueOf(temp)+" \u2103");
                    t2.setText(String.valueOf(humidity)+" (gm/m^3)");

                    String discription=array.getJSONObject(0).optString("description");
                    t3.setText(discription);
                    String name=response.getString("name");
                    t4.setText(name);

                    String icon_type=array.getJSONObject(0).optString("icon");
                    String imageurl="http://openweathermap.org/img/w/"+icon_type+".png";
                    Toast.makeText(weatherforcast.this,icon_type,Toast.LENGTH_LONG).show();
                    Glide.with(getBaseContext()).load(imageurl).into(img);

                    Calendar calender= Calendar.getInstance();
                    SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-DD");
                    String formatted_data=sdf.format(calender.getTime());
                        t5.setText(formatted_data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
    });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }

}