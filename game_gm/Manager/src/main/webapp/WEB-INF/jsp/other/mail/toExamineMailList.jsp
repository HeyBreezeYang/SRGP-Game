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
	<div>
		<div >
			<label>邮件状态：</label>
			<select  name="conditionStatus" id="conditionStatus" >
				<option value=all>全部</option>
				<option value=1>未审核</option>
				<option value=2>通过</option>
				<option value=3>未通过</option>
			</select>
		</div>
		<div>
			<label>开始日期：</label>
			<input type="text" id="startDate">
		</div>
		<div>
			<label>结束日期：</label>
			<input type="text" id="endDate">

		</div>
		<div>
			<button type="button" onclick="queryMail();" >查询</button>
		</div>
	</div>

	<br><br><br>
	<div class="col-md-12" >
		<table class="table table-bordered table-striped table-condensed table-hover">
			<thead>
			<tr>
				<th>序号</th>
				<th>主题</th>
				<th>状态</th>
				<th>创建人</th>
				<th>创建时间</th>
				<th>审核人</th>
				<th>操作</th>

			</tr>
			</thead>


			<tbody>
			<c:forEach items="${mails}" var="mail" varStatus="status">
				<input type="hidden" id="username${status.count}" value="${mail['username']}">
				<input type="hidden" id="email${status.count}" value='${mail["email"]}'>
				<tr>
					<td>${status.count}</td>
					<td>${mail['title']}</td>
					<td>${mail['statusS']}</td>
					<td>${mail['createby']}</td>
					<td>${mail['date']}</td>
					<td>${mail['toexamineBy']}</td>
					<td>
						<c:if test="${mail['status'] != 2}">
							<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='TEMail("${mail['id']}");'>审核</a>
						</c:if>
						<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='delMail("${mail['id']}");'>删除</a>
						<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='viewMail(${status.count});'>查看</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
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

    function viewMail(index) {
        var username = $("#username"+index).val();
        if(username == "all"){
            username = "所有玩家";
        }
        var mail = $("#email"+index).val();
        var mailJson = eval('(' + mail + ')');
        if(mailJson == undefined){
            mailJson == "没有附件";
        }
        alert("玩家："+username+"  附件内容:"+mailJson.funscJson);
    }

    function TEMail(id){
        jQuery.ajax({
            url: "mail/toExamineMail",
            type:"POST",
            async: false,
            data:{"mailId":id},
            success: function(response)
            {
                jQuery('#mailDiv').html(response);
            }
        });

    }

    function delMail(id){
        jQuery.ajax({
            url: "mail/delMail",
            type:"POST",
            data:{"mailId":id},
            success: function(res)
            {	alert(res);
                jQuery.ajax({
                    url: "mail/mailList",
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

    function queryMail() {
        var status = $("#conditionStatus").val();
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        jQuery.ajax({
            url: "mail/mailConditionList",
            type:"POST",
            async: false,
            data:{"mailStatus":status,"startDate":startDate,"endDate":endDate},
            success: function(response)
            {
                jQuery('#mailDiv').html(response);
            }
        });
    }


</script>
		