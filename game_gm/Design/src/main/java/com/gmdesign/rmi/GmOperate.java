package com.gmdesign.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.gmdesign.exception.GmException;

public interface GmOperate extends Remote{
	Object result(String mark, int code, Object... prams)throws GmException,RemoteException;
}
 