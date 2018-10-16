package com.cellsgame.common.buffer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Helper {
	public static ByteOrder order = ByteOrder.LITTLE_ENDIAN;

	public static final int getInt(IoBuffer buf) throws IOException {
		buf.order(order);
		return buf.getInt();
	}

	public static final void putInt(IoBuffer buf, int v) throws IOException {
		buf.order(order);
		buf.putInt(v);
	}

	public static void putLong(IoBuffer buf, long v) {
		buf.order(order);
		buf.putLong(v);
	}

	public static void putDouble(IoBuffer buf, double v) {
		buf.order(order);
		buf.putDouble(v);
	}
	
	public static final String getStr(IoBuffer buf) throws IOException {
//		int MAX_LENGTH = 8 * 1024;
//		IoBuffer buff = IoBuffer.allocate(MAX_LENGTH);
//		for (int n = 0; n < MAX_LENGTH; n++) {
//			byte b1 = buf.get();
//			if (b1 == '\0') {
//				break;
//			}
//			buff.put(b1);
//		}
//		byte[] b = toByteArray(buff);
		return new String(getLenBytes(buf), "UTF-8");
	}

	public static final void putStr(IoBuffer buf, String v)
			throws IOException {
		byte[] b = v.getBytes("UTF-8");
		putLenBytes(buf, b);
	}
	

//	public static final String getWStr(IoBuffer buf) throws IOException {
//		int MAX_LENGTH = 8 * 1024;
//		IoBuffer buff = IoBuffer.allocate(MAX_LENGTH);
//		for (int n = 0; n < MAX_LENGTH; n++) {
//			byte b1 = buf.get();
//			byte b2 = buf.get();
//			if (b1 == '\0' && b2 == '\0') {
//				break;
//			}
//			buff.put(b1);
//			buff.put(b2);
//		}
//		byte[] b = toByteArray(buff);
//		return new String(b, "UTF-16LE");
//	}
//
//	public static final void putWStr(IoBuffer buf, String v)
//			throws IOException {
//		byte[] b = v.getBytes("UTF-16LE");
//		buf.put(b);
//		buf.put((byte) '\0');
//		buf.put((byte) '\0');
//	}

	public static final byte[] getBytes(IoBuffer buf, int len)
			throws IOException {
		byte[] b = new byte[len];
		for (int n = 0; n < len; n++) {
			b[n] = buf.get();
		}
		return b;
	}

	public static final void putBytes(IoBuffer buf, byte[] v) {
		buf.put(v);
	}

	public static final byte[] getBytes(IoBuffer buf, int offset, int len)
			throws IOException {
		byte[] b = new byte[len];
		for (int n = offset; n < offset + len; n++) {
			b[n] = buf.get(n);
		}
		return b;
	}

	public static final void putBytes(IoBuffer buf, byte[] v, int offset,
			int len) {
		int end = offset + len;
		for (int n = offset; n < end; n++) {
			buf.put(v[n]);
		}
	}

	public static final byte[] getLenBytes(IoBuffer buf) throws IOException {
		int len = getInt(buf);
		return getBytes(buf, len);
	}

	public static final void putLenBytes(IoBuffer buf, byte[] v)
			throws IOException {
		putInt(buf, v.length);
		putBytes(buf, v);
	}

	// ////////////////////////////////////////////////////////////////////////////
	public static final byte[] toByteArray(IoBuffer buf) throws IOException {
		return getBytes(buf, 0, buf.position());
	}

	public final static byte[] objectToBytes(Object o) throws IOException {
		WriteBuffer out = WriteBuffer.allocate(1024);
		out.write_object(o);
		byte[] ret  = out.toByteArray();
		out.free();
		return ret;
	}

	public final static byte[] objectToBytes(Object o, int buffLen)	throws IOException {
		WriteBuffer out = WriteBuffer.allocate(buffLen);
		out.write_object(o);
		byte[] ret  = out.toByteArray();
		out.free();
		return ret;
	}

	public final static byte[] mapToBytes(Map m) throws IOException {
		return objectToBytes(m);
	}

	public final static byte[] mapToBytes(Map m, int buffLen)
			throws IOException {
		return objectToBytes(m, buffLen);
	}

	// ////////////////////////////////////////////////////////////////////////////
	public final static ReadBuffer fromFile(String file) throws IOException {
		File f = new File(file);
		FileInputStream fis = new FileInputStream(f);
		DataInputStream dis = new DataInputStream(fis);
		int length = (int) f.length();
		byte[] b = new byte[length];
		dis.readFully(b);
		dis.close();
		fis.close();
		dis = null;
		fis = null;
		f = null;
		return ReadBuffer.warp(b);
	}

	public final static Object loadFromFile(String file) throws IOException {
		ReadBuffer in = fromFile(file);
		return in.read_object();
	}

	public final static Map mapFromBytes(byte[] b) throws IOException {
		return (Map) objectFromBytes(b);
	}

	public final static <T> T objectFromBytes(byte[] b) throws IOException {
		ReadBuffer in = ReadBuffer.warp(b);
		T ret =  (T) in.read_object();
		in.free();
		return ret;
	}
	// ////////////////////////////////////////////////////////////////////////////
}
