package com.example.szef.tmsApp;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GpsService extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    private String Latitude;
    private String Longitude;
    private String timeStamp;
    OkHttpClient client = new OkHttpClient();
    SharedPreferences preferences;

    String myToken;
    String userIdent;
    private AsyncTask myTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        preferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        myToken = preferences.getString("access_token", "");
        userIdent = preferences.getString("userId", "");

        listener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Latitude = String.valueOf(location.getLatitude());
                Longitude = String.valueOf(location.getLongitude());
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
                timeStamp = sdf.format(new Date(location.getTime()));
                myTask = new sendCoordinates().execute();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30000,0,listener);

    }


    public class sendCoordinates extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            RequestBody requestBody = new FormBody.Builder()
                    .add("longitude",Longitude)
                    .add("latitude",Latitude)
                    .add("date",timeStamp)
                    .build();
            System.out.println(Longitude);
            System.out.println(Latitude);
            System.out.println(timeStamp);

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(TmsApplication.URL + "/user/" + userIdent + "/location")
                    .put(requestBody)
                    .header("Authorization", "Bearer " + myToken)
                    .build();

            Call call = client.newCall(request);
            Response response;
            try {
                response = call.execute();

                System.out.println(response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
        if(myTask!=null) {
            myTask.cancel(true);
        }
    }
}
