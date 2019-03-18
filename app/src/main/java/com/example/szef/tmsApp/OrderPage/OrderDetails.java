package com.example.szef.tmsApp.OrderPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.szef.tmsApp.DeliveryPage.DeliveryActivity;
import com.example.szef.tmsApp.CustomListAdapter;
import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.StatusPage.StatusActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderDetails extends AppCompatActivity {

    ArrayList<HashMap<String, String>> hashMap;
    private ListView orderDetails;
    private BottomNavigationView bottomNavigationView;

    String[] nameArray = {"Klient","Data Załadunku","Miejsce Załadunku","Data Rozładunku","Miejsce Rozładunku","Informacje Dodatkowe"};
    String[] infoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        final Intent intent = getIntent();
        hashMap = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("json");
        System.out.println(hashMap);

        orderDetails = findViewById(R.id.orderDetails);

        Toolbar toolbarDetails = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        infoArray = new String[]{getName(hashMap), getLoadDate(hashMap), getLoadPlace(hashMap), getUnloadDate(hashMap), getUnloadPlace(hashMap), getDescription(hashMap)};
        Integer[] imageArray = {R.mipmap.ic_name, R.mipmap.ic_loaddate, R.mipmap.ic_loadplace, R.mipmap.ic_unloaddate, R.mipmap.ic_unloadplace, R.mipmap.ic_additional_message};

        CustomListAdapter listAdapter = new CustomListAdapter(this, imageArray, infoArray, nameArray, R.layout.list_item);
        orderDetails.setAdapter(listAdapter);



        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_orderList:
                        Intent intentList = new Intent(OrderDetails.this,OrderList.class);
                        startActivity(intentList);
                        break;
                    case R.id.navigation_status:
                        Intent intentStatus = new Intent(OrderDetails.this,StatusActivity.class);
                        intentStatus.putExtra("newValue",hashMap);
                        startActivity(intentStatus);
                        break;
                    case R.id.navigation_raport:
                        Intent intentDelivery = new Intent( OrderDetails.this, DeliveryActivity.class);
                        intentDelivery.putExtra("deliveryValue",hashMap);
                        startActivity(intentDelivery);
                        break;
                }
                return true;
            }
        });

    }

    public static String getName(ArrayList<HashMap<String, String>> hashMap) {
        String nameValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("name")) {
                    nameValue = value;
                }
            }
        }
        return nameValue;
    }

    public static String getLoadDate(ArrayList<HashMap<String, String>> hashMap) {
        String ldateValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("loadDate")) {
                    ldateValue = value;
                }
            }
        }
        return ldateValue;
    }

    public String getUnloadDate(ArrayList<HashMap<String, String>> hashMap) {
        String udateValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("unloadDate")) {
                    udateValue = value;
                }
            }
        }
        return udateValue;
    }

    public static String getLoadPlace(ArrayList<HashMap<String, String>> hashMap) {
        String lplaceValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("loadPlace")) {
                    lplaceValue = value;
                }
            }
        }
        return lplaceValue;
    }

    public static String getUnloadPlace(ArrayList<HashMap<String, String>> hashMap) {
        String uplaceValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("unloadPlace")) {
                    uplaceValue = value;
                }
            }
        }
        return uplaceValue;
    }

    public static String getDescription(ArrayList<HashMap<String, String>> hashMap) {
        String descriptionValue = null;
        for(HashMap<String,String> map: hashMap) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("description")) {
                    descriptionValue = value;
                }
            }
        }
        return descriptionValue;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderDetails.this,OrderList.class);
        startActivity(intent);
    }

}
