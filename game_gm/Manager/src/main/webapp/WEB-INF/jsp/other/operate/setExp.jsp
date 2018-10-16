<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">探险配置</h3>
				<div class="panel-options">
					<a href="#" data-toggle="panel">
						<span class="collapse-icon">&ndash;</span>
						<span class="expand-icon">+</span>
					</a>
				</div>
			</div>
			<div class="panel-body">
				<a href="#make" style="display: none;" id="scp"></a>
				<table id="Exctable" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th >服务器</th>
							<th>开始</th>
							<th>结束</th>
							<th>隐藏</th>
						</tr>
					</thead>
					<tbody id="ExcMsg">
						<c:if test="${! empty ExpList}">
							<c:forEach items="${ExpList}" var="ExcData">
								<tr>
									<td>${ExcData['sid']}</td>
									<td>${ExcData['sTime']}</td>
									<td>${ExcData['eTime']}</td>
									<td>${ExcData['hTime']}</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	
		<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">更新设置</h3>
				</div>
				<div class="panel-body">
					<form action="exchange/subExp" role="form" class="form-horizontal" method="post" id="Expform">
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ExcServer">服务器</label>
							<div class="col-sm-5">
								<select class="form-control" id="ExcServer" name="server">
									<c:forEach items="${serverConfig}" var="config">
										<option value="${config.serverID}">${config.serverName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ExpItemIds">开始时间</label>
							<div class="col-sm-5">
								<input type="text" name="sTime" class="form-control" id="ExpItemIds"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ExpItemNum">结束时间</label>
							<div class="col-sm-5">
								<input type="text" name="eTime" class="form-control" id="ExpItemNum"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ExpRefHours">隐藏时间</label>
							<div class="col-sm-5">
								<input type="text" name="hTime" class="form-control" id="ExpRefHours"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label"></label>	
							<div class="col-sm-5">
								<button class="btn btn-purple btn-block" onclick="subExpConfig();" type="button">确定编辑</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>	

<script type="text/javascript">


	function subExpConfig(){
		$("#Expform").ajaxSubmit({
			dataType:'json',
			success:function(){
				forwardPage('exchange/getAllExp',${thisMenu});
			}
		});
	}


</script>		
		