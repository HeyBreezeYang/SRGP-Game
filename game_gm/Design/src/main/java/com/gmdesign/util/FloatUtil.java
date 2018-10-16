package com.gmdesign.util;

import java.math.BigDecimal;

/**
 * Created by HP on 2018/6/15.
 */
public class FloatUtil {

    public static Float getFloatKeepTwoBits(Integer a,Integer b){

        int scale = 2;//设置位数
        int roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
        BigDecimal bd  =   new  BigDecimal((double)a/b);
        bd = bd.setScale(scale,roundingMode);
        float ft = bd.floatValue();
        return ft;
    }

}
