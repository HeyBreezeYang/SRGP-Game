package com.gmtime.rmi.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.gmdesign.exception.GmException;
import com.gmdesign.rmi.GmOperate;
import com.gmtime.cache.CacheGmClassName;
import com.gmtime.rmi.service.RmiResult;

public class GmOperateRmi extends UnicastRemoteObject implements GmOperate {


	public GmOperateRmi() throws RemoteException {
	}

	@Override
	public Object result(String mark, int code, Object... prams) throws GmException,RemoteException {
		RmiResult result=getMethod(mark);
		return result.rmiResult(code,prams);
	}
	private RmiResult getMethod(String mark) throws GmException {
		try {
			Class<?> cl = CacheGmClassName.getClassName(mark);
			return (RmiResult) cl.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new GmException(e.getMessage());
		}
	}
}
