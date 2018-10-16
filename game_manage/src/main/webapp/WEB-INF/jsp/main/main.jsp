<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>平台管理系统</title>
    <meta name="viewport" charset="UTF-8" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="../../../manage/matrix/css/bootstrap.min.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/fullcalendar.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/matrix-style.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/matrix-media.css" />
    <link href="../../../manage/matrix/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" href="../../../manage/matrix/css/jquery.gritter.css" />
    <style type="text/css">
        .myBody tr td{
            text-align: center;
        }
    </style>
</head>
<body>

<div id="header">
    <h1>平台管理</h1>
</div>

<div id="user-nav" class="navbar navbar-inverse">
    <ul class="nav">
        <li  class="dropdown" id="profile-messages" ><a title="" href="#" data-toggle="dropdown" data-target="#profile-messages" class="dropdown-toggle"><i class="icon icon-user"></i>  <span class="text">用户信息</span><b class="caret"></b></a>
            <ul class="dropdown-menu">
                <li><a href="#"><i class="icon-user"></i>个人资料</a></li>
                <li class="divider"></li>
                <li><a href="#" onclick="forwardPage('user/setPwd',10);"><i class="icon-check"></i>修改密码</a></li>
            </ul>
        </li>
        <li class=""><a  href="/manage/platform?user=${user.name}"><i class="icon icon-share-alt"></i> <span class="text">登出</span></a></li>
    </ul>
</div>

<!--<div id="search">-->
<!--<input type="text" placeholder="搜索..."/>-->
<!--<button type="submit" class="tip-bottom" title="Search"><i class="icon-search icon-white"></i></button>-->
<!--</div>-->

<div id="sidebar">
    <a href="#" class="visible-phone"><i class="icon icon-home"></i> 选项</a>
    <c:if test="${user.root==1}">
        <ul>
            <li class="submenu">
                <a href="#">
                    <i class="icon icon-th-list"></i>
                    <span>查看</span>
                    <span class="label label-important">2</span>
                </a>
                <ul>
                    <li><a href="#" onclick="forwardPage('user/list',1);">用户查看</a></li>
                    <li><a  href="#" onclick="forwardPage('',2);">日志查看</a></li>
                </ul>
            </li>
            <li class="submenu"> <a href="#"><i class="icon icon-pencil"></i> <span>设置</span> <span class="label label-important">3</span></a>
                <ul>
                    <li><a  href="#" onclick="forwardPage('menu/gameList',3);">菜单管理</a></li>
                    <li><a  href="#" onclick="forwardPage('user/add',4);">账户设置</a></li>
                    <li><a  href="#" onclick="forwardPage('user/power',5);">权限分配</a></li>
                </ul>
            </li>
            <li class="submenu"> <a href="#"><i class="icon icon-fullscreen"></i> <span>平台</span> <span class="label label-important">2</span></a>
                <ul>
                    <li><a  href="#" onclick="forwardPage('platform/set',6);">APP-ID设置</a></li>
                    <li><a  href="#" onclick="forwardPage('',7);">平台开关</a></li>
                </ul>
            </li>
        </ul>
    </c:if>

</div>

