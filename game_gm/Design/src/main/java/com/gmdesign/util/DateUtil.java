package com.gmdesign.util;




import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.DATE;

/** 
 * @ClassName: DateUtil 
 * @Description: 时间工具类
 * @author xiaoyu Mo (liujun) 
 * @date 2014-3-7 上午10:28:48  
 */
public class DateUtil {

	/** 本地化 */
	private static Locale locale = Locale.SIMPLIFIED_CHINESE;

	/** 缺省的DateFormat对象，可以将一个Date格式化成 yyyy-mm-dd 输出 */
	private static DateFormat dateDF = DateFormat. getDateInstance(
			DateFormat.MEDIUM, locale);

	/** 缺省的DateFormat对象，可以将一个Date格式化成 HH:SS:MM 输出 */
	private static DateFormat timeDF = DateFormat.getTimeInstance(
			DateFormat.MEDIUM, locale);

	/** 缺省的DateFormat对象，可以将一个Date格式化成 yyyy-mm-dd HH:SS:MM 输出 */
	private static DateFormat datetimeDF = DateFormat.getDateTimeInstance(
			DateFormat.MEDIUM, DateFormat.MEDIUM, locale);

	/**
	 * 私有构造函数，表示不可实例化
	 */
	private DateUtil() {
	}

	/**
	 * 返回一个当前的时间，并按格式转换为字符串 例：17:27:03
	 * 
	 * @return String
	 */
	public static String getTime() {
		Date dNow = gcNow.getTime();
		return timeDF.format(dNow);
	}
	private static GregorianCalendar gcNow = new GregorianCalendar();

	/**
	 * 返回一个当前日期，并按格式转换为字符串 例：2009-12-12
	 * 
	 * @return String
	 */
	public static String getDate() {
		Date dNow = gcNow.getTime();
		return dateDF.format(dNow);
	}

	/**
	 * 返回一个当前日期和时间，并按格式转换为字符串 例：2009-12-08 14:27:03
	 * 
	 * @return String
	 */
	public static String getDateTime() {
		Date dNow = gcNow.getTime();
		return datetimeDF.format(dNow);
	}

	/**
	 * 返回当前年的年号
	 * 
	 * @return int
	 */
	public static int getYear() {
		return gcNow.get(GregorianCalendar.YEAR);
	}

	/**
	 * 返回本月月号：从 0 开始
	 * 
	 * @return int
	 */
	public static int getMonth() {
		return gcNow.get(GregorianCalendar.MONTH);
	}

	/**
	 * 返回今天是本月的第几天
	 * 
	 * @return int 从1开始
	 */
	public static int getToDayOfMonth() {
		return gcNow.get(GregorianCalendar.DAY_OF_MONTH);
	}

	/**
	 * 返回一格式化的日期
	 * 
	 * @param date
	 *            Date
	 * @return String yyyy-mm-dd 格式
	 */
	public static String formatDate(Date date) {
		return dateDF.format(date);
	}

	/**
	 * 返回一格式化的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(long date) {
		return formatDate(new Date(date));
	}

	/**
	 * 返回一格式化的时间
	 * 
	 * @param date
	 *            Date
	 * @return String hh:ss:mm 格式
	 */
	public static String formatTime(Date date) {
		return timeDF.format(date);
	}

	/**
	 * 返回一格式化的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(long date) {
		return formatTime(new Date(date));
	}

	/**
	 * 返回一格式化的日期时间
	 * 
	 * @param date
	 *            Date
	 * @return String yyyy-mm-dd hh:mm:ss 格式
	 */
	public static String formatDateTime(Date date) {
		return datetimeDF.format(date);
	}

	/**
	 * 返回一格式化的日期时间
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(long date) {
		return formatDateTime(new Date(date));
	}

	/**
	 * 将字串转成日期和时间，字串格式: yyyy-MM-dd HH:mm:ss
	 * 
	 * @param string
	 *            String
	 * @return Date
	 */
	public static Date toDateTime(String string) {
		return toDate("yyyy-MM-dd HH:mm:ss",string);
	}
	public static Date toDate3(String string) {
		return toDate("dd/MM/yyyy HH:mm",string);
	}
	/**
	 * 将字串转成日期，字串格式: yyyy-MM-dd
	 *            String
	 * @return Date
	 */
	private static Date toDate(String formate,String time){
		try {
			sdf.applyPattern(formate);
			return sdf.parse(time);
		} catch (Exception ex) {
			return null;
		}
	}
	public static Date toDate(String string) {
		return toDate("yyyy-MM-dd",string);
	}
	public static Date toDateForMonth(String string) {
		return toDate("yyyy-MM",string);
	}


