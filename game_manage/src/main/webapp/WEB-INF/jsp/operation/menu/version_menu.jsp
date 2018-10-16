<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>菜单设置</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title">
                <span class="icon"> <i class="icon-th"></i> </span>
                <h5>版本列表</h5>
            </div>
            <div class="widget-content ">
                <a href="#" onclick="forwardPage('menu/gameList',3);"  class="tip-top" data-original-title="back">
                    <i class="icon-backward"></i>
                </a>
                <c:if test="${user.root==1}">
                    <a href="#myAlert" onclick="setVersion(0);" data-toggle="modal" class="btn btn-info" style="margin-left: 1%;margin-bottom: 10px;">添加版本</a>
                </c:if>
                <table class="table table-bordered table-striped with-check">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>版本名</th>
                            <th>URL</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody class="myBody">
                        <c:forEach items="${allVersion}" var="versionList">
                            <tr>
                                <td>${versionList.id}</td>
                                <td>${versionList.versionName}</td>
                                <td>${versionList.url}</td>
                                <td>
                                    <c:if test="${user.root==1}">
                                        <a href="#myAlert"  class="tip-top" data-toggle="modal" title="Update" onclick="setVersion(${versionList.id},'${versionList.versionName}','${versionList.url}');"  >
                                            <i class="icon-pencil"></i>
                                        </a>
                                        &nbsp;&nbsp;
                                        <a href="#myDeleteVersion" data-toggle="modal" class="tip-top" onclick="setVersionDel(${versionList.id},'${versionList.versionName}');" title="Delete">
                                            <i class="icon-remove"></i>
                                        </a>
                                    </c:if>
                                    &nbsp;&nbsp;
                                    <a href="#" onclick="forwardPage('menu/groupList',${versionList.id})"  class="tip-top" title="Go">
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
        <h3>版本信息</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" method="post" action="menu/updateVersion" name="basic_validate"  id="versionSub" novalidate="novalidate">
            <input type="hidden" name="id" id="versionIdUpdate" value="0">
            <input type="hidden" name="gameId" value="${gid}">
            <table>
                <tr>
                    <td width="35%">版本名称</td>
                    <td>
                        <input type="text" name="versionName" id="versionNameUpdate">
                    </td>
                </tr>
                <tr>
                    <td>版本地址</td>
                    <td>
                        <input type="text" name="url" id="versionUrlUpdate">
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-primary" href="#" onclick="subVersion();">确定</a>
    </div>
</div>
<div id="myDeleteVersion" class="modal hide">
    <div class="modal-header">
        <button data-dismiss="modal" class="close" type="button">×</button>
        <h3>删除版本</h3>
    </div>
    <div class="modal-body">
        <p>确定从平台管理系统中删除 <span style="color: red;" id="versionNameDelete"></span> ，以及下属所有菜单的功能，即移除对该版本的所有操作！~
        <form class="form-horizontal" method="post" action="menu/delVersion" name="basic_validate" id="versionDel" novalidate="novalidate">
            <input type="hidden" name="id" id="versionIdDelete">
        </form>
    </div>
    <div class="modal-footer">
        <a data-dismiss="modal" class="btn btn-danger" onclick="subDelVersion();">确定删除</a>
    </div>
</div>
<script type="text/javascript">
    function setVersion(id,name,url) {
        $("#versionIdUpdate").val(id);
        $("#versionNameUpdate").val(name);
        $("#versionUrlUpdate").val(url);
    }
    function setVersionDel(id,name) {
        $("#versionIdDelete").val(id);
        $("#versionNameDelete").html(name);
    }
    function subDelVersion(){
        $("#versionDel").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/versionList',${gid})
            }
        });
    }
    function subVersion(){
        if($("#versionNameUpdate").val()===""){
            return false;
        }
        if($("#versionUrlUpdate").val()===""){
            return false;
        }
        $("#versionSub").ajaxSubmit({
            dataType:'json',
            success:function () {
                forwardPage('menu/versionList',${gid})
            }
        });
    }

</script>