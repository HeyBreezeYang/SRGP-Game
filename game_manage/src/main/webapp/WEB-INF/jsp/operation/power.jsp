<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>设置用户权限</h1>
<hr>
<div class="row-fluid">
    <form action="user/queryPower" method="post" id="queryPowerForm">
        <div class="control-group">
            <br>
            <div class="controls">
                <input type="text" class="span2"  name="name" id="userName">
                <button type="button" onclick="queryUserPower();" class="btn btn-success" style="margin-top: -10px;" >查询</button>
                <c:if test="${user.root==1}">
                    <button type="button" class="btn btn-info" id="setPowerButton" style="margin-top: -10px;display: none;" onclick="subMenus();">设置</button>
                </c:if>
            </div>
        </div>
    </form>
</div>
<div id="resMsg"></div>

<div class="row-fluid">
    <div class="span12">
        <form action="user/subPower" id="subPowerForm" method="post">
            <input type="hidden" name="name" id="thisUser">
            <c:if test="${! empty allMenu}">
                <c:forEach items="${allMenu}" var="menu">
                    <div class="widget-box collapsible">
                        <div class="widget-title">
                            <a href="#collapse${menu.key}" data-toggle="collapse">
								<span class="icon">
									<i class="icon-arrow-right"></i>
								</span>
                                <h5>${menu.key}</h5>
                            </a>
                        </div>
                        <div class="collapse in" id="collapse${menu.key}">
                            <div class="widget-content">
                                <c:forEach items="${menu.value}"  var="msg">
                                    <div class="control-group">
                                        <h5>${msg.key}</h5>
                                        <div class="controls">
                                            <c:forEach items="${msg.value}"  var="msgM">
												<span style="white-space:nowrap;">
													<input class="myMenu" type="checkbox" value="${msgM.id}" name="menus" >${msgM.title}
												</span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </form>
    </div>
</div>
<script type="text/javascript">
    function subMenus() {
        $("#subPowerForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
                var addRes=JSON.parse(data);
                if(addRes["res"]===1){
                    $("#resOK").html("设置权限成功!~~");
                    $("#resMsg").html($("#resA").html())
                }else{
                    $("#resFail").html("ERROR:"+addRes['error']);
                    $("#resMsg").html($("#resB").html())
                }
            }
        });
    }

    function queryUserPower(){
        var mc=$(".myMenu");
        for(var i=0;i<mc.length;i++){
            mc[i].checked=false;
        }
        $("#queryPowerForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
                var power=JSON.parse(data);
                if(power["res"]===1){
                    $("#thisUser").val($("#userName").val());
                    var mc=$(".myMenu");
                    if($("#userName").val()!=='root'){
                        $("#setPowerButton").show();
                    }else{
                        $("#setPowerButton").hide();
                    }
                    for(var i=0;i<mc.length;i++){
                        for(var j=0;j<power["msg"].length;j++){
                            if($(mc[i]).val()==power["msg"][j]){
                                mc[i].checked=true;
                                break;
                            }
                        }
                    }
                }else{
                    $("#resFail").html("ERROR:"+power['error']);
                    $("#resMsg").html($("#resB").html())
                }
            }
        });
    }

</script>