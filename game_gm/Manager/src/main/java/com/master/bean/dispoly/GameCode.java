package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName GameCode
 * @Description
 */
@Getter
@Setter
@ToString
@Table("gm_config.prize_code")
public class GameCode {
    @Column("aid")
    private int aid;
    @Name
    private String prizeCode;
}
