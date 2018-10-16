package com.gmdesign.exception;

import java.util.ArrayList;
import java.util.List;

import com.gmdesign.bean.other.GmHashMap;

public class GmException extends Exception{
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public GmHashMap getErrorMsg(){
		GmHashMap map =new GmHashMap();
		map.put("msg",this.getMessage());
		return map;
	}

	private int code;
	private List<Throwable> manyThrow=new ArrayList<>();
	private static final long serialVersionUID = -1115095680220640442L;
	private StringBuilder excMsg=new StringBuilder();
	public GmException(){
		super();
	}
	public GmException(String massage){
		super(massage);
	}
	public GmException(String massage, int code){
		super(massage);
		this.code=code;
	}
	public GmException(List<Throwable> manyThrow){
		this.manyThrow.addAll(manyThrow);
	}
	@Override
	public String getMessage() {
		if(manyThrow.isEmpty()){			
			return super.getMessage();
		}else{
			excMsg.append("异常包含：");
			for (Throwable aManyThrow : manyThrow) {
				excMsg.append(aManyThrow.getMessage());
				excMsg.append(" ");
			}
			return excMsg.toString();
		}
	} 
}
