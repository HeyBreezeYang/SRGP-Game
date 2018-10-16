package com.cellsgame.game.module.func;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.util.DebugTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FuncsScriptUtil {
    private static final Logger log = LoggerFactory.getLogger(FuncsScriptUtil.class);
    private static final String FUNC_TYPE_HEAD_GIVE_ROUL = "roul";
    private static final String FUNC_TYPE_HEAD_GIVE_ONE = "one";
    private static final String FUNC_TYPE_HEAD_GIVE_SOME = "some";
    private static final String FUNC_TYPE_HEAD_GIVE_ALL = "all";
    private static final String FUNC_TYPE_HEAD_GIVE_CHOSEN = "chosen";

    private static final Map<String, FuncsExecutorsType> funcHeadMapping = GameUtil.createSimpleMap();

    static {
        funcHeadMapping.put(FUNC_TYPE_HEAD_GIVE_ALL, FuncsExecutorsType.All);
        funcHeadMapping.put(FUNC_TYPE_HEAD_GIVE_ONE, FuncsExecutorsType.Random);
        funcHeadMapping.put(FUNC_TYPE_HEAD_GIVE_SOME, FuncsExecutorsType.Random);
        funcHeadMapping.put(FUNC_TYPE_HEAD_GIVE_ROUL, FuncsExecutorsType.Roulette);
        funcHeadMapping.put(FUNC_TYPE_HEAD_GIVE_CHOSEN, FuncsExecutorsType.Selection);
    }

    public static FuncsExecutor trans(String script) throws LogicException {
        String[] infos = script.split(":");
        int ix = 0;
        String typeStr = infos[ix++];
        FuncsExecutorsType execType = funcHeadMapping.get(typeStr);
        if (execType == null)
            throw new RuntimeException("not support ExecutorType:" + typeStr);
        int execParam = 1;
        if (infos.length > 2)
            execParam = Integer.parseInt(infos[ix++]);
        FuncsExecutor exec = execType.getExecutor(CMD.system.now());
        exec.setExecutorParam(execParam);
        String[] subInfos = infos[ix].split(";");
        for (String subInfo : subInfos) {
            String[] args = subInfo.split("\\|");
            AbstractFunc func = transFunc(args);
            exec.acceptFunc(func);
        }
        return exec;
    }

    private static AbstractFunc transFunc(String[] args) throws LogicException {
        try {
            int ix = 0;
            String[] infos = args[ix++].split("\\$");
            int ix2 = 0;
            int funcType = Integer.parseInt(infos[ix2++]);
            SyncFuncType sfType = Enums.get(SyncFuncType.class, funcType);
            SyncFunc func = sfType.getFunc();
            FuncParam param = new FuncParam();
            if (infos.length > 4) {
                param.setParam(Integer.parseInt(infos[ix2++]));
                param.setParam2(Integer.parseInt(infos[ix2++]));
                param.setParam3(Integer.parseInt(infos[ix2++]));
                param.setValue(Integer.parseInt(infos[ix2]));
            } else if (infos.length > 3) {
                param.setParam(Integer.parseInt(infos[ix2++]));
                param.setParam2(Integer.parseInt(infos[ix2++]));
                param.setValue(Integer.parseInt(infos[ix2]));
            } else if (infos.length > 2) {
                param.setParam(Integer.parseInt(infos[ix2++]));
                param.setValue(Integer.parseInt(infos[ix2]));
            } else if (infos.length > 1)
                param.setParam(Integer.parseInt(infos[ix2]));
            func.setParam(param);
            if (ix < args.length)
                func.setRate(Integer.parseInt(args[ix++]));
            if (ix < args.length)
                func.setSteadyExecId(Integer.parseInt(args[ix++]));

            return func;
        } catch (Exception e) {
            log.error("", e);
            StringBuilder sb = new StringBuilder();
            sb.append("==");
            for (int i = 0; i < args.length; i++) {
                sb.append(" args[").append(i).append("]：");
                sb.append(args[i]);
            }
            DebugTool.throwException("====ItemVO==error===args[]====" + sb.toString(), null);
            throw new LogicException(CodeGeneral.General_Csv_Error.getCode(), sb.toString());
        }
    }

}
