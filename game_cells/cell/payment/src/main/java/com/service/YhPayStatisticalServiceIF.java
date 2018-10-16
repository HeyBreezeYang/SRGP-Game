package com.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/23.
 */
public interface YhPayStatisticalServiceIF {
    List queryPlayerDatePay(String msg) throws ParseException;
}
