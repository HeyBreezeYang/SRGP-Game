package com.cellsgame.game.core.module.load;

import java.util.*;
import java.util.concurrent.locks.LockSupport;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.clazz.ClassFileUtils;
import com.cellsgame.game.core.cfg.core.CfgService;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.core.module.i.SysListener;
import com.cellsgame.game.core.msgproc.process.MessageProcess;
import com.cellsgame.game.core.msgproc.process.asm.CMDWriter;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import com.cellsgame.game.core.msgproc.process.asm.visitor.ModuleClassVisitor;
import com.cellsgame.game.module.sys.funOpen.FunOpenChecker;
import com.cellsgame.game.util.DebugTool;
import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-10-24.
 *         只加载 Module.class
 */
public class ModuleLoader {
    // 根据配置顺序加载
    public static final Map<Integer, Module<?>> moduleMap = new TreeMap<>();
    private static final Logger log = LoggerFactory.getLogger(ModuleLoader.class);

    public static void loadAllModule(String pkgName) throws Exception {
        List<String> allPkgClazz = new ArrayList<>();
        ClassFileUtils.getClasses(pkgName, true, allPkgClazz::add);

        createDispatchers(allPkgClazz);

        for (String clazz : allPkgClazz) {
            if (!clazz.endsWith(".ModuleInfo")) {
                continue;
            }
            try {
                Class<?> moduleInfo = Class.forName(clazz);
                Deprecated deprecated = moduleInfo.getAnnotation(Deprecated.class);

                if (IModuleInfo.class.isAssignableFrom(moduleInfo)) {
                    IModuleInfo instance = (IModuleInfo) moduleInfo.newInstance();
                    Module<?> module = instance.getModuleInfo();
                    if (null != deprecated) {
                        MessageProcess.Deprecated(module.getModuleID());
                    } else {
                        moduleMap.put(module.getModuleID(), module);
                    }
                } else {
                    DebugTool.throwException("Class  name:[" + clazz + "] not impl IModuleInfo or Deprecated", null);
                }

            } catch (Throwable ignored) {
                DebugTool.throwException(log, "", ignored);
            }
        }
    }

    public static void loadConfigs(int globalModuleID) throws Exception {
        CfgService.loadAllCfg(globalModuleID, false, moduleMap);
        //依赖配置数据
        FunOpenChecker.init();
    }

    private static void createDispatchers(List<String> pkgName) throws Exception {
        Map<Integer, ModuleAsmInfo> infos = new HashMap<>();
        for (String clazzName : pkgName) {
            try {
                ClassReader reader = new ClassReader(clazzName);
                reader.accept(new ModuleClassVisitor(moduleAsmInfo -> {
                    ModuleAsmInfo old = infos.put(moduleAsmInfo.getModuleID(), moduleAsmInfo);
                    if (null != old) {
                        DebugTool.throwException(log, "重复的模块ID:" + moduleAsmInfo.getModuleID(), new RuntimeException());
                    }
                }), ClassReader.EXPAND_FRAMES);
            } catch (Throwable e) {
                DebugTool.throwException(log, "", e);
            }
        }
        CMDWriter.rewrite(infos.values());
        for (ModuleAsmInfo info : infos.values()) {
            MessageProcess.load(info);
        }
    }

    @SuppressWarnings("unchecked")
    public static void initModuleOnStartup() {
        for (Module module : moduleMap.values()) {
            SysListener listener = module.getListener();
            if (null != listener) {
                Object bean = SpringBeanFactory.getBean(module.getModuleInterFace());
                try {
                    listener.onStartup(bean);
                } catch (Exception e) {
                    DebugTool.throwException("", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void moduleOnShutdown() {
        Thread cur = Thread.currentThread();
        Dispatch.dispatchGameLogic(() -> {
            for (Module module : moduleMap.values()) {
                SysListener listener = module.getListener();
                if (null != listener) {
                    Object bean = SpringBeanFactory.getBean(module.getModuleInterFace());
                    try {
                        listener.onShutdown(bean);
                    } catch (Exception e) {
                        log.warn("onShutdownError: of Module {}", module.getModuleInterFace().getSimpleName(), e);
                    }
                }
            }
            LockSupport.unpark(cur);
        });
        LockSupport.park();
    }
}