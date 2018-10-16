<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<div id="orderDiv">
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
			<div>
				<div class="col-sm-2">
					<label>服务器：</label>
					<select class="form-control" name="qsid" id="qsid" multiple>
						<c:forEach items="${allSelect['allServer']}" var="serverObj" >
							<option value="${serverObj.serverID}">${serverObj.serverName}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-sm-2">
					<label>渠道：</label>
					<select  class="form-control" name="qchannel" id="qchannel" multiple >
						<c:forEach items="${allSelect['allChannel']}" var="channelObj">
							<option value="${channelObj.name}">${channelObj.dsp}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-sm-2">
					<label>充值套餐：</label>
					<select class="form-control" name="qpaySetMeal" id="qpaySetMeal"  multiple>
						<c:forEach items="${allSelect['allSetMeal']}" var="setMealObj">
							<option value="${setMealObj['id']}">${setMealObj['name']}</option>
						</c:forEach>
					</select>
				</div>

				<div class="col-sm-2">
					<label>订单状态：</label>
					<select class="form-control"  name="qstatus" id="qstatus" multiple >
						<option value="1">未发货</option>
						<option value="0">发货成功</option>
						<option value="3">发货失败</option>
					</select>
				</div><br />
				<div class="col-sm-6">
					<label>日期范围：</label>
					<input type="text" id="startDate">
					<label>-</label>
					<input type="text" id="endDate">
				</div><br />
				<div class="col-sm-6">
					<label>玩家ID：</label>
					<input type="text" id="qpid">&nbsp;
					<label>订单号：</label>
					<input type="text" id="qorderNum">
				</div><br />
				<div>
					<button type="button" onclick="queryOrder();" >查询</button>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">订单查询</h3>
				</div>
				<div >
					<table class="table responsive">
						<thead>
						<tr>
							<th>序号</th>
							<th>订单编号</th>
							<th>服务器ID</th>
							<th>渠道</th>
							<th>玩家ID</th>
							<th>玩家名字</th>
							<th>充值套餐</th>
							<th>金额</th>
							<th>订单状态</th>
							<th>充值时间</th>
						</tr>
						</thead>
						<tbody >
						<c:forEach items="${orders}" var="order" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td>${order['orderUuid']}</td>
								<td>${order['serverId']}</td>
								<td>${order['channel']}</td>
								<td>${order['pid']}</td>
								<td>${order['pname']}</td>
								<td>${order['money']}</td>
								<td>${order['price']}</td>
								<td>${order['status']}</td>
								<td>${order['date']}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
<script type="text/javascript">
    laydate.render({
        elem: '#startDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#endDate', //指定元素
        showBottom: false
    });

    function queryOrder() {
        var qsid = $("#qsid").val();
        var qchannel = $("#qchannel").val();
        var qpaySetMeal = $("#qpaySetMeal").val();
        var qstatus = $("#qstatus").val();
        var qstartDate = $("#startDate").val();
        var qendDate = $("#endDate").val();
        var qpid = $("#qpid").val();
        var qorderNum = $("#qorderNum").val();
        jQuery.ajax({
            url: "order/queryOrder",
            type:"POST",
            async: false,
            traditional:true,
            data:{"sid":qsid,"channel":qchannel,"paySetMeal":qpaySetMeal,"status":qstatus,"startDate":qstartDate,"endDate":qendDate,"pid":qpid,"orderNum":qorderNum},
            success: function(response)
            {
                jQuery('#orderDiv').html(response);
            }
        });
    }

    jQuery(document).ready(function($)
    {
        $("#qsid").select2({
            placeholder: '选择服务器',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
        $("#qchannel").select2({
            placeholder: '选择渠道',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
        $("#qpaySetMeal").select2({
            placeholder: '选择套餐',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
        $("#qstatus").select2({
            placeholder: '订单状态',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });

    });

</script>