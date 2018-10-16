package com.cellsgame.game.core.msgproc.process.asm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cellsgame.game.core.config.GameSysConfig;
import com.cellsgame.game.core.msgproc.process.asm.obj.LocalVar;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleAsmInfo;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethod;
import com.cellsgame.game.core.msgproc.process.asm.obj.ModuleMethodParam;
import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Aly on  2016-07-12.
 */
public class ModuleWriter implements Opcodes, Cons {
    private static final Logger log = LoggerFactory.getLogger(ModuleWriter.class);
    private static final boolean writeClassFile = GameSysConfig.getBoolean("game.core.dispatcher.writeClazzFile", false);
    private final Function<Integer, Boolean> isAllowHaveGameMessageMethod;
    private ModuleAsmInfo info;
    private ClassWriter writer;

    public ModuleWriter(ModuleAsmInfo info, Function<Integer, Boolean> isAllowHaveGameMessageMethod) {
        this.info = info;
        this.isAllowHaveGameMessageMethod = isAllowHaveGameMessageMethod;
        writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    }

    private static Class<?> defineClass(byte[] bytes, ClassLoader classloader) {
        return TheUnSafe.getUNSAFE().defineClass(null, bytes, 0, bytes.length, classloader, null);
    }

    public Class<?> writeToClass() {
        writeName();
        writeField();
        writeConstructor0();
        writeConstructor();
        writeGetParamMethod();
        writeGetModuleIDMethod();
        writeDispatchMethod();
        writer.visitEnd();
        byte[] bytes = writer.toByteArray();

        Class<?> clazz = defineClass(bytes, ModuleWriter.class.getClassLoader());
        if (writeClassFile) {
            try (FileOutputStream fos = new FileOutputStream(clazz.getSimpleName() + ".class")) {
                fos.write(bytes);
            } catch (IOException e) {
                log.error("", e);
            }
        }
        return clazz;
    }

    private void writeGetModuleIDMethod() {
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "getModuleID", "()I", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLdcInsn(info.getModuleID());
        mv.visitInsn(IRETURN);
        Label l1 = new Label();
        mv.visitLabel(l1);
//        mv.visitLocalVariable("this", info.getModuleName(), info.getModuleType(), l0, l1, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void writeGetParamMethod() {
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, "getParam", "(ILjava/util/Map;)[Ljava/lang/Object;", "(ILjava/util/Map<**>;)[Ljava/lang/Object;", new String[]{LOGIC_EXCEPTION_CLASS_NAME});
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitVarInsn(ILOAD, 1);
//        Label l1 = new Label();
        Label dflt = new Label();

        List<ModuleMethod> methods = info.getMethods();
        List<ModuleMethod> copy = methods.stream()
                .filter(moduleMethod -> moduleMethod.getParams().size() > 0)
                .sorted(Comparator.comparing(ModuleMethod::getMethodID))
                .collect(Collectors.toList());

