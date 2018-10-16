package com.cellsgame.game.module.modtest;

import com.cellsgame.game.module.Fetter;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * @author tiandong
 * @date 2018/09/20 11:15 星期四
 * @description羁绊关系测试
 */

public class FetterTest {


    /**
     * 羁绊等级管理
     */
    @Test
    public void  test1(){
        Fetter fetter = mock(Fetter.class);


        int fetterAcount=fetter.getFetterAccount();

    }

    /***羁绊升级处理逻辑*/
    public void dealFetter(Fetter fetter){
        int fetterAcount=fetter.getFetterAccount();
        String grade=fetter.getGrade();
        if (fetterAcount>=200&&fetterAcount<400) {
            grade = "B";
        }
        else if(fetterAcount>=400&&fetterAcount<800) {
            grade = "A";
        }
        else if (fetterAcount>=800) {
            grade = "S";
            //------===发邮件

        }
    }

}

