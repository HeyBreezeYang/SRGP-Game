package com.cellsgame.game.module.fight;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.event.*;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.fight.cons.CodeFight;
import com.cellsgame.game.module.fight.cons.EvtTypeFight;
import com.cellsgame.game.module.fight.cons.MapGridType;
import com.cellsgame.game.module.fight.impl.HeroEntity;
import com.cellsgame.game.module.fight.impl.MapEntity;
import com.cellsgame.game.module.fight.impl.TeamEntity;
import com.cellsgame.game.module.fight.sequence.ActFuncType;
import com.cellsgame.game.module.fight.sequence.ActSequence;
import com.google.common.collect.HashBasedTable;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 战役（包括N个回合）
 */
public class Battle implements EvtHolder, StaticEvtListener {
    // 发起战斗的玩家Id
    private Integer playerId;
    // 关卡Id
    private Integer levelId;

    // TODO 客户端行为事件序列
    private ActSequence sequence;

    // 地图
    private MapEntity map;

    private AStar aStar;
    // 己方队伍
    private TeamEntity myTeam;
    // 对手队伍(关卡配置队伍，离线玩家队伍)
    private TeamEntity rivalTeam;

    // 双方
    private Map<Integer, HeroEntity> allHeroes;

    private boolean isMyTurn;

    private CombatManager.CombatEntity combat;

    public Battle(Integer playerId, Integer levelId) {
        this.playerId = playerId;
        this.levelId = levelId;
    }

    private void initMap() {

    }

    private void initMyTeam() {

    }

    private void initRivalTeam() {

    }

    private void initAllHeros() {
        allHeroes.clear();
        Set<HeroEntity> hereos = GameUtil.createSet();
        hereos.addAll(myTeam.getHeros());
        hereos.addAll(rivalTeam.getHeros());

        for (HeroEntity hero: hereos) {
            allHeroes.put(hero.getId(), hero);
        }
    }

    private void doHeroDead(Integer hid) {
        allHeroes.remove(hid);
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public HeroEntity getHeroById(Integer id) {
        return allHeroes.get(id);
    }

    public TeamEntity getMyTeam () {
        return myTeam;
    }

    public void setMyTeam(TeamEntity myTeam) {
        this.myTeam = myTeam;
    }

    public TeamEntity getRivalTeam () {
        return rivalTeam;
    }

    public void setRivalTeam(TeamEntity rivalTeam) {
        this.rivalTeam = rivalTeam;
    }

    public void reset() {
        initAllHeros();
    }

    public void start() throws LoginException {
        List<Map<String, List<?>>> turns = sequence.getTurnList();

        isMyTurn = true;
        for (Map<String, List<?>> turn: turns) {
            // 回合开始
            attackerActions(turn.get("attacker"));
            defenderActions(turn.get("defender"));
            // 回合结束

            // 交换回合
            isMyTurn = !isMyTurn;
        }

        Map awards = sequence.getAwardList();
        if (null != awards) {
            ActFuncType.AWARD.getFunc().exec(this, awards);
        }
    }

    private void attackerActions(List<?> acts) throws LoginException {
        if (null == acts) return;

        for (Map act : (List<Map>)acts) {
            doAction(act);
        }
    }

    private void defenderActions(List<?> acts) throws LoginException {
        if (null == acts) return;

        for (Map act : (List<Map>)acts) {
            doAction(act);
        }
    }

    private void doAction(Map<String, Object> act) throws LoginException {
        String type = (String) act.get("type");

        ActFuncType func = ActSequence.getActFunc(type);
        CodeFight.NO_ACT.throwIfTrue(null == func);

        func.getFunc().exec(this, act);
    }

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypeFight.Dead};
    }

    @SuppressWarnings({ "incomplete-switch", "rawtypes" })
    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> o = event.getType();
        if (o instanceof EvtTypeFight) {
            switch ((EvtTypeFight) o) {
                case Dead:
                    Integer hid = event.getParam(EvtParamType.HERO_ID);
                    this.doHeroDead(hid);
                    break;
            }
        }
        return parent;
    }

    public CombatManager.CombatEntity getCombat() {
        return combat;
    }

    public void setCombat(CombatManager.CombatEntity combat) {
        this.combat = combat;
    }

    public AStar getaStar() {
        return aStar;
    }

    public void setaStar(AStar aStar) {
        this.aStar = aStar;
    }

    public void prepareAStarData() {
        aStar.initMap(map.getHeight(), map.getWidth());
        HashBasedTable<Integer, Integer, Integer> grids = map.getGrids();
        for (Integer i : grids.rowKeySet()) {
            for (Integer j : grids.columnKeySet()) {
                Integer v = grids.get(i, j);
                if (MapGridType.BLOCK.getValue() == v) {
                    aStar.setBlock(i, j);
                }
            }
        }
    }
}
