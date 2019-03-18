package com.example.szef.tmsApp.PasswordPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.TmsApplication;
import com.example.szef.tmsApp.UserPage.UserActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PasswordActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private ProgressDialog pDialog;
    private String userId;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText newRePassword;
    private Button changePassBtn;
    private SharedPreferences preferences;
    private String myToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        newRePassword = findViewById(R.id.newRePassword);
        changePassBtn = findViewById(R.id.approvePassword);
        preferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        myToken = preferences.getString("access_token", "");
        userId = preferences.getString("userId", "");
        Intent myIntent = getIntent();
        final String datas = myIntent.getStringExtra("infos");

        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this, UserActivity.class);
                System.out.println(datas);
                intent.putExtra("data", datas);
                startActivity(intent);
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPassword.getText().toString().equals(newRePassword.getText().toString())) {
                    Toast.makeText(PasswordActivity.this, "Hasło zostało zmienione", Toast.LENGTH_SHORT).show();
                        new ChangePassword().execute();
                        logout();
                } else {
                    Toast.makeText(PasswordActivity.this, "Nowe hasła nie są identyczne", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ChangePassword extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PasswordActivity.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            RequestBody requestBody = new FormBody.Builder()
                    .add("oldPassword",oldPassword.getText().toString())
                    .add("newPassword",newPassword.getText().toString())
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(TmsApplication.URL + "/user/" + userId + "/password")
                    .post(requestBody)
                    .header("Authorization","Bearer "+myToken)
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }


    }
    public void logout() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("" +preferences.getString("userId",""));
        SharedPreferences sharedpreferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(PasswordActivity.this,TmsApplication.class);
        startActivity(intent);
    }

}
