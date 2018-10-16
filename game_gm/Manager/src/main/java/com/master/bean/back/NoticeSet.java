package com.master.bean.back;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by HP on 2018/6/13.
 */
@Getter
@Setter
@ToString
@Table("gm_config.notice_set")
public class NoticeSet {

    @Id
    private int id;
    @Column("setting")
    private String setting;
    @Column("value")
    private String value;
}
