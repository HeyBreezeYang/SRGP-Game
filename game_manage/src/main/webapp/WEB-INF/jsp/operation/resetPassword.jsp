<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>修改信息</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title"> <span class="icon"> <i class="icon-info-sign"></i> </span>
                <h5>重置密码</h5>
            </div>
            <div class="widget-content nopadding">
                <form class="form-horizontal" method="post" action="user/subNewPwd"  id="pwdForm" novalidate="novalidate">
                    <div class="control-group">
                        <label class="control-label">旧密码</label>
                        <div class="controls">
                            <input type="text" name="password" id="oldPwd"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">新密码</label>
                        <div class="controls">
                            <input type="text" id="newPwd" name="pwdN" />
                        </div>
                    </div>
                    <input type="hidden" name="id" value="${user.id}">
                    <div class="form-actions">
                        <input type="button" onclick="subAddPWD();" value="提交" class="btn btn-success">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="resMsg">

</div>

<script type="text/javascript">
    function subAddPWD() {
        $("#pwdForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
               var res =JSON.parse(data);
               if(res['res']===1){
                   $("#resOK").html("成功修改密码!");
                   $("#resMsg").html($("#resA").html())
               }else{
                   $("#resFail").html("ERROR:"+res['msg']);
                   $("#resMsg").html($("#resB").html())
               }
            }
        });
    }

</script>