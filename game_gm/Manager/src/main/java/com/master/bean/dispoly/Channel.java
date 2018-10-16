package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/6/8.
 *
 * @ClassName Channel
 * @Description 渠道配置
 */
@Getter
@Setter
@ToString
@Table("gm_config.channel")
public class Channel {
    @Id
    private int id;
    @Column(value = "cname")
    private String name;
    @Column(value = "dsp")
    private String dsp;
}
