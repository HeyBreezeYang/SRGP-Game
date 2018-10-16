package com.manager.bean;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@ToString
@Setter
@Getter
@Table("t_game")
public class GameBean {
	@Id
	private int id;
	@Column("name")
	private String name;

	private List<VersionBean> version;
	private int index;


	@Override
	public int hashCode() {
		return Integer.valueOf(id).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		GameBean b=(GameBean)obj;
		//
		return Integer.valueOf(this.id).equals(b.getId());
	}
}
