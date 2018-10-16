<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<script type="text/javascript">
        laydate.render({
            elem: '#queryDate', //指定元素
            showBottom: false
        });
        
        function queryRank() {
            var type = $("#rankType").val();
            var date = $("#queryDate").val();
            jQuery.ajax({
                url: "rank/queryRank",
                type:"POST",
                async: false,
                data:{"type":type,"queryData":date},
                success: function(response)
                {
                    jQuery('#rankDiv').html(response);
                }
            });
        }
	</script>
	<div id="rankDiv">
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
				<div >
					<label>排行榜类型：</label>
					<select  name="rankType" id="rankType" >
						<option value="org">势力榜</option>
						<option value="close">亲密榜</option>
						<option value="card">关卡榜</option>
					</select>
				</div>
				<div>
					<label>查看日期：</label>
					<input type="text" id="queryDate">
				</div>
				<div>
					<button type="button" onclick="queryRank();" >查询</button>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">排行榜22:00</h3>
				</div>
				<div >
					<table class="table responsive">
						<thead>
						<tr>
							<th>PID</th>
							<th>角色名</th>
							<th>VIP</th>
							<th>积分值</th>
							<th>排名</th>
						</tr>
						</thead>
						<tbody >
						<c:forEach items="${allRank['rank22']}" var="rank22" varStatus="status">
							<tr>
								<td>${rank22['id']}</td>
								<td>${rank22['nm']}</td>
								<td>${rank22['vip']}</td>
								<td>${rank22['val']}</td>
								<td>${status.count}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">排行榜00:00</h3>
				</div>
				<div >
					<table class="table responsive">
						<thead>
						<tr>
							<th>PID</th>
							<th>角色名</th>
							<th>VIP</th>
							<th>积分值</th>
							<th>排名</th>
						</tr>
						</thead>
						<tbody >
						<c:forEach items="${allRank['rank24']}" var="rank24" varStatus="status">
							<tr>
								<td>${rank24['id']}</td>
								<td>${rank24['nm']}</td>
								<td>${rank24['vip']}</td>
								<td>${rank24['val']}</td>
								<td>${status.count}</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>