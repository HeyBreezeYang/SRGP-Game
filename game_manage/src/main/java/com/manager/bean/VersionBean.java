package com.manager.bean;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@ToString
@Table("t_version")
public class VersionBean {
	@Id
	private int id;
	@Column("version")
	private String versionName;
	@Column("url")
	private String url;

	private Set<GroupBean> group=new HashSet<>();
	@Column("gameId")
	private int gameId;

	public String getGroupData(){
		return JSON.toJSONString(this.group).replaceAll("\"", "'");
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
