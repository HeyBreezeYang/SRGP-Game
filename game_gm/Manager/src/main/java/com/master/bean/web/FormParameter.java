package com.master.bean.web;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FormParameter {
	private int id;
	private String[] thread;
	private String[] key;
	private Map<String,Boolean> parameter=new HashMap<>();
	private String queryName;
	private int queryType;
	private Map<String,String> otherParameter;
	public int getThreadSize(){
		return thread.length-1;
	}
}
