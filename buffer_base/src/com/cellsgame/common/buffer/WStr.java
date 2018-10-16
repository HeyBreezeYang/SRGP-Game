package com.cellsgame.common.buffer;

public class WStr {
	public static String toWChar(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);

			}
		}
		return new String(c);
	}

	public static String toChar(String input) {

		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}
	
	public static final boolean isWStr(String s){
		try {
			byte[] b = s.getBytes("UTF-8");
			for (int i = 0; i < b.length; i++) {
				if (b[i] >= 127 || b[i] < 0) {
					return true; //isw = true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}	
}
