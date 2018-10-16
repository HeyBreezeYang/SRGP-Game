package com.cellsgame.game.module;

/**
 * @author puzhiqiang
 * @date 2018/09/21 18:19 星期五
 * @description
 */

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Observer;

/**
 * 监听这监听羁绊基数变化
 */
@WebListener
public class FetterListener implements ServletContextListener {
    private int fetterAccount;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("监听事件");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
