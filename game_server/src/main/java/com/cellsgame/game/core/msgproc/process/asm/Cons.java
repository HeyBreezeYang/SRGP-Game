package com.cellsgame.game.core.msgproc.process.asm;

import java.util.Map;

import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.GameMessage;
import com.cellsgame.game.core.msgproc.annotation.AModule;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.core.msgproc.annotation.Client;
import com.cellsgame.game.core.msgproc.process.ILogicDispatcher;
import com.cellsgame.game.module.player.PlayerUtil;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.objectweb.asm.Type;

/**
 * @author Aly on  2016-07-11.
 */
public interface Cons {
    String PLAYERVO_CLAZZ_NAME = Type.getInternalName(PlayerVO.class);

    String PLAYER_UTIL_NAME = Type.getInternalName(PlayerUtil.class);

    String Object_Name = Type.getInternalName(Object.class);
    String Object_Array_Name = Type.getInternalName(Object[].class);
    String Object_Array_Desc = Type.getDescriptor(Object[].class);

    String MESSAGECONTROLLER_DESC = Type.getDescriptor(MessageController.class);
    String MESSAGECONTROLLER_NAME = Type.getInternalName(MessageController.class);
    String GAMEMESSAGE_DESC = Type.getDescriptor(GameMessage.class);
    String GAMEMESSAGE_CLAZZ_NAME = Type.getInternalName(GameMessage.class);

    String CMD_DESC = "Lcom/cellsgame/game/core/message/CMD;";//Type.getDescriptor(CMD.class);
    String CMD_NAME = "com/cellsgame/game/core/message/CMD";// Type.getInternalName(CMD.class);

    String MAP_DESC = Type.getDescriptor(Map.class);
    String MAP_DESC_SIGN = "Ljava/util/Map<**>;";

    String AMODULE_ANNOTATION_NAME = Type.getDescriptor(AModule.class);
    String AMODULE_FIELD_NAME = "value";

    String CLIENTMSG_ANNOTATION_NAME = Type.getDescriptor(Client.class);
    String CLIENTMSG_FIELD_NAME = "value";

    String CLIENTPARAM_ANNOTATION_NAME = Type.getDescriptor(CParam.class);
    String CLIENTPARAM_FIELD_NAME = "value";
    String MODULE_NAME_FIX = "$module";

    String DISPATCHER_INTERFACE_NAME = Type.getInternalName(ILogicDispatcher.class);
    String DISPATCHER_INTERFACE_DESC = Type.getDescriptor(ILogicDispatcher.class);
    String DISPATCHER_METHOD_NAME = "dispatchLogic";
    String DISPATCHER_METHOD_Desc = '(' + MESSAGECONTROLLER_DESC + 'I' + Object_Array_Desc + GAMEMESSAGE_DESC + ')' + MAP_DESC;
    String DISPATCHER_METHOD_sign = '(' + MESSAGECONTROLLER_DESC + 'I' + Object_Array_Desc + GAMEMESSAGE_DESC + ')' + MAP_DESC_SIGN;

    int OTHER_PARAM_START_IX = 3;
    String LOGIC_EXCEPTION_CLASS_NAME = Type.getInternalName(LogicException.class);

}