        int sum = copy.size();
        List<LocalVar> vars = new ArrayList<>();
        if (sum > 0) {
            int[] keys = new int[sum];
            Label[] labels = new Label[sum];
            int size = copy.size();
            for (int i = 0; i < size; i++) {
                ModuleMethod moduleMethod = copy.get(i);
                keys[i] = moduleMethod.getMethodID();
                labels[i] = new Label();
            }

            mv.visitLookupSwitchInsn(dflt, keys, labels);
//        Label ll = new Label();
//        mv.visitLabel(ll);


            for (int i = 0; i < size; i++) {
                ModuleMethod moduleMethod = copy.get(i);
                Label label = labels[i];
                mv.visitLabel(label);
//            mv.visitLabel(l1);
                mv.visitFieldInsn(GETSTATIC, info.getModuleName(), "message_" + moduleMethod.getMethodID(), "[Ljava/lang/String;");
                mv.visitInsn(ARRAYLENGTH);
                mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
                mv.visitVarInsn(ASTORE, 3);

                Label l3 = new Label();
                mv.visitLabel(l3);
                mv.visitInsn(ICONST_0);
                mv.visitVarInsn(ISTORE, 4);

                Label l4 = new Label();
                mv.visitLabel(l4);
                mv.visitVarInsn(ILOAD, 4);
                mv.visitFieldInsn(GETSTATIC, info.getModuleName(), "message_" + moduleMethod.getMethodID(), "[Ljava/lang/String;");
                mv.visitInsn(ARRAYLENGTH);

                Label l5 = new Label();
                mv.visitJumpInsn(IF_ICMPGE, l5);


                Label l6 = new Label();
                mv.visitLabel(l6);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitVarInsn(ILOAD, 4);
                mv.visitVarInsn(ALOAD, 2);
                mv.visitFieldInsn(GETSTATIC, info.getModuleName(), "message_" + moduleMethod.getMethodID(), "[Ljava/lang/String;");
                mv.visitVarInsn(ILOAD, 4);
                mv.visitInsn(AALOAD);
                mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
                mv.visitInsn(AASTORE);

                Label l7 = new Label();
                mv.visitLabel(l7);
                mv.visitIincInsn(4, 1);
                mv.visitJumpInsn(GOTO, l4);
                mv.visitLabel(l5);
                mv.visitVarInsn(ALOAD, 3);
                mv.visitInsn(ARETURN);
                vars.add(LocalVar.valueOf("i", "I", null, l4, l5, 4));
                vars.add(LocalVar.valueOf("parm", "[Ljava/lang/Object;", null, l3, dflt, 3));
            }
        }

        mv.visitLabel(dflt);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        Label l8 = new Label();
        mv.visitLabel(l8);

