//package com.cellsgame.game.module.func.impl.func;
//
//import com.cellsgame.common.util.csv.BaseCfg;
//import com.cellsgame.game.cache.CacheConfig;
//import com.cellsgame.game.core.excption.LogicException;
//import com.cellsgame.game.core.message.CMD;
//import com.cellsgame.game.module.func.CheckRec;
//import com.cellsgame.game.module.func.FuncParam;
//import com.cellsgame.game.module.func.SyncFunc;
//import com.cellsgame.game.module.player.vo.PlayerVO;
//import com.cellsgame.game.module.quest.csv.QuestConfig;
//import com.cellsgame.game.module.quest.vo.QuestVO;
//import com.cellsgame.orm.BaseDAO;
//import com.google.common.collect.Table;
//import org.springframework.util.CollectionUtils;
//
//import javax.annotation.Resource;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * File Description.
// *
// * @author Yang
// */
//public class RemoveQuestFunc extends SyncFunc {
//    @Resource
//    private BaseDAO<QuestVO> questDAO;
//
//    @Override
//    public Object exec(Map<?, ?> parent, CMD cmd, PlayerVO player, FuncParam param) throws LogicException {
//        // 删除任务的ID
//        int questId = param.getParam();
//        // 任务配置
//        QuestConfig config = CacheConfig.getCfg(QuestConfig.class, questId);
//        // 删除内存数据
//        QuestVO questVO = player.getQuestVOTable().remove(config.getType(), questId);
//        // 如果有删除任务数据
//        if (questVO != null) {
//            // 是否需要通知
//            boolean notify = CollectionUtils.isEmpty(param.getExtraParams());
//            // 删除任务进度
//            player.getBehaviorController().triggerDisappear(parent, questVO.getCfg().getTriggerType(), questVO.getProgressKey(), notify, cmd);
//            // 删除任务DB
//            questDAO.delete(questVO);
//        }
//        return questVO;
//    }
//
//    @Override
//    public void dealWithPrizeMap(Map prizeMap, PlayerVO player, FuncParam param) {
//
//    }
//
//    @Override
//    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
//        return null;
//    }
//
//    @Override
//    public String checkCfg(FuncParam param, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
//        // 接受任务的ID
//        int questId = param.getParam();
//        // 任务数据
//        BaseCfg questConfig = allCfgData.get(QuestConfig.class, questId);
//        if (null == questConfig) return " 错误的任务ID:" + questId;
//        return null;
//
//    }
//}
