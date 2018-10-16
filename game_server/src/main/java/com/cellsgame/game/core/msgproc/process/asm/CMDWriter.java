package com.cellsgame.game.core.msgproc.process.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.cellsgame.game.core.config.GameSysConfig;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethod;
import org.objectweb.asm.*;

/**
 * Created by alyx on 17-6-29.
 * 重写CMD
 */
public class CMDWriter implements Opcodes, Cons {
    private static final boolean write2ClazzFile = GameSysConfig.getBoolean("game.core.cmd.writeClazzFile", false);

    public static void rewrite(Collection<ModuleAsmInfo> infos) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassReader cr = new ClassReader(CMD_NAME);
        List<ModuleAsmInfo> infoList = new ArrayList<>(infos);
        infoList.sort(Comparator.comparingInt(ModuleAsmInfo::getModuleID));
        infoList.forEach(moduleAsmInfo -> moduleAsmInfo.getMethods().sort(Comparator.comparingInt(ModuleMethod::getMethodID)));

        cr.accept(new ClassVisitor(ASM5, cw) {
            @Override
            public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                return super.visitField(access, name, desc, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                if ("<clinit>".equals(name)) {
                    MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
                    return new MethodVisitor(ASM5, mv) {
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == RETURN) {
                                for (ModuleAsmInfo info : infoList) {
                                    String moduleName = info.getName().substring(info.getName().lastIndexOf('/') + 1);
                                    for (ModuleMethod method : info.getMethods()) {
                                        mv.visitTypeInsn(NEW, CMD_NAME);
                                        mv.visitInsn(DUP);
                                        int methodID = method.getMethodID();
                                        if (methodID >= -1 && methodID <= 5) {
                                            mv.visitInsn(ICONST_M1 + methodID + 1);
                                        } else if (methodID <= Byte.MAX_VALUE && methodID >= Byte.MIN_VALUE) {
                                            mv.visitIntInsn(BIPUSH, methodID);
                                        } else if (methodID <= Short.MAX_VALUE && methodID >= Short.MIN_VALUE) {
                                            mv.visitIntInsn(SIPUSH, methodID);
                                        } else {
                                            mv.visitLdcInsn(methodID);
                                        }
                                        mv.visitLdcInsn(moduleName + "." + method.getName());
                                        mv.visitMethodInsn(INVOKESPECIAL, CMD_NAME, "<init>", "(ILjava/lang/String;)V", false);
                                        mv.visitFieldInsn(PUTSTATIC, CMD_NAME, getCmdName(method), CMD_DESC);
                                    }
                                }
                            }
                            super.visitInsn(opcode);
                        }
                    };
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }

            @Override
            public void visitEnd() {
                for (ModuleAsmInfo info : infoList) {
                    for (ModuleMethod method : info.getMethods()) {
                        FieldVisitor field = cw.visitField(ACC_STATIC | ACC_PUBLIC | ACC_FINAL,
                                getCmdName(method), CMD_DESC, null, null);
                        field.visitEnd();
                    }
                }
                super.visitEnd();
            }
        }, 0);

        byte[] b = cw.toByteArray();
        ClassLoader classLoader = CMDWriter.class.getClassLoader();


        if (write2ClazzFile) {
            try (FileOutputStream fos = new FileOutputStream("CMD.class")) {
                fos.write(b);
            }
        }
        TheUnSafe.getUNSAFE().defineClass(null, b, 0, b.length,
                classLoader, null);

    }

    static String getCmdName(ModuleMethod method) {
        return "A" + method.getMethodID();
    }
}
