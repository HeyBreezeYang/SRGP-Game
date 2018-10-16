package com.gmdesign.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class GroupBean {
	private int id;
	private int gid;
	private int vid;
	private String groupName;
	private String groupIcon="";
	private List<GroupBean> groups=new ArrayList<>();
	private List<MenuBean> menu=new ArrayList<>();
	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		GroupBean b=(GroupBean)obj;
		return Integer.valueOf(this.id).equals(b.getId());
	}
	public GroupBean(){

	}
	public GroupBean(Map<String,Object> map){
		this.id=Integer.parseInt(map.get("id").toString());
		this.gid=Integer.parseInt(map.get("gid").toString());
		this.groupName=map.get("name").toString();
		this.groupIcon=map.get("icon").toString();
		this.vid=Integer.parseInt(map.get("versionId").toString());
	}

	
	
}
