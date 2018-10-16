//package com.cellsgame.game.module.func.impl.checker;
//
//import com.cellsgame.game.cache.CacheConfig;
//import com.cellsgame.game.core.excption.LogicException;
//import com.cellsgame.game.module.func.FuncParam;
//import com.cellsgame.game.module.func.IChecker;
//import com.cellsgame.game.module.player.vo.PlayerVO;
//import com.cellsgame.game.module.quest.csv.QuestConfig;
//import com.cellsgame.game.module.quest.msg.CodeQuest;
//
//public class QuestAcceptChecker implements IChecker {
//
//    @Override
//    public void check(PlayerVO player, FuncParam param) throws LogicException {
//        // 接受任务的ID
//        int questId = param.getParam();
//        // 任务数据
//        QuestConfig questConfig = CacheConfig.getCfg(QuestConfig.class, questId);
//        // 如果任务不存在
//        CodeQuest.Quest_NotFound.throwIfTrue(questConfig == null);
//        // 查看是否已接受
//        CodeQuest.Quest_Accepted.throwIfTrue(player.getQuestVOTable().get(questConfig.getType(), questId) != null);
//        // 如果任务不能重复做, 且已完成任务列表存在该任务
//        CodeQuest.Quest_CanNotAcceptAgain.throwIfTrue(!questConfig.isRepeatable() && player.getFinishedQuests().contains(questId));
//    }
//}
