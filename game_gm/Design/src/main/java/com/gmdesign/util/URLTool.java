package com.gmdesign.util;

import com.gmdesign.exception.GmException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by DJL on 2017/6/1.
 *
 * @ClassName cells
 * @Description
 */
public class URLTool {
    public static String Encode(String data) {
        try {
            return java.net.URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String Dncode(String data) {
        try {
            return java.net.URLDecoder.decode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String sendMsg (String fullAddress)throws GmException {
        return sendMsg(fullAddress,null);
    }

    public static String sendMsg(String fullAddress,String msg,String rType)throws GmException {
        StringBuilder res=new StringBuilder(512);
        try {
            URL url = new URL(fullAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(rType);
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            OutputStream out =  conn.getOutputStream();
            if(msg!=null){
                out.write(msg.getBytes("utf-8"));
            }
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader br =new BufferedReader(new InputStreamReader(in));
            String r;
            while ((r=br.readLine())!=null){
                res.append(r);
            }
            conn.disconnect();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GmException(e.getMessage(),50001);
        }
        return res.toString();
    }

    public static String sendMsg(String fullAddress,String msg) throws GmException {
        return sendMsg(fullAddress,msg,"POST");
    }
}