	public static Date toDate4(String string) {
		return toDate("yyyy-MM-dd h:mm:ss a",string);
	}
	/**
	 * 取值：某日期的年号
	 * 
	 * @param date
	 *            格式: yyyy-MM-dd
	 * @return
	 */
	public static int getYear(String date) {
		Date d = toDate(date);
		if (d == null)
			return 0;
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(d);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 取值：某日期的月号
	 * 
	 * @param date
	 *            格式: yyyy-MM-dd
	 * @return 从0开始
	 */
	public static int getMonth(String date) {
		Date d = toDate(date);
		if (d == null)
			return 0;
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(d);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 取值：某日期的日号
	 * 
	 * @param date
	 *            格式: yyyy-MM-dd
	 * @return 从1开始
	 */
	public static int getDayOfMonth(String date) {
		Date d = toDate(date);
		if (d == null)
			return 0;
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTime(d);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 计算两个日期的年数差
	 * 
	 * @param one
	 *            格式: yyyy-MM-dd
	 * @param two
	 *            格式: yyyy-MM-dd
	 * @return
	 */
	public static int compareYear(String one, String two) {
		return getYear(one) - getYear(two);
	}

	/**
	 * 计算岁数
	 * 
	 * @param date
	 *            格式: yyyy-MM-dd
	 * @return
	 */
	public static int compareYear(String date) {
		return getYear() - getYear(date);
	}
	
	/**
	 * 使用format格式化Date对象为字符串
	 * 
	 * @param date
	 *            date
	 * @return String
	 */ 
	public static String getDateString(Date date) {
		SimpleDateFormat format = sdf;
		format.applyPattern("yyyy-MM-dd");  
		return format.format(date);
	}
	/**
	 * 使用format格式化Date对象为字符串
	 * 
	 * @param date
	 *            date
	 * @return String
	 */ 
	public static String getFullDateString(Date date) {
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getFullDateString2(Date date) {
		sdf.applyPattern("HH:mm");
		return sdf.format(date);
	}
	/**
	 * 使用format格式化Date对象为字符串,获取年份
	 * 
	 * @param date
	 *            date
	 * @return String
	 */
	public static String getYear(Date date) {
		sdf.applyPattern("yyyy");
		return sdf.format(date);
	}  
	
	/**
	 * 使用format格式化Date对象为字符串,获取中文表示的年月日
	 * 
	 * @param date
	 *            date
	 * @return String 2009年12月08日
	 */
	public static String getDateStrC(Date date) {
		sdf.applyPattern("yyyy年MM月dd日");
		return sdf.format(date);
	}
  
	/**
	 * 使用format格式化Date对象为字符串,获取年份
	 * 
	 * @param date
	 *            date
	 * @return String 20091208
	 */
	public static String getDateStrCompact(Date date) {
		if (date == null)
			return "";
		sdf.applyPattern("yyyyMMdd HH:mm:ss");
		return sdf.format(date);
	}
	public static String getDateHour(Date date) {
		if (date == null)
			return "";
		sdf.applyPattern("HH");
		return sdf.format(date);
	}
	/**
	 * 使用format格式化Date对象为字符串,获取年月日和时间
	 * 
	 * @param date
	 *            date
	 * @return String 2009年12月8日 14时03分10秒
	 */
	public static String getDateTimeStrC(Date date) {
		sdf.applyPattern("yyyy年MM月dd日 HH时mm分ss秒");
		return sdf.format(date);
	}


  
	/**
	 * 获取指定日期后下一个月的第一天
	 *
	 * @param date
	 *            .sql.Date date
	 * @return java.sql.Date
	 */
	public static java.sql.Date getNextMonthFirstDate(Date date)
			throws ParseException {
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.set(Calendar.DATE, 1);
		return new java.sql.Date(scalendar.getTime().getTime());
	}   

	/**
	 * 获取指定日期的前面几天
	 *
	 * @param date
	 *            .sql.Date date
	 * @param dayCount
	 *            表示前几天
	 * @return java.sql.Date
	 */
	public static java.sql.Date getFrontDateByDayCount(java.sql.Date date,
			int dayCount) throws ParseException {
		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date);
		scalendar.add(Calendar.DATE, -dayCount);
		return new java.sql.Date(scalendar.getTime().getTime());
	}
 
	/**
	 * 取得指定年份和月份的第一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return Date
	 */
	public static Date getFirstDay(String year, String month)
			throws ParseException {
		SimpleDateFormat format = sdf;
		format.applyPattern("yyyy-MM-dd");  
		return format.parse(year + "-" + month + "-1");
	}  
	
	/**
	 * 取得指定年份和月份的第一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return Date
	 */
	public static Date getFirstDay(int year, int month) throws ParseException {
		SimpleDateFormat format = sdf;
		format.applyPattern("yyyy-MM-dd");  
		return format.parse(year + "-" + month + "-1");
	}   
	
	/**
	 * 取得指定年份和月份的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return Date
	 */
	public static Date getLastDay(String year, String month)
			throws ParseException {
		SimpleDateFormat format = sdf;
		format.applyPattern("yyyy-MM-dd");  
		Date date = format.parse(year + "-" + month + "-1");

		Calendar scalendar = gcNow;
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.add(Calendar.DATE, -1);
		date = scalendar.getTime();
		return date;
	}   
	
	/**
	 * 取得指定年份和月份的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return Date
	 */
	public static Date getLastDay(int year, int month) throws ParseException {
		SimpleDateFormat format = sdf;
		format.applyPattern("yyyy-MM-dd");  
		Date date = format.parse(year + "-" + month + "-1");
		Calendar scalendar = gcNow;
		scalendar.setTime(date);
		scalendar.add(Calendar.MONTH, 1);
		scalendar.add(Calendar.DATE, -1);
		date = scalendar.getTime();
		return date;
	}  
	
	/**
	 * 取得指定年份之间的相隔月数
	 *
	 * @param beforedate
	 * @param afterdate
	 * @return Date
	 */
	public static long getDistinceMonth(String beforedate, String afterdate)
			throws ParseException {
		SimpleDateFormat d = sdf;
		d.applyPattern("yyyy-MM-dd");  
		long monthCount = 0;
		try {
			Date before = d.parse(beforedate);
			Date after = d.parse(afterdate);
			monthCount = (Integer.parseInt(DateUtil.getYear(after)) - Integer
					.parseInt(DateUtil.getYear(before)))
					* 12
					+ DateUtil.getMonth(afterdate)
					- DateUtil.getMonth(beforedate);
		} catch (ParseException e) {
			System.out.println("Date parse error!");
		}
		return monthCount;
	}  
	
	/**
	 * 取得指定年份之间的相隔天数
	 *
	 * @param beforedate
	 *            年
	 * @param afterdate
	 *            月
	 * @return Date
	 */
	public static long getDistinceDay(String beforedate, String afterdate) {
		SimpleDateFormat d = sdf;
		d.applyPattern("yyyy-MM-dd");  
		long dayCount = 0;
		try {
			Date d1 = d.parse(beforedate);
			Date d2 = d.parse(afterdate);
			dayCount = (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			System.out.println("Date parse error!");
		}
		return dayCount;
	}
	 /**
	 * 指定日期加减天数
	 * 
	 * @param date
	 * @param day 加一天就传入1  减一天就传入-1
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static Date getDaytoDay(Date date,int day){
		if(date==null)
			return date;
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(date); 
		 calendar.add(calendar.DATE,day);
		 date=calendar.getTime();
		 return date;
	}

	@SuppressWarnings("static-access")
	public static Date getMonthtoMonth(Date date,int day){
		if(date==null)
			return date;
	 	Calendar calendar = Calendar.getInstance();
	 	calendar.setTime(date); 
	 	calendar.add(calendar.MONTH,day);
	 	date=calendar.getTime();
	 	return date;
	}
	/**
	 * @Title getTime
	 * @Description 得到两个时间之间的时间具体列表 超过当前时间按照当前时间算
	 * @param  startTime
	 * @param  endTime
	 * @return List<String> 返回类型
	 * @throws
	 */
	public static List<String> getEveryDay(String startTime, String endTime) {
		Date date = new Date();
		// 判断超时
		if (startTime != "") {
			if (date.getTime() < stringToDate(startTime).getTime()) {
				startTime = dateToString(date);
			}
		} else {
			startTime = dateToString(date);
		}
		if (endTime != "") {
			if (date.getTime() < stringToDate(endTime).getTime()) {
				endTime = dateToString(date);
			}
		} else {
			endTime = dateToString(date);
		}
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(toDate(startTime));
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(toDate(endTime));
		Calendar calTmp = (Calendar) calStart.clone();
		List<String> dayList = new ArrayList<>();
		dayList.add(startTime);
		while (!calEnd.equals(calTmp)
				&& calTmp.getTime().getTime() < calEnd.getTime().getTime()) {
			calTmp.add(DATE, 1);
			dayList.add(dateToString(calTmp.getTime()));
		}
		calStart.clear();
		calEnd.clear();
		return dayList;
	}
	public static String dateMonthToString(Date date) {
		sdf.applyPattern("yyyy-MM");
		return sdf.format(date);
	}
	public static List<String> getEveryMonth(String startTime, String endTime) {
		Date date = new Date();
		Date s=toDateForMonth(startTime);
		Date e=toDateForMonth(endTime);
		// 判断超时
		if (startTime != "") {
			if (date.getTime() < s.getTime()) {
				startTime = dateMonthToString(date);
			}
		} else {
			startTime = dateMonthToString(date);
		}
		if (endTime != "") {
			if (date.getTime() < e.getTime()) {
				endTime = dateMonthToString(date);
			}
		} else {
			endTime = dateMonthToString(date);
		}
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(s);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(e);
		Calendar calTmp = (Calendar) calStart.clone();
		List<String> dayList = new ArrayList<>();
		dayList.add(startTime);
		while (!calEnd.equals(calTmp)
				&& calTmp.getTime().getTime() < calEnd.getTime().getTime()) {
			calTmp.add(2, 1);
			dayList.add(dateMonthToString(calTmp.getTime()));
		}
		calStart.clear();
		calEnd.clear();
		return dayList;
	}
	public static List<String> getEveryDayNoDurge(String startTime, String endTime) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(toDate(startTime));
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(toDate(endTime));
		Calendar calTmp = (Calendar) calStart.clone();
		List<String> dayList = new ArrayList<>();
		dayList.add(startTime);
		while (!calEnd.equals(calTmp)
				&& calTmp.getTime().getTime() < calEnd.getTime().getTime()) {
			calTmp.add(DATE, 1);
			dayList.add(dateToString(calTmp.getTime()));
		}
		calStart.clear();
		calEnd.clear();
		return dayList;
	}
	/**
	 * @Title: stringToDate
	 * @Description: 字符串转换为date
	 * @param: @param str
	 * @param: @return 
	 * @return: Date yyyy-MM-dd 格式返回类型
	 */
	private static final SimpleDateFormat sdf=new SimpleDateFormat();
	public static Date stringToDate(String str) {
		try {
			sdf.applyPattern("yyyy-MM-dd");  
			return sdf.parse(str);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @Title: dateToString2
	 * @Description: date转换为string
	 * @param: @param date
	 * @return: String yyyy-MM-dd返回类型
	 */
	public static String dateToString(Date date) {
		return sdf.format(date);
	}
	/**
	 * 获取当月天数
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static int nowMonthOfDayNum(String time) throws ParseException{
		Calendar aCalendar = Calendar.getInstance();
		sdf.applyPattern("yyyy-MM");
		aCalendar.setTime(sdf.parse(time));
		int day=aCalendar.getActualMaximum(Calendar.DATE);
		return day;
	}

	/**
	 * 时间戳转字符串格式
	 * @param date 时间戳
	 * @param format 日期格式
	 * @return
	 */
	public static  String getCurrentTimeMillisToString(long date,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);//这个是你要转成后的时间的格式
		String sd = sdf.format(new Date(Long.parseLong(String.valueOf(date))));
		return  sd;
	}

	/***
	 *
	 * 字符串转时间戳
	 * @param strDate 字符串日期
	 * @param format 日期格式
	 * @return
	 * @throws ParseException
	 */
    public static long getStringToCurrentTimeMillis(String strDate,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(strDate).getTime();
    }

	/**
	 * 获取当前日期的23:59:59
	 *
	 */
	public static long getToDayMorning(){
		long now = System.currentTimeMillis() / 1000l;
		long daySecond = 60 * 60 * 24;
		long dayTime = now - (now + 8 * 3600) % daySecond;
		return dayTime * 1000;
	}
}
