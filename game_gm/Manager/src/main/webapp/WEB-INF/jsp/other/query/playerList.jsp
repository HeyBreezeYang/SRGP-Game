<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div id="playerListDiv">
	<div class="breadcrumb-env">
		<ol class="breadcrumb bc-1">
			<li>
				<a href="#" onclick="moveMain();">
					<i class="fa-home"></i>回到主页
				</a>
			</li>
		</ol>
	</div>
	<br><br>
	<div class="col-md-12" >
		<table class="table table-bordered table-striped table-condensed table-hover">
			<thead>
			<tr>
				<th>序号</th>
				<th>服务器ID</th>
				<th>角色名</th>
				<th>状态</th>
				<th>日期</th>

			</tr>
			</thead>


			<tbody>
			<c:forEach items="${banBacks}" var="BanBack" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${BanBack['server']}</td>
					<td>${BanBack['name']}</td>
					<c:if test="${BanBack['type'] == 1}">
						<td>冻结</td>
					</c:if>
					<c:if test="${BanBack['type'] == 2}">
						<td>禁言</td>
					</c:if>
					<td><jsp:useBean id="timestamp" class="java.util.Date"/>
					<jsp:setProperty name="timestamp" property="time" value="${BanBack['logTime']}"/>
					<fmt:formatDate value="${timestamp}" pattern="yyyy-MM-dd"/></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</div>
<script type="text/javascript">

    function addIpWhiteList(){
        var ip = $("#ip").val();
        var remark = $("#remark").val();
        jQuery.ajax({
            url: "ipWhiteList/addIpWhiteList",
            type:"POST",
            data:{"ip":ip,"remark":remark},
            success: function(res)
            {
                alert(res);
                jQuery.ajax({
                    url: "ipWhiteList/ipWhiteListPage",
                    type:"POST",
                    async: false,
                    success: function(response)
                    {
                        jQuery('#ipWhiteListDiv').html(response);
                    }
                });
            }
        });

    }

    function delIpWhiteList(id){
        jQuery.ajax({
            url: "ipWhiteList/delIpWhiteList",
            type:"POST",
            data:{"id":id},
            success: function(res)
            {
                alert(res);
                jQuery.ajax({
                    url: "ipWhiteList/ipWhiteListPage",
                    type:"POST",
                    async: false,
                    success: function(response)
                    {
                        jQuery('#ipWhiteListDiv').html(response);
                    }
                });
            }
        });
    }

</script>