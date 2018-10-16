package com.cellsgame.common.buffer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.mina.core.buffer.IoBuffer;

@SuppressWarnings({"rawtypes"})
public class WriteBuffer {
	
	public IoBuffer buff = null;

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private WriteBuffer(){};
	
	public final static WriteBuffer allocate(int size) {
		WriteBuffer bout = new WriteBuffer();
		bout.buff = IoBuffer.allocate(size).setAutoExpand(true);
		bout.buff.order(ByteOrder.LITTLE_ENDIAN);
		return bout;
	}

	public final void write_nil() {
		buff.put(Type.TYPE_NIL);
	}

	public final void write_bool(boolean v) {
		if (v)
			buff.put(Type.TYPE_TRUE);
		else
			buff.put(Type.TYPE_FALSE);
	}

	public final void write_int(int v) throws IOException {
		buff.put(Type.TYPE_INT);
		Helper.putInt(buff, v);
	}

	public final void write_double(double v) throws  IOException {
		buff.put(Type.TYPE_DOUBLE);
		Helper.putDouble(buff, v);
	}
	
	public final void write_long(long v) throws IOException {
		buff.put(Type.TYPE_LONG);
		Helper.putLong(buff, v);
	}

//	public final void write_cstr(String v) throws IOException {
//		buff.put(Type.TYPE_CSTR);
//		Helper.putCStr(buff, v);
//	}
	
	public final void write_str(String v) throws IOException {
		byte[] b = v.getBytes("UTF-8");
		int len = b.length;
		switch (len) {
		case 0:
			buff.put(Type.STR_0);
			break;
		case 1:
			buff.put(Type.STR_1);
			Helper.putBytes(buff, b);
			break;
		case 2:
			buff.put(Type.STR_2);
			Helper.putBytes(buff, b);
			break;
		case 3:
			buff.put(Type.STR_3);
			Helper.putBytes(buff, b);
			break;
		case 4:
			buff.put(Type.STR_4);
			Helper.putBytes(buff, b);
			break;
		case 5:
			buff.put(Type.STR_5);
			Helper.putBytes(buff, b);
			break;
		case 6:
			buff.put(Type.STR_6);
			Helper.putBytes(buff, b);
			break;
		case 7:
			buff.put(Type.STR_7);
			Helper.putBytes(buff, b);
			break;
		case 8:
			buff.put(Type.STR_8);
			Helper.putBytes(buff, b);
			break;
		case 9:
			buff.put(Type.STR_9);
			Helper.putBytes(buff, b);
			break;
		case 10:
			buff.put(Type.STR_10);
			Helper.putBytes(buff, b);
			break;
		case 11:
			buff.put(Type.STR_11);
			Helper.putBytes(buff, b);
			break;
		case 12:
			buff.put(Type.STR_12);
			Helper.putBytes(buff, b);
			break;
		case 13:
			buff.put(Type.STR_13);
			Helper.putBytes(buff, b);
			break;
		case 14:
			buff.put(Type.STR_14);
			Helper.putBytes(buff, b);
			break;
		case 15:
			buff.put(Type.STR_15);
			Helper.putBytes(buff, b);
			break;
		case 16:
			buff.put(Type.STR_16);
			Helper.putBytes(buff, b);
			break;
		case 17:
			buff.put(Type.STR_17);
			Helper.putBytes(buff, b);
			break;
		case 18:
			buff.put(Type.STR_18);
			Helper.putBytes(buff, b);
			break;
		case 19:
			buff.put(Type.STR_19);
			Helper.putBytes(buff, b);
			break;
		case 20:
			buff.put(Type.STR_20);
			Helper.putBytes(buff, b);
			break;
		case 21:
			buff.put(Type.STR_21);
			Helper.putBytes(buff, b);
			break;
		case 22:
			buff.put(Type.STR_22);
			Helper.putBytes(buff, b);
			break;
		case 23:
			buff.put(Type.STR_23);
			Helper.putBytes(buff, b);
			break;
		case 24:
			buff.put(Type.STR_24);
			Helper.putBytes(buff, b);
			break;
		case 25:
			buff.put(Type.STR_25);
			Helper.putBytes(buff, b);
			break;
		case 32:
			buff.put(Type.STR_32);
			Helper.putBytes(buff, b);
			break;
		default:
			buff.put(Type.TYPE_STRING);
			Helper.putLenBytes(buff, b);
			break;
		}
	}

//	public final void write_wstr(String v) throws IOException {
//		buff.put(Type.TYPE_WSTR);
//		Helper.putWStr(buff, v);
//	}

