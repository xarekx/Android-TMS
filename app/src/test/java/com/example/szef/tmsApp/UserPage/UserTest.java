package com.example.szef.tmsApp.UserPage;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void getFullName() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        String output = user.getFullName(mockedMap);
        String expected = "Janusz Tracz";
        assertEquals(expected, output);
    }

    @Test
    public void getDriverLicense() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        String output = user.getDriverLicense(mockedMap);
        String expected = "abc53213";
        assertEquals(expected, output);
    }

    @Test
    public void getUserName() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        String output = user.getUserName(mockedMap);
        String expected = "kierowca2";
        assertEquals(expected, output);
    }

    @Test
    public void getPhoneNumber() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        mockedMap.put("phoneNumber", "782151397");
        String output = user.getPhoneNumber(mockedMap);
        String expected = "782151397";
        assertEquals(expected, output);
    }

    @Test
    public void getAddress() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        mockedMap.put("phoneNumber", "782151397");
        mockedMap.put("address", "Przemysława 2a");
        String output = user.getAddress(mockedMap);
        String expected = "Przemysława 2a";
        assertEquals(expected, output);
    }

    @Test
    public void getEmail() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        mockedMap.put("phoneNumber", "782151397");
        mockedMap.put("address", "Przemysława 2a");
        mockedMap.put("email", "arektarnowski1565@wp.pl");
        String output = user.getEmail(mockedMap);
        String expected = "arektarnowski1565@wp.pl";
        assertEquals(expected, output);
    }

    @Test
    public void getDateOfBirth() {
        HashMap<String, String> mockedMap = new HashMap<>();
        User user = new User();
        mockedMap.put("fullName", "Janusz Tracz");
        mockedMap.put("drivingLicenseNumber", "abc53213");
        mockedMap.put("username", "kierowca2");
        mockedMap.put("phoneNumber", "782151397");
        mockedMap.put("address", "Przemysława 2a");
        mockedMap.put("email", "arektarnowski1565@wp.pl");
        mockedMap.put("dateOfBirth", "Wed Oct 16 2013 00:00:00 CEST");
        String output = user.getDateOfBirth(mockedMap);
        String expected = "2013/10/16";
        assertEquals(expected, output);
    }
}