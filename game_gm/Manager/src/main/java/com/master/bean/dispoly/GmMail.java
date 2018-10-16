package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * Created by HP on 2018/6/4.
 */
@Getter
@Setter
@ToString
@Table("gm_config.gm_mail_data")
public class GmMail {

    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("channel")
    private String channel;
    @Column("email")
    private String email;
    @Column("userName")
    private String username;
    @Column("createBy")
    private String createBy;
    @Column("createTime")
    private long createTime;
    @Column("status")
    private int status;
    @Column("toexamineBy")
    private String toexamineBy;
    @Column("toexamineTime")
    private long toexamineTime;
    @Column("updateBy")
    private String updateBy;

}
