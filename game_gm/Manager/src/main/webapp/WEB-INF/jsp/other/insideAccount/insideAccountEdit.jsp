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
					<h3 class="panel-title">内部号管理</h3>
				</div>
					<form action="insideAccount/saveOrUpdateInside" role="form" class="form-horizontal" method="post" id="insideAccountForm">

						<input type="hidden" id="insideId" name="insideId" value="${inside['id']}" >
						<div>
							<label class="col-sm-1 control-label" for="sid">服务器</label>
    							<div class="form-group" id="make">

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
							<div class="form-group" id="make1">

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
							<div class="form-group">
								<label class="col-sm-1 control-label" for="rolename" >角色名</label>
								<div class="col-sm-4" >
									<input type="text" name="rolename" class="form-control" id="rolename" value="${inside['rolename']}"  />
								</div>
								<label class="col-sm-1 control-label" for="pid">PID</label>
								<div class="col-sm-4" >
									<input type="text" name="pid" class="form-control" id="pid" value="${inside['pid']}"    />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label" for="paymoney">充值金额</label>
								<div class="col-sm-4" >
									<input type="text" name="paymoney" class="form-control" id="paymoney" value="${inside['paymoney']}" />
								</div>
								<label class="col-sm-1 control-label" for="username">使用人</label>
								<div class="col-sm-4" >
									<input type="text" name="username" class="form-control" id="username" value="${inside['username']}" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label" for="userphone">联系电话</label>
								<div class="col-sm-4" >
									<input type="text" name="userphone" class="form-control" id="userphone" value="${inside['userphone']}" />
								</div>
								<label class="col-sm-1 control-label" for="ascription">使用人归属</label>
								<div class="col-sm-4" >
									<input type="text" name="ascription" class="form-control" id="ascription" value="${inside['ascription']}" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label" for="applyuser">申请运营</label>
								<div class="col-sm-4" >
									<input type="text" name="applyuser" class="form-control" id="applyuser" value="${inside['applyuser']}" />
								</div>
								<label class="col-sm-1 control-label" for="applyreason">申请原因</label>
								<div class="col-sm-4" >
									<input type="text" name="applyreason" class="form-control" id="applyreason" value="${inside['applyreason']}" />
								</div>
							</div>
							<label class="col-sm-1 control-label" for="remark">备注</label>
							<div class="col-sm-4" >
								<textarea id="remark" name="remark" cols="100" rows="5" warp="virtual" >${inside['remark']}</textarea>
							</div>
						</div>
						<div class="col-sm-4" >
							<button  type="button" onclick="upInsideAccount();">确定</button>
						</div>
					</form>
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
        var inside = <%= session.getAttribute("inside")%>
        if (inside != null || inside.size() > 0) {
            var serverId = inside['sid'].split(',');
            var channelId = inside['channel'].split(',');
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

    function upInsideAccount(){
        $("#setMarkDiv").html("确定修改当前内部号吗？");
        $("#reSubBtn").attr("onclick","subNoticeConfig();");
        jQuery('#reMark').modal('show',{backdrop: 'fade'});
    }

	function subNoticeConfig(){
		$("#insideAccountForm").ajaxSubmit({
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
		