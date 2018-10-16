package com.cellsgame.gateway.core;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ResourceBundle;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import com.cellsgame.gateway.message.client.ByteToWebSocketFrameEncoder;
import com.cellsgame.gateway.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServer extends Server{

	public static class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
			ctx.fireChannelRead(msg.content().retain());
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			ctx.close();
			cause.printStackTrace();
		}
	}

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

	private static ResourceBundle sslConfig;

	private static SSLEngine sslEngine;

	private static  boolean sslEnabled = false;

	public WebSocketServer(String name) {
		super(name);
		// 配置文件地址
		sslConfig = ResourceBundle.getBundle("ssl");

//		if(Utils.getBoolean(sslConfig.getString("ssl.enabled"))) {
//			sslEngine = getSslEngine();
//		}
		sslEnabled = Utils.getBoolean(sslConfig.getString("ssl.enabled"));
	}


	public SSLEngine getSslEngine(){
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			KeyStore ks = KeyStore.getInstance("PKCS12");
//			String keyStorePath = "config/kdsg-2.bj.1251321816.clb.myqcloud.com.pfx";
//			String keyPassword = "0zu139sxa73235";
			String keyStorePath = sslConfig.getString("ssl.filePath");
			String keyPassword = sslConfig.getString("ssl.filePass");

			ks.load(new FileInputStream(keyStorePath), keyPassword.toCharArray());
			kmf.init(ks, keyPassword.toCharArray());
			sslcontext.init(kmf.getKeyManagers(), null, null);
			SSLEngine engine = sslcontext.createSSLEngine();
			engine.setUseClientMode(false);
			engine.setNeedClientAuth(false);
			LOG.error("加载到证书,使用wss://协议");
			return engine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.error("未加载到证书,默认使用ws://协议");
		return null;
	}


//	public SSLEngine getSslEngine2(){
//		try {
//			KeyStore ks = KeyStore.getInstance("JKS");
//			InputStream ksInputStream = new FileInputStream("/data/gornix.jks");
//			ks.load(ksInputStream, "123456".toCharArray());
//			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			kmf.init(ks, "654321".toCharArray());
//			SSLContext sslContext = SSLContext.getInstance("TLS");
//			sslContext.init(kmf.getKeyManagers(), null, null);
//			SSLEngine engine = sslContext.createSSLEngine();
//			engine.setUseClientMode(false);
//			engine.setNeedClientAuth(false);
//			return engine;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	@Override
	protected ChannelInitializer<SocketChannel> createInitializer() {
		return new ChannelInitializer<SocketChannel>() {
			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				doInitChannelTimeout(ch);

//				SslContext  sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//
//				pipeline.addLast(sslCtx.newHandler(ch.alloc()));
//				SSLEngine engine = getSslEngine();
				if (sslEnabled) {
					pipeline.addFirst("ssl", new SslHandler(getSslEngine()));
				}

				pipeline.addLast(new HttpServerCodec());
				
				pipeline.addLast(new ChunkedWriteHandler());
				
				pipeline.addLast(new HttpObjectAggregator(64*1024));
				
				pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
				
				pipeline.addLast(new BinaryWebSocketFrameHandler());
				
				pipeline.addLast(new ByteToWebSocketFrameEncoder());

				doInitChannelCodec(ch);
				
			}
		};
	}
	
	
	
	@Override
	public Logger getLogger() {
		return LOG;
	}
}
