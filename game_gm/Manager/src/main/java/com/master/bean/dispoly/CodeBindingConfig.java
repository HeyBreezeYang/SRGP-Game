package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName GM
 * @Description
 */
@Getter
@Setter
@ToString
@Table("gm_config.prize_binding")
public class CodeBindingConfig {
    @Id
    private int id;
    @Column("aid")
    private int aid;
    @Column("bid")
    private int bid;
}
