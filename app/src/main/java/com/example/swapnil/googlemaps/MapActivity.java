package com.example.swapnil.googlemaps;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.*;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by SWAPNIL on 11-01-2018.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    public void onMapReady(GoogleMap googleMap) {
       // Toast.makeText(this, "this map is ready ", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
       // MapStyleOptions style=MapStyleOptions.loadRawResourceStyle(this,R.);
        if (mLoactionPermissionGrantted) {
            getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
                    return;
                }mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
                init();
         //   mMap.setMapStyle(style);

        }
    }

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean mLoactionPermissionGrantted = false;
    private static final int LocationPermissionRequestCode = 1234;
    private GoogleMap mMap;
    public  static LatLng First_location;
    public LatLng Second_location;

    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //widgets
    private EditText mSearchText;
    private ImageView gps;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mSearchText=(EditText)findViewById(R.id.intput_search);
        getLocationPermission();
           }


           private void addlines()
           {    //Second_location=new LatLng(x,y);
               mMap.clear();
               final Polyline polyline;
              // polyline = mMap.addPolyline(new PolylineOptions().add(First_location, Second_location).width(15).color(Color.BLUE).geodesic(true));
              //polyline.setPattern(createDashedLine(mMap,First_location,Second_location,Color.BLUE));
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Second_location,17));
               MarkerOptions options=new MarkerOptions().position(Second_location).title(Second_location.toString());
               mMap.addMarker(options);
               /* mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                    @Override
                    public void onPolylineClick(Polyline polyline) {
                        Toast.makeText(MapActivity.this,"POLYLINE",Toast.LENGTH_SHORT).show();
                    }
                });*/
           }
    private void init(){
        Log.d(TAG,"init: initializing");
        gps=(ImageView)findViewById(R.id.ic_locate);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH||actionId ==EditorInfo.IME_ACTION_DONE||keyEvent.getAction()==keyEvent.ACTION_DOWN||
                        keyEvent.getAction()==keyEvent.KEYCODE_ENTER){
                    //searching method
                    geoLocate();
                   // addlines();
                }
                return false;
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        keybordhidden();
    }

    private void geoLocate()
    {
        Log.d(TAG,"geoLocate: geoLocating");
        String searchString=mSearchText.getText().toString();
        Geocoder geocoder =new Geocoder(MapActivity.this);
        List<Address> list=new ArrayList<>();
        try{
            list=geocoder.getFromLocationName(searchString,1);
        }catch(IOException i){
            Log.e(TAG,"geoLocate: IO Exception"+i.getMessage());
    }
    if(list.size()>0)
    {
        Address address=list.get(0);

        Log.e(TAG,"Found a location"+address.toString());
       Second_location=new LatLng(address.getLatitude(),address.getLongitude());
        //MarkerOptions options=new MarkerOptions().position(Second_location).title("");
        //mMap.addMarker(options);
       movecamera(Second_location,15,address.toString());
        addlines();
       //movecamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
      //  Toast.makeText(this,address.toString(),Toast.LENGTH_SHORT).show();
    }
    }
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLoction: getting the device current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLoactionPermissionGrantted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            double p,l;
                            Log.d(TAG, "OnComplete: foundLocation!");
                            //Toast.makeText(MapActivity.this,"found location",Toast.LENGTH_SHORT).show();
                            Location currentLocation = (Location) task.getResult();
                           // Toast.makeText(MapActivity.this,"current location: LAT. "+currentLocation.getLatitude(),Toast.LENGTH_SHORT).show();
                            p=currentLocation.getLatitude();
                            l=currentLocation.getLongitude();
                            First_location=new LatLng(p,l);
                            Second_location=First_location;
                            movecamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                            addlines();

                        } else {
                            Log.d(TAG, "OnComplete: unable to find Location!");
                            Toast.makeText(MapActivity.this, "unable to find location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLoction: SecurityException" + e.getMessage());
        }
    }

    private void movecamera(LatLng latLng, float zoom,String title) {
        Log.d(TAG, "move camera: moving the camera to: lat: " + latLng.latitude + " , lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //droping a pin on req. place
        //if(!title.equals("My Location"))
        //{
            MarkerOptions options=new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);
        //}
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
        //addlines();
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLoactionPermissionGrantted = true;
                {  initMap();
                   // addlines();
                    }
            //initMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LocationPermissionRequestCode);
        }
    }else{
                ActivityCompat.requestPermissions(this,permissions,LocationPermissionRequestCode);
            }}



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLoactionPermissionGrantted=false;
        switch(requestCode){
            case LocationPermissionRequestCode:
            {
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++)
                    {
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        {
                            mLoactionPermissionGrantted=false;
                            return;
                        }
                    }
                    mLoactionPermissionGrantted=true;
                    initMap();
                }
            }
        }}
        private void keybordhidden()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    public void info(View view)
    {
        String str="Click on marker to get latitude & longitude";
        String str2="अक्षांश और देशांतर प्राप्त करने के लिए मार्कर पर क्लिक करें";
        Toast.makeText(MapActivity.this,str,Toast.LENGTH_LONG).show();
        Toast.makeText(MapActivity.this,str2,Toast.LENGTH_LONG).show();
    }

    }
