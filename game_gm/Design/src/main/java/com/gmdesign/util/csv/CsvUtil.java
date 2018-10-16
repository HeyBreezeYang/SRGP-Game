/**
 * @Title: Csvutil.java
 * @Package: com.xd100.function.util
 * @author: xiaoyu Mo (liujun)
 * @date: 2014-7-18 下午4:30:21
 */
package com.gmdesign.util.csv;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CsvUtil
 * @Description Csv文件读取类
 * @author xiaoyu Mo (liujun)
 * @date 2014-7-18 下午4:30:21
 * 
 */
public class CsvUtil {

	/**
	 * @Title readerCsv
	 * @Description 读取CSV文件
	 * @return List 返回类型
	 */
	public static List<String[]> readerCsv(String csvFilePath) {
		ArrayList<String[]> csvList = new ArrayList<>(); // 用来保存数据
		try {
			//本地就使用GBK
			CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("GBK")); //GBK可以识别繁体 而GB2312只能是简体
			reader.readHeaders(); // 跳过表头 如果需要表头的话，不要写这句。
			while (reader.readRecord()) { // 逐行读入除表头的数据
				csvList.add(reader.getValues());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return csvList;
	}

	/**
	 * @Title writerCsv
	 * @Description 写入CSV文件
	 */
	public static void writerCsv(String csvFilePath, String[] contents) {
		try {
			CsvWriter wr = new CsvWriter(csvFilePath, ',', Charset.forName("GBK"));//GBK可以识别繁体 而GB2312只能是简体
			wr.writeRecord(contents);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
