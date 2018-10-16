package com.manager.bean;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/6/26.
 *
 * @ClassName AppBean
 * @Description 设置APP-ID
 */
@ToString
@Setter
@Getter
@Table("open_key")
public class AppBean {
    @Id
    private int id;
    @Column("appID")
    private String appID;
    @Column("openID")
    private String openID;
    @Column("versionID")
    private int versionID;

    public String sendJsonMsg(){
        return JSON.toJSONString(this);
    }
}
