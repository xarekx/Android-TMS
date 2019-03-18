package com.example.szef.tmsApp.OrderPage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
//import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.example.szef.tmsApp.CardActivity;
import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.TmsApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderList extends AppCompatActivity {

    private String TAG = OrderList.class.getSimpleName();
    private ListView viewList;
    private ProgressDialog pDialog;
    public static String token;
    public static String userIdent;
    ArrayList<HashMap<String, String>> orderList;
    ArrayList<HashMap<String, String>> orderDetailsList;
    private final OkHttpClient client = new OkHttpClient();
    private SimpleAdapter adapter;
    private SimpleAdapter adapter1;
    private int arek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        SharedPreferences preferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        token = preferences.getString("access_token", "");
        userIdent = preferences.getString("userId","");

        orderDetailsList = new ArrayList<>();
        orderList = new ArrayList<>();

        viewList = findViewById(R.id.listView);

        Toolbar toolbarDetails = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new FetchData().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);

        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        System.out.println(item);
        SearchView searchView = (SearchView) item.getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                viewList.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(OrderList.this, OrderDetails.class);
                        System.out.println(adapter.getItem(position));
                        orderDetailsList.add((HashMap<String, String>) adapter.getItem(position));
                        intent.putExtra("json", orderDetailsList);
                        startActivity(intent);
                        orderDetailsList.remove(orderDetailsList.get(position));
                    }
                });

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public class FetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OrderList.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Request requestData = new Request.Builder()
                    .url(TmsApplication.URL + "/user/" + userIdent + "/order/")
                    .header("Authorization","Bearer "+token)
                    .build();
            Response responseData;
            String jsonStr = null;

            try  {
                responseData = client.newCall(requestData).execute();
                if (!responseData.isSuccessful()) {
                    throw new IOException("Unexpected code " + responseData);
                }
                jsonStr = responseData.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String ID_order = c.getString("id");

                        //Client node
                        String id_client = c.getString("clientId");
                        String order_status = c.getString("status");
                        String order_number = c.getString("orderNumber");
                        String description = c.getString("description");
//                        String clientName_client = c.getString("clientName");
//                        String country_client = client.getString("country");
//                        String city_client = client.getString("city");
//                        String postCode_client = client.getString("postCode");
//                        String street_client = client.getString("street");
//                        String houseNumber_client = client.getString("houseNumber");
//                        String nip_client = client.getString("nip");

                        String loadPlace_order = c.getString("loadPlace");
                        String unloadPlace_order = c.getString("unloadPlace");
                        String loadDate_order = c.getString("loadDate");
                        String unloadDate_order = c.getString("unloadDate");
//                        String price_order = c.getString("price");

                        Request request = new Request.Builder()
                                .url(TmsApplication.URL + "/client/" + id_client)
                                .header("Authorization","Bearer "+ token)
                                .build();
                        Response response = null;

                        try  {
                            response = client.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                throw new IOException("Unexpected code " + response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String jsonData = Objects.requireNonNull(response).body().string();
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String clientName = jsonObject.getString("name");



                        //Driver Node
//                        JSONObject driver = c.getJSONObject("driver");

//                        String id_driver = driver.getString("id");
//                        String username_driver = driver.getString("username");
//                        String password_driver = driver.getString("password");
//                        String name_driver = driver.getString("name");
//                        String surname_driver = driver.getString("surname");
//                        String fullName_driver = driver.getString("fullName");
//                        String role_driver = driver.getString("role");

//                        String filePath_order = c.getString("filePath");
//                        if (!order_status.equals("TRANSPORT_COMPLETED")) {
                            HashMap<String, String> order = new HashMap<>();
                            order.put("id", ID_order);
                            order.put("name", clientName);
                            order.put("loadDate", loadDate_order);
                            order.put("unloadDate", unloadDate_order);
                            order.put("unloadPlace", unloadPlace_order);
                            order.put("loadPlace", loadPlace_order);
                            order.put("orderNumber", order_number);
                            order.put("description", description);
                            order.put("status", order_status);

                            orderList.add(order);
//                        }
                    }
                    } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
                return null;
            }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();


            adapter = new SimpleAdapter(OrderList.this, orderList,
                    R.layout.orders, new String[]{"orderNumber", "loadDate", "unloadDate"}, new int[]{R.id.orderNumber,
            R.id.loadDate,R.id.unloadDate});

            viewList.setAdapter(adapter);

            viewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent (OrderList.this, OrderDetails.class);
                    orderDetailsList.add(orderList.get(position));
                    intent.putExtra("json", orderDetailsList);
                    startActivity(intent);
                    orderDetailsList.remove(orderList.get(position));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }


}




