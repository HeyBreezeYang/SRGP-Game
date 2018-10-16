package com.gmtime.rmi.service;

import com.gmdesign.exception.GmException;

public interface RmiResult {
	Object rmiResult(int code,Object... prams) throws GmException;
}
