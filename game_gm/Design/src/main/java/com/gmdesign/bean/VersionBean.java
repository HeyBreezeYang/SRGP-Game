package com.gmdesign.bean;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VersionBean {
	private int id;
	private String versionName;
	private String url;
	private Set<GroupBean> group=new HashSet<>();
	private int gameId;
	public String getGroupData(){
		return JSON.toJSONString(this.group).replaceAll("\"", "'");
	}
	public VersionBean(){}
	public VersionBean(Map<String,Object> map){
		this.id=Integer.parseInt(map.get("id").toString());
		this.versionName=map.get("version").toString();
		this.url=map.get("url").toString();
		this.gameId=Integer.parseInt(map.get("gameId").toString());
	}
	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		VersionBean b=(VersionBean)obj;
		return Integer.valueOf(this.id).equals(b.getId());
	}
}
