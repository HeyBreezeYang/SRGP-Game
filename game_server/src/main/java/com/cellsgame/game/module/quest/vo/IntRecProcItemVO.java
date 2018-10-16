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
public class IntRecProcItemVO extends ProcItemVO {
	private Integer rec;

	public Integer getRec() {
		return rec;
	}

	public void setRec(Integer rec) {
		this.rec = rec;
	}

	/**
	 * @see com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO#toInfoMap()
	 */
	@Override
	public Map toInfoMap() {
		Map ret = GameUtil.createSimpleMap();
		ret.put(MsgFactoryQuest.RECORD_INT, rec);
		return ret;
	}
}