        mv.visitLocalVariable("this", info.getModuleType(), null, l0, l8, 0);
        mv.visitLocalVariable("method", "I", null, l0, l8, 1);
        mv.visitLocalVariable("params", "Ljava/util/Map;", "Ljava/util/Map<**>;", l0, l8, 2);
        for (LocalVar localVar : vars) {
            mv.visitLocalVariable(localVar.getName(),
                    localVar.getDesc(), localVar.getSignature(),
                    localVar.getStart(), localVar.getEnd(), localVar.getIndex());
        }
        mv.visitMaxs(5, 5);
        mv.visitEnd();
    }

    private void writeConstructor0() {
        MethodVisitor mv = writer.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        mv.visitCode();
        info.getMethods().forEach(moduleMethod -> {
            int paramSize = moduleMethod.getParams().size();
            if (paramSize <= 0)
                return;
            Label l0 = new Label();
            mv.visitLabel(l0);

            mv.visitIntInsn(BIPUSH, paramSize);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/String");
            int procIX = 0;
            int methodParmIndex = 0;
            while (true) {
                ModuleMethodParam param = moduleMethod.getParams().get(methodParmIndex++);
                if (null != param) {
                    mv.visitInsn(DUP);
                    mv.visitIntInsn(BIPUSH, procIX);
                    mv.visitLdcInsn(param.getClientName());
                    mv.visitInsn(AASTORE);
                    procIX++;
                }
                if (procIX >= paramSize) {
                    break;
                }
            }
            mv.visitFieldInsn(PUTSTATIC, info.getModuleName(), "message_" + moduleMethod.getMethodID(), "[Ljava/lang/String;");
        });
        mv.visitInsn(RETURN);
        mv.visitMaxs(4, 0);
        mv.visitEnd();
    }

    private void writeField() {
        FieldVisitor bo = writer.visitField(ACC_PRIVATE, "bo", "L" + info.getName() + ";", null, null);
        bo.visitEnd();
        for (ModuleMethod moduleMethod : info.getMethods()) {
            int methodID = moduleMethod.getMethodID();
            if (methodID >= info.getModuleID() && moduleMethod.getParams().size() > 0) {
                FieldVisitor fv = writer.visitField(ACC_PRIVATE + ACC_STATIC, "message_" + methodID, "[Ljava/lang/String;", null, null);
                fv.visitEnd();
            }
        }
    }

    private void writeConstructor() {
        MethodVisitor method = writer.visitMethod(ACC_PUBLIC, "<init>", "(L" + info.getName() + ";)V", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(ALOAD, 0);
        method.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitVarInsn(ALOAD, 0);
        method.visitVarInsn(ALOAD, 1);
        method.visitFieldInsn(PUTFIELD, info.getModuleName(), "bo", info.getNameType());
        Label l3 = new Label();
        method.visitLabel(l3);
        method.visitInsn(RETURN);
        method.visitLocalVariable("this", info.getModuleType(), null, l0, l3, 0);
        method.visitLocalVariable("bo", info.getNameType(), null, l0, l3, 1);
        method.visitMaxs(2, 2);
        method.visitEnd();
    }

    private void writeDispatchMethod() {
        MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, DISPATCHER_METHOD_NAME,
                DISPATCHER_METHOD_Desc, DISPATCHER_METHOD_sign, new String[]{LOGIC_EXCEPTION_CLASS_NAME});
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
//        mv.visitVarInsn(ALOAD, 2);
//        mv.visitMethodInsn(INVOKEVIRTUAL, GAMEMESSAGE_CLAZZ_NAME, "getSubMethod", "()I", false);
        Label returnNull = new Label();
//        mv.visitJumpInsn(IFLE, returnNull);

//        Label paramLabel = new Label();
//        mv.visitLabel(paramLabel);

//        mv.visitVarInsn(ALOAD, 2);
//        mv.visitMethodInsn(INVOKEVIRTUAL, GAMEMESSAGE_CLAZZ_NAME, "getClientParam", "()[Ljava/lang/Object;", false);
//        mv.visitVarInsn(ASTORE, 3);

        Label getMethod = new Label();
        mv.visitLabel(getMethod);
        mv.visitVarInsn(ILOAD, 2);

        List<ModuleMethod> methods = info.getMethods();
        methods.sort(Comparator.comparingInt(ModuleMethod::getMethodID));
        int[] keys = new int[methods.size()];
        Label[] labels = new Label[methods.size()];
        for (int i = 0; i < methods.size(); i++) {
            ModuleMethod moduleMethod = methods.get(i);
            keys[i] = moduleMethod.getMethodID();
            labels[i] = new Label();
        }
        mv.visitLookupSwitchInsn(returnNull, keys, labels);

//        Label ll = new Label();
//        mv.visitLabel(ll);
        for (int i = 0; i < methods.size(); i++) {
            ModuleMethod moduleMethod = methods.get(i);
            Label label = labels[i];
            mv.visitLabel(label);

            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, info.getModuleName(), "bo", info.getNameType());
            List<String> parmTyps = new ArrayList<>();
            SignatureReader reader = new SignatureReader(moduleMethod.getDesc());
            reader.accept(new ParamSignatureVisitor(parmTyps, mv, moduleMethod));

            mv.visitMethodInsn(INVOKEINTERFACE, info.getName(), moduleMethod.getName(), moduleMethod.getDesc(), true);
            if (moduleMethod.getDesc().endsWith("V")) {
                // void 返回类型
                Label l5 = new Label();
                mv.visitLabel(l5);
                mv.visitInsn(ACONST_NULL);
                mv.visitInsn(ARETURN);
            } else {
                mv.visitInsn(ARETURN);
            }
        }

        mv.visitLabel(returnNull);
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);

        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLocalVariable("this", info.getModuleType(), null, l0, l5, 0);
        mv.visitLocalVariable("controller", MESSAGECONTROLLER_DESC, null, l0, l5, 1);
        mv.visitLocalVariable("cmd", GAMEMESSAGE_DESC, null, l0, l5, 2);
        mv.visitLocalVariable("parm", "[Ljava/lang/Object;", null, getMethod, l5, 3);
        mv.visitMaxs(6, 4);
        mv.visitEnd();
    }

    private void writeName() {
        writer.visit(V1_8, ACC_PUBLIC + ACC_SUPER, info.getModuleName(), null, Object_Name, new String[]{DISPATCHER_INTERFACE_NAME});
    }

    private class ParamSignatureVisitor extends SignatureVisitor {
        private List<String> paramTyps;
        private MethodVisitor mv;
        private ModuleMethod moduleMethod;
//        private int otherParmStoreIx = OTHER_PARAM_START_IX;        // 其他参数store起始量

        ParamSignatureVisitor(List<String> paramTyps, MethodVisitor mv, ModuleMethod moduleMethod) {
            super(ASM5);
            this.paramTyps = paramTyps;
            this.mv = mv;
            this.moduleMethod = moduleMethod;
        }

        @Override
        public SignatureVisitor visitParameterType() {
            // 每个参数一个新的对象
            return new SignatureVisitor(ASM5) {
                int arraySize;

                @Override
                public void visitClassType(String name) {
                    if (name.length() == 0)
                        return;
                    paramTyps.add(name);

                    WriterConfig.ParamBuilder builder = WriterConfig.defaultParamBuilder.get(name);
                    if (null != builder) {
                        if (name.equals(Cons.GAMEMESSAGE_CLAZZ_NAME)
                                && !isAllowHaveGameMessageMethod.apply(moduleMethod.getMethodID())) {
                            throw new RuntimeException("方法:" + moduleMethod.getMethodID() + " 不允许使用 ReqFightMessage");
                        }
                        builder.build(mv, moduleMethod);
                    } else {
                        writeOtherParm(name);
                        if (arraySize > 0) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < arraySize; i++) {
                                sb.append('[');
                            }
                            sb.append('L').append(name).append(';');
                            mv.visitTypeInsn(CHECKCAST, sb.toString());
                        } else {
                            mv.visitTypeInsn(CHECKCAST, name);

                        }
//                        mv.visitVarInsn(ASTORE, otherParmStoreIx++);
                    }
                }

                @Override
                public SignatureVisitor visitTypeArgument(char wildcard) {
                    // 处理泛型中的参数类型
                    return new SignatureVisitor(ASM5) {
                        @Override
                        public void visitEnd() {
                            super.visitEnd();
                        }
                    };
                }

                @Override
                public SignatureVisitor visitClassBound() {
                    return super.visitClassBound();
                }

                private void writeOtherParm(String name) {
                    // 其他参数需要客户端解析
                    // methodID 小于moduleID的不参与检查
                    if (moduleMethod.getMethodID() >= info.getModuleID()) {
                        ModuleMethodParam param = moduleMethod.getParams().get(paramTyps.size() - 1);
                        if (null == param) {
                            throw new RuntimeException(
                                    String.format("%s.%s 字段类型%s 非默认参数  需要在接口中指定客户端参数名字", info.getName(), moduleMethod.getName(), name));
                        }
                    }
                    int count = paramTyps.stream()
                            .filter(n -> !WriterConfig.defaultParamBuilder.containsKey(n))
                            .mapToInt(value -> 1).sum();
                    if (count > 0) {
                        mv.visitVarInsn(ALOAD, 3);
                        mv.visitIntInsn(BIPUSH, count - 1);
                        mv.visitInsn(AALOAD);
                    }
                }

                @Override
                public SignatureVisitor visitArrayType() {
                    arraySize++;
                    return super.visitArrayType();
                }

                @Override
                public void visitBaseType(char descriptor) {
                    String name = String.valueOf(descriptor);
                    paramTyps.add(name);
                    // 其他参数需要客户端解析
                    writeOtherParm(name);
                    if (arraySize > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < arraySize; i++) {
                            sb.append('[');
                        }
                        sb.append(name);
                        mv.visitTypeInsn(CHECKCAST, sb.toString());
                    } else {
                        switch (descriptor) {
                            case 'Z':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                                break;
                            case 'C':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Character");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                                break;
                            case 'B':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Boolean");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                                break;
                            case 'S':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Short");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                                break;
                            case 'I':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Integer");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                                break;
                            case 'F':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Float");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                                break;
                            case 'J':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Long");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                                break;
                            case 'D':
                                mv.visitTypeInsn(CHECKCAST, "java/lang/Double");
                                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Double", "value", "()D", false);
                                break;
                        }
                    }
//                    mv.visitVarInsn(ASTORE, otherParmStoreIx++);
                }
            };
        }
    }
}
