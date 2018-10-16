package com.cellsgame.game.module.sys.impl;


import java.util.List;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cache.CacheWord;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.sys.SystemBO;
import com.cellsgame.game.module.sys.cache.SystemRecordCache;
import com.cellsgame.game.module.sys.cons.SysDateRecType;
import com.cellsgame.game.module.sys.csv.WeightConfig;
import com.cellsgame.game.module.sys.vo.SystemRecordVO;
import com.cellsgame.game.module.sys.vo.SystemVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

/**
 * @author Aly on  2016-07-18.
 */
public class SystemBOImpl implements SystemBO {

    @Resource
    private BaseDAO<ShopItemRecordVO> shopItemRecordDAO;
    @Resource
    private BaseDAO<ShopVO> shopDAO;
    @Resource
    private BaseDAO<SystemRecordVO> systemRecordDAO;


    @Override
    public void init() {

        CacheWord.init();
        //
        initOrLoadSysRec();
        // 系统商店
        SystemVO.getInstance().loadSystemShop(shopDAO.getByRelationKey(0, SystemVO.GAME_DB_UNIQUE_ID));
        // 全服限购商品
        SystemVO.getInstance().loadShopItemLimitRecord(shopItemRecordDAO.getByRelationKey(0, SystemVO.GAME_DB_UNIQUE_ID));

    }

    @Override
    public void recordTime(SysDateRecType recordType, long time) {
        SystemRecordCache.getRecord().getRecordDate().put(recordType.getCode(), time);
        systemRecordDAO.save(SystemRecordCache.getRecord());
    }

    @Override
    public long getRecordTime(SysDateRecType recordType) {
        Long time = SystemRecordCache.getRecord().getRecordDate().get(recordType.getCode());
        if (time == null)
            time = 0L;
        return time;
    }

    @Override
    public int randomWeightValue(int weightId) {
        WeightConfig config = CacheConfig.getCfg(WeightConfig.class, weightId);
        int random = GameUtil.r.nextInt(config.getSumWeight());
        for (WeightConfig.Item item : config.getItems()) {
            if (random > item.getWeight()) {
                random -= item.getWeight();
                continue;
            } else {
                return item.getValue();
            }
        }
        return 0;
    }

    private void initOrLoadSysRec() {
        List<DBObj> dbobjs = systemRecordDAO.getAll();
        SystemRecordVO recvo;
        recvo = new SystemRecordVO();
        if (dbobjs == null || dbobjs.isEmpty()) {
            systemRecordDAO.save(recvo);
        } else {
            DBObj rec = dbobjs.get(0);
            recvo.readFromDBObj(rec);
        }
        SystemRecordCache.setRecord(recvo);
    }

}
