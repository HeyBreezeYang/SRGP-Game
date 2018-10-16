<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<script type="text/javascript">
		function getBanMsg() {
		    $("#playerName").val($("#user").val());
            $("#serverId").val($("#server").val());
            $("#userSub").ajaxSubmit({
                dataType:'json',
                type:'post',
                success:function(data){
                    if(data['error']!=null){
                        console.log(data['error']);
					}else{
                        showOperateButton(data);
					}
                }
            });
        }
        function banOperate(cid,tid) {
            $("#operateId").val(cid);
            $("#doTime").val($("#"+tid).val());
            $("#ban").ajaxSubmit({
                dataType:'json',
                type:'post',
                success:function(data){
                    if(data['error']!=null){
                        alert("失败");
                        console.log(data['error']);
                    }else{
                        alert("成功");
                        showOperateButton(data);
                    }
                }
            });
        }
        function getSubOrderMsg(){
            $("#payment").ajaxSubmit({
                dataType:'json',
                type:'post',
                success:function(data){
                    $("#msgText").html("返回结果:"+JSON.stringify(data));
                    jQuery('#msgMaket').modal('show',{backdrop: 'fade'});
                }
            });
		}
		function showOperateButton(data){
            if(data['ban1']==0){
                $("#dj").show();
                $("#jd").hide();
            }else{
                $("#jd").show();
                $("#dj").hide();
            }
            if(data['ban2']==0){
                $("#jy").show();
                $("#jj").hide();
            }else{
                $("#jj").show();
                $("#jy").hide();
            }
		}
	</script>
	<div class="breadcrumb-env">
		<ol class="breadcrumb bc-1">
			<li>
				<a href="#" onclick="moveMain();">
					<i class="fa-home"></i>回到主页
				</a>
			</li>
		</ol>
	</div>
	<div style="width: 100%;">
		<%--<div class="col-md-12">
			<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">封禁操作</h3>
						<div class="panel-options">
							<a href="#" data-toggle="panel">
								<span class="collapse-icon">&ndash;</span>
								<span class="expand-icon">+</span>
							</a>
						</div>
					</div>
					<div class="panel-body">
						<form action="player/queryPlayerState" role="form" class="form-horizontal" method="post" id="userSub" >
							<div class="form-group" id="sid">
								<label class="col-sm-1 control-label" for="server">服务器</label>
								<div class="col-sm-3">
									<select class="form-control" id="server" name="server">
										<c:forEach items="${allServer}" var="serverObj">
											<option value="${serverObj.serverid}">${serverObj.servername}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label" for="user">角色名</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="user" name="user"/>
								</div>
								<span class="input-group-btn">
										<button class="btn btn-blue" type="button" id="queryButton" onclick="getBanMsg();">获取</button>
								</span>
							</div>
						</form>
					</div>
					<div class="panel-body">
						<form action="player/banOperate" role="form" class="form-horizontal" method="post" id="ban">
							<input type="hidden" id="playerName" name="pName">
							<input type="hidden" id="operateId" name="cid">
							<input type="hidden" id="serverId" name="serverId">
							<input type="hidden" id="doTime" name="time">
							<div class="form-group">
								<div style="display: none;" id="dj">
									<div class="col-xs-1">
										<input type="text" class="form-control" placeholder="分钟" id="dTime"/>
									</div>
									<button class="btn btn-red" type="button" onclick="banOperate(101,'dTime');" >冻结</button>
								</div>
								<button class="btn btn-info" type="button" onclick="banOperate(103,'d');" style="display: none;" id="jd">解冻</button>
							</div>
							<div class="form-group-separator"></div>
							<div class="form-group">
								<div style="display: none;" id="jy">
									<div class="col-xs-1">
										<input type="text" class="form-control" placeholder="分钟"  id="jTime"/>
									</div>
									<button class="btn btn-red" type="button" onclick="banOperate(102,'jTime');" >禁言</button>
								</div>
								<button class="btn btn-info" type="button" onclick="banOperate(104,'j');" style="display: none;" id="jj">解禁</button>
							</div>
						</form>
					</div>
				</div>
			</div>--%>
		<div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">补单操作</h3>
                </div>
                <div class="panel-body">
                    <form action="player/orderOperate" role="form" class="form-horizontal" method="post" id="payment">
						<%--<input type="hidden" id="playerName" name="pName">
						<input type="hidden" id="operateId" name="cid">
						<input type="hidden" id="serverId" name="serverId">
						<input type="hidden" id="doTime" name="time">--%>
						<div class="form-group">
                            <%--@declare id="server"--%><label class="col-sm-1 control-label" for="server">服务器</label>
                            <div class="col-sm-3">
                                <select class="form-control"  name="server">
                                    <c:forEach items="${allServer}" var="serverObj">
                                        <option value="${serverObj.serverid}">${serverObj.servername}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <%--<div class="form-group">
                            <label class="col-sm-1 control-label">角色ID</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="userId"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">物品ID</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="goodsId"/>
                            </div>
                        </div>--%>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">订单号</label>
                            <div class="col-sm-3">
                                <input type="text" class="form-control" name="orderId"/>
                            </div>
                            <span class="input-group-btn">
                                    <button class="btn btn-blue" type="button" onclick="getSubOrderMsg();">补单</button>
                            </span>
                        </div>
                    </form>
                </div>
            </div>
        </div>
	</div>