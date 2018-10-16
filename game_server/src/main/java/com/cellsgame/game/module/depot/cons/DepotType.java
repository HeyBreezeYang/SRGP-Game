package com.cellsgame.game.module.depot.cons;

import java.util.concurrent.atomic.AtomicInteger;

import com.cellsgame.game.module.goods.vo.GoodsVO;

/**
 * @author Aly on  2016-11-03.
 */
public enum DepotType {
	;


    private static final AtomicInteger incr = new AtomicInteger(0);

    static {
        DepotType[] values = values();
        for (DepotType dType : values) {
            dType.setDepotTypeCode(incr.incrementAndGet());  //对AtomicInteger原子的加1并返回加1后的值
        }
    }

    private int depotTypeCode;
    private Class<? extends GoodsVO> gvoClass;
    private int capacity;
    private String dataSign;


    DepotType(Class<? extends GoodsVO> clazz, String dataSign, int capacity) {
        gvoClass = clazz;
        this.dataSign = dataSign;
        this.capacity = capacity;
    }

    public int getDepotTypeCode() {
        return depotTypeCode;
    }

    public void setDepotTypeCode(int depotTypeCode) {
        this.depotTypeCode = depotTypeCode;
    }

    public int getCapacity(){
        return capacity;
    }

    public String getDataSign() {
        return dataSign;
    }

    public Class<? extends GoodsVO> getGvoClass() {
        return gvoClass;
    }

}
