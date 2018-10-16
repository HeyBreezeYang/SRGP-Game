package com.cellsgame.game.module.player.csv;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * 固定执行配置
 * @author peterveron
 *
 */
public class FixedExecConfig extends BaseCfg {

	private int total;
	
	private int fixed;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getFixed() {
		return fixed;
	}

	public void setFixed(int fixed) {
		this.fixed = fixed;
	}
}
