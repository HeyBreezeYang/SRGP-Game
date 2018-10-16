package com.gmtime.gm.dao.bean;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DataBaseConfig{
	private String id;
	private String ip;
	private String port;
	private String account;
	private String password;
	private String dsp;

	public DataBaseConfig(Map<String,Object> map){
		this.id=map.get("id").toString();
		ip=map.get("ip").toString();
		port=map.get("prot").toString();
		account=map.get("account").toString();
		password=map.get("password").toString();
		dsp=map.get("dsp").toString();
	}
}
