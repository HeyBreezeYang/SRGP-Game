<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<script type="text/javascript">
		function queryData(fid){
			$("#query-"+fid).ajaxSubmit({
				dataType:'json',
				type:'post',
				url:'query/queryData',
				success:function(data){
					$("#tb-"+fid).html("");
					var tk=$(".table-"+fid);
					var htm="";
					$.each(data,function(i,dcem){
						htm+="<tr>";
						for(var i=0;i<tk.length;i++){
                            if(dcem!=null){
                                htm+="<td>"+dcem[$(tk[i]).attr("abbr")]+"</td>";
                            }
						}
						htm+="</tr>";
					});
					$("#tb-"+fid).html(htm);
				}
			});
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
		<c:forEach items="${formList}" var="fList">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">${fList.queryName}</h3>
					<div class="panel-options">
						<a href="#" data-toggle="panel">
							<span class="collapse-icon">&ndash;</span>
							<span class="expand-icon">+</span>
						</a>
					</div>
				</div>
				<div class="panel-body">
					<div class="form-group">
						<form role="form" class="form-horizontal" id="query-${fList.id}">
							<input type="hidden"  name="other" value="${fList.queryType}"/>
							<c:if test="${fList.parameter['server']==true}">
								<label >服务器ID：</label>
								<select name="serverId">
									<c:if test="${fList.parameter['serverAll']==true}"><option value="all">全服</option></c:if>
									<c:forEach items="${serverConfig}" var="config">
										<option value="${config.serverID}">${config.serverName}</option>
									</c:forEach>
								</select>
							</c:if>
							<c:if test="${fList.parameter['channel']==true}">
								<label >渠道：</label>
								<select name="other">
									<option value="all">全部</option>
									<c:forEach items="${channl}" var="config">
										<option value="${config.name}">${config.dsp}</option>
									</c:forEach>
								</select>
							</c:if>
							<c:if test="${fList.parameter['start']==true}">
								<label >开始时间：</label>
								<input type="text"  name="start" readonly="readonly" class="datepicker" data-format="yyyy-mm-dd"/>
							</c:if>
							<c:if test="${fList.parameter['end']==true}">
								<label >结束时间：</label>
								<input type="text"  name="end" readonly="readonly" class="datepicker" data-format="yyyy-mm-dd"/>
							</c:if>
							<c:if test="${fList.parameter['pName']==true}">
								<label >名字：</label>
								<input type="text"  name="other" />
							</c:if>
							<c:if test="${fList.parameter['oName']==true}">
								<label >查询参数：</label>
								<input type="text"  name="other" />
							</c:if>
							<c:if test="${fList.parameter['player']==true}">
								<label >玩家：</label>
								<input type="text"  name="other" readonly="readonly" class="datepicker" data-format="yyyy-mm-dd"/>
								<label >--</label>
								<input type="text"  name="other" readonly="readonly" class="datepicker" data-format="yyyy-mm-dd"/>
							</c:if>
							<c:if test="${fList.otherParameter!=null}">
								<label >类型参数：</label>
								<select name="other">
									<c:forEach items="${fList.otherParameter}" var="queryParam">
										<option value="${queryParam.key}">${queryParam.value}</option>
									</c:forEach>
								</select>
							</c:if>
							<button type="button" onclick="queryData(${fList.id});">查询</button>
						</form>
					</div>
					<table class="table table-striped table-bordered" id="example-${fList.id}">
						<thead>
							<tr class="replace-inputs">
								<c:forEach	begin="0" end="${fList.threadSize}" var="k">
									<th abbr="${fList.key[k]}" class="table-${fList.id}">${fList.thread[k]}</th>
								</c:forEach>
							</tr>
						</thead>
						<tbody id="tb-${fList.id}">
							<tr>
								
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</c:forEach>
	</div>
	<link rel="stylesheet" href="${GmBase}/assets/js/daterangepicker/daterangepicker-bs3.css">
	<script src="${GmBase}/assets/js/daterangepicker/daterangepicker.js"></script>
	<script src="${GmBase}/assets/js/datepicker/bootstrap-datepicker.js"></script>
	<script src="${GmBase}/assets/js/timepicker/bootstrap-timepicker.min.js"></script>
	<script src="${GmBase}/assets/js/colorpicker/bootstrap-colorpicker.min.js"></script>
	<script src="${GmBase}/assets/js/jquery-ui/jquery-ui.min.js"></script>
	<script src="${GmBase}/assets/js/xenon-custom.js"></script>