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
			<h3 class="panel-title">重置备份</h3>
			<div class="panel-options">
				<a href="#" data-toggle="panel">
					<span class="collapse-icon">&ndash;</span>
					<span class="expand-icon">+</span>
				</a>
			</div>
		</div>
		<div class="panel-body">
			<form action="gm/subBackUp" role="form" class="form-horizontal" method="post" id="backUp">
				<input type="hidden" id="mouth_back" name="type">
				<div class="form-group">
					<label class="col-sm-1 control-label">备份日期</label>
					<div class="col-sm-2">
						<input type="text" class="form-control datepicker" name="startTime" data-format="yyyy-mm-dd">
					</div>
					<div class="col-sm-2">
						<input type="text" class="form-control datepicker" name="endTime" data-format="yyyy-mm-dd">
					</div>
				</div>
				<div class="form-group-separator"></div>
				<div class="form-group">
					<button class="btn btn-pink" type="button" onclick="backupData(10001)">综合数据</button>
					<button class="btn btn-success"  type="button" onclick="backupData(10002)">留存率</button>
					<button class="btn btn-orange"  type="button" onclick="backupData(10005)">LTV</button>
					<button class="btn btn-info" type="button" onclick="backupData(10003)">登录数据</button>
					<button class="btn btn-danger"  type="button" onclick="backupData(10004)">新增角色</button>
					<button class="btn btn-pink"  type="button" onclick="backupData(10006)">充值数据</button>
					<button class="btn btn-black"  type="button" onclick="backupData(20004)">副本统计</button>
					<button class="btn btn-danger"  type="button" onclick="backupData(20005)">玩家数据</button>
				</div>
			</form>
		</div>				
	</div>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title">重置缓存</h3>
			<div class="panel-options">
				<a href="#" data-toggle="panel">
					<span class="collapse-icon">&ndash;</span>
					<span class="expand-icon">+</span>
				</a>
			</div>				
		</div>
		<div class="panel-body">
			<form action="gm/subCache" role="form" class="form-horizontal" method="post" id="cache">
				<input type="hidden" id="mouth_cache" name="type">
				<div class="form-group">
					<button class="btn btn-info" type="button" onclick="cacheData(1001)">配置文件</button>
				</div>
			</form>
		</div>
	</div>
</div>

<link rel="stylesheet" href="${GmBase}/assets/js/daterangepicker/daterangepicker-bs3.css">
<script src="${GmBase}/assets/js/daterangepicker/daterangepicker.js"></script>
<script src="${GmBase}/assets/js/datepicker/bootstrap-datepicker.js"></script>
<script src="${GmBase}/assets/js/timepicker/bootstrap-timepicker.min.js"></script>
<script src="${GmBase}/assets/js/colorpicker/bootstrap-colorpicker.min.js"></script>
<script src="${GmBase}/assets/js/jquery-ui/jquery-ui.min.js"></script>
<script src="${GmBase}/assets/js/xenon-custom.js"></script>
<script type="text/javascript">
	function backupData(mid){
		$("#mouth_back").val(mid);
		$("#setMarkDiv").html("确定重置该项备份数据吗？");
		$("#reSubBtn").attr("onclick","subBack();");
		jQuery('#reMark').modal('show',{backdrop: 'fade'});
	}
	function subBack(){
		$("#backUp").ajaxSubmit({
			dataType:'json',
			success:function(data){

			}
		});
	}

	function cacheData(mid){
		$("#mouth_cache").val(mid);
		$("#setMarkDiv").html("确定更新GM该项缓存数据？");
		$("#reSubBtn").attr("onclick","subCache();");
		jQuery('#reMark').modal('show',{backdrop: 'fade'});
	}
	function subCache(){
		$("#cache").ajaxSubmit({
			dataType:'json',
			success:function(data){

			}
		});
	}


</script>
