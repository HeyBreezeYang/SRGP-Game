package com.cellsgame.game.core.msgproc.process.asm.visitor;

import java.util.HashMap;

import com.cellsgame.game.core.msgproc.process.asm.Cons;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethod;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethodParam;
import org.objectweb.asm.*;

/**
 * @author Aly on  2016-07-11.
 */
class ModuleMethodVisitor extends MethodVisitor {

    private ModuleAsmInfo info;
    private ModuleMethod method;


    ModuleMethodVisitor(ModuleAsmInfo info, int access, String name, String desc, String signature, String[] exceptions) {
        super(Opcodes.ASM5);
        this.info = info;
        method = new ModuleMethod(info, access, name, desc, signature, exceptions);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Cons.CLIENTMSG_ANNOTATION_NAME.equals(desc)) {
            info.getMethods().add(method);
            return new MethodAnnotationVisitor(method);
        }
        return super.visitAnnotation(desc, visible);
    }


    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        // 遍历interface
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
        if (null == method || !Cons.CLIENTPARAM_ANNOTATION_NAME.equals(desc)) {
            return null;
        }

        if (method.getParams() == null) {
            method.setParams(new HashMap<>());
        }
        ModuleMethodParam methodParam = new ModuleMethodParam();
        methodParam.setIx(parameter);
        method.getParams().put(parameter, methodParam);
        return new ParamAnnotationVisitor(methodParam);
    }

    @Override
    public void visitAttribute(Attribute attr) {
        super.visitAttribute(attr);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private static class ParamAnnotationVisitor extends AnnotationVisitor {

        private ModuleMethodParam methodParam;

        ParamAnnotationVisitor(ModuleMethodParam methodParam) {
            super(Opcodes.ASM5);
            this.methodParam = methodParam;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String desc) {
            return super.visitAnnotation(name, desc);
        }

        @Override
        public void visit(String name, Object value) {
            if (Cons.CLIENTPARAM_FIELD_NAME.equals(name)) {
                methodParam.setClientName(String.valueOf(value));
            }
            super.visit(name, value);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
        }
    }

    private static class MethodAnnotationVisitor extends AnnotationVisitor {
        private ModuleMethod method;

        MethodAnnotationVisitor(ModuleMethod method) {
            super(Opcodes.ASM5);
            this.method = method;
        }

        @Override
        public void visit(String name, Object value) {
            if (!Cons.CLIENTMSG_FIELD_NAME.equals(name))
                return;
            Integer methodID = (Integer) value;
            for (ModuleMethod moduleMethod : method.getParent().getMethods()) {
                if (moduleMethod.getMethodID() == methodID) {
                    throw new RuntimeException(
                            String.format("module %s 有重复方法ID %d 方法A: %s 方法B:%s",
                                    method.getParent().getName(), methodID, moduleMethod.getName(), method.getName()));
                }
            }
            method.setMethodID(methodID);
        }

        @Override
        public AnnotationVisitor visitAnnotation(String name, String desc) {
            return super.visitAnnotation(name, desc);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
        }
    }

}
