package com.cellsgame.game.core.msgproc.process.asm.obj;

import java.util.ArrayList;
import java.util.List;

import com.cellsgame.game.core.msgproc.process.asm.Cons;

/**
 * @author Aly on  2016-07-11.
 *         module 数据缓存
 */
public class ModuleAsmInfo {
    private boolean isModuleClass;
    private int moduleID;
    private List<ModuleMethod> methods = new ArrayList<>();
    private int version;
    private int access;
    private String name;
    private String signature;
    private String supperName;
    private String[] interfaces;

    public ModuleAsmInfo(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.version = version;
        this.access = access;
        this.name = name;
        this.signature = signature;
        this.supperName = superName;
        this.interfaces = interfaces;
    }

    public boolean isModuleClass() {
        return isModuleClass;
    }

    public void setModuleClass(boolean moduleClass) {
        isModuleClass = moduleClass;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public List<ModuleMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ModuleMethod> methods) {
        this.methods = methods;
    }

    public int getVersion() {
        return version;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public String getNameType() {
        return 'L' + name + ';';
    }


    public String getSignature() {
        return signature;
    }

    public String getSupperName() {
        return supperName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public String getModuleName() {
//        return "IPO" + Cons.MODULE_NAME_FIX;
        return name + Cons.MODULE_NAME_FIX;
    }

    public String getModuleType() {
        return 'L' + name /*"LIPO"+name */ + Cons.MODULE_NAME_FIX + ';';
    }
}
