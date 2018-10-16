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
		<div class="mailPageVid" id="mailDiv">
			<div class="panel-body">
				<div>
					<div>
						<label class="col-sm-1 control-label" for="querysid">服务器</label>
						<div class="form-group" id="sid_group">

							<div class="col-sm-4">
								<select class="form-control" name="sid" id="querysid">
									<option value="all">全服</option>
									<c:forEach items="${allServer}" var="serverObj">
										<option value="${serverObj.serverid}">${serverObj.servername}</option>
									</c:forEach>
								</select>

							</div>
						</div>
						<label class="col-sm-1 control-label" for="querychannel">渠道</label>
						<div class="form-group" id="channel_group">

							<div class="col-sm-4">
								<select class="form-control" name="channel" id="querychannel" >
									<option value="all">全渠道</option>
									<c:forEach items="${allChannel}" var="channelObj">
										<option value="${channelObj.id}">${channelObj.dsp}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<br /><br /><br />
					<div>
						<label>角色名：</label>
						<input type="text" id="queryrolename">
						<label>PID：</label>
						<input type="text" id="querypid">
					</div><br />
					<div>
						<label>金额：</label>
						<input type="text" id="querymoney">
						<label>状态：</label>
						<select name="channel" id="querystatus">
							<option value="all">查询所有</option>
							<option value="0">待审核</option>
							<option value="1">通过</option>
							<option value="2">不通过</option>
						</select>
						<button type="button" onclick="queryInsideAccount();" >查询</button>
					</div><br />
					<div>
						<button type="button" onclick="addInsideAccount();" >添加内部号</button>
					</div><br />
				</div>
				<div>
					<label>角色名：</label><input type="text" id="granRoleName"><label>金额：</label><input type="text" id="grantResource"><button type="button" onclick="grantResource();" >发放资源</button>
				</div>

			<br><br><br>
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
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
<script type="text/javascript">

   /* laydate.render({
        elem: '#startDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#endDate',
        showBottom: false

    });*/
	function updateInsideAccount(id){
        jQuery.ajax({
            url: "insideAccount/insideAccountEdit",
            type:"POST",
            async: false,
            data:{"id":id},
            success: function(response)
            {
                jQuery('#mailDiv').html(response);
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
                jQuery.ajax({
                    url: "insideAccount/insideAccountList",
                    type:"POST",
                    async: false,
                    success: function(response)
                    {
                        jQuery('#mailDiv').html(response);
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
                jQuery('#mailDiv').html(response);
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
               jQuery('#mailDiv').html(response);
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
                   url: "insideAccount/insideAccountList",
                   type:"POST",
                   async: false,
                   success: function(response)
                   {
                       jQuery('#mailDiv').html(response);
                   }
               });
           }
       });
	   
   }

    /*jQuery(document).ready(function($)
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

    });*/
    
</script>