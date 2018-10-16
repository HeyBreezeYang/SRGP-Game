package com.manager.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DJL on 2017/6/23.
 *
 * @ClassName PlatFormSql
 * @Description 查询自定义SQL
 */
public enum PlatFormSql {
    MENU_QUERY_USER(){
        @Override
        public String getSql() {
            return "select t_game.id,t_game.`name` gname,D.vsnid,D.gameId,D.version,D.vurl," +
                    "D.groupid,D.mid,D.gid,D.versionId,D.`name`,D.title,D.murl from t_game RIGHT JOIN  " +
                    "(select t_version.id vsnid,t_version.gameId,t_version.version,t_version.url vurl," +
                    "C.groupid,C.mid,C.gid,C.versionId,C.`name`,C.title,C.url murl from t_version right JOIN " +
                    "(select t_group.id groupid,B.id mid,t_group.gid,t_group.versionId,t_group.`name`,B.title,B.url " +
                    "from t_group right JOIN (select id,groupId,title,url from t_menu RIGHT JOIN " +
                    "(select menuId from uid_mid where userId=@uid)A ON t_menu.id=A.menuId)B ON t_group.id=B.groupId)C " +
                    "on t_version.id=C.versionId)D ON t_game.id=D.gameId";
        }
        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("id");
            column.add("gname");
            column.add("vsnid");
            column.add("gameId");
            column.add("version");
            column.add("vurl");
            column.add("groupid");
            column.add("mid");
            column.add("gid");
            column.add("versionId");
            column.add("name");
            column.add("title");
            column.add("murl");
            return column;
        }
        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("uid");
            return params;
        }
    },
    GROUP_QUERY(){
        @Override
        public String getSql() {
            return "select id,versionId,gid,`name`,icon from t_group where versionId in(select versionId from t_group where id in (select groupId from t_menu RIGHT JOIN " +
                    "(select menuId from uid_mid where userId=@uid)A ON t_menu.id=A.menuId GROUP BY groupId) GROUP BY versionId)";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("id");
            column.add("versionId");
            column.add("gid");
            column.add("name");
            column.add("icon");
            return column;
        }

        @Override
        public List<String> getParams() {
            List<String> params=new ArrayList<>();
            params.add("uid");
            return params;
        }
    },
    QUERY_ALL_MENU(){
        @Override
        public String getSql() {
            return "select A.id,A.title,C.version,D.`name` from (select id,groupId,title from t_menu)A JOIN " +
                    "(select id,versionId from t_group)B JOIN (select id,version,gameId from t_version)C JOIN " +
                    "(SELECT id,`name` from t_game)D ON A.groupId=B.id and B.versionId=C.id and D.id=C.gameId;";
        }

        @Override
        public List<String> getColumn() {
            List<String> column=new ArrayList<>();
            column.add("id");
            column.add("title");
            column.add("version");
            column.add("name");
            return column;
        }

        @Override
        public List<String> getParams() {
            return null;
        }
    }
    ;
   public abstract String  getSql();
   public abstract List<String> getColumn();
   public abstract List<String> getParams();
}
