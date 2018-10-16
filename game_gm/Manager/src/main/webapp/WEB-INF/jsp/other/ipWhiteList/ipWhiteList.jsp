<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="ipWhiteListDiv">
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
				<div class="form-group">
					<div class="form-group">
						<label class="col-sm-1 control-label" for="ip">IP</label>
						<div class="col-sm-4" >
							<input type="text" name="ip" class="form-control" id="ip" value="" />
						</div>
					</div>
					<label class="col-sm-1 control-label" for="remark">备注</label>
					<div class="col-sm-4" >
						<textarea id="remark" name="remark" cols="60" rows="5" warp="virtual"></textarea>
					</div>
				</div>
				<div class="col-sm-4" >
					<button  type="button" onclick="addIpWhiteList();">增加</button>
				</div>
		</div><br><br />
	</div>
	<br><br>
	<div class="col-md-12" >
		<table class="table table-bordered table-striped table-condensed table-hover">
			<thead>
			<tr>
				<th>序号</th>
				<th>IP</th>
				<th>备注</th>
				<th>操作</th>

			</tr>
			</thead>


			<tbody>
			<c:forEach items="${ipWhiteLists}" var="ipWhiteList" varStatus="status">
				<tr>
					<td>${status.count}</td>
					<td>${ipWhiteList["ip"]}</td>
					<td>${ipWhiteList["remark"]}</td>
					<td>
						<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='delIpWhiteList("${ipWhiteList['id']}");'>删除</a>
					</td>
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