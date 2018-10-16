package com.cellsgame.game.module.skill.bo.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.hero.cache.CacheHeroOpenedSkill;
import com.cellsgame.game.module.hero.csv.HeroCfg;
import com.cellsgame.game.module.hero.msg.CodeHero;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.skill.bo.SkillBO;
import com.cellsgame.game.module.skill.cache.CacheSkill;
import com.cellsgame.game.module.skill.cons.SkillType;
import com.cellsgame.game.module.skill.csv.SkillCfg;
import com.cellsgame.game.module.skill.csv.SkillSealCfg;
import com.cellsgame.game.module.skill.msg.CodeSkill;
import com.cellsgame.game.module.skill.vo.SkillSealVO;
import com.cellsgame.game.module.skill.vo.SkillVO;
import com.cellsgame.orm.BaseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SkillBOImpl implements SkillBO {
    private static final Logger log = LoggerFactory.getLogger(SkillBOImpl.class);
    @Resource
    private BaseDAO<HeroVO> heroDAO;

    @Resource
    private  BaseDAO<SkillVO> skillDAO;

    @Override
    public void buildAsLoad(CMD cmd, PlayerVO pvo, Map<String, ?> data) throws LogicException {

    }

    @Override
    public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {

    }

    @Override
    public Map heroSkillLearn(PlayerVO pvo, int heroId, int skillCId, CMD cmd) throws LogicException {
        log.debug("heroSkillLearn : heroId:{}, skillCId:{}", heroId, skillCId);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));
        HeroVO heroVO = pvo.getHeroes().get(heroId);

        SkillCfg cfg = CacheSkill.skillMap.get(skillCId);
        CodeSkill.SKILL_NOT_CONFIG.throwIfTrue(null == cfg);

        // 检查技能是否解锁
        checkHeroSkillStar(heroVO, cfg.getId());

        // 检查前置技能
        checkHeroSkillsLearn(heroVO, cfg.getPreIds());

        // 检查兵种
        checkArms(heroVO.getCid(), skillCId);

        // 检查颜色武器
        checkColorWeapons(heroVO.getCid(), skillCId);

        // 检查英雄SP
        CodeHero.HERO_SP_MINUS.throwIfTrue(cfg.getNeed_sp() > heroVO.getSp());

        heroVO.setSp(heroVO.getSp() - cfg.getNeed_sp());

        SkillVO skillVO = getHeroOpenedSkill(heroVO, skillCId);
        skillVO.setLearned(true);

        heroDAO.save(heroVO);
        skillDAO.save(skillVO);

        Map result = GameUtil.createSimpleMap();
        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map heroSkillInherit(PlayerVO pvo, int heroId1, int heroId2, List<Integer>inheritSkills, CMD cmd) throws LogicException {
        log.debug("heroSkillInherit : heroId1:{}, heroId2:{}", heroId1, heroId2);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId1));
        HeroVO heroVO1 = pvo.getHeroes().get(heroId1);

        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId2));
        HeroVO heroVO2 = pvo.getHeroes().get(heroId2);

        // 超出可继承数量
        CodeSkill.SKILL_INHERIT_LIMIT.throwIfTrue(inheritSkills.size() > 3);

        for(Integer id: inheritSkills) {
            // 检查技能是否属于英雄2
            CodeSkill.SKILL_NOT_FOUND.throwIfTrue(!heroVO2.getOpenedSkills().containsKey(id));
        }
        // TODO 前置技能单独检查，未完成
        checkHeroGroupedSkills(heroVO1, groupSkillsByInherit(heroVO2, inheritSkills));

        for(Integer id: inheritSkills) {
            // 检查星级条件
            SkillVO skillVO = heroVO2.getOpenedSkills().get(id);
            checkHeroSkillStar(heroVO1, skillVO.getCid());

            // 是否仅限自己装备
            CodeSkill.SKILL_ONLY_SELF.throwIfTrue(CacheHeroOpenedSkill.isOnlySelf(heroId2, id));

            // 检查兵种
            checkArms(heroId1, id);
            // 检查颜色武器
            checkColorWeapons(heroId1, id);
        }
        // SP消耗1.5倍
        return null;
    }

    @Override
    public Map heroSkillEquipOn(PlayerVO pvo, int heroId, int skillId, CMD cmd) throws LogicException {
        log.debug("heroSkillEquipOn : heroId:{}, skillId:{}", heroId, skillId);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));
        HeroVO heroVO = pvo.getHeroes().get(heroId);


        SkillVO skillVO = heroVO.getOpenedSkills().get(skillId);
        CodeSkill.SKILL_NOT_FOUND.throwIfTrue(null == skillVO);
        CodeSkill.SKILL_EQUIPPED_ON.throwIfTrue(skillVO.isEquiped());
        CodeSkill.SKILL_NOT_LEARN.throwIfTrue(!skillVO.isLearned());

        SkillCfg cfg = CacheSkill.skillMap.get(skillVO.getCid());
        // 检查兵种
        checkArms(heroVO.getCid(), skillVO.getCid());

        // 检查颜色武器
        checkColorWeapons(heroVO.getCid(), skillVO.getCid());

        // 卸下已装备的其他技能
        SkillVO equipedVO = heroVO.getEquippedSkills().get(cfg.getType());
        if (null != equipedVO) {
            equipedVO.setEquiped(false);
            skillDAO.save(equipedVO);
        }
        skillVO.setEquiped(true);
        skillDAO.save(skillVO);

        // 更新英雄已装备技能表
        heroVO.getEquippedSkills().put(cfg.getType(), skillVO);

        Map result = GameUtil.createSimpleMap();
        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map heroSkillEquipOff(PlayerVO pvo, int heroId, int skillId, CMD cmd) throws LogicException {
        log.debug("heroSkillEquipOff : heroId:{}, skillId:{}", heroId, skillId);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));
        HeroVO heroVO = pvo.getHeroes().get(heroId);

        SkillVO skillVO = heroVO.getOpenedSkills().get(skillId);
        CodeSkill.SKILL_NOT_FOUND.throwIfTrue(null == skillVO);
        CodeSkill.SKILL_EQUIPPED_OFF.throwIfTrue(!skillVO.isEquiped());

        skillVO.setEquiped(false);
        skillDAO.save(skillVO);

        SkillCfg cfg = CacheSkill.skillMap.get(skillVO.getCid());
        heroVO.getEquippedSkills().remove(cfg.getType());

        Map result = GameUtil.createSimpleMap();
        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map sealCreate(PlayerVO pvo, int skillCId, CMD cmd) throws LogicException {
        log.debug("sealCreate : skillCId:{}", skillCId);
        SkillSealCfg cfg = CacheSkill.skillSealMap.get(skillCId);
        CodeSkill.SKILL_NOT_CONFIG.throwIfTrue(null == cfg);
        CodeSkill.SKILL_NOT_SEAL.throwIfTrue(cfg.getType() != SkillType.SEAL.getValue());

        // TODO 检查玩家的英雄是否拥有和该圣印对应的技能
        checkedMatchedSkill(pvo,cfg.getSkill_id());

        // 检查前置圣印
        checkPlayerSealsLearn(pvo, cfg.getPreIds());

        Map result = GameUtil.createSimpleMap();
        // TODO 执行json_func，扣道具
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(cfg.getNeed_items());
        executor.exec(result, pvo);

        createSeal(pvo, skillCId, cmd);

        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map sealUpgrade(PlayerVO pvo, int skillId, CMD cmd) throws LogicException {
        log.debug("sealUpgrade : skillId:{}", skillId);
        CodeSkill.SKILL_NOT_FOUND.throwIfTrue(!pvo.getSeals().containsKey(skillId));
//
        SkillVO sealVO = pvo.getSeals().get(skillId);




        // 是否满级
        SkillSealCfg cfg = CacheSkill.skillSealMap.get(sealVO.getCid());
        CodeSkill.SKILL_FULL_LEVEL.throwIfTrue(cfg.getRear_id() == 0);

        Map result = GameUtil.createSimpleMap();

        // TODO 执行json_func，扣道具
        FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
        executor.addSyncFunc(cfg.getUpgrade_need_items());
        executor.exec(result, pvo);

        // 更新配置档ID
        sealVO.setCid(cfg.getRear_id());

        skillDAO.save(sealVO);
        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map heroSealEquipOn(PlayerVO pvo, int heroId, int skillId, CMD cmd) throws LogicException {
        log.debug("heroSealEquipOn : heroId:{}, skillId:{}", heroId, skillId);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));
        HeroVO heroVO = pvo.getHeroes().get(heroId);

        CodeSkill.SKILL_NOT_FOUND.throwIfTrue(!pvo.getSeals().containsKey(skillId));


        SkillVO sealVO = pvo.getSeals().get(skillId);

        // 检查兵种
        checkArms(heroVO.getCid(), sealVO.getCid());

        // 检查颜色武器
        checkColorWeapons(heroVO.getCid(), sealVO.getCid());

        Map result = GameUtil.createSimpleMap();
        // 卸下已装备的其他圣印
        SkillSealCfg cfg = CacheSkill.skillSealMap.get(sealVO.getCid());
        HeroVO holderVO = pvo.getHeroes().get(sealVO.getHolderId());
        if (null != holderVO) {
            SkillVO equipedVO = holderVO.getEquippedSkills().get(cfg.getType());
            if (null != holderVO) {
                equipedVO.setEquiped(false);
                equipedVO.setHolderId(0);
                skillDAO.save(equipedVO);

                holderVO.getEquippedSkills().remove(cfg.getType());
            }
        }
        sealVO.setEquiped(true);
        sealVO.setHolderId(heroId);
        skillDAO.save(sealVO);

        // 更新英雄已装备技能表
        heroVO.getEquippedSkills().put(cfg.getType(), sealVO);
        // TODO 返回消息未定
        return result;
    }

    @Override
    public Map heroSealEquipOff(PlayerVO pvo, int heroId, int skillId, CMD cmd) throws LogicException {
        log.debug("heroSealEquipOff : heroId:{}, skillId:{}", heroId, skillId);
        // 检查玩家拥有的英雄
        CodeHero.HERO_NOT_FOUND.throwIfTrue(!pvo.getHeroes().containsKey(heroId));
        HeroVO heroVO = pvo.getHeroes().get(heroId);

        CodeSkill.SKILL_NOT_FOUND.throwIfTrue(!pvo.getSeals().containsKey(skillId));

        SkillVO sealVO = pvo.getSeals().get(skillId);
        CodeSkill.SEAL_NOT_EQUIP.throwIfTrue(sealVO.getHolderId() != heroId);

        SkillSealCfg cfg = CacheSkill.skillSealMap.get(sealVO.getCid());


        Map result = GameUtil.createSimpleMap();

        sealVO.setEquiped(false);
        sealVO.setHolderId(0);
        skillDAO.save(sealVO);

        heroVO.getEquippedSkills().remove(cfg.getType());
        // TODO 返回消息未定
        return result;
    }

    /**
     * 检查技能星级是否满足
     * @param heroVO
     * @param skillCId
     */
    private void checkHeroSkillStar(HeroVO heroVO, Integer skillCId) {
        if (CacheHeroOpenedSkill.hasSkill(heroVO.getCid(), skillCId)) {
            Integer openStar= CacheHeroOpenedSkill.getSkillOpenStar(heroVO.getCid(), skillCId);
            CodeSkill.SKILL_STAR_LIMIT.throwIfTrue(heroVO.getStar() < openStar);
        }
    }

    /**
     * 按技能学习的前后置关系给英雄把给定的技能列表分组
     * @param heroVO
     * @param ids
     * @return {cid1: {post_cid11,post_cid12},id2: {post_cid21,post_cid22}}
     */
    private Map<Integer, Set<Integer>> groupSkillsByInherit(HeroVO heroVO, List<Integer> ids) {
        Map<Integer, Set<Integer>> preCfgs = GameUtil.createSimpleMap();
        for(Integer id: ids) {
            SkillVO skillVO = heroVO.getOpenedSkills().get(id);
            CodeSkill.SKILL_NOT_FOUND.throwIfTrue(null == skillVO);
            SkillCfg skillCfg = CacheSkill.skillMap.get(skillVO.getCid());
            preCfgs.put(skillVO.getCid(), skillCfg.getPreIds());
        }

        Map<Integer, Set<Integer>> postCIds = GameUtil.createSimpleMap();
        for(Integer cid: preCfgs.keySet()) {
            postCIds.put(cid, GameUtil.createSet());
        }

        Integer[] cids = (Integer[]) preCfgs.keySet().toArray();
        for (int i = 0;i < cids.length;i ++) {
            for (Integer cid: postCIds.keySet()) {
                if (cid == cids[i]) {
                    continue;
                }
                if (preCfgs.get(cid).contains(cids[i])) {
                    if (!postCIds.get(cids[i]).contains(cid)){
                        postCIds.get(cids[i]).add(cid);
                    }
                }
            }
        }
        return postCIds;
    }

    private void checkHeroGroupedSkills(HeroVO heroVO, Map<Integer, Set<Integer>> postCIds) {
        Set<Integer> markLearn = GameUtil.createSet();
        for (Integer cid: postCIds.keySet()) {
            SkillCfg skillCfg = CacheSkill.skillMap.get(cid);

            if (!markLearn.contains(cid)) {
                checkHeroSkillsLearn(heroVO, skillCfg.getPreIds());
                markLearn.add(cid);
            } else {
                for (Integer postId : postCIds.get(cid)) {
                    if (!markLearn.contains(postId)) {
                        markLearn.add(postId);
                    }
                }
            }
        }
    }
    /**
     * 检查英雄是否学习过给定的技能
     * @param heroVO
     * @param preCIds
     */
    private void checkHeroSkillsLearn(HeroVO heroVO, Set<Integer> preCIds) {
        if (preCIds.size() == 0) {
            return;
        }
        Map<Integer, SkillVO> skills = heroVO.getOpenedSkills();
        for (Integer cid : preCIds) {

            if (0 == cid) continue;

            for (SkillVO vo : skills.values()) {
                //只要有一个前置技能已学些，则认为技能可学
                if (vo.getCid() == cid && vo.isLearned()) {
                    return;
                }
            }
        }
        CodeSkill.SKILL_PRE_NEEDED.throwException();
    }

    /**
     * 检查玩家的英雄中是否学习过给定的技能
     * @param pvo
     * @param preCIds
     */
    private void checkPlayerSealsLearn(PlayerVO pvo, Set<Integer> preCIds) {
        if (preCIds.size() == 0) {
            return;
        }

        Map<Integer, SkillVO> skills = pvo.getSeals();
        if (skills.size() == 0) {
            return;
        }

        for (Integer cid : preCIds) {

            if (0 == cid) continue;

            for (SkillVO vo : skills.values()) {
                if (vo.getCid() == cid && vo.isLearned()) {
                    return;
                }
            }
        }
        CodeSkill.SEAL_PRE_NEEDED.throwException();
    }

    // TODO 可优化
    // 检查玩家所有英雄已开放的技能
    private void checkedMatchedSkill(PlayerVO pvo, Integer skillCId) {
        if (0 == skillCId) {
            return;
        }

        Map<Integer, HeroVO> heroes = pvo.getHeroes();
        for (HeroVO hero : heroes.values()) {
            Map<Integer, SkillVO> skills = hero.getOpenedSkills();
            for (SkillVO vo : skills.values()) {
                if (vo.getCid() == skillCId) {
                    return;
                }
            }
        }
        CodeSkill.SKILL_FULL_LEVEL.throwIfTrue(true);
    }

    // 获取指定的英雄已开放技能
    private SkillVO getHeroOpenedSkill(HeroVO heroVO, Integer skillCId) {
        Map<Integer, SkillVO> skills = heroVO.getOpenedSkills();
        for (SkillVO vo : skills.values()) {
            if (vo.getCid() == skillCId) {
                return vo;
            }
        }
        return null;
    }
    /**
     * 兵种限制
     * @param heroCId
     * @param skillCId
     */
    private  void checkArms(Integer heroCId, Integer skillCId) {
        HeroCfg heroCfg = CacheConfig.getCfg(HeroCfg.class, heroCId);
        SkillCfg skillCfg = CacheSkill.skillMap.get(skillCId);
        int[] arms = skillCfg.getLimit_arms();

        if (arms.length == 0) {
            return;
        }

        for (int i = 0;i < arms.length; i++) {
            if (heroCfg.getMove_type() == arms[i]) {
                return;
            }
        }
        CodeSkill.SKILL_ARM_LIMIT.throwIfTrue(true);
    }

    // TODO 通用功能
    private Integer getColorWeaponCode(HeroCfg cfg) {
        return ((cfg.getWeapon_type() - 1) << 2) + cfg.getColor_type();
    }

    private void checkColorWeapons(Integer heroCId, Integer skillCId) {
        HeroCfg heroCfg = CacheConfig.getCfg(HeroCfg.class, heroCId);
        SkillCfg skillCfg = CacheSkill.skillMap.get(skillCId);
        Integer code = getColorWeaponCode(heroCfg);
        int[] arms = skillCfg.getLimit_weapons();
        if (0 == arms.length ) {
            return;
        }

        // TODO 999是魔数
        for (int i = 0;i < arms.length; i++) {
            if (code != 999 && code == arms[i]) {
                return;
            }
        }
        CodeSkill.SKILL_WEAPON_LIMIT.throwIfTrue(true);
    }

    private void createSeal(PlayerVO pvo, Integer cid, CMD cmd) {

        SkillVO sealVO = new SkillVO();

        sealVO.setCid(cid);

        sealVO.setEquiped(false);
        sealVO.setLearned(true);
        sealVO.setPlayerId(pvo.getId());

        sealVO.writeToDBObj();
        pvo.getSeals().put(sealVO.getId(), sealVO);

        skillDAO.save(sealVO);
    }

}
