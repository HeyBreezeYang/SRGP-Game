package com.manager.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName manage
 * @Description
 */
@Getter
@Setter
@ToString
@Table("t_log")
public class LogBean {
    @Id
    private int id;
    @Column("userId")
    private int uid;
    @Column("type")
    private String type;
    @Column("message")
    private String message;
    @Column("modifyDate")
    private long time;

    private UserBean user;

    public void setUser(UserBean user){
        this.uid=user.getId();
        this.user=user;
    }
}
