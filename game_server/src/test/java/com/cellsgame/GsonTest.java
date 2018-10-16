package com.cellsgame;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * File Description.
 *
 * @author Yang
 */
public class GsonTest {
    public static void main(String[] args) {
        Gson gsonTest = new GsonBuilder().create();
        Map a = gsonTest.fromJson("{146:'>=',1461='>='}", Map.class);
        System.out.println(a);
        System.out.println("-----------------------------------------------------------------");
    }

}
