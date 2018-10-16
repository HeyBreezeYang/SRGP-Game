<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>菜单设置</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title">
                <span class="icon"> <i class="icon-th"></i> </span>
                <h5>游戏列表</h5>
            </div>
            <div class="widget-content ">
                <c:if test="${user.root==1}">
                    <a href="#myAlert" data-toggle="modal" onclick="setGameId(0);"  class="btn btn-info" style="margin-left: 1%;margin-bottom: 10px;">添加游戏</a>
                </c:if>
                <table class="table table-bordered table-striped with-check">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>游戏名</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody class="myBody">
                        <c:forEach items="${allGame}" var="gameList">
                            <tr>
                                <td>${gameList.id}</td>
                                <td>${gameList.name}</td>
                                <td>
                                    <c:if test="${user.root==1}">
                                        <a href="#myAlert" onclick="setGameId(${gameList.id},'${gameList.name}');" class="tip-top" data-toggle="modal" title="Update"  >
                                            <i class="icon-pencil"></i>
                                        </a>
                                        &nbsp;&nbsp;
                                        <a href="#myDeleteGame" onclick="setGameDel(${gameList.id},'${gameList.name}');"  data-toggle="modal" class="tip-top"  title="Delete">
                                            <i class="icon-remove"></i>
                                        </a>
                                    </c:if>
                                    &nbsp;&nbsp;
                                    <a href="#" onclick="forwardPage('menu/versionList',${gameList.id})"  class="tip-top" title="Go">
                                        <i class="icon-share-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="myAlert" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>游戏信息</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" method="post" name="basic_validate"  id="gameSub" novalidate="novalidate">
            <table>
                <tr>
                    <td width="35%">游戏名</td>
                    <td>
                        <input type="hidden" name="id" id="gameIdUpdate" value="0">
                        <input type="text" name="name" id="gameNameUpdate">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary" href="#" onclick="subAddGame();">确定</a>
    </div>
</div>
<div id="myDeleteGame" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>删除游戏</h3>
    </div>
    <div class="modal-body">
        <p>确定从平台管理系统中删除 <span style="color: red;" id="gameNameDelete"></span> ，以及下属所有菜单的功能，即移除对该游戏的所有操作！~
        <form class="form-horizontal" method="post" action="menu/delGame" name="basic_validate" id="gameDel" novalidate="novalidate">
            <input type="hidden" name="id" id="gameIdDelete">
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-danger" onclick="subDelGame();">确定删除</a>
    </div>
</div>

<script type="text/javascript">
    function subDelGame() {
        $("#gameDel").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/gameList',3);
            }
        });
    }
    function subAddGame() {
        if($("#gameName").val()===""){
            return false;
        }
        $("#gameSub").ajaxSubmit({
            url:"menu/updateGame",
            dataType:'json',
            success:function () {
                forwardPage('menu/gameList',3);
            }
        });
    }
    function setGameId(id,name){
        $("#gameIdUpdate").val(id);
        $("#gameNameUpdate").val(name);
    }
    function setGameDel(id,name){
        $("#gameIdDelete").val(id);
        $("#gameNameDelete").html(name);
    }
</script>