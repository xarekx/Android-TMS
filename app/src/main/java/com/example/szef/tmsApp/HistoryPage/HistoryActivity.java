package com.example.szef.tmsApp.HistoryPage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.StatusPage.StatusActivity;
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

public class HistoryActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> deliveryValue;
    private ProgressDialog pDialog;
    SharedPreferences preferences;
    private String myToken;
    private String userIdent;
    private String[] allStatus;
    private final OkHttpClient client = new OkHttpClient();
    private ListView listView;
    private ArrayList<HashMap<String, String>> orderList;
    History history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        final Intent intent = getIntent();
        deliveryValue = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("extras");
        preferences = getSharedPreferences("keys", Context.MODE_PRIVATE);
        myToken = preferences.getString("access_token", "");
        userIdent = preferences.getString("userId", "");
        listView = findViewById(R.id.listViewHistory);
        allStatus = new String[]{"Do Wykonania", "W drodze na załadunek", "Na miejscu załadunku",
                "W trakcie załadunku", "Załadowany", "W drodze na rozładunek",
                "Na miejscu rozładunku", "W trakcie rozładunku", "Rozładowany", "Transport zakończony", "Problem"};
        orderList = new ArrayList<>();
         history = new History(deliveryValue);

        Toolbar toolbarDetails = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarDetails.setTitle("Lista Statusów");
        toolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, StatusActivity.class);
                intent.putExtra("newValue", deliveryValue);
                startActivity(intent);
            }
        });
        new GetLoggedStatus().execute();

    }

    public class GetLoggedStatus extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HistoryActivity.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Request requestData = new Request.Builder()
                    .url(TmsApplication.URL + "/order/" + history.getOrderID() + "/status")
                    .header("Authorization", "Bearer " + myToken)
                    .build();
            Response responseData;
            try {
                responseData = client.newCall(requestData).execute();
                if (!responseData.isSuccessful()) {
                    throw new IOException("Unexpected code " + responseData);
                }
                String jsonStr;
                jsonStr = responseData.body().string();
                JSONArray user = new JSONArray(jsonStr);
                for (int i = 0; i < user.length(); i++) {
                    JSONObject u = user.getJSONObject(i);
                    String orderStatus = u.getString("orderStatus");
                    String message = u.getString("message");
                    String date = u.getString("date");
                    if (orderStatus.equals("TO_COMPLETE")) {
                        orderStatus = allStatus[0];
                    }
                    if (orderStatus.equals("ON_THE_WAY_TO_THE_LOADING_PLACE")) {
                        orderStatus = allStatus[1];
                    }
                    if (orderStatus.equals("ON_THE_LOADING_PLACE")) {
                        orderStatus = allStatus[2];
                    }
                    if (orderStatus.equals("DURING_LOADING")) {
                        orderStatus = allStatus[3];
                    }
                    if (orderStatus.equals("LOADED")) {
                        orderStatus = allStatus[4];
                    }
                    if (orderStatus.equals("ON_THE_WAY_TO_THE_UNLOADING_PLACE")) {
                        orderStatus = allStatus[5];
                    }
                    if (orderStatus.equals("ON_THE_UNLOADING_PLACE")) {
                        orderStatus = allStatus[6];
                    }
                    if (orderStatus.equals("DURING_UNLOADING")) {
                        orderStatus = allStatus[7];
                    }
                    if (orderStatus.equals("UNLOADED")) {
                        orderStatus = allStatus[8];
                    }
                    if (orderStatus.equals("TRANSPORT_COMPLETED")) {
                        orderStatus = allStatus[9];
                    }
                    if (orderStatus.equals("PROBLEM")) {
                        orderStatus = allStatus[10];
                    }

                    HashMap<String, String> status = new HashMap<>();

                    status.put("orderStatus", orderStatus);
                    status.put("message", message);
                    status.put("date", date);
                    orderList.add(status);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            final ListAdapter adapter = new SimpleAdapter(HistoryActivity.this, orderList,
                    R.layout.status_item, new String[]{"orderStatus", "message", "date"}, new int[]{R.id.clientName,
                    R.id.loadDate, R.id.unloadDate});

            listView.setAdapter(adapter);
        }


    }


}
