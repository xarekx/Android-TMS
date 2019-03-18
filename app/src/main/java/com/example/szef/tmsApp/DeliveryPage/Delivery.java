package com.example.szef.tmsApp.DeliveryPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Delivery {
    private final ArrayList<HashMap<String, String>> mdeliveryValue;

    public Delivery(ArrayList<HashMap<String, String>> deliveryValue) {
        mdeliveryValue = deliveryValue;
    }

    public String getName() {
        String nameValue = null;
        for (HashMap<String, String> map : mdeliveryValue) {
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if (key.equals("name")) {
                    nameValue = value;
                }
            }
        }
        return nameValue;
    }

    public String getUserId() {
        String idValue = null;
        for (HashMap<String, String> map : mdeliveryValue) {
            for (Map.Entry<String, String> mapEntry : map.entrySet()) {
                String value = mapEntry.getValue();
                String key = mapEntry.getKey();
                if (key.equals("id")) {
                    idValue = value;
                }
            }
        }
        return idValue;
    }

}
