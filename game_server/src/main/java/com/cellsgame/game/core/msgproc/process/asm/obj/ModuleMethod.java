package com.cellsgame.game.core.msgproc.process.asm.obj;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Aly on  2016-07-11.
 *         module 方法数据
 */
public class ModuleMethod {
    private ModuleAsmInfo parent;
    private int methodID;
    // 使用treeMap 方便顺序遍历出参数列表
    private Map<Integer, ModuleMethodParam> params = new TreeMap<>();
    private int access;
    private String name;
    private String desc;
    private String signature;
    private String[] exceptions;

    public ModuleMethod(ModuleAsmInfo parent, int access, String name, String desc, String signature, String[] exceptions) {
        this.parent = parent;
        this.access = access;
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.exceptions = exceptions;
    }

    public int getMethodID() {
        return methodID;
    }

    public void setMethodID(int methodID) {
        this.methodID = methodID;
    }


    public Map<Integer, ModuleMethodParam> getParams() {
        return params;
    }

    public void setParams(Map<Integer, ModuleMethodParam> params) {
        this.params = params;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getSignature() {
        return signature;
    }

    public String[] getExceptions() {
        return exceptions;
    }

    public ModuleAsmInfo getParent() {
        return parent;
    }
}
