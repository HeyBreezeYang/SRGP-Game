package com.cellsgame.game.core.msgproc.process.asm;

import java.util.HashMap;
import java.util.Map;

import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethod;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Aly on  2016-07-13.
 */
class WriterConfig implements Opcodes {
    static Map<String, ParamBuilder> defaultParamBuilder = new HashMap<>();

    static {
        defaultParamBuilder.put(null, (mv, moduleMethod) -> {
        });
        defaultParamBuilder.put(Cons.PLAYERVO_CLAZZ_NAME, (mv, moduleMethod) -> {
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, Cons.PLAYER_UTIL_NAME,
                    "checkAndGetPlayerVO",
                    '(' + Cons.MESSAGECONTROLLER_DESC + ")L" + Cons.PLAYERVO_CLAZZ_NAME + ";", false);
        });
        defaultParamBuilder.put(Cons.MESSAGECONTROLLER_NAME, (mv, moduleMethod) -> mv.visitVarInsn(ALOAD, 1));
        defaultParamBuilder.put(Cons.GAMEMESSAGE_CLAZZ_NAME, (mv, moduleMethod) -> {
            mv.visitVarInsn(ALOAD, 4);
//            mv.visitInsn(ACONST_NULL);
        });

        defaultParamBuilder.put(Cons.CMD_NAME, (mv, moduleMethod)
                -> {
//            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(GETSTATIC, Cons.CMD_NAME, CMDWriter.getCmdName(moduleMethod), Cons.CMD_DESC);
            mv.visitMethodInsn(INVOKEVIRTUAL, Cons.CMD_NAME, "now", "()" + Cons.CMD_DESC, false);

        });

    }

    interface ParamBuilder {
        void build(MethodVisitor mv, ModuleMethod moduleMethod);
    }

}
