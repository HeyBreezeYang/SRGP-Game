package com.master.bean.dispoly;

import com.gmdesign.util.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName CodeConfig
 * @Description
 */
@Getter
@Setter
@ToString
@Table("gm_config.prize_code_attr")
public class CodeConfig {
    @Id
    private int id;
    @Column("serverId")
    private String sid;
    @Column("platform")
    private String channel;
    @Column("type")
    private int type;
    @Column("specialType")
    private int specialType;
    @Column("cretime")
    private long createTime;
    @Column("endTime")
    private String endTime;
    @Column("dsp")
    private String dsp;
    @Column("num")
    private int num;
    @Column("mailTitle")
    private String mailTitle;
    @Column("mailText")
    private String mailText;
    @Column("func")
    private String func;


    private String prizeCode;
    private String[] endTimeForConfig;

    public void setEndTimeForConfig(String[] endTimeForConfig) {
        this.endTimeForConfig = endTimeForConfig;
        String t1=endTimeForConfig[1].replace("AM","上午").replace("PM","下午");
        String t2=endTimeForConfig[0]+" "+t1;
        this.endTime= DateUtil.getFullDateString(DateUtil.toDate4(t2));
    }


}
