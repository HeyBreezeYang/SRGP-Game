package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/3/6.
 *
 * @ClassName Parameter
 * @Description 各种参数
 */
@Getter
@Setter
@ToString
@Table("gm_config.individual_parameter")
public class Parameter {
    @Name
    private String id;
    @Column(value = "prams")
    private String prams;
    @Column(value = "dsp")
    private String dsp;
}
