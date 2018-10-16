<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>添加管理员</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title"> <span class="icon"> <i class="icon-info-sign"></i> </span>
                <h5>账号信息</h5>
            </div>
            <div class="widget-content nopadding">
                <form class="form-horizontal" method="post" action="user/sub" name="password_validate" id="userForm" novalidate="novalidate">
                    <div class="control-group">
                        <label class="control-label">账号</label>
                        <div class="controls">
                            <input type="text" name="name" id="user"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">密码</label>
                        <div class="controls">
                            <input type="password" name="password" id="pwd" />
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">ROOT</label>
                        <div class="controls">
                            <label>
                                <input type="radio" name="root" value="1"/>YES</label>
                            <label>
                                <input type="radio" name="root" checked="checked"  value="0"/>NO</label>
                        </div>
                    </div>
                    <input type="hidden" name="uid" value="${user.id}">
                    <div class="form-actions">
                        <input type="button" onclick="subAddUser();" value="提交" class="btn btn-success">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="resMsg">

</div>

<script type="text/javascript">
    function subAddUser() {
        $("#userForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
               var res =JSON.parse(data);
               if(res['res']===1){
                   $("#resOK").html("成功添加账号:"+$("#user").val());
                   $("#resMsg").html($("#resA").html())
               }else{
                   $("#resFail").html("ERROR:"+res['msg']);
                   $("#resMsg").html($("#resB").html())
               }
            }
        });
    }

</script>