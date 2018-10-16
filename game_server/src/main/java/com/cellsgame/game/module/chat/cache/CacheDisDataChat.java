/**
 * 
 */
package com.cellsgame.game.module.chat.cache;

import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.cons.ModuleID;

/**
 * @author peterveron
 *
 */
public enum CacheDisDataChat implements CacheDisData{

    PlayerLevelReq(1),// 需求玩家等级
    ;
    private int id;
    private int[] data;

    CacheDisDataChat(int id) {
        this.id = createId(id);
    }

    @Override
    public int getModule() {
        return ModuleID.Chat;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int[] getData() {
        return data;
    }

    @Override
    public void setData(int[] data) {
        this.data = data;
    }

}
