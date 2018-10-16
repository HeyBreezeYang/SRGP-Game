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
					<h3 class="panel-title">邮件编辑</h3>
				</div>
				<div class="panel-body">
					<form action="mail/sendMail" role="form" class="form-horizontal" method="post" id="mailform">
						<div class="form-group" id="make">
							<label class="col-sm-1 control-label" for="mailServer">服务器</label>
							<div class="col-sm-6">
								<select class="form-control" name="server" id="mailServer">
									<option value="all">所有服务器</option>
									<c:forEach items="${serverConfig}" var="config">
										<option value="${config.sid}">${config.dsp}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="title">主题</label>
							<div class="col-sm-6">
								<input type="text" id="title" name="title" class="form-control">
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="mailText">内容</label>
							<div class="col-sm-6">
								<textarea class="form-control" rows="8" name="context" id="mailText" style="resize: none;" ></textarea>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="playerPamras">收件玩家</label>
							<div class="col-sm-6 " >
								<input type="text" name="player" class="form-control myParmas" id="playerPamras"/>
							</div>
							<div class="col-sm-3">
								<div class="form-block">
									<label>
										<input type="checkbox" class="cbr cbr-info" onchange="changeAllPlayer(this)">
										<input type="hidden" id="apn" name="allPlayer" value="0">
										所有玩家
									</label>
								</div>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="mailFunc">附件</label>
							<div class="col-sm-6">
								<textarea class="form-control" rows="8" name="func" id="mailFunc" style="resize: none;" ></textarea>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group ">
							<label class="col-sm-1 control-label">定时</label>
							<div class="col-sm-1 " >
								<input type="checkbox" class="iswitch-lg iswitch-turquoise" onchange="isTimer(this)">
								<input type="hidden" value="0" name="timer" id="timer">
							</div>
						</div>
						<div  id="timeParams" style="display: none" >
							<div class="form-group ">
								<label class="col-sm-1 control-label" for="star">开始</label>
								<div class="col-sm-2 " >
									<input type="text" name="startTime" class="form-control" id="star"  data-mask="datetime" />
								</div>
								<label class="col-sm-1 control-label" for="end">结束</label>
								<div class="col-sm-2 " >
									<input type="text" name="endTime" class="form-control" id="end"  data-mask="datetime" />
								</div>
							</div>
							<div class="form-group ">
								<label class="col-sm-1 control-label">秒分时日月周年</label>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_1" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_2" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_3" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_4" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_5" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_6" value="*"/>
								</div>
								<div class="col-sm-1 " >
									<input type="text" name="config" class="form-control cfgm" id="c_7" value="*"/>
								</div>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label"></label>	
							<div class="col-sm-6">
								<button class="btn btn-purple btn-block" onclick="upMail();" type="button">确定编辑</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>

<script src="${GmBase}/assets/js/tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="${GmBase}/assets/js/inputmask/jquery.inputmask.bundle.js"></script>
<script src="${GmBase}/assets/js/xenon-custom.js"></script>
<script type="text/javascript">
	function isTimer(vr){
		if(vr.checked){
			$("#timeParams").show(170);
			$("#timer").val(1);
		}else{
			$("#timer").val(0);
			$("#end").val("");
			$("#star").val("");
			var c =$(".cfgm");
			for(var i=0;i< c.length;i++){
				c[i].value="*";
			}
			$("#timeParams").hide(170);
		}
	}
	function changeAllPlayer(vr){
		var fl=vr.checked;

		if(fl){
			$("#apn").val(1);
			$("#playerPamras").val("");
			$("#playerPamras").attr("disabled",true);
			$("#playerPamras").tagsinput('removeAll');
		}else{
			$("#apn").val(0);
			$("#playerPamras").attr("disabled",false);
		}
	}

	function upMail(){
		$("#setMarkDiv").html("确定编辑这个邮件吗？");
		$("#reSubBtn").attr("onclick","subMail();");
		jQuery('#reMark').modal('show',{backdrop: 'fade'});
	}
	function subMail(){
		$("#mailform").ajaxSubmit({
			dataType:'json'
		});
	}
	
	// Just for demo purpose
	function shuffleArray(array){
		for (var i = array.length - 1; i > 0; i--) 
		{
			var j = Math.floor(Math.random() * (i + 1));
			var temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
		return array;
	}

	jQuery(document).ready(function($)
			{
				var i = -1,
				colors = ['primary', 'secondary', 'red', 'blue', 'warning', 'success', 'purple'];
				colors = shuffleArray(colors);
				$("#playerPamras").tagsinput({
					tagClass: function()
					{
						i++;
						return "label label-" + colors[i%colors.length];
					}
				});
		});
</script>		
		