package com.master.bean;

import java.text.DecimalFormat;

/**
 * Created by DJL on 2017/7/21.
 *
 * @ClassName GM
 * @Description
 */
public class ComputerTableBean {
    private static DecimalFormat df = new DecimalFormat ("#.####");

    public static String convertProportion(Object o){
        if(o==null){
            return "-";
        }else{
            return df.format(100*Double.parseDouble(o.toString())).concat("%");
        }
    }

    public static String convertThree(Object msg){
        if(msg==null){
            msg=0D;
        }
        return df.format(msg);
    }

    public static String convertOne(Double msg){
        if(msg==null){
            msg=0D;
        }
        return df.format(msg);
    }

    public static String convertTwo(Double msg){
        if(msg==null){
            msg=0D;
        }
        double a=msg * 100;
        return df.format(a).concat("%");
    }

}
