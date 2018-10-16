package com.master.util;

import java.io.*;
import javax.servlet.http.HttpServletResponse;


public class DownLoadUtil {

	public static void downFile(HttpServletResponse response,String downLoadFileName,File file) throws IOException{
		// 设置导出类型为流
		response.setContentType("APPLICATION/OCTET-STREAM");
		// 设置文件名
		response.setHeader("Content-Disposition", "attachment;fileName="+downLoadFileName);
		InputStream in = new FileInputStream(file);
		OutputStream os = response.getOutputStream();
		// 设置大小
		response.setBufferSize(in.available());//JAVA API  in.available()返回此输入流下一个方法调用可以不受阻塞地从此输入流读取（或跳过）的估计字节数
		byte[] b = new byte[in.available()];
		int i;
		while ((i = in.read(b)) > 0) {
			os.write(b, 0, i);
		}
		in.read(b);
		in.close();
		os.flush();
		os.close();
		//删除临时文件
		file.delete();
	}
}