	public void write_table(Map v) throws IOException {
		int len = v.size();
		buff.put(Type.TYPE_TABLE); // type
		buff.putInt(len); // num
		Iterator it = v.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object var = v.get(key);
			
			write_object(key);
			write_object(var);
		}
	}
	
	public void write_table(Set v) throws IOException {
		int len = v.size();
		buff.put(Type.TYPE_TABLE); // type
		buff.putInt(len); // num
		Integer p = 1;
		for (Object o : v) {
			Integer k = p++;
			write_object(k);
			write_object(o);
		}
	}
	
	public void write_table(List v) throws IOException {
		int len = v.size();
		buff.put(Type.TYPE_TABLE); // type
		buff.putInt(len); // num
		Integer p = 1;
		for (Object o : v) {
			Integer k = p++;
			write_object(k);
			write_object(o);
		}
	}
	
	
	public void write_list(List v) throws IOException {
		int len = v.size();
		buff.put(Type.TYPE_LIST);
		buff.putInt(len);
		for (Object o : v) {
			write_object(o);
		}
	}
	
	public void write_array(boolean[] v) throws IOException {
		int len = v.length;
		buff.put(Type.TYPE_LIST);
		buff.putInt(len);
		for (boolean o : v) {
			write_object(o);
		}
	}
	
	
	
	public void write_array(Integer[] v) throws IOException {
		int len = v.length;
		buff.put(Type.TYPE_INTEGER_ARRAY);
		buff.putInt(len);
		for (Integer o : v) {
			write_object(o);
		}
	}
	
	public void write_array(String[] v) throws IOException {
		int len = v.length;
		buff.put(Type.TYPE_STRING_ARRAY);
		buff.putInt(len);
		for (String o : v) {
			write_object(o);
		}
	}
	
	
	public void write_array(int[] v) throws IOException{
		int len = v.length;
		buff.put(Type.TYPE_INTEGER_ARRAY);
		buff.putInt(len);
		for (int o : v) {
			write_object(o);
		}
	}

	public void write_array(long[] v) throws IOException{
		int len = v.length;
		buff.put(Type.TYPE_LONG_ARRAY);
		buff.putInt(len);
		for (long o : v) {
			write_object(o);
		}
	}

	public void write_array(Long[] v) throws IOException{
		int len = v.length;
		buff.put(Type.TYPE_LONG_ARRAY);
		buff.putInt(len);
		for (Long o : v) {
			write_object(o);
		}
	}
	

	public void write_bytes(byte[] b){
		int len = b.length;
		buff.put(Type.TYPE_BYTES);
		buff.putInt(len);
		buff.put(b);
	}
	
	public final void write_object(Object o) throws IOException {
		if (o == null){
			write_nil();
		}else if (o instanceof Set){
			Set v = (Set) o;
			write_table(v);
		}else if (o instanceof Map){
			Map v = (Map) o;
			write_table(v);
		}else if (o instanceof List){
			List v = (List) o;
			write_list(v);
		}else if (o instanceof Integer){
			int v = ((Integer) o).intValue();
			write_int(v);
		}else if(o instanceof Integer[]){
			Integer[] v = (Integer[])o;
			write_array(v);
		}else if(o instanceof int[]){
			int[] v = (int[])o;
			write_array(v);
		}else if(o instanceof String[]){
			String[] v = (String[])o;
			write_array(v);
		}
		else if (o instanceof String){
			String s = (String) o;
			write_str(s);
//			if(WStr.isWStr(s)){
//				write_wstr(s);
//			}else{
//				write_cstr(s);
//			}
		}else if (o instanceof Boolean){
			boolean v = ((Boolean) o).booleanValue();
			write_bool(v);
		}else if (o instanceof byte[]){
			byte[] v = (byte[]) o;
			write_bytes(v);
		}else if (o instanceof Date){
			String s = sdf.format(o);
			write_str(s);
		}else if (o instanceof Long){
			long v = ((Long) o).longValue();
			write_long(v);
		} else if (o instanceof Double){
			double v = (Double) o;
			write_double(v);
		}else if(o instanceof boolean[]){
			boolean[] v = (boolean[]) o;
			write_array(v);
		}else if(o instanceof  long[]){
			long[] v = (long[])o;
			write_array(v);
		}else if(o instanceof  Long[]){
			Long[] v = (Long[])o;
			write_array(v);
		}
		else{
			throw new IOException("unsupported object:"+o + " type:" + o.getClass().getName());
		}
	}
	
	public final byte[] toByteArray() throws IOException{
		return Helper.toByteArray(buff);
	}

	public static void main(String[] args) {
		Float a = 0.5233F;
		System.out.println(a.intValue());
	}

	public final void toFile(String file) throws IOException{
		FileOutputStream fos = new FileOutputStream(file);
		byte[] b = toByteArray();
		fos.write(b);
		fos.close();
		fos = null;
	}
	
	
	public  void free(){
		buff.free();
	}
	
	public void capacity(int newCapacity){
		buff.capacity(newCapacity);
	}
}
