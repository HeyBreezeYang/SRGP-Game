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
				<h3 class="panel-title">任务浏览</h3>
			</div>
			<div class="panel-body">
				<table id="Tasktable" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th>描述</th>
							<th>名称</th>
							<th>分组</th>
							<th>结束时间</th>
							<th width="10%">配置</th>
							<th>任务class</th>
							<th width="7%">状态</th>
							<th width="13%">操作</th>
						</tr>
					</thead>
					<tbody id="TaskMsg">
						<c:if test="${! empty TaskList}">
							<c:forEach items="${TaskList}" var="TaskData">
								<tr>
									<td id="TaskMsgForDsp-${TaskData.id}">${TaskData.dsp}</td>
									<td id="TaskMsgForName-${TaskData.id}">${TaskData.timeName}</td>
									<td id="TaskMsgForGroup-${TaskData.id}">${TaskData.groupName}</td>
									<td>${TaskData.endTime}</td>
									<td>${TaskData.config}</td>
									<td>${TaskData.clazz}</td>
									<td id="TaskMsgForStatues-${TaskData.id}">${TaskData.statues}</td>
									<td>
										<a href="#" id="taskCotrBtn-${TaskData.id}" class="btn
											<c:if test="${TaskData.statues==1}">btn-primary </c:if>
											<c:if test="${TaskData.statues==0}">btn-warning </c:if>
										btn-sm btn-icon icon-left" onclick="controlTask(${TaskData.id});">
											<c:if test="${TaskData.statues==1}">暂停</c:if>
											<c:if test="${TaskData.statues==0}">恢复</c:if>
										</a>
										<a href="#" class="btn btn-info btn-sm btn-icon icon-left" onclick="reTask(${TaskData.id});">修改</a>
										<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delTask(${TaskData.id});">删除</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</div>
<script type="text/javascript">
	jQuery(document).ready(function($)
		{
			$("#Tasktable").dataTable({
				aLengthMenu: [
					[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
				]
			});
	});
	function reTask(tbId){
		$("#reId_reDiv").val(tbId);
		$("#reSubBtn").attr("onclick","subReTask();");
		jQuery('#reMark').modal('show', {backdrop: 'static'});
		jQuery.ajax({
			url: "timer/gotoRetaskDiv",
			success: function(response)
			{
				jQuery('#setMarkDiv').html(response);
			}
		});
	}
	function subReTask(){
		var tbId=$("#reId_reDiv").val();
		var dsp=$("#TaskMsgForDsp-"+tbId).html();
		var name=$("#TaskMsgForName-"+tbId).html();
		var group=$("#TaskMsgForGroup-"+tbId).html();
		var star=$("#startTime_taskDiv").val();
		var end=$("#endTime_taskDiv").val();
		var cfg=$(".cfg");
		if(cfg[0].value==""){
			cfg[0].value="*";
		}
		var config=cfg[0].value;
		for(var i=1;i<cfg.length;i++){
			if(cfg[i].value==""){
				cfg[i].value="*";
			}
			config+=","+cfg[i].value;
		}
		$.ajax({
			url:'timer/reTimer',
			data:{"id":tbId,"name":name,"group":group,"start":star,"end":end,"dsp":dsp,"config":config},
			dataType:'text',
			type: 'post',
			success:function(data){
				forwardPage('timer/come',${thisMenuID});
			}
		});
	}
	function subDelTask(){
		var tbId=$("#delId_delDiv").val();
		var name=$("#TaskMsgForName-"+tbId).html();
		var group=$("#TaskMsgForGroup-"+tbId).html();
		$.ajax({
			url:'timer/delTimer',
			data:{"id":tbId,"name":name,"group":group},
			dataType:'text',
			type: 'post',
			success:function(data){
				forwardPage('timer/come',${thisMenuID});
			}
		});
	}
	function delTask(deltbId){
		$("#delId_delDiv").val(deltbId);
		
		$("#delText").html("确定删除这个定时任务？");
		$("#detSubBtn").attr("onclick","subDelTask();");
		jQuery('#delMake').modal('show',{backdrop: 'fade'});
	}
	function controlTask(tbId){
		var name=$("#TaskMsgForName-"+tbId).html();
		var group=$("#TaskMsgForGroup-"+tbId).html();
		var j=$("#TaskMsgForStatues-"+tbId).html();
		if(j==0){
			j=1;
			$("#taskCotrBtn-"+tbId).html("暂停");
			$("#taskCotrBtn-"+tbId).attr("class","btn btn-primary btn-sm btn-icon icon-left");
		}else if(j==1){
			j=0;
			$("#taskCotrBtn-"+tbId).html("恢复");
			$("#taskCotrBtn-"+tbId).attr("class","btn btn-warning btn-sm btn-icon icon-left");
		}
		$.ajax({
			url:'timer/controlTimer',
			data:{"id":tbId,"state":j,"name":name,"group":group},
			dataType:'text',
			type: 'post',
			success:function(data){
				$("#TaskMsgForStatues-"+tbId).html(j);	
				$("#taskCotrBtn-"+tbId).blur();
			}
		});
		
	}
</script>