<div id="content">

    <div id="content-header">
        <div id="breadcrumb"><a href="#" onclick="moveMain();" title="进入系统" class="tip-bottom"><i class="icon-home"></i> 入口</a></div>
    </div>


    <div class="container-fluid" id="mainWindow">
        <div id="entrance">
            <div class="quick-actions_homepage">
                <ul class="quick-actions">
                    <c:if test="${! empty dataMsg}">
                        <li class="bg_lg span2"> <a href="#" onclick="goG('${dataMsg.url}',${dataMsg.id},'${dataMsg.versionName}');"><i class="icon-signal"></i>${dataMsg.versionName}</a><input type="hidden" id="data${dataMsg.id}" value="${dataMsg.groupData}"></li>
                    </c:if>
                    <c:if test="${! empty accountMsg}">
                        <li class="bg_lo span2"> <a href="#" onclick="goG('${accountMsg.url}',${accountMsg.id},'${accountMsg.versionName}');"><i class="icon-th-list"></i>${accountMsg.versionName}</a><input type="hidden" id="data${accountMsg.id}" value="${accountMsg.groupData}"></li>
                    </c:if>
                </ul>
            </div>
            <c:if test="${! empty user}">
                <c:forEach items="${user.game}" var="gameList">
                    <c:if test="${(gameList.index-1)%2==0}">
                        <div class="row-fluid">
                    </c:if>
                    <div class="span6">
                        <div class="widget-box">
                            <div class="widget-title bg_lo" data-toggle="collapse"
                                 href="#collapseG${gameList.id}" style="cursor: pointer;">
                                <span class="icon"><i class="icon-chevron-down"></i></span>
                                <h5>${gameList.name}</h5>
                            </div>
                            <div class="widget-content nopadding updates collapse in" id="collapseG${gameList.id}">
                                <c:forEach items="${gameList.version}" var="versionList">
                                    <div class="new-update clearfix">
                                        <i class="icon-move"></i>
                                        <a style="cursor: pointer;"  onclick="goG('${versionList.url}',${versionList.id},'${versionList.versionName}');">
                                                ${versionList.versionName}
                                        </a>
                                        <input type="hidden" id="data${versionList.id}" value="${versionList.groupData}">
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <c:if test="${(gameList.index-1)%2!=0}">
                        </div>
                    </c:if>
                </c:forEach>
                <c:if test="${user.gameLength%2!=0}">
                    </div>
                </c:if>
            </c:if>
        </div>
        <div id="page"></div>
    </div>
</div>
<form method="post" target="_blank" id="goGm">
    <input type="hidden" id="data" name="menuData">
    <input type="hidden" id="vsn" name="versionName">
    <input type="hidden"  name="name" value="${user.name}">
    <input type="hidden"  name="root" value="${user.root}">
    <input type="hidden"  name="userKey" value="${user.userKey}">
</form>
<div id="resA" style="display: none;">
    <div class="alert alert-success alert-block">
        <a class="close" data-dismiss="alert" href="#">×</a>
        <h4 class="alert-heading">设置成功</h4>
        <span id="resOK"></span>
    </div>
</div>
<div id="resB" style="display: none;">
    <div class="alert alert-error alert-block">
        <a class="close" data-dismiss="alert" href="#">×</a>
        <h4 class="alert-heading">错误提示</h4>
        <span id="resFail"></span>
    </div>
</div>
<div class="row-fluid">
    <div id="footer" class="span12"> 2017 &copy; 赛尔互动-平台管理系统 </div>
</div>

<script src="../../../manage/matrix/js/excanvas.min.js"></script>
<script src="../../../manage/matrix/js/jquery.min.js"></script>
<script src="../../../manage/matrix/js/jquery-form.js"></script>
<script src="../../../manage/matrix/js/jquery.ui.custom.js"></script>
<script src="../../../manage/matrix/js/bootstrap.min.js"></script>
<script src="../../../manage/matrix/js/jquery.flot.min.js"></script>
<script src="../../../manage/matrix/js/jquery.flot.resize.min.js"></script>
<script src="../../../manage/matrix/js/jquery.peity.min.js"></script>
<script src="../../../manage/matrix/js/fullcalendar.min.js"></script>
<script src="../../../manage/matrix/js/matrix.js"></script>
<script src="../../../manage/matrix/js/jquery.validate.js"></script>
<script src="../../../manage/matrix/js/matrix.form_validation.js"></script>
<script src="../../../manage/matrix/js/jquery.uniform.js"></script>
<script src="../../../manage/matrix/js/select2.min.js"></script>
<script src="../../../manage/matrix/js/matrix.popover.js"></script>
<script src="../../../manage/matrix/js/jquery.dataTables.min.js"></script>
<script src="../../../manage/matrix/js/matrix.tables.js"></script>

<script type="text/javascript">
    function goG(url,mid,vn){
        var k=$("#data"+mid).val();
        $("#data").val(k);
        $("#vsn").val(vn);
        $("#goGm").attr("action",url).submit();
    }
    function forwardPage(url,menuId){
        $("#entrance").hide();
        $.post(url,{menuId:menuId},function(data){
            $('#page').html(data);
        });
    }
    function moveMain(){
        $("#entrance").show();
        $("#page").html("");
    }
</script>
</body>
</html>