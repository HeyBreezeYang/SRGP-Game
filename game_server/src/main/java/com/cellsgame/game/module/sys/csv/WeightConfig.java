package com.cellsgame.game.module.sys.csv;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;

import java.util.List;

public class WeightConfig extends BaseCfg {

    public static class Item {

        private int weight;

        private int value;

        public Item(int weight, int value){
            this.weight = weight;
            this.value = value;
        }

        public int getWeight() {
            return weight;
        }

        public int getValue() {
            return value;
        }
    }

    private int sumWeight;

    private List<Item> items = GameUtil.createList();

    public int getSumWeight() {
        return sumWeight;
    }

    public void setItems(String str){
        String[] subInfos = str.split(";");
        for (String subInfo : subInfos) {
            String[] args = subInfo.split("\\|");
            int weight = Integer.parseInt(args[0]);
            int value = Integer.parseInt(args[1]);
            sumWeight += weight;
            items.add(new Item(weight, value));
        }
    }

    public List<Item> getItems() {
        return items;
    }
}
