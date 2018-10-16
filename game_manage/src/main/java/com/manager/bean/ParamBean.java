package com.manager.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/6/26.
 *
 * @ClassName ParamBean
 * @Description 参数
 */
@Getter
@Setter
@ToString
@Table("t_param")
public class ParamBean {
    @Id(auto=false)
    private int id;
    @Column("param")
    private String param;
    @Column("dsp")
    private String dsp;

}
