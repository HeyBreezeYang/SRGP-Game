package com.design.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by yfzhang on 2017/7/20.
 */
public class HttpUtil {

    public static String doPost(String postUrl, String params) throws Exception {
        URL url = new URL(postUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(params.length()));

        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            outputStream = conn.getOutputStream();
            outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(params);
            outputStreamWriter.flush();
            //响应失败
            if (conn.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + conn.getResponseCode());
            }
            //接收响应流
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        }finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }

    private static final int _MAXUPLOAD = 100 * 1024;
    private static final int _TRYTIME = 10 * 100;
    private static final int _SLEEPTIME = 5; // 50 ms
    private static final int _BUFFSIZE = 128 * 1024;

    public static int MAX_FILE_IN_MEMERY = 1024 * 1024 * 2;

    public static byte[] getPostBuffer(HttpURLConnection conn) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int trys = 0;
        try {
            int len = conn.getContentLength();
            if (len > _MAXUPLOAD)
                return null;
            byte[] buff = new byte[_BUFFSIZE];
            InputStream in = conn.getInputStream();
            do {
                int r = in.read(buff);
                if (r < 0)
                    break;
                if (r > 0)
                    buf.write(buff, 0, r);

                if (trys++ >= _TRYTIME)
                    return null;
                if (buf.size() < len)
                    Thread.sleep(_SLEEPTIME);
            } while (buf.size() < len);
            buff = buf.toByteArray();
            if (buff == null || buff.length < 1)
                return null;
            return buff;
        } catch (Exception ignored) {
        }
        return null;
    }

   /* public static Map<?, ?> postRequest(String postUrl, Object[] params) throws Exception {
        String postParams = buildParams(params);
        byte[] sidbytes = Helper.objectToBytes(postParams);
        URL url = new URL(postUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        conn.setRequestProperty("Content-Length", Integer.toString(sidbytes.length));
        OutputStream out = conn.getOutputStream();
        out.write(sidbytes);
        out.flush();
        out.close();
        byte[] buff = getPostBuffer(conn);
        if (buff == null || buff.length < 1) {
            throw new Exception("get data error");
        }
        return (Map<?, ?>) Helper.mapFromBytes(buff);
    }

    private static String buildParams(Object[] params) {
        StringBuilder str = new StringBuilder();
        for (Object param : params) {
            str.append(param).append('&');
        }
        return str.toString();
    }*/
}
