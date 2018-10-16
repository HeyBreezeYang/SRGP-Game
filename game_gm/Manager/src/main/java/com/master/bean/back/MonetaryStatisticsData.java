package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/14.
 *
 * @ClassName MonetaryStatisticsData
 * @Description 货币统计
 */
@ToString
@Getter
@Setter
@Table("analysis_log.monetary_statistics")
public class MonetaryStatisticsData {
    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("channel")
    private String channel;
    @Column("logTime")
    private String time;
    @Column("typeC")
    private String currencyType;
    @Column("typeU")
    private String useType;
    @Column("numC")
    private Integer currencyNum;
    @Column("numP")
    private Integer peopleNum;
}
