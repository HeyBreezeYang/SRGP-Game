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
	<div style="width: 100%;">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">商城浏览</h3>
				<div class="panel-options">
					<a href="#" data-toggle="panel">
						<span class="collapse-icon">&ndash;</span>
						<span class="expand-icon">+</span>
					</a>
				</div>
			</div>
			<div class="panel-body">
				<button class="btn btn-secondary" onclick="addShop();">新增商城</button>
				<a href="#make" style="display: none;" id="scp"></a>
				<table id="Shoptable" class="table table-striped table-bordered" cellspacing="0" width="100%">
					<thead>
						<tr>
							<th width="7%">服务器</th>
							<th width="5%">ID</th>
							<th width="8%">商城类型</th>
							<th>开始</th>
							<th>结束</th>
							<th>物品配置</th>
							<th>数量配置</th>
							<th>刷新时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody id="ShopMsg">
						<c:if test="${! empty ShopList}">
							<c:forEach items="${ShopList}" var="ShopData">
								<tr id="Shop-${ShopData.id}">
									<td class="ShopMsg-${ShopData.id}" >${ShopData.server}</td>
									<td class="ShopMsg-${ShopData.id}" >${ShopData.id}</td>
									<td class="ShopMsg-${ShopData.id}">${ShopData.type}</td>
									<td class="ShopMsg-${ShopData.id}" >${ShopData.start}</td>
									<td class="ShopMsg-${ShopData.id}">${ShopData.end}</td>
									<td>
										<textarea class="ShopMsg-${ShopData.id}" style="resize: none;height: 100%;width: 100%;border: none;" readonly="readonly">${ShopData.itemIds}</textarea>
									</td>
									<td>
										<textarea class="ShopMsg-${ShopData.id}" style="resize: none;height: 100%;width: 100%;border: none;" readonly="readonly">${ShopData.itemNum}</textarea>
									</td>
									<td>
										<textarea class="ShopMsg-${ShopData.id}" style="resize: none;height: 100%;width: 100%;border: none;" readonly="readonly">${ShopData.refHours}</textarea>
									</td>
									<td>
										<a href="#make" class="btn btn-info btn-sm btn-icon icon-left" onclick="reShop(${ShopData.id},'${ShopData.server}');">修改</a>
										<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delShop(${ShopData.id},'${ShopData.server}');">删除</a>
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	
		<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">Shop编辑</h3>
				</div>
				<div class="panel-body">
					<form action="shop/subShop" role="form" class="form-horizontal" method="post" id="Shopform">
						<input type="hidden" id="ShopId" value="-1" name="id">
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ShopServer">服务器</label>
							<div class="col-sm-5">
								<select class="form-control" id="ShopServer" name="server">
									<option value="all">全服</option>
									<c:forEach items="${serverConfig}" var="config">
										<option value="${config.serverID}">${config.serverName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ShopType">商城类型</label>
							<div class="col-sm-5">
								<input type="text" id="ShopType" name="type" class="form-control">
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ShopItemIds">商品配置</label>
							<div class="col-sm-5">
								<input type="text" name="itemIds" class="form-control shopDesign" id="ShopItemIds"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="startTime">开始时间</label>
							<div class="col-sm-2">
								<input type="text" name="start" class="form-control shopDesign" id="startTime"/>
							</div>
							<label class="col-sm-1 control-label" for="endTime">结束时间</label>
							<div class="col-sm-2">
								<input type="text" name="end" class="form-control shopDesign" id="endTime"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ShopItemNum">数量配置</label>
							<div class="col-sm-5">
								<input type="text" name="itemNum" class="form-control shopDesign" id="ShopItemNum"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">
							<label class="col-sm-1 control-label" for="ShopRefHours">刷新时间</label>
							<div class="col-sm-5">
								<input type="text" name="refHours" class="form-control shopDesign" id="ShopRefHours"/>
							</div>
						</div>
						<div class="form-group-separator"></div>
						<div class="form-group">	
							<label class="col-sm-1 control-label"></label>	
							<div class="col-sm-5">
								<button class="btn btn-purple btn-block" onclick="subShopConfig();" type="button">确定编辑</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>	

<script type="text/javascript">

	function reShop(tbId){
		var k=$(".ShopMsg-"+tbId);
		$("#ShopServer").val($(k[0]).html());
		$("#ShopId").val($(k[1]).html());
		$("#ShopType").val($(k[2]).html());
        $("#startTime").val($(k[3]).html());
        $("#endTime").val($(k[4]).html());
        $("#ShopItemIds").val($(k[5]).html());
        $("#ShopItemNum").val($(k[6]).html());
        $("#ShopRefHours").val($(k[7]).html());
	}
	function addShop(){
		document.getElementById("scp").click();
		$("#ShopId").val(-1);
		$("#ShopType").val("");
        $("#startTime").val("");
        $("#endTime").val("");
		$("#ShopItemIds").val("");
        $("#ShopItemNum").val("");
        $("#ShopRefHours").val("");
	}
	function delShop(deltbId,sid){
		$("#delId_delDiv").val(deltbId);
		
		$("#delText").html("确定删除该条商城配置？");
		$("#detSubBtn").attr("onclick","subDelShop(\""+sid+"\");");
		jQuery('#delMake').modal('show',{backdrop: 'fade'});
	}
	function subShopConfig(){
		$("#Shopform").ajaxSubmit({
			dataType:'json',
			success:function(){
				forwardPage('shop/getAllShop',${thisMenu});
			}
		});
	}

	function subDelShop(sid){
		var id=$("#delId_delDiv").val();
		$.ajax({
			url:'shop/subDelShop?id='+id+'&sid='+sid,
			dataType:'text',
			type: 'post',
			success:function(){
				forwardPage('shop/getAllShop',${thisMenu});
			}
		});
	}
	jQuery(document).ready(function($)
			{
				$("#Shoptable").dataTable({
					aLengthMenu: [
						[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
					]
				});
		});
</script>		
		