package com.manager.bean;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@ToString
@Table("t_group")
public class GroupBean {
	@Id
	private int id;
	@Column("gid")
	private Integer gid;
	@Column("versionId")
	private int vid;
	@Column("name")
	private String groupName;
	@Column("icon")
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
	public GroupBean(){}
	public GroupBean(PlatFormHashMap map){
		this.id=Integer.parseInt(map.get("id").toString());
		this.gid=Integer.parseInt(map.get("gid").toString());
		this.groupName=map.get("name").toString();
		this.groupIcon=map.get("icon").toString();
		this.vid=Integer.parseInt(map.get("versionId").toString());
	}
}
