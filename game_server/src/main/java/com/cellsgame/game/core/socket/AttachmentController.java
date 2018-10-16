package com.cellsgame.game.core.socket;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.context.MessageController;

/**
 * Created by Yang on 16/7/12.
 */
public abstract class AttachmentController {
    private Map<String, Object> attribute = GameUtil.createMap();

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return attribute.containsKey(key) ? (T) attribute.get(key) : null;
    }

    public <T> T getAndRemoveAttribute(String key) {
        return attribute.containsKey(key) ? (T) attribute.remove(key) : null;
    }

    public void setAttribute(String key, Object value) {
        attribute.put(key, value);
    }

    public AttachmentController destroy() {
        if (this.attribute != null) this.attribute.clear();
        return this;
    }

    public Map<String, Object> getAttributes() {
        return attribute;
    }

    public void copyAttribute(MessageController oldController) {
        attribute.putAll(oldController.getAttributes());
    }
}
