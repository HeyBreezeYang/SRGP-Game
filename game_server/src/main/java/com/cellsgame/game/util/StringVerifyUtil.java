package com.cellsgame.game.util;

import java.util.regex.Pattern;

/**
 * Created by yfzhang on 2017/8/8.
 */
public class StringVerifyUtil {

    /**
     * 验证必须是 数字 or 字母 or 下划线
     */
    public static final Pattern PATTERN_ABC_0 = Pattern.compile("^[_\\s@a-zA-Z0-9]{0,}$");
    /**
     * 过滤 非 数字 or 字母 or 下划线
     */
    public static final Pattern PATTERN_REPLACE_ABC_0 = Pattern.compile("[^_\\s@a-zA-Z0-9\\u4e00-\\u9fa5]");
    /**
     * 验证必须是 数字 or 字母 or 下划线
     */
    public static final Pattern PATTERN_ABC_PWD = Pattern.compile("^[~\"\"?<>\\[\\]={}_\\-*&()!:@$%\\^.\\s@a-zA-Z0-9]{0,}$");
    /**
     * 验证只能是,汉字，数字，字母，下划线
     */
    public static final Pattern PATTERN_ABC_1 = Pattern.compile("^[_\\s@a-zA-Z0-9\\u4e00-\\u9fa5]{0,}$");
    /**
     * 过滤 非 汉字，数字，字母，下划线
     */
    public static final Pattern PATTERN_REPLACE_ABC_1 = Pattern.compile("[^_\\s@a-zA-Z0-9\\u4e00-\\u9fa5]");
    /**
     * 验证必须是字母开头
     */
    public static final Pattern PATTERN_ABC = Pattern.compile("^[a-zA-Z]\\S+$");
    /**
     * 纯汉字
     */
    public static final Pattern PATTERN_UUU = Pattern.compile("^[\\u4e00-\\u9fa5]{0,}$");
    /**
     * 过滤 非 纯汉字
     */
    public static final Pattern PATTERN_REPLACE_UUU = Pattern.compile("[^\\u4e00-\\u9fa5]");
    /**
     * 汉字，字母，数字，以及一些常规字符
     */
    public static final Pattern PATTERN_ABC_00_UUU = Pattern.compile("^[~`“”\"\"?<>\\[\\]【】{}_\\-——=《》*&（）()!！:：#@$￥%……\\^.。,，\\s@a-zA-Z0-9\\u4e00-\\u9fa5]{0,}$");
    /**
     * 过滤 非 汉字，字母，数字，以及一些常规字符
     */
    public static final Pattern PATTERN_REPLACE_ABC_00_UUU = Pattern.compile("[^~`“”\"\"?<>\\[\\]【】{}_=\\-——《》*&（）()!！:：#@$￥%……\\^.。,，\\s@a-zA-Z0-9\\u4e00-\\u9fa5]");

    public static final boolean isPATTERN_ABC_1(String str){
        return PATTERN_ABC_1.matcher(str).matches();
    }


    public static void main(String[] args){
        String s = "可I来哦SO";
        System.out.println(isPATTERN_ABC_1(s));
    }
}
