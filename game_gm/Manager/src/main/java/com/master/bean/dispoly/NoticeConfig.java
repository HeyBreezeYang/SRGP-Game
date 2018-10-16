package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName NoticeConfig
 * @Description 公告配置表
 */
@Getter
@Setter
@ToString
@Table("gm_config.notice")
public class NoticeConfig {
    @Name
    private String id;
    @Column("opTime")
    private Long time;
    @Column("msg")
    private String msg;
}
