/**
 * 
 */
package com.cellsgame.game.module.stats.cons;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.event.EvtType;

/**
 * @author peterveron
 *
 */
public enum EvtTypeStats implements EvtType {
	

	SPEC_BIZ_COUNT(1*StatsCons.COMPLEX_TYPE_3),
	SPEC_ID_GOODS_COLLC(1*StatsCons.COMPLEX_TYPE_8),
	SPEC_ID_GOODS_USE (2*StatsCons.COMPLEX_TYPE_8),

	;
	
	private static final AtomicInteger incr = new AtomicInteger(ModuleID.Statistic);
	
	private static Map<Integer,EvtTypeStats> typedEvtTypeStatistic = GameUtil.createMap();
	
	static {
		EvtTypeStats[] values = values();
		for (EvtTypeStats eType : values) {
			eType.setEvtCode(incr.incrementAndGet());
			typedEvtTypeStatistic.put(eType.getStatsType(), eType);
		}
	}
	
	public static  EvtTypeStats getByStatsType(int statsType){
		return typedEvtTypeStatistic.get(statsType);
	}


	private int code;
	
	private int statsType;
	
	/**
	 * 
	 */
	private EvtTypeStats(int statsType) {
		this.setStatsType(statsType);
	}
	
	
	public Integer unwrapStatsType(Integer specStatsType){
		if(specStatsType == null)
			return null;
		if(specStatsType>=StatsCons.COMPLEX_TYPE_START)
			return specStatsType%statsType;
		return specStatsType;
	}
	
	
	/**
	 * @see com.cellsgame.game.core.event.EvtType#getEvtCode()
	 */
	@Override
	public int getEvtCode() {
		return code;
	}

	/**
	 * @see com.cellsgame.game.core.event.EvtType#setEvtCode(int)
	 */
	@Override
	public void setEvtCode(int code) {
		this.code = code;
	}


	public int getStatsType() {
		return statsType;
	}


	public void setStatsType(int statsType) {
		this.statsType = statsType;
	}



}
