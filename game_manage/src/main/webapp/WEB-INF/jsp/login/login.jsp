<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <title>平台登录</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="stylesheet" href="../../../manage/matrix/css/bootstrap.min.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="../../../manage/matrix/css/matrix-login.css" />
    <link href="../../../manage/matrix/font-awesome/css/font-awesome.css" rel="stylesheet" />
    <script type="text/javascript">
        function subLogin() {
            $("#loginform").submit();
        }
    </script>
</head>
<body>
<div id="loginbox">
    <form id="loginform" class="form-vertical" action="http://106.75.142.189:8109/manage/system/main/comePlatform" method="post">
        <div class="control-group normal_text"> <h2 style="color: #B2DFEE">Cells 平台管理系统</h2></div>
        <div class="control-group">
            <div class="controls">
                <div class="main_input_box">
                    <span class="add-on bg_lg"><i class="icon-user"></i></span><input type="text" name="name" id="userName" placeholder="账号" />
                </div>
            </div>
        </div>
        <div class="control-group">
            <div class="controls">
                <div class="main_input_box">
                    <span class="add-on bg_ly"><i class="icon-lock"></i></span><input type="password" name="password" placeholder="密码" />
                </div>
            </div>
        </div>
        <div class="form-actions">
            <span class="pull-left"><a href="#" class="flip-link btn btn-info" id="to-recover">关于平台</a></span>
            <span class="pull-right"><a onclick="subLogin();" href="#" class="btn btn-success" >登录</a></span>
        </div>
    </form>
    <form id="recoverform" action="#" class="form-vertical">
        <p class="normal_text">
            暂时没有什么想要说的~~~~~反正管理内容包括:各个对应游戏GM工具的菜单管理,账户的权限与密码管理,登录平台和充值平台的控制,数据统计中心入口,各个GM管理后台入口~~~~~~
        </p>

        <div class="form-actions">
            <span class="pull-left"><a href="#" class="flip-link btn btn-success" id="to-login">&laquo; 返回</a></span>
        </div>
    </form>
    <c:if test="${userError!=null}">
        <div class="alert alert-error alert-block">
            <a class="close" data-dismiss="alert" href="#">×</a>
            <h4 class="alert-heading">登录失败</h4>
            ${userError}
        </div>
    </c:if>
</div>

<script src="../../../manage/matrix/js/jquery.min.js"></script>
<script src="../../../manage/matrix/js/bootstrap.min.js"></script>
<script src="../../../manage/matrix/js/matrix.login.js"></script>
</body>

</html>