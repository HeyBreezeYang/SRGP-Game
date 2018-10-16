package com.cellsgame.game.core.msgproc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aly on  2016-07-11.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Client {
    /**
     * 客户消息访问点
     */
    int value();
}
