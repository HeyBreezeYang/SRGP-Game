package com.master.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings( { "deprecation", "rawtypes" })
public class DecodeHelper {

	public static void decodeUrl(HttpServletRequest req) throws IOException {
		String qstr = req.getQueryString();
		if (qstr != null && !qstr.isEmpty()) {
			int p = qstr.indexOf("#");
			if (p > 0)
				qstr = qstr.substring(0, p);
		}
		decodeUrl(req, qstr);
	}

	public static void decodeUrl(HttpServletRequest req, String q)
			throws IOException {
		if (q == null)
			return;

		if (q.length() < 1)
			return;

		q = java.net.URLDecoder.decode(q, "UTF-16LE");
		
		Hashtable ht = javax.servlet.http.HttpUtils.parseQueryString(q);
		Enumeration keys = ht.keys();

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String[] value = (String[]) ht.get(key);
			if (value != null && value.length > 0) {
				if (value.length == 1)
					req.setAttribute(key, value[0]);
				else
					req.setAttribute(key, value);
			}
		}

	}

	@SuppressWarnings("unused")
	private static String s(byte[] a) throws UnsupportedEncodingException {
		byte[] b = new byte[(a.length + (a.length % 2)) + 2];
		b[0] = (byte) 0xFF;
		b[1] = (byte) 0xFE;
		System.arraycopy(a, 0, b, 2, a.length);
		return new String(b, "Unicode");
	}

	private static final int _MAXUPLOAD = 100 * 1024;
	private static final int _TRYTIME = 10 * 100;
	private static final int _SLEEPTIME = 5; // 50 ms
	private static final int _BUFFSIZE = 128 * 1024;

	public static int MAX_FILE_IN_MEMERY = 1024 * 1024 * 2;

	public static byte[] getPostBuffer(HttpServletRequest request) {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int trys = 0;
		try {
			int len = request.getContentLength();
			if (len > _MAXUPLOAD)
				return null;

			byte[] buff = new byte[_BUFFSIZE];
			InputStream in = request.getInputStream();

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
		} catch (Exception e) {
		} finally {
			buf = null;
		}
		return null;
	}
}
