package com.gmtime.main;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.gmdesign.exception.GmException;
import com.gmdesign.rmi.GmOperate;
import com.gmtime.cache.CacheGmConfig;
import com.gmtime.cache.CacheTimer;
import com.gmtime.cache.GmDataConnectionCache;
import com.gmtime.cache.context.SpringContext;
import com.gmtime.gm.service.other.ServerConfigServiceIF;
import com.gmtime.gm.service.task.TimerTaskServiceIF;
import com.gmtime.gm.service.task.bean.TaskString;
import com.gmtime.rmi.impl.GmOperateRmi;
import com.gmtime.rmi.service.bean.TimerTaskBean;
import com.gmtime.time.QuartzManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
* @ClassName TimerSet
* @Description 启动项管理
* @author DJL
* @date 2016-1-4 上午11:27:00
 */
public class TimerSet implements Runnable{
	private static Logger log = LoggerFactory.getLogger(TimerSet.class);
	private TimerTaskServiceIF timerTaskService=(TimerTaskServiceIF) SpringContext.getBean("timerTaskService");
	private ServerConfigServiceIF serverConfigService=(ServerConfigServiceIF)SpringContext.getBean("serverConfigService");
	private HashMap rmi_url= (HashMap) SpringContext.getBean("rmi_url");
	TimerSet(){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				log.info("准备备份缓存数据。。。。");
				try {
					work=false;
					QuartzManager.shutdown();
					saveData();
					GmDataConnectionCache.closeConnectionPool();
					Thread.sleep(5000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("数据备份完毕。。。。");
			}

			private void saveData() throws GmException {
				timerTaskService.deleteCacheObj();
				timerTaskService.backTask(CacheTimer.getTimerAll());
				timerTaskService.saveCache(CacheTimer.converTask());
				/*
				 * 备份其他缓存数据~~
				 * 
				 */
			}
		});
	}
	
	void init() throws GmException {
		initTimerTask();
		initTimerTaskObject();
	}
	/**
	 * t_timer 中所有定时任务对象
	 */
	private void initTimerTask() throws GmException {
		List<TimerTaskBean> beans =this.timerTaskService.queryTaskAll();
		for (TimerTaskBean bean : beans) {
			CacheTimer.setTimerNum(bean.getTimeName(), bean);
		}
	}
	/**
	 *  t_taskobj 所有定时中使用的任务对象
	 */
	private void initTimerTaskObject()throws GmException {
		List<TaskString> beans=this.timerTaskService.queryCacheObj();
		for (TaskString bean : beans) {
			CacheTimer.setTaskObject(bean.getName(), bean.getTaskObj());
		}
	}
	private void quartzStart() throws ClassNotFoundException, GmException {
		QuartzManager.start();
		for(String key:CacheTimer.allKeyForTimerNum()){
			TimerTaskBean tb=CacheTimer.getTimerNum(key);
			QuartzManager.addJob(tb.getDsp(),tb.getTimeName(),tb.getGroupName(),Class.forName(tb.getClazz())
					,tb.getStartTime(),tb.getEndTime(),tb.getConfig());
			if(tb.getStatues()==0){
				QuartzManager.pauseJob(tb.getTimeName(),tb.getGroupName());
			}
		}
	}
	private volatile boolean work=true;
	@Override
	public void run() {
		try {
			CacheGmConfig.setChannel(serverConfigService.initChanel());
			CacheGmConfig.setServer(serverConfigService.initServer());
			GmDataConnectionCache.initConnection(serverConfigService.getDBConfig());
			log.info("初始化连接池");
			rmiStart();
			log.info("启动RMI");
			quartzStart();
			log.info("启动TIMER");
			new Thread(() -> {
                System.out.println("cmd> Input you want~!");
                while(work){
                    System.out.print("cmd>");
                    Scanner sc = new Scanner(System.in);
                    String cmd  = sc.nextLine();
                    opera(cmd);
                }
            }).start();
			log.info("启动输入线程");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void rmiStart() throws RemoteException, AlreadyBoundException, MalformedURLException {
		GmOperate gm =new GmOperateRmi();
		LocateRegistry.createRegistry(Integer.parseInt(rmi_url.get("PORT").toString()));
		Naming.bind("//"+rmi_url.get("IP").toString()+":"+rmi_url.get("PORT").toString()+"/"+rmi_url.get("NAME").toString(),gm);
	}

	private void opera(String cmd){
		switch (cmd) {
			case "gm stop":
				System.exit(0);
			case "memory":
				System.out.println("cmd> " + Runtime.getRuntime().freeMemory() / 1024);
				break;
			case "timer name":
				System.out.println("cmd> " + CacheTimer.queryNowTimer());
				break;
			case "timer object":
				System.out.println("cmd> " + CacheTimer.queryNowObject());
				break;
			default:
				System.out.println("cmd> Haven`t code!~~");
				break;
		}
	}
}
