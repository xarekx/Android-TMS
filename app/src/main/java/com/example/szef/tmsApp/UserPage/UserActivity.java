package com.example.szef.tmsApp.UserPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.szef.tmsApp.CardActivity;
import com.example.szef.tmsApp.CustomListAdapter;
import com.example.szef.tmsApp.PasswordPage.PasswordActivity;
import com.example.szef.tmsApp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class UserActivity extends AppCompatActivity {

    private Button changePasswordButton;
    private ListView userDetailsList;
    private Map<String,String> maps = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userDetailsList = findViewById(R.id.userDetailsList);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        Toolbar toolbar = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        final String jsonStr = intent.getStringExtra("data");
        try {
            JSONObject user = new JSONObject(jsonStr);
            String userId = user.getString("id");
            String userFullName = user.getString("fullName");
            String userDriverLicense = user.getString("drivingLicenseNumber");
            String userName = user.getString("username");
            String userEmail = user.getString("email");
            String userPhone = user.getString("phoneNumber");
            String userAdress = user.getString("address");
            String birthDate = user.getString("dateOfBirth");
            maps.put("id",userId);
            maps.put("fullName",userFullName);
            maps.put("drivingLicenseNumber",userDriverLicense);
            maps.put("username", userName);
            maps.put("email", userEmail);
            maps.put("phoneNumber", userPhone);
            maps.put("address", userAdress);
            maps.put("dateOfBirth", birthDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Integer[] imageArray = {R.mipmap.ic_name, R.mipmap.ic_driver_license, R.mipmap.ic_username, R.mipmap.ic_email, R.mipmap.ic_phone, R.mipmap.ic_place, R.mipmap.ic_birthday};
        final String[] nameArray = {"Imie i nazwisko", "Numer prawa jazdy", "Login", "Email", "Numer telefonu", "Adres", "Data urodzenia"};
        final String[] infoArray = {User.getFullName(maps), User.getDriverLicense(maps), User.getUserName(maps), User.getEmail(maps), User.getPhoneNumber(maps), User.getAddress(maps), User.getDateOfBirth(maps)};

        final CustomListAdapter listAdapter = new CustomListAdapter(this, imageArray, infoArray, nameArray, R.layout.list_item_profile);
        userDetailsList.setAdapter(listAdapter);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, PasswordActivity.class);
                System.out.println(jsonStr);
                intent.putExtra("infos", jsonStr);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserActivity.this, CardActivity.class);
        startActivity(intent);
    }
}
