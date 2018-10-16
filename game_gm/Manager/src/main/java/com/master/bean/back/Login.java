package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/7/21.
 *
 * @ClassName Login
 * @Description login表
 */
@Getter
@Setter
@ToString
@Table("analysis_log.login")
public class Login {
    @Id
    private int id;
    @Column("pid")
    private String pid;
    @Column("loginTime")
    private String loginTime;
    @Column("sid")
    private String sid;
    @Column("platform")
    private String platform;
}
