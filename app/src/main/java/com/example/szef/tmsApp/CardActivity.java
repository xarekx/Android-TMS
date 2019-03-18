package com.example.szef.tmsApp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.szef.tmsApp.OrderPage.OrderList;
import com.example.szef.tmsApp.UserPage.UserActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CardActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    public static final int IMAGE_GALLERY_REQUEST = 5;
    private String myToken;
    private final OkHttpClient client = new OkHttpClient();
    private String jsonStr = null;
    String userId;
    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        preferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        myToken = preferences.getString("access_token", "");
        new GetUserInformation().execute();

    }

    public class GetUserInformation extends AsyncTask<Void,Void,Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            Request requestData = new Request.Builder()
                    .url(TmsApplication.URL + "/user/authorized")
                    .header("Authorization","Bearer "+myToken)
                    .build();
            Response responseData;
            try  {
                responseData = client.newCall(requestData).execute();
                if (!responseData.isSuccessful()) {
                    throw new IOException("Unexpected code " + responseData);
                }
                jsonStr = responseData.body().string();
                JSONObject user = new JSONObject(jsonStr);
                userId = user.getString("id");
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putString("userId",userId);
                prefEditor.apply();
                FirebaseMessaging.getInstance().subscribeToTopic("" +preferences.getString("userId",""));
                if (!gpsPermission()) {
                    Intent i = new Intent(getApplicationContext(), GpsService.class);
                    startService(i);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public void goToUserActivity(View v) {
        Intent intent = new Intent(CardActivity.this,UserActivity.class);
        intent.putExtra("data",jsonStr);
        startActivity(intent);

    }

    public void goToOrderList(View v) {
        Intent intent = new Intent(CardActivity.this,OrderList.class);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("" +preferences.getString("userId",""));
        SharedPreferences sharedpreferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        stopService(i);
        Intent intent = new Intent(CardActivity.this,TmsApplication.class);
        Toast.makeText(this, R.string.logoff, Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CardActivity.this, TmsApplication.class);
        startActivity(intent);
    }

    public void runService() {
        if (gpsPermission()) {
            Intent i = new Intent(getApplicationContext(), GpsService.class);
            startService(i);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                runService();
            } else {
                gpsPermission();
            }
        }
    }

    private boolean gpsPermission() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }
}



