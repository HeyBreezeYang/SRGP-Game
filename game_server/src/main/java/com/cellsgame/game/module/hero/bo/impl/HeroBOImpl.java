package com.cellsgame.game.module.hero.bo.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.bo.impl.DepotBOImpl;
import com.cellsgame.game.module.depot.bo.impl.EventDepotBOImpl;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.hero.bo.HeroBO;
import com.cellsgame.game.module.hero.cache.CacheHeroLv;
import com.cellsgame.game.module.hero.cache.CacheHeroProp;
import com.cellsgame.game.module.hero.cache.HeroLv;
import com.cellsgame.game.module.hero.cache.HeroProp;
import com.cellsgame.game.module.hero.csv.*;
import com.cellsgame.game.module.hero.msg.CodeHero;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HeroBOImpl implements HeroBO {
    @Resource
    private DepotBO depotBO;

    @Resource
    private BaseDAO<HeroVO> heroDAO;


    @Override
    public void init() {
        List<DBObj> list = heroDAO.getAll();
        HeroVO vo;
        for (DBObj dbObj : list) {
            vo = new HeroVO();
            vo.readFromDBObj(dbObj);

            PlayerVO pvo = CachePlayer.getPlayerByPid(vo.getPlayerId());
            // TODO add by luojf945
            pvo.getHeroes().put(vo.getCid(), vo);
        }
    }

    @Override
    public Map summonIn(CMD cmd, PlayerVO pvo, int group) {
        // TODO ,先临时从英雄配置表里随机抽取5个英雄下发给客户端
        Map parent = GameUtil.createSimpleMap();

        Map<Integer, HeroCfg> cfgs = CacheConfig.getCfgsByClz(HeroCfg.class);
        List<Integer> heroes = GameUtil.createList();

        if (cfgs != null) {
            int count = cfgs.keySet().size();
            Object[] ids = cfgs.keySet().toArray();

            for (int i = 0; i < 5;i ++) {
                int index = GameUtil.r.nextInt(count - i);
                heroes.add((Integer)ids[index]);
            }
        }

        parent.put("heroes", heroes);

        return MsgUtil.brmAll(parent, cmd.getCmd());
    }

    @Override
    public Map summonOut(CMD cmd, PlayerVO pvo) {
        return null;
    }

    @Override
    public Map summon(CMD cmd, PlayerVO pvo, int heroCId) throws LogicException {
        // 检查英雄配置里，英雄是否存在
        HeroCfg cfg = CacheConfig.getCfg(HeroCfg.class, heroCId);
        CodeHero.HERO_NOT_CONFIG.throwIfTrue(null == cfg);

        // TODO 从英雄召唤配置表里获取所需道具数量
        Map result = GameUtil.createSimpleMap();

        // TODO 根据配置档，随机英雄星级

        // TODO 执行json_func，扣道具
//        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//        executor.addSyncFunc();
//        executor.exec(result, pvo);

        // 创建英雄对像
        createHero(pvo, cfg, cmd);

        // 返回召唤结果
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map levelUp(CMD cmd, PlayerVO pvo, int heroId, Map<Integer, Integer> useItems) throws LogicException {
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));

        HeroVO heroVO = pvo.getHeroes().get(heroId);
        // 检查英雄等级
        CodeHero.HERO_FULL_LEVEL.throwIfTrue(heroVO.getLevel() == HeroLvCfg.FULL_LEVEL);

        Map<Integer, Integer> preUse = GameUtil.createSimpleMap();
        Integer newLevel = tryLvAllIn(pvo, heroVO, preUse);

        Map result = GameUtil.createSimpleMap();
        if (newLevel > heroVO.getLevel()) {
            // TODO 扣除道具
//            for (Integer cid : preUse.keySet()) {
//                Integer num = preUse.get(cid);
//                depotBO.changeGoodsNum(result, pvo, cid, num, cmd);
//            }
            // 升级属性
            heroLvLogic(pvo, heroVO, newLevel);
            save(heroVO);
        }

        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map starUp(CMD cmd, PlayerVO pvo, int heroId, Map<Integer, Integer> useItems) throws LogicException {
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));

        HeroVO heroVO = pvo.getHeroes().get(heroId);

        // 检查英雄星级
        CodeHero.HERO_MAX_STAR.throwIfTrue(heroVO.getStar() == HeroStarCfg.MAX_STAR);

        HeroStarCfg cfg = HeroStarCfg.configs.get(heroId).get(heroVO.getStar() + 1);

        Map result = GameUtil.createSimpleMap();

        // TODO 执行json_func
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(cfg.getCost_items());
        executor.exec(result, pvo);

        heroVO.setStar(heroVO.getStar() + 1);
        // TODO 觉醒sp加成配置
        // 升星后突破归零
        heroVO.setStage(0);

        save(heroVO);

        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map stageUp(CMD cmd, PlayerVO pvo, int heroId1, int heroId2) throws LogicException {

        HeroVO heroVO1 = pvo.getHeroes().get(heroId1);
        HeroVO heroVO2 = pvo.getHeroes().get(heroId2);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(null == heroVO1);
        CodeHero.HERO_NOT_FOUND.throwIfTrue(null == heroVO2);

        // 检查突破等级
        CodeHero.HERO_MAX_STAGE.throwIfTrue(heroVO1.getStage() == HeroStageCfg.FULL_STAGE);

        // 检查星级是否相等
        CodeHero.HERO_NOT_SAME_STAR.throwIfTrue(heroVO1.getStar() != heroVO2.getStar());
        CodeHero.HERO_NOT_SAME_ID.throwIfTrue(heroVO1.getCid() != heroVO2.getCid());

        heroVO1.setStage(heroVO1.getStage() + 1);
        // TODO 突破sp加成配置

        // TODO 检查有哪些技能可继承
        // TODO 继承英雄技能

        // TODO 清除羁绊关系

        save(heroVO1);
        delete(heroVO2);

        pvo.getHeroes().remove(heroVO2.getId());
        return null;
    }


    @Override
    public Map careerNew(CMD cmd, PlayerVO pvo, int heroId, Map<Integer, Integer> useItems) throws LogicException {
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));

        HeroVO heroVO = pvo.getHeroes().get(heroId);

        // 检查英雄是否已经转职过
        CodeHero.HERO_CAREER_EXCHANGED.throwIfTrue(heroVO.isCareerExchanged());

        // 检查英雄是否满级
        CodeHero.HERO_NOT_FULL_LEVEL.throwIfTrue(heroVO.getStar() < HeroStarCfg.MAX_STAR);

        HeroStarCfg cfg = HeroStarCfg.configs.get(heroId).get(heroVO.getStar() + 1);

        Map result = GameUtil.createSimpleMap();
        // TODO 执行json_func
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(cfg.getCost_items());
        executor.exec(result, pvo);

        heroVO.setLevel(1);
        heroVO.setCareerExchanged(true);

        save(heroVO);

        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[0];
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        return null;
    }


    /**
     * 临时创建英雄发送给客户端
     * @param pvo
     */
    public void createHeroTest(PlayerVO pvo,HeroCfg cfg){
        for(int i=0;i<2;i++) {
            HeroVO heroVO = new HeroVO();

            // 获取一级时属性
            HeroProp oneLv = getOneLvProps(pvo, cfg.getId());

            heroVO.setPlayerId(pvo.getId());
            heroVO.setCid(cfg.getId());
            heroVO.setLevel(1);
            heroVO.setStage(0);

            // TODO 根据配置随机星级
            heroVO.setStar(1);

            heroVO.setHp(oneLv.getHp());
            heroVO.setSpeed(oneLv.getSpeed());
            heroVO.setAttack(oneLv.getAttack());
            heroVO.setDefense(oneLv.getDefense());
            heroVO.setMagicDefense(oneLv.getMagic_defense());
            heroVO.setTechnique(oneLv.getTechnique());
            heroVO.setLuck(oneLv.getLuck());


            //TODO 测试--存储异常--测试需要注释下面两句
            //heroVO.writeToDBObj();
            //save(heroVO);
            pvo.getHeroList().add(heroVO);
        }

    }
    /**
     *  临时添加道具给客户端
     *
     */
    public void creatGoodsTest(PlayerVO pvo, GoodsVO gvo, CMD cmd){
        DepotBO depotBO = new DepotBOImpl();
        EventDepotBOImpl depotBOimpl = new EventDepotBOImpl(depotBO);
        depotBOimpl.createDefaultDepot(pvo,cmd);
        //pvo.getDepotVO();

    }


    private void createHero(PlayerVO pvo, HeroCfg cfg, CMD cmd) {
        HeroVO heroVO = new HeroVO();

        // 获取一级时属性
        HeroProp oneLv = getOneLvProps(pvo, cfg.getId());

        heroVO.setPlayerId(pvo.getId());
        heroVO.setCid(cfg.getId());
        heroVO.setLevel(1);
        heroVO.setStage(0);

        // TODO 根据配置随机星级
        heroVO.setStar(1);

        heroVO.setHp(oneLv.getHp());
        heroVO.setSpeed(oneLv.getSpeed());
        heroVO.setAttack(oneLv.getAttack());
        heroVO.setDefense(oneLv.getDefense());
        heroVO.setMagicDefense(oneLv.getMagic_defense());
        heroVO.setTechnique(oneLv.getTechnique());
        heroVO.setLuck(oneLv.getLuck());

        Map result = GameUtil.createSimpleMap();
        // TODO 初始化技能 执行json_func
//        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
//        executor.addSyncFunc(cfg.getA_skills());
//        executor.addSyncFunc(cfg.getB_skills());
//        executor.addSyncFunc(cfg.getC_skills());
//        executor.addSyncFunc(cfg.getAoyi_skills());
//        executor.addSyncFunc(cfg.getSupport_skills());
//        executor.exec(result, pvo);

        heroVO.writeToDBObj();

        pvo.getHeroes().put(heroVO.getId(), heroVO);

        save(heroVO);
    }

    private Map<Integer, Integer> FuncConfig_ListToMap(List<FuncConfig> items) {
        Map<Integer, Integer> ret = GameUtil.createMap();

        Iterator it = items.iterator();
        while(it.hasNext()) {
            FuncConfig cfg = (FuncConfig) it.next();
            ret.put(cfg.getParam(), (int)cfg.getValue());
        }

        return ret;
    }

    private HeroProp getOneLvProps(PlayerVO pvo, Integer cid) {
        Map<Integer, HeroProp> fullLvCfg = CacheHeroProp.heroPropMap.get(cid);
        return fullLvCfg.get(1);
    }
    /**
     * 获取英雄满级时属性（转职前，及转职后）
     * @param pvo
     * @param heroVO
     * @return
     */
    private HeroProp getFullLvProps(PlayerVO pvo, HeroVO heroVO) {
        Map<Integer, HeroProp> fullLvCfg = CacheHeroProp.heroPropMap.get(heroVO.getCid());
        return fullLvCfg.get(HeroLvCfg.FULL_LEVEL + (heroVO.isCareerExchanged() ? HeroLvCfg.FULL_LEVEL:0));
    }


    /**
     * 英雄等级增加逻辑
     * @param pvo
     * @param heroVO
     * @param newLevel
     */
    private void heroLvLogic(PlayerVO pvo, HeroVO heroVO, Integer newLevel) {
        int added = 0;
        int curLevel = heroVO.getLevel();
        int totalTimes = HeroLvCfg.FULL_LEVEL - curLevel;
        int useTimes = newLevel - curLevel;

        if (newLevel <= curLevel) {
            return;
        }

        heroVO.setLevel(newLevel);

        // TODO 扣经验
        heroVO.setExp(0);

        HeroProp cfg = getFullLvProps(pvo, heroVO);

        added = getPropAdded(totalTimes, useTimes, heroVO.getHp(), cfg.getHp());
        heroVO.setHp(heroVO.getHp() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getSpeed(), cfg.getSpeed());
        heroVO.setSpeed(heroVO.getSpeed() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getAttack(), cfg.getAttack());
        heroVO.setAttack(heroVO.getAttack() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getDefense(), cfg.getDefense());
        heroVO.setDefense(heroVO.getDefense() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getMagicDefense(), cfg.getMagic_defense());
        heroVO.setMagicDefense(heroVO.getMagicDefense() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getTechnique(), cfg.getTechnique());
        heroVO.setTechnique(heroVO.getTechnique() + added);

        added = getPropAdded(totalTimes, useTimes, heroVO.getLuck(), cfg.getLuck());
        heroVO.setLuck(heroVO.getLuck() + added);

        // TODO 升级加SP
    }

    private int tryLvAllIn(PlayerVO pvo, HeroVO heroVO, Map<Integer, Integer> preUse) {
        Integer curLevel = heroVO.getLevel();
        Integer leftExp = heroVO.getExp();

        HeroCfg heroCfg = CacheConfig.getCfg(HeroCfg.class, heroVO.getCid());
        Map<Integer, HeroLv> levelCfgs = CacheHeroLv.heroLvMap.get(heroCfg.getColor_type());

        boolean isExit = false;
        while (!isExit && curLevel < HeroLvCfg.FULL_LEVEL) {
            int added = 0;
            HeroLv levelValue = levelCfgs.get(curLevel + 1);
            Integer needExp = levelValue.getExp();
            // TODO 先使用经验(剩余经验值必须小于等于升一级所需经验值)
            {
                needExp -= leftExp;
                leftExp = 0;
            }
            // 使用普通道具
            {
                Map<Integer, Integer> costItems = FuncConfig_ListToMap(levelValue.getCost_items());
                // 按优先级使用普通道具
                int[] arr = itemsForOneLv(pvo, costItems, preUse, needExp);
                added = arr[0];
                needExp = arr[1];
            }
            // 普通道具+经验，不够够升一级。检查通用道具
            if (added == 0) {
                Map<Integer, Integer> costItems = FuncConfig_ListToMap(levelValue.getCost_general_items());
                // 按优先级使用普通道具
                int[] arr = itemsForOneLv(pvo, costItems, preUse, needExp);
                added = arr[0];
            }
            // 所有道具加起来，都不够升级
            if (added == 0) {
                isExit = true;
            } else {
                curLevel += added;
            }
        }
        return curLevel;
    }
    /**
     * @param pvo:PlayerVO对象
     * @param costItems:升级所需道具
     * @param preUse:当前可用的道具
     * @param needExp:升级所需经验
     * @return [0]=0，不升级；[0]=1升一级。[1]:这一级升级还需要多少经验
     */
    private int[] itemsForOneLv(PlayerVO pvo, Map<Integer, Integer> costItems, Map<Integer, Integer> preUse, Integer needExp) {
        int added = 0;
        for (Integer key :costItems.keySet()) {
            if (!preUse.containsKey(key)) {
                preUse.put(key, depotBO.getGoodsQuantity(pvo, key));
            }
            Integer currentNum = preUse.get(key);
            if (currentNum  >= needExp) {
                preUse.put(key, currentNum - needExp);
                needExp = 0;
                added ++;
                break;
            } else {
                preUse.put(key, 0);
                needExp -= currentNum;
            }
        }
        return new int[]{added, needExp};
    }

    /**
     * @param totalTimes:总的随机次数
     * @param useTimes:此次随机次数
     * @param curValue:初始值
     * @param maxValue:最终值
     * @return 随机值
     */
    // 给定初始值，最终值和随机次数。计算随机值，并返回。注：随机值不超过(Math.ceil((maxValue - curValue)/totalTimes))次
    private int getPropAdded(Integer totalTimes, Integer useTimes, Integer curValue, Integer maxValue) {
        if (useTimes > totalTimes) {
            return -1;
        }

        int addMode =  (maxValue - curValue) % totalTimes;
        int addFactor = (maxValue - curValue - addMode) / totalTimes;

        int added = 0;
        while (useTimes -- > 0) {
            int validValue = maxValue - (curValue + added);
            int randomValue = GameUtil.r.nextInt(totalTimes --);
            if (randomValue < validValue) {
                added += addFactor;
                if (randomValue < addMode) {
                    added += 1;
                }
            }
        }
        return added;
    }

    private void save(HeroVO vo) {
        heroDAO.save(vo);
    }

    private void delete(HeroVO vo) {
        heroDAO.delete(vo);
    }
    @Override
    public void buildAsLoad(CMD cmd, PlayerVO pvo, Map<String, ?> data) throws LogicException {
        List<DBObj> procDatas = (List<DBObj>) data.get(DATA_SIGN_HERO);
        if(procDatas != null && !procDatas.isEmpty()){
            for (DBObj dbObj : procDatas) {
                HeroVO vo = new HeroVO();
                vo.readFromDBObj(dbObj);
                pvo.getHeroes().put(vo.getCid(), vo);
            }
        }
    }

    @Override
    public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {

    }
}
