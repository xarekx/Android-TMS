package com.example.szef.tmsApp.UserPage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class User {

    public User() {
    }


    public static String getFullName(Map<String, String> map) {
        String fullName = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("fullName")) {
                fullName = entry.getValue();
            }
        }
        return fullName;
    }

    public static String getDriverLicense(Map<String, String> map) {
        String driverLicense = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("drivingLicenseNumber")) {
                driverLicense = entry.getValue();
            }
        }
        return driverLicense;
    }

    public static String getUserName(Map<String, String> map) {
        String userName = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("username")) {
                userName = entry.getValue();
            }
        }
        return userName;
    }

    public static String getPhoneNumber(Map<String, String> map) {
        String phoneNumber = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("phoneNumber")) {
                phoneNumber = entry.getValue();
            }
        }
        return phoneNumber;
    }

    public static String getAddress(Map<String, String> map) {
        String adress = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("address")) {
                adress = entry.getValue();
            }
        }
        return adress;
    }

    public static String getEmail(Map<String, String> map) {
        String email = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("email")) {
                email = entry.getValue();
            }
        }
        return email;
    }

    public static String getDateOfBirth(Map<String, String> map) {
        String dateOfBirth;
        String output = null;
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (entry.getKey().equals("dateOfBirth")) {
                dateOfBirth = entry.getValue();
                DateFormat dateFormat = new SimpleDateFormat(
                        "EEE MMM dd yyyy HH:mm:ss zzz", Locale.US);
                DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                try {
                    output = outputFormat.format(dateFormat.parse(dateOfBirth));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return output;
    }
}
