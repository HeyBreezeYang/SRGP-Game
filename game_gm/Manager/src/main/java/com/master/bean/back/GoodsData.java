package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/15.
 *
 * @ClassName GoodsData
 * @Description 物品统计数据
 */
@ToString
@Getter
@Setter
@Table("analysis_log.goods")
public class GoodsData {
    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("logTime")
    private String time;
    @Column("type")
    private String type;
    @Column("gCid")
    private String cid;
    @Column("numC")
    private Integer num;
    @Column("numP")
    private Integer peopleNum;
}
