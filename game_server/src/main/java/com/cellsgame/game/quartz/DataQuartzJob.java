package com.cellsgame.game.quartz;

import java.util.Collection;
import java.util.Map;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.orm.BaseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataQuartzJob {
    private static Logger log = LoggerFactory.getLogger(DataQuartzJob.class);
    private static Collection<BaseDAO> daoSet;

    public static void init() {
        Map<String, BaseDAO> daoSetMap = SpringBeanFactory.getBeanByType(BaseDAO.class);
        log.debug("init daos {}", daoSetMap.keySet());
        daoSet = daoSetMap.values();
    }

    public void execute() {
//        log.warn(" >>> DataQuartzJob  execute <<< ");
        for (BaseDAO baseDAO : daoSet) {
            baseDAO.saveAndRemoveCache();
            baseDAO.deleteAndRemoveCache();
        }
    }

    public Collection<BaseDAO> getDaoSet() {
        return daoSet;
    }
}