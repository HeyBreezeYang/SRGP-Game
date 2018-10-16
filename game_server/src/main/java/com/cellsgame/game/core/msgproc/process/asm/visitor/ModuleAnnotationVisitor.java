package com.cellsgame.game.core.msgproc.process.asm.visitor;

import com.cellsgame.game.core.msgproc.process.asm.Cons;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author Aly on  2016-07-11.
 */
class ModuleAnnotationVisitor extends AnnotationVisitor {
    private ModuleAsmInfo value;

    ModuleAnnotationVisitor(ModuleAsmInfo value) {
        super(Opcodes.ASM5);
        this.value = value;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return super.visitAnnotation(name, desc);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        return super.visitArray(name);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    @Override
    public void visit(String name, Object value) {
        if (Cons.AMODULE_FIELD_NAME.equals(name)) {
            this.value.setModuleID(((Integer) value));
        }
        super.visit(name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        super.visitEnum(name, desc, value);
    }
}
