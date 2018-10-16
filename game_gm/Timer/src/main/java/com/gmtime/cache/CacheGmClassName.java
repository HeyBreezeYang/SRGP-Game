package com.gmtime.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName CacheGmClassName
* @Description 缓存所以 RMI 具体实现的 路径
* @author DJL
* @date 2015-12-24 下午6:36:07
*
 */
public class CacheGmClassName {
	private CacheGmClassName(){}
	private static final Map<String, Class<?>> RMI_NAME =new HashMap<>();
	static{
		try {
			RMI_NAME.put("gm_clear",Class.forName("com.gmtime.rmi.service.impl.GmTimerClear"));
			RMI_NAME.put("gm_task", Class.forName("com.gmtime.rmi.service.impl.GmTaskManager"));
			RMI_NAME.put("gm_mail", Class.forName("com.gmtime.rmi.service.impl.GmSendMail"));
			RMI_NAME.put("gm_back", Class.forName("com.gmtime.rmi.service.impl.LogDataBack"));
			RMI_NAME.put("gm_control", Class.forName("com.gmtime.rmi.service.impl.PlayerManage"));
			RMI_NAME.put("gm_query", Class.forName("com.gmtime.rmi.service.impl.GameMessage"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Class<?> getClassName(String key){
		return RMI_NAME.get(key);
	}
}
