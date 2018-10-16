package com.cellsgame.game.module.sys.cache;

import com.cellsgame.game.module.sys.vo.SystemRecordVO;

public class SystemRecordCache {
	private static SystemRecordVO record;

	public static SystemRecordVO getRecord() {
		return record;
	}

	public static void setRecord(SystemRecordVO record) {
		SystemRecordCache.record = record;
	}
}
