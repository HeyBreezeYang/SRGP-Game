package com.gmdesign.bean;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuBean {
	private int id;
	private String url;
	private String title;
	private int gid;
	public MenuBean(){}
	public MenuBean(Map<String,Object> map){
		this.id=Integer.parseInt(map.get("id").toString());
		this.url=map.get("url").toString();
		this.title=map.get("title").toString();
		this.gid=Integer.parseInt(map.get("groupId").toString());
	}
}
