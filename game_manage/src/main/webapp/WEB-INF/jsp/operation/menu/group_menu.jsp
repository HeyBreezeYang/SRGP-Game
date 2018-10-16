<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>菜单设置</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title">
                <span class="icon"> <i class="icon-th"></i> </span>
                <h5>分组列表</h5>
            </div>
            <div class="widget-content ">
                <a href="#" onclick="forwardPage('menu/versionList',${gid})" class="tip-top" data-original-title="back">
                    <i class="icon-backward"></i>
                </a>
                <c:if test="${user.root==1}">
                    <a href="#myAlert" data-toggle="modal" onclick="setGroup(0);" class="btn btn-info" style="margin-left: 1%;margin-bottom: 10px;">添加分组</a>
                </c:if>
                <table class="table table-bordered table-striped with-check">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>分组名</th>
                        <th>分组图标</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody class="myBody">
                    <c:forEach items="${allGroup}" var="groupList">
                        <tr>
                            <td>${groupList.id}</td>
                            <td>${groupList.groupName}</td>
                            <td>${groupList.groupIcon}</td>
                            <td>
                                <c:if test="${user.root==1}">
                                    <a href="#myAlert" class="tip-top" data-toggle="modal" title="Update" onclick="setGroup(${groupList.id},'${groupList.groupName}','${groupList.groupIcon}');" >
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
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div id="myAlert" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>分组信息</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" method="post" action="menu/updateGroup" name="basic_validate"  id="groupSub" novalidate="novalidate">
            <input type="hidden" name="vid" value="${vid}">
            <input type="hidden" name="id" value="0" id="groupIdUpdate">
            <table>
                <tr>
                    <td width="35%">分组名称</td>
                    <td>
                        <input type="text" name="groupName" id="groupNameUpdate"/>
                    </td>
                </tr>
                <tr>
                    <td>分组图标</td>
                    <td>
                        <input type="text" name="groupIcon" id="groupIconUpdate"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary" href="#" onclick="subGroup();">确定</a>
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
    function subGroup() {
        if($("#groupNameUpdate").val()===""){
            return false;
        }
        if($("#groupIconUpdate").val()===""){
            return false;
        }
        $("#groupSub").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/groupList',${vid})
            }
        });
    }
    function setGroup(id, name, icon) {
        $("#groupIdUpdate").val(id);
        $("#groupNameUpdate").val(name);
        $("#groupIconUpdate").val(icon);
    }
    function setGroupDel(id,name) {
        $("#groupIdDelete").val(id);
        $("#groupNameDelete").html(name);
    }

    function subDelGroup() {
        $("#groupDel").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/groupList',${vid})
            }
        });
    }

</script>