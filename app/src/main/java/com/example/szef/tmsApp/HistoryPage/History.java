package com.example.szef.tmsApp.HistoryPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class History {

    private ArrayList<HashMap<String, String>> deliveryValue;

    public History(ArrayList<HashMap<String, String>> deliveryValue) {
        this.deliveryValue = deliveryValue;
    }


    public String getOrderID() {
        String orderId = null;
        for (HashMap<String, String> map : deliveryValue) {
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if (key.equals("id")) {
                    orderId = value;
                }
            }
        }
        return orderId;
    }
}
