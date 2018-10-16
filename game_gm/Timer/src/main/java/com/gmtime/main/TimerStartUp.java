package com.gmtime.main;

import java.io.FileInputStream;

import com.gmtime.cache.context.SpringContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * 
* @ClassName TimerStartUp
* @Description 程序启动入口
* @author DJL
* @date 2016-1-4 上午11:26:39
*
 */
public class TimerStartUp {

	public static void main(String[] args) {
		try {
			ConfigurationSource source = new ConfigurationSource(new FileInputStream("configer/log4j2.xml"));
			Configurator.initialize(null, source);
			SpringContext.setSpringFilePath();
			TimerSet timer=new TimerSet();
			timer.init();
			Thread app=new Thread(timer);
			app.setDaemon(false);
			app.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
