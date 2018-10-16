package com.cellsgame.game.core.cfg.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * Created by Aly on 2015-06-18.
 * 被标记的BaseCfg   中的字段 会被检查
 *
 * @see BaseCfg
 * 所有字段都不能设置默认值  为空表示不进行检查
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Check {
    /**
     * 参照的配置档  只检查主键
     */
    Class<? extends BaseCfg> ref() default BaseCfg.class;

    /**
     * 检查int 的取值范围 [min,max]  两边都是闭区间
     */
    int[] between() default {};

    /**
     * 检查是否为空
     */
    boolean notNull() default false;

    /**
     * 强制检查size
     */
    int size() default -1;

    /**
     * 检查 该类型是枚举类
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Enum> Enum() default Enum.class;

    /**
     * JS 脚本
     * 会传入两个参数
     * cfg 当前配置档的值
     * val 当前字段的的值
     * 返回值为true  或者非零数 均表示要打印出错误信息
     */
    String js() default "";
}
