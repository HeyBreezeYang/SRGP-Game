package com.cellsgame;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;

public class TestClient {
    @Test
    public void TestClient() {

        Socket client = null;
        OutputStream outputStream = null;
        OutputStreamWriter ow = null;

        try {
                // 创建一个socket对象，并建立网络连接（可以使用以下三种方式任何一种）
                client = new Socket("192.168.10.120", 9999);
//              client = new Socket("127.0.0.1",8989);
//              client = new Socket(InetAddress.getByName("127.0.0.1"),8989);
                System.out.println("client socket " + client.hashCode() + ",建立了socket实例时间：" + new Date());
                //设置等待
//                client.setSoTimeout(15000);
                client.setKeepAlive(true);
                //获取远程请求地址和端口
                InetAddress inetAddress = client.getInetAddress();
                int port = client.getPort();
                String hostAddress = inetAddress.getHostAddress();
                String hostName = inetAddress.getHostName();
                InetAddress localHost = inetAddress.getLocalHost();
                System.out.println("远程主机 ：" + inetAddress + ",远程端口：" + port + ",远程hostAddress：" + hostAddress + ",远程hostName：" + hostName);

                //获取发起的地址和端口
                InetAddress localAddress = client.getLocalAddress();
                int localPort = client.getLocalPort();
                String hostAddress2 = localAddress.getHostAddress();
                String hostName2 = localAddress.getHostName();
                System.out.println("本地主机 ：" + localAddress + ",本地端口 :" + localPort + ",本地hostAddress：" + hostAddress2 + ",本地hostName：" + hostName2);
                //打开输出流，要发送消息
                outputStream = client.getOutputStream();


                ByteBuffer buffer= ByteBuffer.allocate(12);
                buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(8).put("20180818".getBytes("UTF-8"));


                outputStream.write(buffer.array());
                outputStream.flush();
//                ow = new OutputStreamWriter(outputStream);
//              ow.write("I'm jane,please take care!");
//                ow.flush();
                // 关掉输出流，说明客户端已经写完了
                client.shutdownOutput();

                //获取输入流，接收服务器返回的消息
                InputStream inputStream = client.getInputStream();
                InputStream is=inputStream;
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String info=null;
                while((info=br.readLine()) == null) {
                    Thread.yield();
                }
                System.out.println("结束了"+info);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }
}