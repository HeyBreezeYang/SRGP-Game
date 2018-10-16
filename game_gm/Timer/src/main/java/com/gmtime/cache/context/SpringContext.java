package com.gmtime.cache.context;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringContext {
	private static FileSystemXmlApplicationContext context;
	public static void setSpringFilePath(){
		context=new FileSystemXmlApplicationContext("configer/applicationContext-jdbc.xml");
	}
	private SpringContext(){}
	public static Object getBean(String name){
		return context.getBean(name);
	}
}
