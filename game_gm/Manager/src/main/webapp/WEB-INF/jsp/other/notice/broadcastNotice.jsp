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
					<h3 class="panel-title">跑马灯公告</h3>
				</div>
				<div class="panel-body">
					<form action="" role="form" class="form-horizontal" method="post" id="noticeLoginForm">
						<input type="hidden" id="noticeId" name="noticeId" value="${notice["id"]}">
						<input type="hidden" id="noticeType" name="noticeType" value="跑马灯公告">
						<div class="form-group">
							<label class="col-sm-1 control-label" for="noticeTitle">标题</label>
							<div class="col-sm-11" id="testPamras">
								<input type="text" name="noticeTitle" class="form-control" id="noticeTitle" value="${notice['title']}" />
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label" for="noticeContext">公告内容</label>
							<div class="col-sm-11">
								<textarea class="form-control" rows="13" name="noticeContext" id="noticeContext" style="resize: none;" >${notice['context']}</textarea>
							</div>
							优先级：<input type="text" name="level" value="${notice['level']}">
						</div>
						<label>
							<input type="checkbox" class="cbr cbr-info" name="isAllServer" value="all">
							是否全服

							<select class="form-control" name="sid" id="sid" multiple>
								<option></option>
								<optgroup label="游戏服务器">
									<c:forEach items="${allServer}" var="serverObj">
										<option value="${serverObj.serverid}">${serverObj.servername}</option>
									</c:forEach>
								</optgroup>
							</select>
						</label>
						<label>
							<input type="checkbox" class="cbr cbr-info" name="isAllChannel" value="all">
							是否全渠道

							<select class="form-control" name="channel" id="channel" multiple>
								<option></option>
								<optgroup label="游戏渠道">
									<c:forEach items="${allChannel}" var="channelObj">
										<option value="${channelObj.id}">${channelObj.dsp}</option>
									</c:forEach>
								</optgroup>
							</select>
						</label>
						<div>
							<label>开始日期：</label>
							<input type="text" id="startTime" name="startTime">
						</div>
						<div>
							<label>结束日期：</label>
							<input type="text" id="endTime" name="endTime">

						</div>
						<div>
							<label>间隔时间：</label>
							<input type="text" id="intervalTime" name="intervalTime">
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label"></label>	
							<div class="col-sm-6">
								<button class="btn btn-purple btn-block" onclick="upNoticeLogin();" type="button">确定</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
<script type="text/javascript">

    laydate.render({
        elem: '#startTime', //指定元素
        showBottom: false,
        format:'yyyy-MM-dd HH:mm:ss'
    });
    laydate.render({
        elem: '#endTime',
        showBottom: false,
        format:'yyyy-MM-dd HH:mm:ss'

    });

    jQuery(document).ready(function($)
    {
        $("#sid").select2({
            placeholder: '选择需要发送的服务器',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
        $("#channel").select2({
            placeholder: '选择需要发送的渠道',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });

    });

    function upNoticeLogin(){
        var intervalTime = $("#intervalTime").val()
		if(intervalTime<=30){
			alert("间隔时间低于最少时间，请重新设置");
		}else {
            $("#setMarkDiv").html("确定修改为当前游戏公告吗？");
            $("#reSubBtn").attr("onclick","subNoticeConfig();");
            jQuery('#reMark').modal('show',{backdrop: 'fade'});
		}

    }

	function subNoticeConfig(){
		$("#noticeLoginForm").ajaxSubmit({
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
				$("#noticePhoto").tagsinput({
					tagClass: function()
					{
						i++;
						return "label label-" + colors[i%colors.length];
					}
				});
		});
</script>		
		