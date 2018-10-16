<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div class="breadcrumb-env" id="mailDiv">
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
					<h3 class="panel-title">编辑邮件</h3>
				</div>
					<form action="mail/saveOrUpdateMail" role="form" class="form-horizontal" method="post" id="mailLoginForm">
						<input type="hidden" id="mailId" name="mailId" value="${mail["id"]}">
						<input type="hidden" id="createBy" name="createBy" value="${mail["createBy"]}">
						<input type="hidden" id="createTime" name="createTime" value="${mail["createTime"]}">
						<input type="hidden" id="mailStatus" name="mailStatus" value="${mail["status"]}">
						<input type="hidden" id="operation" name="operation" value="edit">
						<div>
							<label class="col-sm-1 control-label" for="sid">服务器</label>
								<%--<input type="checkbox" class="cbr cbr-info" name="isAllServer" value="all">
                                是否全服
    --%>						<div class="form-group" id="make">

									<div class="col-sm-6">
								<select class="form-control" name="sid" id="sid" multiple>
									<option value="all">全服</option>
									<c:forEach items="${allServer}" var="serverObj">
										<option value="${serverObj.serverid}">${serverObj.servername}</option>
									</c:forEach>
								</select>

									</div>
							</div>
						</div>
						<div>
							<label class="col-sm-1 control-label" for="channel">渠道</label>
								<%--<input type="checkbox" class="cbr cbr-info" name="isAllChannel" value="all">
                                是否全渠道--%>
							<div class="form-group" id="make">

								<div class="col-sm-6">
								<select class="form-control" name="channel" id="channel" multiple>
									<option value="all">全渠道</option>
									<c:forEach items="${allChannel}" var="channelObj">
										<option value="${channelObj.id}">${channelObj.dsp}</option>
									</c:forEach>
								</select>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="mailTitle">邮件主题</label>
							<div class="col-sm-11" id="testPamras">
								<input type="text" name="mailTitle" class="form-control" id="mailTitle" value="${mail['title']}" />
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label" for="mailContext">邮件内容</label>
							<div class="col-sm-11">
								<textarea class="form-control" rows="13" name="mailContext" id="mailContext" style="resize: none;" >${mail['context']}</textarea>
							</div>

						</div>
						<div>
							需要发送的玩家：<c:if test="${mail['username'] != 'all'}">
							<input type="text" name="username" value="${mail['username']}">
							<input type="checkbox" class="cbr cbr-info" name="isAllUsername" value="all">全部玩家
						</c:if>
							<c:if test="${mail['username'] == 'all'}">
								<input type="text" name="username" value="">
								<input type="checkbox" class="cbr cbr-info" name="isAllUsername" value="all" checked>全部玩家
							</c:if>

						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="enclosure">附件</label>
							<div class="col-sm-11">
								<textarea class="form-control" rows="13" name="enclosure" id="enclosure" style="resize: none;" >${mail['enclosure']}</textarea>
							</div>
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

    jQuery(function($) {
        var mail = <%= session.getAttribute("mail")%>
        if (mail != null || mail.size() > 0) {
            var serverId = mail['sid'].split(',');
            var channelId = mail['channel'].split(',');
            $("#sid").select2({
                placeholder: $("#sid").val(serverId).trigger("change"),
                allowClear: true
            }).on('select2-open', function()
            {
                // Adding Custom Scrollbar
                $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
            });
            $("#channel").select2({
                placeholder: $("#channel").val(channelId).trigger("change"),
                allowClear: true
            }).on('select2-open', function()
            {
                // Adding Custom Scrollbar
                $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
            });
        }
    });

    function upNoticeLogin(){
        $("#setMarkDiv").html("确定修改当前邮件吗？");
        $("#reSubBtn").attr("onclick","subNoticeConfig();");
        jQuery('#reMark').modal('show',{backdrop: 'fade'});
    }

	function subNoticeConfig(){
		$("#mailLoginForm").ajaxSubmit({
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
		