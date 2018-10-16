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
				<button class="btn btn-success btn-block" onclick="addBroadcastNotice();" type="button">添加轮播公告</button>
			</div>
			<br><br><br>
			<div class="col-md-12" >
				<table class="table table-bordered table-striped table-condensed table-hover">
					<thead>
					<tr>
						<th>序号</th>
						<th>标题</th>
						<th>公告类型</th>
						<th>开始时间</th>
						<th>结束时间</th>
						<th>间隔时间s/秒</th>
						<th>操作</th>

					</tr>
					</thead>


					<tbody>
					<c:forEach items="${notices}" var="notice">
						<tr>
							<td>${notice['id']}</td>
							<td>${notice['title']}</td>
							<td>${notice['type']}</td>
							<td>${notice['startTime']}</td>
							<td>${notice['endTime']}</td>
							<td>${notice['intervalTime']}</td>
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
<%--		<div style="display: none;" id="setNoticeDiv">
			<div class="col-md-4">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">游戏公告</h3>
						<div class="panel-options">
							<a href="#" data-toggle="remove">
								&times;
							</a>
						</div>
					</div>
					<div class="panel-body">
						<div class="form-group">
							<label class="col-sm-2 control-label">标题</label>
							<div class="col-sm-10">
								<input type="text" name="Title" class="form-control">
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">内容</label>
							<div class="col-sm-10">
								<textarea class="form-control" rows="11" name="Content" style="resize: none;"></textarea>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-2 control-label">优先级</label>
							<div class="col-sm-4">
								<input type="text" name="priority" class="form-control" >
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>--%>
<script type="text/javascript">
	function addBroadcastNotice() {

        jQuery.ajax({
            url: "notice/broadcastNotice",
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
		