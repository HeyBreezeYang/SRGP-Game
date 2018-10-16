package com.gmtime.rmi.service.impl;

import com.gmtime.rmi.service.RmiResult;

/**
 * 
* @ClassName GmTimerClear
* @Description 缓存清理
* @author DJL
* @date 2015-12-23 下午7:20:04
*
 */
public class GmTimerClear implements RmiResult {

	private static final int RESET_CACHE=10001;
	@Override
	public Object rmiResult(int code,Object... prams) {
		switch (code) {
		case RESET_CACHE:
			return true;
		default:
			return null;
		}
	}
}
