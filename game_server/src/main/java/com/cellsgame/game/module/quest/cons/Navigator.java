package com.cellsgame.game.module.quest.cons;

import java.util.ArrayList;

import com.cellsgame.common.util.JSONUtils;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.google.gson.reflect.TypeToken;

public class Navigator {
	public static void main(String[] args) {
		ArrayList<CditCfg> fromJson = JSONUtils.fromJson("[{type:0, param:1, val:2, recMode:1, exFuncs:[{type:2, param:2, value:2}]},{type:1, param:1, val:3, recMode:1, exFuncs:[{type:2, param:2, value:2}]}]", new TypeToken<ArrayList<CditCfg>>(){}.getType());
		for (CditCfg cditCfg : fromJson) {
			System.out.println("--------------------------------");
			System.out.println("type:"+cditCfg.getType());
			System.out.println("param:"+cditCfg.getParam());
			System.out.println("val:"+cditCfg.getVal());
			System.out.println("recMode:"+cditCfg.getRecMode());
			System.out.println("exFuncs:"+cditCfg.getExFuncs());
		}
		
	}
}
