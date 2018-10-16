package com.manager.bean;

import java.util.List;

import com.manager.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Getter
@Setter
@ToString
@Table("t_user")
public class UserBean {
	@Id
	private int id;
	@Column("name")
	private String name;
	@Column("password")
	private String password;
	@Column("createTime")
	private long createTime;
	@Column("uid")
	private int uid;
	@Column("root")
	private short root;

	private UserBean father;
	private String userKey;
	private long loginTime;
	private boolean loginState;
	private List<GameBean> game;


	public int getGameLength(){
		int a =0;
		for (GameBean gb:game){
			if(gb.getVersion()!=null&&!gb.getVersion().isEmpty()) {
				a++;
			}
		}
		return a;
	}

	public String getCreateTimeMsg(){
		return DateUtil.formatDateTime(this.createTime);
	}

}
