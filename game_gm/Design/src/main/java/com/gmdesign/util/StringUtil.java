package com.gmdesign.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.gmdesign.exception.GmException;
import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

/**
 * @ClassName StringUtil
 * @Description String封装
 * @author xiaoyu Mo (liujun)
 * @date 2014-4-8 下午4:23:41
 */
public class StringUtil {
	public static boolean isNumeric2(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (Character.isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
	/**
	 * 将String转换为Integer
	 * 
	 * @param s
	 * @return
	 */
	public static Integer parseInt(String s) {
		Integer n = 0;
		try {
			n = Integer.parseInt(s);
		} catch (Exception e) {

		}
		return n;
	}

	/**
	 * 将String转换为Float
	 * 
	 * @param s
	 * @return
	 */
	public static Float parseFloat(String s) {
		Float f = 0.0f;
		try {
			f = Float.parseFloat(s);
		} catch (Exception e) {

		}
		return f;
	}

	/**
	 * 将String转换为Double
	 * 
	 * @param s
	 * @return
	 */
	public static double parseDouble(String s) {
		Double d = 0.0;
		try {
			d = Double.parseDouble(s);
		} catch (Exception e) {

		}
		return d;
	}

	/**
	 * 字符串转换为日期 更多日期处理的请参�?：DateUtil.java
	 * 
	 * @param s
	 * @return
	 */
	public static Date strToDate(String s) {
		Date date = new Date();
		ParsePosition pos = new ParsePosition(0);
		;
		try {
			if (s.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				date = sdf.parse(s, pos);
			}
		} catch (Exception e) {

		}
		return date;
	}

	public static String join(Object... os) {
		StringBuilder sb = new StringBuilder();
		for (Object o : os) {
			sb.append(o);
		}
		return sb.toString();
	}

	/* 是否是中文符号 */
	// 逗号
	public static boolean isChineseSign(String str) {
		if (str.indexOf("，") != -1) {
			return true;
		}
		return false;
	}

	public static boolean chineseCheck(String str) {
		char[] ch = { '｛', '｝', '（', '）', '：', '—', '，', '；', '。' };
		for (char c : ch) {
			if (str.indexOf(c) != -1) {
				return true;
			}
		}
		return false;
	}


	/**
	 * @param str
	 * @return 去掉前后空格的字符串
	 */
	public static String trim(String str) {
		if (str == null)
			return "";
		str = str.trim();
		while (str.startsWith(" ")) {
			str = str.substring(1, str.length()).trim();
		}
		return str;
	}


	/**
	 * TODO 整型数组转字符串
	 * 
	 * @param arry
	 * @return
	 */
	public static String IntegerArrayToString(Integer[] arry) {
		if (arry == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for (int num : arry) {
			str.append(num);
			str.append(",");
		}
		return str.toString();
	}

	public static <T> String arrayToString(T[] arry) {
		if (arry == null) {
			return "";
		}
		StringBuilder sb=new StringBuilder();
		if(arry.length>0){
			sb.append(arry[0]);
			for(int i=1;i<arry.length;i++){
				sb.append(",");
				sb.append(arry[i]);
			}			
		}
		return sb.toString();
	}

	/**
	 * TODO 字符串转整型数组
	 * 
	 * @param str
	 * @param regex
	 *            分隔符
	 * @return
	 */
	public static int[] StringToIntArray(String str, String regex) {
		if (str == null || str.equals("")) {
			return new int[] {};
		}
		int num_arry[];
		try {
			String str_arry[] = str.split(regex);
			num_arry = new int[str_arry.length];
			for (int i = 0; i < str_arry.length; i++) {
				if (str_arry[i].equals(""))
					continue;
				num_arry[i] = Integer.parseInt(str_arry[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new int[] {};
		}
		return num_arry;
	}

	// 判断参数是否为空
	public static boolean paramJudge(String... param) {
		if (param == null) {
			return false;
		}
		for (String par : param) {
			if (par == null || "".equals(par)) {
				return false;
			}
		}
		return true;
	}

	// 把布尔值转换为中文
	public static String booleanToChinese(boolean flag) {
		if (flag) {
			return "成功";
		}
		return "失败";
	}

	// 把布尔值转换为字符串
	public static String booleanToString(boolean flag) {
		if (flag) {
			return "true";
		}
		return "false";
	}

	public static String intToChinese(int num) {
		if (num == 0) {
			return "失败";
		}
		return "成功";
	}

	/**
	 * TODO 计算初始条数
	 * 
	 * @param page
	 *            第几页
	 * @param row
	 *            每页好多行
	 * @return 初始行数
	 */
	public static int startRow(int page, int row) {
		return (page - 1) * row;
	}

	// 根据数量的多少生成相应的问号字符串
	public static String getQuestionMarkString(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			if (i < num - 1) {
				sb.append("?,");
			} else {
				sb.append("?");
			}
		}
		return sb.toString();
	}

	/**
	 * TODO is null ||"" return "0"
	 *
	 * @param obj
	 * @return
	 */
	public static String isNullString(Object obj) {
		if (obj == null || "".equals(obj)) {
			return "0";
		}
		return String.valueOf(obj);
	}

	/**
	 * TODO
	 * 
	 * @param obj
	 * @return
	 */
	public static int objToint(Object obj) {
		return Integer.parseInt(isNullString(obj));
	}

	/**
	 * TODO deflute 0 array
	 * 
	 * @param length
	 * @return
	 */
	public static Object[] newObjectArray(int length) {
		Object[] obj = new Object[length];
		for (int i = 0; i < length; i++) {
			obj[i] = 0;
		}
		return obj;
	}

	/**
	 * @param sql
	 * @return 判断sql能否对数据库造成改动 正确sql返回true 错误返回false
	 */
	public static boolean isCorrectSql(String sql) {
		sql = sql.toUpperCase();
		List<String> keywordlist = new ArrayList<String>();
		keywordlist.add("DELETE");
		keywordlist.add("UPDATE");
		keywordlist.add("DROP");
		keywordlist.add("ALTER");
		keywordlist.add("CREATE");
		keywordlist.add("TRUNCATE");
		keywordlist.add("TABLE");
		keywordlist.add("DATABASE");
		keywordlist.add("ADD");
		keywordlist.add("SHOW");
		keywordlist.add("USE");
		keywordlist.add("GRANT");
		keywordlist.add("GRANTS");
		keywordlist.add("PROCEDURE");
		keywordlist.add("*");
		for (String keyword : keywordlist) {
			if (sql.indexOf(keyword) != -1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 字符串是否纯数字组成
	 * 
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < '0' || str.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}
	public static String changePassword(String psd){
		char[] k=DigestUtils.md5Hex(psd).toCharArray();
		k[4]=(char) (k[4]^k[8]);
		k[8]=(char) (k[8]^k[4]);
		k[4]=(char) (k[4]^k[8]);
		k[14]=(char) (k[14]^k[7]);
		k[7]=(char) (k[7]^k[14]);
		k[14]=(char) (k[14]^k[7]);
		return String.valueOf(k);
	}

	public static String byte2hex(byte[] b) {  
        StringBuilder hs = new StringBuilder();  
        String stmp;  
        for (int n = 0; b!=null && n < b.length; n++) {  
            stmp = Integer.toHexString(b[n] & 0XFF);  
            if (stmp.length() == 1)  
                hs.append('0');  
            hs.append(stmp);  
        }  
        return hs.toString().toUpperCase();  
    }  
      
	public static byte[] hex2byte(byte[] b) {  
        if((b.length%2)!=0)  
            throw new IllegalArgumentException();  
        byte[] b2 = new byte[b.length/2];  
        for (int n = 0; n < b.length; n+=2) {  
            String item = new String(b,n,2);  
            b2[n/2] = (byte)Integer.parseInt(item,16);  
        }  
        return b2;  
    }  

	public static String coverTimeConfig(String[] config)throws GmException {
		if (config.length!=7){
			throw new GmException("不是定时配置~");
		}
		if(config[3].equals("")&&config[5].equals("")){
			config[5]="?";
		}else if(!(config[3].equals("?")||config[5].equals("?"))){
			if(config[3].equals("")){
				config[3]="?";
			}else if(config[5].equals("")){
				config[5]="?";
			}else{
				config[5]="?";
			}
		}

		StringBuilder sb =new StringBuilder(32);
		sb.append(config[0].equals("")?"*":config[0]);
		for(int i=1;i<config.length;i++){
			sb.append(" ").append(config[i].equals("")?"*":config[i]);
		}
		return sb.toString();
	}

	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	private static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(encryptKey.getBytes());
		kgen.init(128, secureRandom);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
		return cipher.doFinal(content.getBytes("utf-8"));
	}

	public static String encodeUrlForJson(Object s) throws UnsupportedEncodingException {
		return URLEncoder.encode(JSON.toJSONString(s),"UTF-8");
	}
}
