package com.cellsgame.game.core.event;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cellsgame.game.core.message.CMD;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.util.CollectionUtils;

public class GameEventManager {

    /**
     * 静态静态监听
     */
    private static final Multimap<EvtType, StaticEvtListener> staticListeners = ArrayListMultimap.create();

    private static void regStaticListener(StaticEvtListener listener) {
        EvtType[] types = listener.getListenTypes();
        for (EvtType eType : types) {
            if (null == eType) continue;
            staticListeners.put(eType, listener);
        }
    }

    static Map<?, ?> eventHappen(Map<?, ?> parent, CMD cmd, EvtHolder holder, GameEvent event) {
        parent = eventTypeHappened(parent, cmd, holder, EventTypeAll.ALL, event);
        return eventTypeHappened(parent, cmd, holder, event.getType(), event);
    }

    private static Map eventTypeHappened(Map parent, CMD cmd, EvtHolder holder, EvtType type, GameEvent event) {
        // 触发
        return doEvent(staticListeners.get(type), cmd, holder, parent, event);
    }

    private static Map doEvent(Collection<StaticEvtListener> typedListeners, CMD cmd, EvtHolder holder, Map parent, GameEvent event) {
        // 如果不存在监听器, 直接返回
        if (!CollectionUtils.isEmpty(typedListeners)) {
            for (StaticEvtListener listener : typedListeners) {
                parent = listener.listen(parent, cmd, holder, event);
            }
        }
        return parent;
    }
    

    public void setStaticListeners(List<StaticEvtListener> eventListeners) {
        if (null == eventListeners) return;
        for (StaticEvtListener listener : eventListeners) {
            regStaticListener(listener);
        }
    }
}
