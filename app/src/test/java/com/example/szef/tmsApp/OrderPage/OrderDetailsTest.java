package com.example.szef.tmsApp.OrderPage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class OrderDetailsTest {

    @Test
    public void getName() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        OrderDetails orderDetails = new OrderDetails();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockInput.add(mockHashmap);
        String output;
        String expected = "Kowalczuk-Kowalczuk";

        output = orderDetails.getName(mockInput);
        assertEquals(expected, output);

    }

    @Test
    public void getLoadDate() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockHashmap.put("loadDate", "01/07/2016");
        mockInput.add(mockHashmap);
        String output;
        String expected = "01/07/2016";
        OrderDetails orderDetails = new OrderDetails();
        output = orderDetails.getLoadDate(mockInput);
        assertEquals(expected, output);
    }

    @Test
    public void getUnloadDate() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockHashmap.put("loadDate", "01/07/2016");
        mockHashmap.put("unloadDate", "03/07/2016");
        mockInput.add(mockHashmap);
        String output;
        String expected = "03/07/2016";
        OrderDetails orderDetails = new OrderDetails();
        output = orderDetails.getUnloadDate(mockInput);
        assertEquals(expected, output);
    }

    @Test
    public void getLoadPlace() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockHashmap.put("loadDate", "01/07/2016");
        mockHashmap.put("unloadDate", "03/07/2016");
        mockHashmap.put("loadPlace", "abu Dhabi");
        mockInput.add(mockHashmap);
        String output;
        String expected = "abu Dhabi";
        OrderDetails orderDetails = new OrderDetails();
        output = orderDetails.getLoadPlace(mockInput);
        assertEquals(expected, output);
    }

    @Test
    public void getUnloadPlace() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockHashmap.put("loadDate", "01/07/2016");
        mockHashmap.put("unloadDate", "03/07/2016");
        mockHashmap.put("loadPlace", "abu Dhabi");
        mockInput.add(mockHashmap);
        String output;
        String expected = "nice unload place";
        OrderDetails orderDetails = new OrderDetails();
        output = orderDetails.getUnloadPlace(mockInput);
        assertEquals(expected, output);

    }

    @Test
    public void getDescription() {
        ArrayList<HashMap<String, String>> mockInput = new ArrayList<>();
        HashMap<String, String> mockHashmap = new HashMap<>();
        mockHashmap.put("name", "Kowalczuk-Kowalczuk");
        mockHashmap.put("unloadPlace", "nice unload place");
        mockHashmap.put("description", "fajny opisek komć lub lajk");
        mockHashmap.put("loadDate", "01/07/2016");
        mockHashmap.put("unloadDate", "03/07/2016");
        mockHashmap.put("loadPlace", "abu Dhabi");
        mockInput.add(mockHashmap);
        String output;
        String expected = "fajny opisek komć lub lajk";
        OrderDetails orderDetails = new OrderDetails();
        output = orderDetails.getDescription(mockInput);
        assertEquals(expected, output);

    }
}