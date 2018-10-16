package com.cellsgame.game.core.cfg.valid.impl;

import javax.script.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.annoation.Check;
import com.cellsgame.game.core.cfg.valid.AnnotationChecker;
import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly @2016-12-27.
 */
public class JSChecker extends AnnotationChecker {
    private static final Logger log = LoggerFactory.getLogger(JSChecker.class);
    private Map<String, ProcType> preType = new HashMap<>();
    private ScriptEngine engine;
    private Set<String> errorJS = new HashSet<>();
    private Map<String, CompiledScript> compiledCache;
    private Pattern rep = Pattern.compile("\\.");

    @Override
    protected void checkFieldValue(Object obj, StringBuilder errorInfo, Field field, Check c, BaseCfg cfg, Map<Integer, BaseCfg> refData) {
        if (null == engine) {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            engine = engineManager.getEngineByExtension("js");
        }
        try {
            if (errorJS.contains(c.js())) {
                return;
            }
            ProcType type = preType.get(c.js());
            Object eval = null;
            if (null == type) {
                eval = tryExeAll(field, c, cfg, obj);
            } else {
                switch (type) {
                    case Simple:
                        eval = tryExeSimple(c.js(), cfg, obj);
                        break;
                    case Function:
                        eval = tryExeFunction(field, c, cfg, obj);
                        break;
                }
            }
            if (null != eval && (
                    (eval instanceof Number && (((Number) eval).intValue() != 0))
                            || (eval instanceof Boolean && (Boolean) eval)
            )) {
                String info = String.format("%nError:CfgID:[%s]\t Class:[%s.%s] \t js check:[%s]  value:[%s] is error",
                        cfg.getId(), cfg.getClass().getSimpleName(), field.getName(), c.js(), MoreObjects.toStringHelper(obj).addValue(obj).toString());
                errorInfo.append(info);
            }
        } catch (Throwable e) {
            errorJS.add(c.js());
            log.warn(" 校验错误: {}  js:[{}]", field, c.js(), e);
        }

    }

    private Object tryExeAll(Field field, Check c, BaseCfg cfg, Object obj) throws ScriptException, NoSuchMethodException {
        Object eval;
        try {
            eval = tryExeSimple(c.js(), cfg, obj);
            preType.put(c.js(), ProcType.Simple);
        } catch (ScriptException e) {
            eval = tryExeFunction(field, c, cfg, obj);
            if (null == eval) {
                errorJS.add(c.js());
                log.error(cfg.getClass().getSimpleName() + "：" + c.js() + "try exeFunc Return val is Null\n\t->" + e.getMessage());
            } else
                preType.put(c.js(), ProcType.Function);
        }
        return eval;
    }

    private Object tryExeSimple(String js, BaseCfg cfg, Object obj) throws ScriptException {
        SimpleBindings bindings = getNewBindings(cfg, obj);
        if (engine instanceof Compilable) {
            CompiledScript script = compiledScript(js, false);
            return script.eval(bindings);
        } else {
            return engine.eval(js, bindings);
        }
    }

    private CompiledScript compiledScript(String js, boolean evalIfNew) throws ScriptException {
        if (null == compiledCache) compiledCache = new HashMap<>();
        CompiledScript script = compiledCache.get(js);
        if (null == script) {
            script = ((Compilable) engine).compile(js);
            compiledCache.put(js, script);
            if (evalIfNew) script.eval();
        }
        return script;
    }

    private Object tryExeFunction(Field field, Check c, BaseCfg cfg, Object obj) throws ScriptException, NoSuchMethodException {
        String functionName = rep.matcher(cfg.getClass().getName() + "_" + field.getName()).replaceAll("\\$");
        if (engine instanceof Invocable) {
            compiledScript("function " + functionName + "(cfg,val){" + c.js() + "}", true);
            return ((Invocable) engine).invokeFunction(functionName, cfg, obj);
        } else {
            compiledScript("function " + functionName + "(){" + c.js() + "}", true);
            SimpleBindings bindings = getNewBindings(cfg, obj);
            return engine.eval(functionName + "()", bindings);
        }
    }

    private SimpleBindings getNewBindings(BaseCfg cfg, Object obj) {
        SimpleBindings bindings = new SimpleBindings();
        bindings.put("cfg", cfg);
        bindings.put("obj", obj);
        bindings.put("val", obj);
        return bindings;
    }

    @Override
    public boolean needCheck(Check c) {
        return StringUtil.isNotEmpty(c.js());
    }

    private enum ProcType {
        Function, Simple
    }
}
