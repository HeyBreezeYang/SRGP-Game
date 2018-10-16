package com.cellsgame.game.core.msgproc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aly on  2016-07-11.
 *         <p>
 *         标注在客户端访问的方法上面 用于参数解析
 *         如果 没有标注名称 使用默认参数名称
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CParam {

    /**
     * @return 客户端参数 名字
     */
    String value();
}
