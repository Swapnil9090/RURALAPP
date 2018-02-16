package com.example.swapnil.googlemaps;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class satelliteweather extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST=9001;
    Button b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satelliteweather);

        if(isServicesOK()){
            init();
        }
    }
    private void init()
    {
        Button btnmap=(Button)findViewById(R.id.btnmap);
        Button weather=(Button)findViewById(R.id.weather1);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(satelliteweather.this,weatherforcast.class);
                startActivity(i);
            }
        });
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(satelliteweather.this,MapActivity.class);
                startActivity(intent);
            }
        });

    }

    public boolean isServicesOK()
    {
        Log.d(TAG,"isServicesOK: checking google services version");
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(satelliteweather.this);
        if(available== ConnectionResult.SUCCESS)
        {
            Log.d(TAG,"isServicesOK: Google services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog= GoogleApiAvailability.getInstance().getErrorDialog(satelliteweather.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this,"you cannot make map request",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
