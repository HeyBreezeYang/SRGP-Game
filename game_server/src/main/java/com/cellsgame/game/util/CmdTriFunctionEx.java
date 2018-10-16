/**
 * 
 */
package com.cellsgame.game.util;

import com.cellsgame.game.core.message.CMD;

/**
 * @author peterveron
 *
 */
public interface CmdTriFunctionEx<T,U,K,R> {
	public R apply(CMD cmd, T t,U u,K k, Object ... params);
}
