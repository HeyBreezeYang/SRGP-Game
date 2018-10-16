package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2018/4/13.
 *
 * @ClassName gm
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.shop")
public class ShopData {
    @Id
    private int id;
    @Column("sid")
    private String server;
    @Column("channel")
    private String channel;
    @Column("goods")
    private String gid;
    @Column("logTime")
    private String date;
    @Column("num")
    private int num;
    @Column("fre")
    private int frequency;
    @Column("payNum")
    private int payNum;
    @Column("shopType")
    private int type;
}
