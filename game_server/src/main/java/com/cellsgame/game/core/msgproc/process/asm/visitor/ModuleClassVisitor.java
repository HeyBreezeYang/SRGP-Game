package com.cellsgame.game.core.msgproc.process.asm.visitor;


import java.util.function.Consumer;

import com.cellsgame.game.core.msgproc.process.asm.Cons;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import org.objectweb.asm.*;

/**
 * @author Aly on  2016-07-11.
 */
public class ModuleClassVisitor extends ClassVisitor {

    private ModuleAsmInfo info;
    private boolean isInterFace;
    private Consumer<ModuleAsmInfo> listener;
    private boolean deprecated = false;

    public ModuleClassVisitor(Consumer<ModuleAsmInfo> listener) {
        super(Opcodes.ASM5);
        this.listener = listener;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Cons.AMODULE_ANNOTATION_NAME.equals(desc)) {
            info.setModuleClass(true);
            return new ModuleAnnotationVisitor(info);
        } else if (Deprecated.class.getName().equals(desc)) {
            deprecated = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        info = new ModuleAsmInfo(version, access, name, signature, superName, interfaces);
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            isInterFace = true;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (isInterFace && info.isModuleClass()) {
            return new ModuleMethodVisitor(info, access, name, desc, signature, exceptions);
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        if (info.isModuleClass() && !deprecated) {
            if (!isInterFace) {
                throw new RuntimeException("client注解只能用于 接口" + info.getName());
            }
            listener.accept(info);
        }
    }
}
