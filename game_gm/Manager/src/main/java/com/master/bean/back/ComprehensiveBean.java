package com.master.bean.back;

import com.master.bean.ComputerTableBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * Created by DJL on 2017/7/21.
 *
 * @ClassName ComprehensiveBean
 * @Description
 */
@ToString
@Getter
@Setter
@Table("analysis_log.synthetical")
public class ComprehensiveBean {


    public void setCountAll(ComprehensiveBean bean){
        this.dayPay+=bean.getDayPay();
        this.newPayNum+=bean.getNewPayNum();
        this.newPay+=bean.getPay();
        this.newPlayer+=bean.getNewPlayer();
        this.dayPayNum+=bean.getDayPayNum();
        this.payNum+=bean.getPayNum();
        this.pay+=bean.getPay();
        this.login+=bean.getLogin();
        this.register+=bean.getRegister();
        this.player+=bean.getPlayer();
    }

    @Id
    private int id;
    @Column("sid")
    private String sid;
    @Column("platform")
    private String platform;
    @Column("logTime")
    private String logTime;
    @Column("newPlayer")
    private Integer newPlayer=0;
    @Column("player")
    private Integer player=0;
    @Column("register")
    private Integer register=0;
    @Column("login")
    private Integer login=0;
    @Column("pay")
    private Double pay=0D;
    @Column("dayPay")
    private Double dayPay=0D;
    @Column("newPay")
    private Double newPay=0D;
    @Column("payNum")
    private Integer payNum=0;
    @Column("dayPayNum")
    private Integer dayPayNum=0;
    @Column("newPayNum")
    private Integer newPayNum=0;

    public double getArppu(){
        try {
            return this.dayPay/(double)this.dayPayNum;
        }catch (Exception e){
            return 0;
        }
    }
    public double getArppuAll(){
        try {
            return this.pay/(double)this.payNum;
        }catch (Exception e){
            return 0;
        }
    }
    public double getArpu(){
        try {
            return this.pay/(double)this.player;
        }catch (Exception e){
            return 0;
        }
    }
    public double getPermeate(){
        try {
            return (double)this.dayPayNum/(double)this.login;
        }catch (Exception e){
            return 0;
        }
    }
    public double getConvert(){
        try {
            return (double)this.newPayNum/(double)this.newPlayer;
        }catch (Exception e){
            return 0;
        }
    }
    public double getConvertAll(){
        try {
            return (double)this.payNum/(double)this.player;
        }catch (Exception e){
            return 0;
        }
    }
    public double getConvertLogin(){
        try {
            return (double)this.login/(double)this.player;
        }catch (Exception e){
            return 0;
        }
    }


    public Object[] toObjectArray(){
        return new Object[]{
                this.sid,this.platform,this.logTime,this.newPlayer,this.player,this.register,this.login
                ,this.pay,this.dayPay,this.newPay,this.payNum,this.dayPayNum,this.newPayNum
                , ComputerTableBean.convertOne(getArppu()),ComputerTableBean.convertOne(getArppuAll()),ComputerTableBean.convertOne(getArpu()),
                ComputerTableBean.convertTwo(getPermeate()),ComputerTableBean.convertTwo(getConvert()),ComputerTableBean.convertTwo(getConvertAll()),
                ComputerTableBean.convertTwo(getConvertLogin())
        };
    }

}
