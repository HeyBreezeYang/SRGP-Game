package com.cellsgame.game.core.cfg.valid;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.clazz.ClassUtils;
import com.cellsgame.common.util.csv.BaseCfg;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Val
 */
public class CfgCheckerManager {
    private static Logger log = LoggerFactory.getLogger(CfgCheckerManager.class);
    private static CfgCheckerManager manager = new CfgCheckerManager();

    private CfgCheckerManager() {
    }

    public static CfgCheckerManager getManager() {
        return manager;
    }


    private Map<String, CfgChecker> getAllChecker() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Map<String, CfgChecker> cache = GameUtil.createMap();
        String url = CfgCheckerManager.class.getPackage().getName() + ".impl";
        List<Class<?>> cls = ClassUtils.getClasses(url);
        for (Class<?> c : cls) {
            if (CfgChecker.class.isAssignableFrom(c)) {
                if (!Modifier.isAbstract(c.getModifiers())) {
                    try {
                        cache.put(c.getName(), (CfgChecker) c.newInstance());
                    } catch (Exception e) {
                        throw new RuntimeException("" + c.getClass(), e);
                    }
                    log.debug("加载配置检查器-->{}", c.getName());
                }
            }
        }
        return cache;
    }

    private Map<Class<? extends BaseCfg>, List<Field>> getAllField(Set<Class<? extends BaseCfg>> clazzs) throws IOException, ClassNotFoundException {
        Map<Class<? extends BaseCfg>, List<Field>> needCheckField = new HashMap<>();
        for (Class<? extends BaseCfg> clazz : clazzs) {
            List<Field> fields = new ArrayList<>();
            Class cls = clazz;
            while (cls != Object.class) {
                Collections.addAll(fields, cls.getDeclaredFields());
                cls = cls.getSuperclass();
            }
            if (fields.size() > 0) {
                needCheckField.put(clazz, fields);
            }
        }
        return needCheckField;
    }

    public StringBuilder checkAllCfg(Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) throws Exception {
        Map<String, CfgChecker> allChecker = getAllChecker();
        Map<Class<? extends BaseCfg>, List<Field>> allField = getAllField(allCfgData.rowKeySet());

        // 优先执行基础检查
        StringBuilder sb = new StringBuilder();
        for (CfgChecker checker : allChecker.values()) {
            sb.append(checker.doCheck(allField, allCfgData));
            log.debug("checker:验证完成 {} ", checker.getClass().getSimpleName());
        }
        return sb;
    }
}
