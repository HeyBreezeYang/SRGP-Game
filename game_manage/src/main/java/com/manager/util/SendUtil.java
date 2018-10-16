package com.manager.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.manager.exception.ManageException;

/**
 * Created by DJL on 2017/6/26.
 *
 * @ClassName SendUtil
 * @Description
 */
public class SendUtil {
    public static void sendHttpMsg(String u) throws ManageException{
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            OutputStream out =  conn.getOutputStream();
            out.flush();
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader br =new BufferedReader(new InputStreamReader(in));
            String c=br.readLine();
            System.out.println(c);
            conn.disconnect();
        } catch (Exception e) {
            throw new ManageException(e.getMessage());
        }
    }
}
