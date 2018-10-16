package com.manager.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@ToString
@Setter
@Getter
@Table("t_menu")
public class MenuBean {
	@Id
	private int id;
	@Column("url")
	private String url;
	@Column("title")
	private String title;
	@Column("groupId")
	private int gid;
}
