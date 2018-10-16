<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<div id="insideLog">
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="#" onclick="moveMain();">
							<i class="fa-home"></i>回到主页
						</a>
					</li>
				</ol>
			</div>

			<div class="col-md-12" >
				<div>
					<div>
						<label>开始日期：</label>
						<input type="text" id="startDate">
					</div>
					<div>
						<label>结束日期：</label>
						<input type="text" id="endDate">
					</div>
					<div>
						<button type="button" onclick="queryByDate();">查询</button>
					</div>
				</div>
				<div>
					<table class="table table-bordered table-striped table-condensed table-hover">
						<thead>
						<tr>
							<th>序号</th>
							<th>区服</th>
							<th>渠道</th>
							<th>角色名</th>
							<th>金额</th>
							<th>操作员</th>
							<th>时间</th>

						</tr>
						</thead>


						<tbody>
						<c:forEach items="${insodeLogs}" var="insideLog">
							<tr>
								<td>${insideLog["id"]}</td>
								<td>${insideLog["sid"]}</td>
								<td>${insideLog["channel"]}</td>
								<td>${insideLog["rolename"]}</td>
								<td>${insideLog["money"]}</td>
								<td>${insideLog["operatorBy"]}</td>
								<td>${insideLog["logTimeS"]}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
<script type="text/javascript">

    laydate.render({
        elem: '#startDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#endDate',
        showBottom: false

    });
	function queryByDate(){
        var startdate = $("#startDate").val();
        var enddate = $("#endDate").val();
        jQuery.ajax({

            url: "insideAccount/insideAccountLog",
            type:"POST",
            async: false,
            data:{"startDate":startdate,"endDate":enddate},
            success: function(response)
            {
                jQuery('#insideLog').html(response);
            }
        });

	}

</script>