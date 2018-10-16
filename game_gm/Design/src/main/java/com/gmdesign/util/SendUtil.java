package com.gmdesign.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2017/6/26.
 *
 * @ClassName SendUtil
 * @Description
 */
public class SendUtil {

    private static void pramsWrite(HttpURLConnection conn,String prams) throws IOException {
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(prams);
        out.flush();
        out.close();
    }
    public static byte[] sendHttpMsgResponseByte(String u,String prams)throws GmException{
        byte[] resData;
        InputStream in=null;
        BufferedInputStream bi = null;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            if(prams!=null){
                pramsWrite(conn,prams);
            }
            conn.connect();
            in = conn.getInputStream();
            bi =new BufferedInputStream(in);
            resData = new byte[conn.getContentLength()];
            int readLength;
            // 数据数组偏移量
            int offset = 0;
            readLength = bi.read(resData, offset, conn.getContentLength());
            // 已读取的长度
            int readAlreadyLength = readLength;
            while (readAlreadyLength < conn.getContentLength()) {
                readLength = bi.read(resData, readAlreadyLength, conn.getContentLength()-readAlreadyLength);
                readAlreadyLength = readAlreadyLength + readLength;
            }
        }catch (Exception e) {
            e.printStackTrace();
            throw new GmException(e.getMessage());
        }finally {
            if (bi!=null){
                try {
                    bi.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resData;
    }
    public static String sendHttpMsg(String u,String prams) throws GmException{
        String c;
        try {
            URL url = new URL(u);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            if(prams!=null){
                pramsWrite(conn,prams);
            }
            InputStream in = conn.getInputStream();
            BufferedReader br =new BufferedReader(new InputStreamReader(in));
            c=br.readLine();
            br.close();
            in.close();
            conn.disconnect();
        } catch (Exception e) {
            throw new GmException(e.getMessage());
        }
        return c;
    }
}
