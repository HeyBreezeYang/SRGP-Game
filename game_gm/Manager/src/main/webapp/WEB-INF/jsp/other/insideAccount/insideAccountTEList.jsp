<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div id="insideTElistDiv">
		<div class="breadcrumb-env">
			<ol class="breadcrumb bc-1">
				<li>
					<a href="#" onclick="moveMain();">
						<i class="fa-home"></i>回到主页
					</a>
				</li>
			</ol>
		</div>
		<div class="panel-body">
			<div>
				<form action="insideAccount/saveOrUpdateInside" role="form" class="form-horizontal" method="post" id="insideAccountForm">
					<input type="hidden" id="insideId",name="insideId" value="">
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
							<label class="col-sm-1 control-label" for="rolename">角色名</label>
							<div class="col-sm-2" >
								<input type="text" name="rolename" class="form-control" id="rolename" value="" />
							</div>
							<label class="col-sm-1 control-label" for="pid">PID</label>
							<div class="col-sm-2" >
								<input type="text" name="pid" class="form-control" id="pid" value="" />
							</div>
							<label class="col-sm-1 control-label" for="paymoney">充值金额</label>
							<div class="col-sm-2" >
								<input type="text" name="paymoney" class="form-control" id="paymoney" value="" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="username">使用人</label>
							<div class="col-sm-2" >
								<input type="text" name="username" class="form-control" id="username" value="" />
							</div>
							<label class="col-sm-1 control-label" for="userphone">联系电话</label>
							<div class="col-sm-2" >
								<input type="text" name="userphone" class="form-control" id="userphone" value="" />
							</div>
							<label class="col-sm-1 control-label" for="ascription">使用人归属</label>
							<div class="col-sm-2" >
								<input type="text" name="ascription" class="form-control" id="ascription" value="" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="applyuser">申请运营</label>
							<div class="col-sm-4" >
								<input type="text" name="applyuser" class="form-control" id="applyuser" value="" />
							</div>
							<label class="col-sm-1 control-label" for="applyreason">申请原因</label>
							<div class="col-sm-4" >
								<input type="text" name="applyreason" class="form-control" id="applyreason" value="" />
							</div>
						</div>
						<label class="col-sm-1 control-label" for="remark">备注</label>
						<div class="col-sm-4" >
							<textarea id="remark" name="remark" cols="100" rows="5" warp="virtual"></textarea>
						</div>
					</div>
					<div class="col-sm-4" >
						<button  type="button" onclick="upInsideAccount();">添加内部号</button>
					</div>
				</form>
			</div><br><br />
			<div>
				<label>角色名：</label><input type="text" id="granRoleName"><label>金额：</label><input type="text" id="grantResource"><button type="button" onclick="grantResource();" >发放资源</button>
			</div>
		</div>
		<br><br>
		<div class="col-md-12" >
			<table class="table table-bordered table-striped table-condensed table-hover">
				<thead>
				<tr>
					<th>序号</th>
					<th>区服</th>
					<th>渠道</th>
					<th>角色名</th>
					<th>使用人</th>
					<th>金额</th>
					<th>状态</th>
					<th>操作</th>

				</tr>
				</thead>


				<tbody>
				<c:forEach items="${insides}" var="inside">
					<tr>
						<td>${inside["id"]}</td>
						<td>${inside["sid"]}</td>
						<td>${inside["channel"]}</td>
						<td>${inside["rolename"]}</td>
						<td>${inside["username"]}</td>
						<td>${inside["paymoney"]}</td>
						<c:if test="${inside['status'] == 0}">
							<td>待审核</td>
						</c:if>
						<c:if test="${inside['status'] == 1}">
							<td>审核通过</td>
						</c:if>
						<c:if test="${inside['status'] == 2}">
							<td>审核不通过</td>
						</c:if>
						<td>
								<%--<c:if test="${mail['status'] != 2}">--%>
							<c:if test="${inside['status'] == 0}">
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='updateInsideAccount("${inside['id']}");'>编辑</a>
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='insideAccountTE("${inside['id']}",1);'>通过</a>
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='insideAccountTE("${inside['id']}",2);'>不通过</a>
							</c:if>
							<c:if test="${inside['status'] != 0}">
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='updateInsideAccount("${inside['id']}");'>编辑</a>
							</c:if>
							<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='delInsideAccount("${inside['id']}");'>删除</a>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
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

    function upInsideAccount(){
        $("#setMarkDiv").html("确定添加当前内部号吗？");
        $("#reSubBtn").attr("onclick","subNoticeConfig();");
        jQuery('#reMark').modal('show',{backdrop: 'fade'});
    }

    function subNoticeConfig(){
        $("#insideAccountForm").ajaxSubmit({
            dataType:'json'
        });
        $("#insideAccountForm").reset();
    }

	function updateInsideAccount(id){
        jQuery.ajax({
            url: "insideAccount/insideAccountEdit",
            type:"POST",
            async: false,
            data:{"id":id},
            success: function(response)
            {
                jQuery('#insideTElistDiv').html(response);
            }
        });

	}

    function delInsideAccount(id){
        jQuery.ajax({
            url: "insideAccount/delInsideAccount",
            type:"POST",
            data:{"id":id},
            success: function(res)
            {
                alert(res);
                jQuery.ajax({
                    url: "insideAccount/insideAccountTEList",
                    type:"POST",
                    async: false,
                    success: function(response)
                    {
                        jQuery('#insideTElistDiv').html(response);
                    }
                });
            }
        });

    }

   function queryInsideAccount() {
       var querysid = $("#querysid").val();
       var querychannel = $("#querychannel").val();
       var queryrolename = $("#queryrolename").val();
       var querypid = $("#querypid").val();
       var querymoney = $("#querymoney").val();
       var querystatus = $("#querystatus").val();
       jQuery.ajax({
           url: "insideAccount/insideAccountListCondition",
           type:"POST",
           async: false,
           traditional:true,
           data:{"sid":querysid,"channel":querychannel,"rolename":queryrolename,"pid":querypid,"money":querymoney,"status":querystatus},
           success: function(response)
           {
               jQuery('#insideTElistDiv').html(response);
           }
       });
   }

   function addInsideAccount() {
       jQuery.ajax({
           url: "insideAccount/insideAccount",
           type:"POST",
           async: false,
           success: function(response)
           {
               jQuery('#insideTElistDiv').html(response);
           }
       });
   }
   
   function insideAccountTE(id,type) {
       jQuery.ajax({
           url: "insideAccount/insideAccountTE",
           type:"POST",
           data:{"id":id,"type":type},
           success: function(res)
           {
               jQuery.ajax({
                   url: "insideAccount/insideAccountTEList",
                   type:"POST",
                   async: false,
                   success: function(response)
                   {
                       jQuery('#insideTElistDiv').html(response);
                   }
               });
           }
       });
   }

   function grantResource() {
       var grantrolename = $("#granRoleName").val();
       var paymoney = $("#grantResource").val();
       jQuery.ajax({
           url: "insideAccount/grantResource",
           type:"POST",
           data:{"paymoney":paymoney,"rolename":grantrolename},
           success: function(res)
           {
               alert(res);
               jQuery.ajax({
                   url: "insideAccount/insideAccountTEList",
                   type:"POST",
                   async: false,
                   success: function(response)
                   {
                       jQuery('#insideTElistDiv').html(response);
                   }
               });
           }
       });

   }

    jQuery(document).ready(function($)
    {
        $("#querysid").select2({
            placeholder: '选择需要发送的服务器',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
        $("#querychannel").select2({
            placeholder: '选择需要发送的渠道',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });

    });
    
</script>