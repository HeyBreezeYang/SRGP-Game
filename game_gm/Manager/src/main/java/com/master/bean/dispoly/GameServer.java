package com.master.bean.dispoly;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/8/10.
 *
 * @ClassName GM
 * @Description
 */
@Getter
@Setter
@ToString
@Table("gm_config.game_server")
public class GameServer {
    @Id
    private int logicID;
    @Name
    @Column("serverID")
    private String serverID;
    @Column("serverName")
    private String serverName;
    @Column("state")
    private int state;
    @Column("recommend")
    private int recommend;
    @Column("serverIP")
    private String serverIP;
    @Column("serverPort")
    private int serverPort;
    @Column("httpPort")
    private int httpPort;
    @Column("openTime")
    private String openTime;
    @Column("resourceURL")
    private Integer resourceURL;
    @Column("extranetIP")
    private String extranetIP;
    @Column("deliverPort")
    private int deliverPort;

    public String getRecommendName(){
        return recommend==1?"是":"否";
    }
    public String getStateName(){
        switch (state){
            case 1:
                return "流畅";
            case 2:
                return "火爆";
            case 3:
                return "拥挤";
            case 4:
                return "维护";
            default:
                return "未知";
        }
    }
    public String getServerUrl(){
        return "http://"+this.getServerIP()+":"+this.httpPort+"/game";
    }
}
