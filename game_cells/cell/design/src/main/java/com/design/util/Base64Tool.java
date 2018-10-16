package com.design.util;

import java.util.Base64;

public class Base64Tool {
	public static String Encode(String msg){
		return Encode(msg.getBytes());
	}
	public static String Encode(byte[] msg){
		return Base64.getEncoder().encodeToString(msg);
	}
	public static String Dencode(String val){
		return new String(Decode2(val));
	}
	public static byte[] Decode2(String val){
		return Base64.getDecoder().decode(val);
	}
}
