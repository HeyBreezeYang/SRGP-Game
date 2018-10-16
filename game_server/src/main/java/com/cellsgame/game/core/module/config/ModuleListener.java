package com.cellsgame.game.core.module.config;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.module.i.SysListener;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.sys.funOpen.CommandMatcher;
import com.cellsgame.game.module.sys.funOpen.FunOpenOp;
import com.cellsgame.game.module.sys.funOpen.FunOpenOp2;
import com.cellsgame.game.module.sys.funOpen.FunType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Aly @2016-11-23.
 */
public class ModuleListener<T> extends ModuleConfig<T> {
    private Consumer<T> onStartup;
    private Consumer<T> onShutdown;
    private Map<FunType, FunOpenOp2<T>> listen;

    ModuleListener(ModuleBuilder<T> moduleBuilder) {
        super(moduleBuilder);
    }

    @Override
    void buildTO(Module<T> module) {

        if (null != onStartup || null != onShutdown) {
            module.listener = new SysListener<T>() {
                @Override
                public void onStartup(T bean) {
                    if (null != onStartup) onStartup.accept(bean);
                }

                @Override
                public void onShutdown(T bean) {
                    if (null != onShutdown) onShutdown.accept(bean);
                }
            };
        }
        if (null != listen) {
            for (Map.Entry<FunType, FunOpenOp2<T>> entry : listen.entrySet()) {
                FunType left = entry.getKey();
                left.defaultOrSet(CommandMatcher.builder(module.getModuleID()).allCmd().build());
                FunOpenOp2<T> right = entry.getValue();
                if (null == right) return;
                Class<T> moduleInterFace = module.getModuleInterFace();
                left.setOpenFun(new FunOpenOp() {
                    private T bean;

                    @Override
                    public Map listen(CMD cmd, Map parent, PlayerVO pvo) {
                        if (bean == null) bean = SpringBeanFactory.getBean(moduleInterFace);
                        parent = right.listen(bean, cmd, parent, pvo);
                        return parent;
                    }
                });
            }
        }
    }

    public ModuleBuilder<T> next() {
        return moduleBuilder;
    }

    public ModuleListener<T> onStartup(Consumer<T> run) {
        this.onStartup = run;
        return this;
    }

    public ModuleListener<T> onShutdown(Consumer<T> run) {
        this.onShutdown = run;
        return this;
    }

    /**
     * Module 开启检查
     * 登录和条件达到/超过 的时候都检查
     * <p>
     * 设置后 在请求的时候会检查 该module的开启条件
     */
    public ModuleListener<T> listenModuleOpen(FunType funType, FunOpenOp2<T> listenOpen) {
        if (null == listen) {
            listen = new HashMap<>();
        }
        listen.put(funType, listenOpen);
        return this;
    }

    public ModuleListener<T> listenModuleOpen(FunType funType) {
        return listenModuleOpen(funType, null);
    }
}
