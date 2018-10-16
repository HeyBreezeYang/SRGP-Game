/**
 * 
 */
package com.cellsgame.game.module.quest.vo;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.quest.msg.MsgFactoryQuest;
import com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO;

/**
 * @author peterveron
 *
 */
public class LongRecProcItemVO extends ProcItemVO {
	private Long rec;

	public Long getRec() {
		return rec;
	}

	public void setRec(Long rec) {
		this.rec = rec;
	}

	/**
	 * @see com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO#toInfoMap()
	 */
	@Override
	public Map toInfoMap() {
		Map ret = GameUtil.createSimpleMap();
		ret.put(MsgFactoryQuest.RECORD_LONG, Long.toString(rec));
		return ret;
	}
}
