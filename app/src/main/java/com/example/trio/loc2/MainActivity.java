package com.example.trio.loc2;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    protected static final String TAG = "MainActivity";

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;


    protected TextView latT;
    protected TextView longiT;
    protected TextView errorT;
    String sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latT = (TextView) findViewById((R.id.lat));
        longiT = (TextView) findViewById((R.id.longi));
        errorT = (TextView) findViewById((R.id.error));



        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();


    }



    @Override
    public void onConnected(Bundle connectionHint) {



        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            latT.setText(" Latitude is: " + String.valueOf(mLastLocation.getLatitude()));
            longiT.setText(" Longitude is: " + String.valueOf(mLastLocation.getLongitude()));
            sms = "Please Come as soon as possible, My Latitude is: " + String.valueOf(mLastLocation.getLatitude()) + " And Longitude is: " + String.valueOf(mLastLocation.getLongitude());

        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

        errorT.setText("Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {


        errorT.setText("Connection suspended");
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    public void sendMsg(View view) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("9470116734", null, sms, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent..",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later..",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}
