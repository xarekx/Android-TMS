package com.example.szef.tmsApp.StatusPage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.szef.tmsApp.CustomListAdapter;
import com.example.szef.tmsApp.DeliveryPage.Delivery;
import com.example.szef.tmsApp.HistoryPage.History;
import com.example.szef.tmsApp.HistoryPage.HistoryActivity;
import com.example.szef.tmsApp.OrderPage.OrderDetails;
import com.example.szef.tmsApp.OrderPage.OrderList;
import com.example.szef.tmsApp.R;
import com.example.szef.tmsApp.TmsApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StatusActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private ArrayList<HashMap<String, String>> valueFromDetails;
    private ListView list;
    private String[] dialogItems;
    private Button mButtonStatus;
    private Button mButtonSendStatus;
    private EditText editProblem;
    private String orderID;
    private String newStatus;
    private String[] allStatus;
    private ProgressDialog pDialog;
    Delivery mDelivery;
    History mHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        final Intent intent = getIntent();
        valueFromDetails = (ArrayList<HashMap<String, String>>) intent.getSerializableExtra("newValue");
        list = findViewById(R.id.statusList);
        editProblem = findViewById(R.id.EditProblem);
        mButtonStatus = findViewById(R.id.buttonStatus);
        mButtonSendStatus = findViewById(R.id.sendStatus);
        allStatus = new String [] {"Do Wykonania","W drodze na załadunek","Na miejscu załadunku",
                "W trakcie załadunku","Załadowany","W drodze na rozładunek",
                "Na miejscu rozładunku","W trakcie rozładunku", "Rozładowany", "Transport zakończony","Problem"};
        mDelivery = new Delivery(valueFromDetails);
        mHistory = new History(valueFromDetails);

        Toolbar toolbarDetails = findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbarDetails);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbarDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatusActivity.this, OrderDetails.class);
                intent.putExtra("json",valueFromDetails);
                startActivity(intent);
            }
        });

        final Integer[] imageArray = {R.mipmap.ic_name, R.mipmap.ic_status};
        final String[] nameArray = {"Klient", "Status"};
        final String[] infoArray = {mDelivery.getName(), getStatus()};

        final CustomListAdapter listAdapter = new CustomListAdapter(this, imageArray, infoArray, nameArray, R.layout.list_item);
        list.setAdapter(listAdapter);
        if(infoArray[1].equals("Problem")) {
            mButtonSendStatus.setEnabled(true);
            newStatus = "PROBLEM";
        }

        mButtonStatus.setOnClickListener(new View.OnClickListener() {
            int firstTime = 0;
            @Override
            public void onClick(View v) {
                if (infoArray[1].equals("Do Wykonania")) {
                    dialogItems = new String[]{"W drodze na załadunek", "Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("W drodze na załadunek") && firstTime == 0) {
                    dialogItems = new String[] {"Na miejscu załadunku","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("Na miejscu załadunku") && firstTime == 0) {
                    dialogItems = new String[] {"W trakcie załadunku","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("W trakcie załadunku") && firstTime == 0) {
                    dialogItems = new String[] {"Załadowany","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("Załadowany") && firstTime == 0) {
                    dialogItems = new String[] {"W drodze na rozładunek","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("W drodze na rozładunek") && firstTime == 0) {
                    dialogItems = new String[] {"Na miejscu rozładunku","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("Na miejscu rozładunku") && firstTime == 0) {
                    dialogItems = new String[] {"W trakcie rozładunku","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("W trakcie rozładunku") && firstTime == 0) {
                    dialogItems = new String[] {"Rozładowany","Problem"};
                    firstTime = 1;
                }
                if (infoArray[1].equals("Rozładowany") && firstTime == 0) {
                    dialogItems = new String[] {"Transport zakończony","Problem"};
                    firstTime = 1;
                }
                if(infoArray[1].equals("Transport zakończony")) {
                    dialogItems = new String[] {"Transport zakończony","Problem"};

                }
                if(infoArray[1].equals("Problem")) {
                    dialogItems = new String[]{"Problem"};
                }

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(StatusActivity.this);
                mBuilder.setTitle("Wybierz Status");
                mBuilder.setIcon(R.drawable.ic_action_dialoglist);
                mBuilder.setSingleChoiceItems(dialogItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        infoArray[1] = dialogItems[which];
                        dialog.dismiss();
                        Toast.makeText(StatusActivity.this, "Status zaaktualizowany", Toast.LENGTH_SHORT).show();
                        mButtonSendStatus.setEnabled(true);
                        list.setAdapter(listAdapter);


                    }
                });
                mBuilder.setNegativeButton("Odrzuć", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });

        mButtonSendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newStatus = returnStatus(infoArray[1]);
                orderID = mHistory.getOrderID();
                new SendStatus().execute();
                Toast.makeText(StatusActivity.this, "Wysłano Status", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StatusActivity.this, OrderList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deliverymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.statusHistory) {
            Intent intent = new Intent(StatusActivity.this, HistoryActivity.class);
            intent.putExtra("extras", valueFromDetails);
            startActivity(intent);
            return true;
        }
        if (id == R.id.sendStatusItem) {
            Intent intent = new Intent(StatusActivity.this, OrderDetails.class);
            intent.putExtra("json", valueFromDetails);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getStatus() {
        String statusValue = null;
        for(HashMap<String,String> map: valueFromDetails) {
            for(Map.Entry<String,String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if(key.equals("status")) {
                    statusValue = value;
                    if(statusValue.equals("TO_COMPLETE")) {
                        statusValue = allStatus[0];
                    }
                    if(statusValue.equals("ON_THE_WAY_TO_THE_LOADING_PLACE")) {
                        statusValue = allStatus[1];
                    }
                    if(statusValue.equals("ON_THE_LOADING_PLACE")) {
                        statusValue = allStatus[2];
                    }
                    if(statusValue.equals("DURING_LOADING")) {
                        statusValue = allStatus[3];
                    }
                    if(statusValue.equals("LOADED")) {
                        statusValue = allStatus[4];
                    }
                    if(statusValue.equals("ON_THE_WAY_TO_THE_UNLOADING_PLACE")) {
                        statusValue = allStatus[5];
                    }
                    if(statusValue.equals("ON_THE_UNLOADING_PLACE")) {
                        statusValue = allStatus[6];
                    }
                    if(statusValue.equals("DURING_UNLOADING")) {
                        statusValue = allStatus[7];
                    }
                    if(statusValue.equals("UNLOADED")) {
                        statusValue = allStatus[8];
                    }
                    if(statusValue.equals("TRANSPORT_COMPLETED")) {
                        statusValue = allStatus[9];
                    }
                    if(statusValue.equals("PROBLEM")) {
                        statusValue = allStatus[10];
                    }
                }
            }
        }
        return statusValue;
    }

    public String returnStatus(String infoArray) {
        if(infoArray.equals("Do Wykonania")) {
            newStatus = "TO_COMPLETE";
        }
        if(infoArray.equals("W drodze na załadunek")) {
            newStatus = "ON_THE_WAY_TO_THE_LOADING_PLACE";
        }
        if(infoArray.equals("Na miejscu załadunku")) {
            newStatus = "ON_THE_LOADING_PLACE";
        }
        if(infoArray.equals("W trakcie załadunku")) {
            newStatus = "DURING_LOADING";
        }
        if(infoArray.equals("Załadowany")) {
            newStatus = "LOADED";
        }
        if(infoArray.equals("W drodze na rozładunek")) {
            newStatus = "ON_THE_WAY_TO_THE_UNLOADING_PLACE";
        }
        if(infoArray.equals("Na miejscu rozładunku")) {
            newStatus = "ON_THE_UNLOADING_PLACE";
        }
        if(infoArray.equals("W trakcie rozładunku")) {
            newStatus = "DURING_UNLOADING";
        }
        if(infoArray.equals("Rozładowany")) {
            newStatus = "UNLOADED";
        }
        if(infoArray.equals("Transport zakończony")) {
            newStatus = "TRANSPORT_COMPLETED";
        }
        if(infoArray.equals("Problem")) {
            newStatus = "PROBLEM";
        }
        return newStatus;
    }

    public class SendStatus extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(StatusActivity.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Proszę czekać...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println(newStatus);
            RequestBody requestBody = new FormBody.Builder()
                    .add("status",newStatus)
                    .add("message",""+editProblem.getText().toString())
                    .build();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(TmsApplication.URL + "/order/" + orderID + "/status")
                    .put(requestBody)
                    .header("Authorization","Bearer "+ OrderList.token)
                    .build();

            Call call = client.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                System.out.println(response);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (response != null) {
                    response.body().close();
                }
            }
            return null;
        }

    }
}
