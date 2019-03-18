package com.example.szef.tmsApp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;

import javax.crypto.KeyGenerator;


public class TmsApplication extends AppCompatActivity {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final String KEY_ALIAS ="tmsApp";
    private KeyStore keyStore;
    KeyGenerator keyGenerator;

    private EditText Name;
    private EditText Password;
    private Button loginBtn;
    private ImageView Logo;
    private SharedPreferences preferences;
    private ProgressDialog pDialog;
    public static final String URL = "https://tms-inzynierka.herokuapp.com";
//    public static final String URL = "http://192.168.8.101:8080";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tms_application);

        preferences = getSharedPreferences("keys", Activity.MODE_PRIVATE);
        Name = findViewById(R.id.EditLogin);
        Password = findViewById(R.id.EditPassword);
        loginBtn = findViewById(R.id.LogBtn);
        Logo = findViewById(R.id.Logo);
        Name.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask().execute(Name.getText().toString(), Password.getText().toString());

            }
        });

    }

    public class LoginTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TmsApplication.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... args) {
            String urlParameters = "grant_type=" + "password" + "&username=" + args[0] + "&password=" + args[1];

            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;

            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(URL+"/oauth/token");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("charset", "utf-8");
                urlConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                urlConnection.setRequestProperty("Authorization","Basic YW5kcm9pZDpzZWNyZXQ=");

                urlConnection.setUseCaches(false);

                try (DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                    wr.write(postData);

                }
                int responseCode = urlConnection.getResponseCode();

                if (responseCode >= 400 && responseCode <= 499) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TmsApplication.this, "Nie udało się zalogować", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                if(responseCode >=200 && responseCode <=299) {

                    InputStream in = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    StringBuilder buffer = new StringBuilder();
                    String line;


                    while ((line = reader.readLine()) != null) {

                        buffer.append(line);
                        JSONObject json = new JSONObject(line);
                        String access_token = json.getString("access_token");
                        String refresh_token = json.getString("refresh_token");
                        String type_token = json.getString("token_type");
                        SharedPreferences.Editor prefEditor = preferences.edit();
                        prefEditor.putString("access_token", access_token);
                        prefEditor.putString("refresh_token",refresh_token);
                        prefEditor.putString("token_type",type_token);
                        prefEditor.apply();

                    }
                    Intent intent = new Intent(TmsApplication.this,CardActivity.class);
                    startActivity(intent);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(TmsApplication.this, "Zalogowano", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       Intent intent = new Intent(Intent.ACTION_MAIN);
       intent.addCategory(Intent.CATEGORY_HOME);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       startActivity(intent);
    }
}
