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
		<div class="noticePageVid" id="noticeDiv">
			<div class="col-md-1" >
				<button class="btn btn-success btn-block" onclick="addGameNoticeDiv();" type="button">添加游戏公告</button>
			</div>
			<div class="col-md-1">
				<button class="btn btn-success btn-block" onclick="addSystemNoticeDiv();" type="button">添加系统公告</button>
			</div>
			<br><br><br>
			<div class="col-md-12" >
				<table class="table table-bordered table-striped table-condensed table-hover">
					<thead>
					<tr>
						<th>序号</th>
						<th>标题</th>
						<th>公告类型</th>
						<th>操作</th>

					</tr>
					</thead>


					<tbody>
					<c:forEach items="${notices}" var="notice">
						<tr>
							<td>${notice['id']}</td>
							<td>${notice['title']}</td>
							<td>${notice['type']}</td>
							<td>
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='upNotice("${notice['id']}","${notice['type']}");'>编辑</a>
								<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='delNotice("${notice['id']}");'>删除</a>
								<%--<button  onclick="upNotice(1,${notice['type']},${notice['msg']});" type="button">编辑</button>--%>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

<script type="text/javascript">
	function addGameNoticeDiv() {

        jQuery.ajax({
            url: "notice/gameNotice",
            async: false,
            data:{"noticeId":null},
            success: function(response)
            {
                jQuery('#noticeDiv').html(response);
            }
        });


    }
    function addSystemNoticeDiv() {
        jQuery.ajax({
            url: "notice/systemNotice",
            async: false,
            data:{"noticeId":null},
            success: function(response)
            {
                jQuery('#noticeDiv').html(response);
            }
        });
    }
	function upNotice(id,type){
		if (type == "游戏公告"){
            jQuery.ajax({
                url: "notice/updateGameNoticePage",
                type:"POST",
                async: false,
                data:{"noticeId":id},
                success: function(response)
                {
                    jQuery('#noticeDiv').html(response);
                }
            });
		}else if(type == "系统公告"){
            jQuery.ajax({
                url: "notice/updateSystemNoticePage",
                type:"POST",
                async: false,
                data:{"noticeId":id},
                success: function(response)
                {
                    jQuery('#noticeDiv').html(response);
                }
            });
		}

	}

	function  delNotice(id) {
        jQuery.ajax({
            url: "notice/delGameNotice",
            type:"POST",
            data:{"noticeId":id},
            success: function(res)
            {
               alert(res);
                jQuery.ajax({
                    url: "notice/noticeList",
                    type:"POST",
                    async: false,
                    success: function(response)
                    {
                        jQuery('#noticeDiv').html(response);
                    }
                });
            }
        });
    }


</script>
		