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
			<h3 class="panel-title">服务器列表</h3>
		</div>
		<div class="panel-body">
			<table id="serverTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th>ID</th>
						<th>服务器</th>
						<th>名称</th>
						<th>状态</th>
						<th>是否推荐</th>
						<th>serverIP</th>
						<th>开服时间</th>
					</tr>
				</thead>
				<tbody id="serverMsg">
					<c:if test="${! empty serverList}">
						<c:forEach items="${serverList}" var="serverData">
							<tr>
								<td>${serverData.logicID}</td>
								<td>${serverData.serverID}</td>
								<td>${serverData.serverName}</td>
								<td>${serverData.stateName}</td>
								<td>${serverData.recommendName}</td>
								<td>${serverData.serverIP}</td>
								<td>${serverData.openTime}</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">设置推荐</h3>
			<div class="panel-options">
				<a href="#" data-toggle="panel">
					<span class="collapse-icon">&ndash;</span>
					<span class="expand-icon">+</span>
				</a>
			</div>
		</div>
		<div class="panel-body">
			<form action="server/changeServerRecommend" role="form" class="form-horizontal" method="post" id="serverRec">
				<div class="form-group">
					<label class="col-sm-1 control-label">推荐服务器</label>
					<div class="col-sm-3">
						<select class="form-control"  name="sid">
							<c:forEach items="${serverConfig}" var="config">
								<option value="${config.serverID}">${config.serverName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-1">
						<button class="btn btn-purple btn-block" onclick="changeMark('serverRec','推荐')" type="button">设置</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">修改状态</h3>
			<div class="panel-options">
				<a href="#" data-toggle="panel">
					<span class="collapse-icon">&ndash;</span>
					<span class="expand-icon">+</span>
				</a>
			</div>
		</div>
		<div class="panel-body">
			<form action="server/changeServerState" role="form" class="form-horizontal" method="post" id="serverState">
				<div class="form-group">
					<label class="col-sm-1 control-label" for="select_sId">服务器</label>
					<div class="col-sm-3">
						<select class="form-control" multiple="multiple" id="select_sId" name="serverArray">
							<c:forEach items="${serverConfig}" var="config">
								<option value="${config.serverID}">${config.serverName}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group-separator"></div>
				<div class="form-group">
					<label class="col-sm-1 control-label">状态</label>
					<div class="col-sm-3">
						<select class="form-control" name="state">
							<option value="1">流畅</option>
							<option value="2">火爆</option>
							<option value="3">拥挤</option>
							<option value="4">维护</option>
						</select>
					</div>
					<div class="col-sm-1">
						<button class="btn btn-purple btn-block" onclick="changeMark('serverState','状态')" type="button">设置</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
    function subChange(fid){
        $("#"+fid).ajaxSubmit({
            dataType:'json',
			success : function (data) {
                $("#msgText").html("返回结果:"+data);
                jQuery('#msgMaket').modal('show',{backdrop: 'fade'});
                forwardPage('server/oneTimeModify',${thisMenuID});
            }
        });
	}
	function changeMark(fid,content) {
        $("#setMarkDiv").html("确定服务器修改"+content);
        $("#reSubBtn").attr("onclick","subChange('"+fid+"');");
        jQuery('#reMark').modal('show',{backdrop: 'fade'});
    }


	jQuery(document).ready(function($)
	{
		$("#serverTable").dataTable({
			aLengthMenu: [
				[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
			]
		});
        $("#select_sId").multiSelect({
            afterInit: function()
            {
                // Add alternative scrollbar to list
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar();
            },
            afterSelect: function()
            {
                // Update scrollbar size
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar('update');
            }
        });
	});

</script>
