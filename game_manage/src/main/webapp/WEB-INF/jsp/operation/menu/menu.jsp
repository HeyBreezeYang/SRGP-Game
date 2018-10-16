<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>菜单设置</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title">
                <span class="icon"> <i class="icon-th"></i> </span>
                <h5>菜单列表</h5>
            </div>
            <div class="widget-content ">
                <a href="#" onclick="forwardPage('menu/groupList',${vid})" class="tip-top" data-original-title="back">
                    <i class="icon-backward"></i>
                </a>
                <c:if test="${user.root==1}">
                    <a href="#myAlert" onclick="setMenu(0);" data-toggle="modal" class="btn btn-info" style="margin-left: 1%;margin-bottom: 10px;">添加菜单</a>
                    <a href="#myAlert2" onclick="setGroup(0);"  data-toggle="modal" class="btn btn-success" style="margin-left: 1%;margin-bottom: 10px;">添加分组</a>
                </c:if>
                <table class="table table-bordered table-striped with-check">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>名称</th>
                        <th>URL</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody class="myBody">
                        <c:forEach items="${sonGroup}" var="groupList">
                            <tr>
                                <td>${groupList.id}</td>
                                <td>${groupList.groupName}</td>
                                <td>---</td>
                                <td>
                                    <c:if test="${user.root==1}">
                                        <a href="#myAlert2" class="tip-top" data-toggle="modal" title="Update" onclick="setGroup(${groupList.id},'${groupList.groupName}');"  >
                                            <i class="icon-pencil"></i>
                                        </a>
                                        &nbsp;&nbsp;
                                        <a href="#myDeleteGroup"  data-toggle="modal" class="tip-top" onclick="setGroupDel(${groupList.id},'${groupList.groupName}');" title="Delete">
                                            <i class="icon-remove"></i>
                                        </a>
                                    </c:if>
                                    &nbsp;&nbsp;
                                    <a href="#" onclick="forwardPage('menu/menuList',${groupList.id})"  class="tip-top" title="Go">
                                        <i class="icon-share-alt"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:forEach items="${allMenu}" var="menuList">
                            <tr>
                                <td>${menuList.id}</td>
                                <td>${menuList.title}</td>
                                <td>${menuList.url}</td>
                                <td>
                                    <c:if test="${user.root==1}">
                                        <a href="#myAlert" class="tip-top" data-toggle="modal" title="Update" onclick="setMenu(${menuList.id},'${menuList.title}','${menuList.url}')">
                                            <i class="icon-pencil"></i>
                                        </a>
                                        &nbsp;&nbsp;
                                        <a href="#myDeleteMenu"  data-toggle="modal" class="tip-top" onclick="setMenuDel(${menuList.id},'${menuList.title}')" title="Delete">
                                            <i class="icon-remove"></i>
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div id="myAlert2" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>分组信息</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" method="post" action="menu/updateGroup" name="basic_validate"  id="groupSub" novalidate="novalidate">
            <input type="hidden" name="vid" value="${vid}">
            <input type="hidden" name="gid" value="${groupID}">
            <input type="hidden" name="id" value="0" id="groupIdUpdate">
            <table>
                <tr>
                    <td width="35%">分组名称</td>
                    <td>
                        <input type="text" name="groupName" id="groupNameUpdate">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary" href="#" onclick="subGroup();">确定</a>
    </div>
</div>

<div id="myAlert" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>菜单信息</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" method="post" action="menu/updateMenu" name="basic_validate"  id="menuSub" novalidate="novalidate">
            <input type="hidden" name="gid" value="${groupID}">
            <input type="hidden" name="id" value="0" id="menuIdUpdate">
            <table>
                <tr>
                    <td width="35%">菜单名称</td>
                    <td>
                        <input type="text" name="title" id="menuNameUpdate">
                    </td>
                </tr>
                <tr>
                    <td>菜单链接</td>
                    <td>
                        <input type="text" name="url" id="menuUrlUpdate">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary" href="#" onclick="subMenu();">确定</a>
    </div>
</div>

<div id="myDeleteMenu" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>删除菜单</h3>
    </div>
    <div class="modal-body">
        <p>确定从平台管理系统中删除 <span style="color: red;" id="menuNameDelete"></span>  菜单的功能！~
        <form class="form-horizontal" method="post" action="menu/delMenu" name="basic_validate" id="menuDel" novalidate="novalidate">
            <input type="hidden" name="id" id="menuIdDelete">
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-danger" onclick="subDelMenu();">确定删除</a>
    </div>
</div>
<div id="myDeleteGroup" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>删除分组</h3>
    </div>
    <div class="modal-body">
        <p>确定从平台管理系统中删除 <span style="color: red;" id="groupNameDelete"></span> ，以及下属所有菜单的功能，即移除对该分组的所有操作！~
        <form class="form-horizontal" method="post" action="menu/delGroup" name="basic_validate" id="groupDel" novalidate="novalidate">
            <input type="hidden" name="id" id="groupIdDelete">
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-danger" onclick="subDelGroup();">确定删除</a>
    </div>
</div>


<script type="text/javascript">
    function subMenu() {
        if($("#menuNameUpdate").val()===""){
            return false;
        }
        if($("#menuUrlUpdate").val()===""){
            return false;
        }
        $("#menuSub").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/menuList',${groupID})
            }
        });
    }
    function setMenu(id, name, url) {
        $("#menuIdUpdate").val(id);
        $("#menuNameUpdate").val(name);
        $("#menuUrlUpdate").val(url);
    }
    function setMenuDel(id,name) {
        $("#menuIdDelete").val(id);
        $("#menuNameDelete").html(name);
    }

    function subDelMenu() {
        $("#menuDel").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/menuList',${groupID})
            }
        });
    }


    function subGroup() {
        if($("#groupNameUpdate").val()===""){
            return false;
        }
        $("#groupSub").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/menuList',${groupID})
            }
        });
    }
    function setGroup(id, name) {
        $("#groupIdUpdate").val(id);
        $("#groupNameUpdate").val(name);
    }
    function setGroupDel(id,name) {
        $("#groupIdDelete").val(id);
        $("#groupNameDelete").html(name);
    }

    function subDelGroup() {
        $("#groupDel").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/menuList',${groupID})
            }
        });
    }


</script>