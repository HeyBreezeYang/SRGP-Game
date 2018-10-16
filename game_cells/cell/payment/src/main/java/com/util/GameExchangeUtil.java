package com.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DJL on 2017/9/27.
 *
 * @ClassName cells
 * @Description
 */
public class GameExchangeUtil {

    private static final Map<Integer,Integer> TWD=new HashMap<>();
    private static final Map<Integer,Integer> HKD=new HashMap<>();
    private static final Map<Double,Integer> USD=new HashMap<>();

    static {
        TWD.put(30,6);
        TWD.put(150,30);
        TWD.put(300,68);
        TWD.put(590,128);
        TWD.put(1050,233);
        TWD.put(1490,328);
        TWD.put(2990,648);

        HKD.put(8,6);
        HKD.put(38,30);
        HKD.put(78,68);
        HKD.put(158,128);
        HKD.put(278,233);
        HKD.put(398,328);
        HKD.put(788,648);

        USD.put(0.99,6);
        USD.put(4.99,30);
        USD.put(9.99,68);
        USD.put(19.99,128);
        USD.put(34.99,233);
        USD.put(49.99,328);
        USD.put(99.99,648);
    }

    public static Integer getPrice(Object money,int type){
        switch (type){
            case 1:
                return TWD.get(Integer.parseInt(money.toString()));
            case 2:
                return HKD.get(Integer.parseInt(money.toString()));
            case 3:
                return USD.get(Double.parseDouble(money.toString()));
            default:
                return Integer.parseInt(money.toString());
        }
    }

}
