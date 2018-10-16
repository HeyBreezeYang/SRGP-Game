package com.cellsgame.game.module.rank.vo;

import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @author Aly on 2017-03-14.
 */
public class RankVO extends DBVO {
    
	private int id;
    
    private int keyId;
    
    @Save(ix = 1)
    private int type;
    
    @Save(ix = 2)
    private long value;
    
    @Save(ix = 3)
    private long ix;

    public RankVO() {
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object o) {
        id = (int) o;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{getKeyId()};
    }

    @Override
    protected void setRelationKeys(Object[] objects) {
        setKeyId(((int) objects[0]));
    }

    @Override
    protected void init() {
        setKeyId(0);
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer integer) {

    }



	public long getIx() {
		return ix;
	}

	public void setIx(long ix) {
		this.ix = ix;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

}
