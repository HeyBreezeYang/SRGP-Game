package com.design.util;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.design.context.PlatformCode;
import com.design.exception.PlatformException;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by DJL on 2017/6/1.
 *
 * @ClassName StringUtil
 * @Description
 */
public class StringUtil {

    public static void judgeMap(Map map,String... key) throws PlatformException {
        if (map == null) {
            throw new PlatformException("参数解析错误", PlatformCode.PARAMETER_ERROR);
        }
        for (String k:key){
            if(!map.containsKey(k)){
                throw new PlatformException("参数 no ".concat(k), PlatformCode.PARAMETER_FORMAT_ERROR);
            }
        }
    }

    public static boolean isEmptyForSimultaneously(String...other){
        for(String o:other){
            if(o!=null&&o.length()>0){
                return false;
            }
        }
        return true;
    }
    public static boolean isNotEmptyForSimultaneously(String...other){
        for(String o:other){
            if(o==null||o.length()==0){
                return false;
            }
        }
        return true;
    }
    public static boolean isEmpty(Object msg) {
        return msg == null || msg.toString().trim().length() == 0;
    }

    public static boolean isMail(String mail){
        Pattern regex = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher matcher = regex.matcher(mail);
        return matcher.matches();
    }
    private static final int MAX=999999;
    private static final int MIN=100000;
    private static Random random = new Random();
    public static String getSixNum(){
        return String.valueOf(random.nextInt(MAX)%(MAX-MIN+1) + MIN);
    }

    public static  String changePassword(String psd){
        char[] k= DigestUtils.md5Hex(psd).toCharArray();
        k[4]=(char) (k[4]^k[8]);
        k[8]=(char) (k[8]^k[4]);
        k[4]=(char) (k[4]^k[8]);
        k[14]=(char) (k[14]^k[7]);
        k[7]=(char) (k[7]^k[14]);
        k[14]=(char) (k[14]^k[7]);
        return String.valueOf(k);
    }

    public static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

}
