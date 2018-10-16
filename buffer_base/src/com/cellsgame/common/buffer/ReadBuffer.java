package com.cellsgame.common.buffer;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;


@SuppressWarnings({"unchecked", "rawtypes"})
public class ReadBuffer {

	private IoBuffer buff = IoBuffer.allocate(512).setAutoExpand(true);

	private ReadBuffer() {
	}

	public static ReadBuffer warp(byte[] src) {
		ReadBuffer lin = new ReadBuffer();
		lin.buff = IoBuffer.wrap(src);
		lin.buff.order(ByteOrder.LITTLE_ENDIAN);
		return lin;
	}

	public final byte read_byte() throws IOException {
		return buff.get();
	}

	public final boolean read_bool() throws IOException {
		byte b = read_byte();
		return (b == 1);
	}

	public final int read_int() throws IOException {
		byte tag = buff.get();
		if (tag != Type.TYPE_INT) {
			throw new IOException("read_int:type error");
		}
		return buff.getInt();
	}

	public final long read_long() throws IOException {
		byte tag = buff.get();
		if (tag != Type.TYPE_LONG) {
			throw new IOException("read_int:type error");
		}
		return buff.getLong();
	}

//	public final String read_cstr() throws IOException {
//		byte tag = buff.get();
//		if (tag == Type.TYPE_NIL)
//			return null;
//		else if (tag != Type.TYPE_CSTR) {
//			throw new IOException("read_cstr:type error");
//		}
//		return Helper.getCStr(buff);
//	}
//
//	public final String read_wstr() throws IOException {
//		byte tag = buff.get();
//		if (tag == Type.TYPE_NIL)
//			return null;
//		else if (tag != Type.TYPE_WSTR) {
//			throw new IOException("read_wstr:type error");
//		}
//		return Helper.getWStr(buff);
//	}

	public final Object read_object() throws IOException {
		byte tag = buff.get();
		switch (tag) {
		case Type.TYPE_NIL: {
			return null;
		}
		case Type.TYPE_FALSE: {
			return false;
		}
		case Type.TYPE_TRUE: {
			return true;
		}
		case Type.TYPE_INT: {
			int v = buff.getInt();
			return v;
		}
		case Type.TYPE_LONG: {
			long v = buff.getLong();
			return v;
		}
		case Type.TYPE_DOUBLE: {
			double v = buff.getDouble();
			return v;
		}
//		case Type.TYPE_CSTR: {
//			String v = Helper.getCStr(buff);
//			return v;
//		}
//		case Type.TYPE_WSTR: {
//			String v = Helper.getWStr(buff);
//			return v;
//		}
		case Type.TYPE_TABLE: {
			Map vs = new HashMap();
			int len = buff.getInt();
			for (int i = 0; i < len; i++) {
				Object key = read_object();
				Object var = read_object();
				vs.put(key, var);
			}
			return vs;
		}
		case Type.TYPE_BYTES: {
			int len = buff.getInt();
			byte[] v = Helper.getBytes(buff, len);
			return v;
		}
		case Type.TYPE_INTEGER_ARRAY:{
			int len = buff.getInt();
			Integer[] vs = new Integer[len];
			for (int i = 0; i < len; i++) {
				Object var = read_object();
				vs[i] = (Integer) var;
			}
			return vs;
			
		}
		case Type.TYPE_LONG_ARRAY:{
			int len = buff.getInt();
			Long[] vs = new Long[len];
			for (int i = 0; i < len; i++) {
				Object var = read_object();
				vs[i] = (Long) var;
			}
			return vs;
		}
		case Type.TYPE_STRING_ARRAY:{
			int len = buff.getInt();
			String[] vs = new String[len];
			for (int i = 0; i < len; i++) {
				Object var = read_object();
				vs[i] = (String) var;
			}
			return vs;
			
		}
		case Type.TYPE_LIST:{
			List vs = new ArrayList();
			int len = buff.getInt();
			for (int i = 0; i < len; i++) {
				Object var = read_object();
				vs.add(var);
			}
			return vs;
			
		}
		case Type.STR_0: {
			return "";
		}
		case Type.STR_1: {
			return readStringImpl(1);
		}
		case Type.STR_2: {
			return readStringImpl(2);
		}
		case Type.STR_3: {
			return readStringImpl(3);
		}
		case Type.STR_4: {
			return readStringImpl(4);
		}
		case Type.STR_5: {
			return readStringImpl(5);
		}
		case Type.STR_6: {
			return readStringImpl(6);
		}
		case Type.STR_7: {
			return readStringImpl(7);
		}
		case Type.STR_8: {
			return readStringImpl(8);
		}
		case Type.STR_9: {
			return readStringImpl(9);
		}
		case Type.STR_10: {
			return readStringImpl(10);
		}
		case Type.STR_11: {
			return readStringImpl(11);
		}
		case Type.STR_12: {
			return readStringImpl(12);
		}
		case Type.STR_13: {
			return readStringImpl(13);
		}
		case Type.STR_14: {
			return readStringImpl(14);
		}
		case Type.STR_15: {
			return readStringImpl(15);
		}
		case Type.STR_16: {
			return readStringImpl(16);
		}
		case Type.STR_17: {
			return readStringImpl(17);
		}
		case Type.STR_18: {
			return readStringImpl(18);
		}
		case Type.STR_19: {
			return readStringImpl(19);
		}
		case Type.STR_20: {
			return readStringImpl(20);
		}
		case Type.STR_21: {
			return readStringImpl(21);
		}
		case Type.STR_22: {
			return readStringImpl(22);
		}
		case Type.STR_23: {
			return readStringImpl(23);
		}
		case Type.STR_24: {
			return readStringImpl(24);
		}
		case Type.STR_25: {
			return readStringImpl(25);
		}
		case Type.STR_32: {
			return readStringImpl(32);
		}
		case Type.TYPE_STRING: {
			int len = buff.getInt();
			return readStringImpl(len);
		}
		default:
			throw new IOException("read_object:type error" + tag);
		}
	}
	
	private final String readStringImpl(int length) throws IOException {
		if (length <= 0)
			return "";
		byte[] bytes = new byte[length];
		buff.get(bytes);
		return new String(bytes, "UTF-8");
	}

	public final Map read_table() throws IOException {
		byte tag = buff.get();
		if (tag == Type.TYPE_NIL)
			return null;
		else if (tag != Type.TYPE_TABLE)
			throw new IOException("read_table:type error");

		Map vs = new Hashtable();
		int len = buff.getInt();
		for (int i = 0; i < len; i++) {
			Object key = read_object();
			Object var = read_object();
			vs.put(key, var);
		}
		return vs;
	}

	public final byte[] read_bytes() throws IOException {
		int len = buff.getInt();
		return read_bytes(len);
	}
	
	public final byte[] read_bytes(int len) throws IOException {
		return Helper.getBytes(buff, len);
	}
	
	public void free(){
		buff.free();
	}

}
