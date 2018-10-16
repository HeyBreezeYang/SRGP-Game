package com.cellsgame.game.module.player.csv;

import com.cellsgame.common.util.csv.BaseCfg;

/**
 * File Description.
 *
 * @author Yang
 */
public class PlayerNameConfig extends BaseCfg {
    private String firstName;
    private String malelastName;
    private String femalelastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMalelastName() {
        return malelastName;
    }

    public void setMalelastName(String malelastName) {
        this.malelastName = malelastName;
    }

    public String getFemalelastName() {
        return femalelastName;
    }

    public void setFemalelastName(String femalelastName) {
        this.femalelastName = femalelastName;
    }
}
