package com.gmdesign.bean;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GameBean {
	private int id;
	private String name;
	private List<VersionBean> version;
	private int index;

	public GameBean(){}
	public GameBean(Map<String,Object> map){
		this.id=Integer.parseInt(map.get("id").toString());
		this.name=map.get("name").toString();
	}
	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		GameBean b=(GameBean)obj;
		return Integer.valueOf(this.id).equals(b.getId());
	}


}
