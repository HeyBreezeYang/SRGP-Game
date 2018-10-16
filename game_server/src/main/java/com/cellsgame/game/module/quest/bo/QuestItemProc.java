package com.cellsgame.game.module.quest.bo;

import java.util.Map;

import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.module.quest.csv.IQuestItemCfg;
import com.cellsgame.game.module.quest.msg.MsgFactoryQuest;
import com.cellsgame.game.module.quest.vo.QuestHolder;
import com.cellsgame.game.module.quest.vo.QuestProcVO;
import com.cellsgame.game.module.quest.vo.QuestProcVO.ProcItemVO;

/**
 * 任务项处理器
 * 任务由 条件以及行为的集合 组成, 这些条件以及行为统称任务项, 而关于这些任务项的处理器则称为任务项处理器
 * 每一个类型的任务项都有一个专属的任务项处理器, 这个处理器定义了关于这个任务项的配置对象以及进度对象的各
 * 种业务处理
 * 
 * @author peterveron
 * @param <QuestItemVO>
 *
 */
public interface QuestItemProc<T extends ProcItemVO, K extends IQuestItemCfg> {
	public static byte[] defaultCategory = new byte[]{};
	
	public static MsgFactoryQuest msgFactory = MsgFactoryQuest.instance();
	/**
	 * 获取进度对象类对象
	 * 用于反序列化时构造对象用
	 * @return
	 */
	public Class<T> getProcItemClass();
	
	/**
	 * 通过配置对象以及任务持有者生成进度对象
	 * 该对象的类由具体处理器自行定义, 与{@link #getProcItemClass()}返回对象类型相同即可
	 * @param qCfg 
	 * @param qHolder
	 * @return
	 */
	public T createProcItem(K qCfg, QuestHolder qHolder);

	/**
	 * 进度对象是否达成配置对象要求
	 * @param cfg
	 * @param item
	 * @return
	 */
	public boolean satisfied(K cfg, T item);
	
	
	/**
	 * 任务项监听事件
	 * 进度数据如果改变返回true, 没有改变则返回false
	 * @param parent
	 * @param cditCfg
	 * @param procItem
	 * @param qProcVO
	 * @param qHolder
	 * @param event
	 * @return
	 */
	public abstract boolean listenEvt(Map parent, K itemCfg, T procItem, QuestProcVO qProcVO, QuestHolder qHolder, GameEvent event);


	/**
	 * 本方法定义该类型任务项如何通过任务持有者以及事件对象获取用于从任务项-任务缓存池中查询出相应任务的键值
	 * @param holder
	 * @param evt
	 * @return
	 */
	public <V> V getSearchKey(QuestHolder holder, GameEvent evt);

	/**
	 * 本方法定义如何从该类型任务项的配置对象中获取 将任务填入 任务项-任务缓存池 时所必须的键值																					
	 * @param item
	 * @return
	 */
	public <V> V getCacheKey(K item);
	
	default public <V> V extractCacheKey(IQuestItemCfg cfg){
		return getCacheKey((K)cfg);
	}

	
	/**
	 * 从配置对象上获取用于缓存的分类，默认为不进行分类处理
	 * @param item
	 * @return
	 */
	default public Object getCacheCategory(K item){
		return defaultCategory;
	}
	
	
	default public Object extractCacheCategory(IQuestItemCfg item){
		return getCacheCategory((K)item);
	}

	/**
	 * 从任务持有者以及事件对象上获取用于查询的分类，默认为不分类处理
	 * @param holder
	 * @param evt
	 * @return
	 */
	default public Object getSearchCategory(QuestHolder holder, GameEvent evt){
		return defaultCategory;
	}
	
}
