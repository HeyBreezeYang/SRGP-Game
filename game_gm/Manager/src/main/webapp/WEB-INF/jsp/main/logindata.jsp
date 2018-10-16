
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta charset="utf-8" http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta name="description" content="Xenon Boostrap Admin Panel" />
	<meta name="author" content="" />
	<title>GM-${vName}</title>

	<link rel="stylesheet" href="${GmBase}/assets/css/fonts/linecons/css/linecons.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/fonts/fontawesome/css/font-awesome.min.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/bootstrap.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/xenon-core.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/xenon-forms.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/xenon-components.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/xenon-skins.css">
	<link rel="stylesheet" href="${GmBase}/assets/js/datatables/dataTables.bootstrap.css">
	<link rel="stylesheet" href="${GmBase}/assets/js/select2/select2.css">
	<link rel="stylesheet" href="${GmBase}/assets/js/select2/select2-bootstrap.css">
	<link rel="stylesheet" href="${GmBase}/assets/js/multiselect/css/multi-select.css">
	<link rel="stylesheet" href="${GmBase}/assets/css/custom.css">
	<script src="${GmBase}/assets/js/jquery-1.11.1.min.js"></script>
	<script src="${GmBase}/assets/js/jquery-form.js"></script>
	<script src="${GmBase}/assets/js/laydate/laydate.js"></script>
	<link rel="stylesheet" href="${GmBase}/assets/js/laydate/theme/default/laydate.css">
	<script type="text/javascript">

        var start_time = new Date();
        var end_time = "";
        var littieTime = parseInt('${nowTime}');
        var t = setInterval(function() {
            if (document.readyState == "complete") {
                aa();
            }
        }, 0);
        var nd=setInterval(function () {
            $.ajax({
                type: "post",
                url:"queryNowData",
                dataType:"json",
                success: function(result){
                    convertTable(result);
                }
            });
        },20000);
        function convertTable(result) {
            var txt="";
            for(var i=0;i<result.length;i++){
                txt+="<tr>" +
                    "<td>" +
                    result[i][0]+
                    "</td><td>" +
                    result[i][1]+
                    "</td><td>" +
                    result[i][2]+
                    "</td><td>" +
                    result[i][5]+
                    "</td><td>" +
                    result[i][4]+
                    "</td><td>" +
                    result[i][3]+
                    "</td>"+
                    "</tr>"
            }
            $("#nowDataId").html(txt);
        }
        function aa() {
            end_time = new Date();
            littieTime += (end_time.getTime() - start_time.getTime());
            clearInterval(t);
            showServerTime();
        }
        function showServerTime() {
            var t = new Date();
            littieTime += 1000;
            t.setTime(littieTime);
            $("#serverTime").html(t.toLocaleString());
            var mytime = setTimeout("showServerTime()", 1000);
        }
        function forwardPage(url,menuId){
            $("#countWindow").hide();
            $('#menuId').val(menuId);
            $.post(url,{menuId:menuId},function(data){
                $('#page').html(data);
            });
        }
        function moveMain(){
            $("#countWindow").show();
            $("#page").html("");
        }
	</script>
</head>
<body class="page-body">
<input type="hidden" id="menuId">
<div class="page-container">
	<div class="sidebar-menu toggle-others fixed">
		<div class="sidebar-menu-inner">
			<header class="logo-env" style=" height: 75px;">
				<div class="logo">
					<h1 style="color: white;margin-top: 2px;" class="logo-expanded" >GM管理系统</h1>
					<h1 style="color: white;margin-top: 2px;" class="logo-collapsed">GM</h1>
				</div>
				<div class="mobile-menu-toggle visible-xs">
					<a href="#" data-toggle="mobile-menu">
						<i class="fa-bars"></i>
					</a>
				</div>
			</header>
			<ul id="main-menu" class="main-menu" style="margin-top: 5px;">
				${menu}
			</ul>
		</div>
	</div>
	<div class="main-content">
		<nav class="navbar user-info-navbar" role="navigation">
			<ul class="user-info-menu left-links list-inline list-unstyled">
				<li class="hidden-sm hidden-xs">
					<a href="#" data-toggle="sidebar">
						<i class="fa-bars"></i>
					</a>
				</li>
				<li class="dropdown user-profile">
					<a data-toggle="dropdown">
						TIME:
						<span id="serverTime"></span>
					</a>
				</li>
			</ul>
			<ul class="user-info-menu right-links list-inline list-unstyled">
				<li class="search-form">
					<form method="get" action="extra-search.html">
						<input type="text" name="s" class="form-control search-field" placeholder="Type to search..." />
						<button type="submit" class="btn btn-link">
							<i class="linecons-search"></i>
						</button>
					</form>
				</li>
				<li class="dropdown user-profile">
					<a data-toggle="dropdown">
						GM:
						<span>
							${GM.name}
						</span>
					</a>
				</li>
			</ul>
		</nav>
		<div class="row" id="countWindow">
			<div class="col-sm-12">
				<div class="panel panel-body">
					<div class="row">
						<table class="table table-bordered table-striped table-condensed table-hover">
							<thead>
								<tr>
									<th>日期</th>
									<th>总角色</th>
									<th>新角色</th>
									<th>首次注册</th>
									<th>登录</th>
									<th>总充值</th>
									<th>日充值</th>
									<th>日充值人数</th>
									<th>总付费人数</th>
									<th>ARPPU</th>
									<th>ARPU</th>
									<th>付费渗透</th>
									<th>付费转化</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach items="${comprehensive}" var="list">
									<tr>
										<td>${list[0]}</td>
										<td>${list[1]}</td>
										<td>${list[11]}</td>
										<td>${list[2]}</td>
										<td>${list[3]}</td>
										<td>${list[4]}</td>
										<td>${list[5]}</td>
										<td>${list[12]}</td>
										<td>${list[6]}</td>
										<td>${list[7]}</td>
										<td>${list[8]}</td>
										<td>${list[9]}</td>
										<td>${list[10]}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-sm-12">
				<div class="panel panel-body">
					<div class="row">
						<table class="table table-bordered table-striped table-condensed table-hover">
							<thead>
								<tr>
									<th>日期</th>
									<th>新增人数</th>
									<th>次留</th>
									<th>三留</th>
									<th>四留</th>
									<th>五留</th>
									<th>六留</th>
									<th>七留</th>
								</tr>
							</thead>

							<tbody>
								<c:forEach items="${rate}" var="list">
									<tr>
										<td>${list[0]}</td>
										<td>${list[1]}</td>
										<td>${list[2]}</td>
										<td>${list[3]}</td>
										<td>${list[4]}</td>
										<td>${list[5]}</td>
										<td>${list[6]}</td>
										<td>${list[7]}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<div class="col-sm-4">
				<div class="panel panel-body">
					<div class="row" style="max-height: 10%; overflow:auto;">
						<table class="table table-bordered table-striped table-condensed table-hover">
							<thead>
								<tr>
									<th>服务器</th>
									<th>今日新增</th>
									<th>今日活跃</th>
									<th>今日充值</th>
									<th>今日充值人数</th>
									<th>最近在线</th>
								</tr>
							</thead>
							<tbody id="nowDataId">
								<c:forEach items="${nowData}" var="list">
									<tr>
										<td>${list[0]}</td>
										<td>${list[1]}</td>
										<td>${list[2]}</td>
										<td>${list[5]}</td>
										<td>${list[4]}</td>
										<td>${list[3]}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="row" id="page"></div>

	</div>
</div>
<div class="page-loading-overlay">
	<div class="loader-2"></div>
</div>

<div>
	<div class="modal fade" id="delMake">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">删除提示</h4>
				</div>
				<div class="modal-body" id="delText"></div>
				<input type="hidden" id="delId_delDiv">
				<div class="modal-footer">
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-red" data-dismiss="modal" id="detSubBtn">确定</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="msgMaket">
		<div class="modal-dialog" id="msgMaket2">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">获取显示</h4>
				</div>
				<div class="modal-body" id="msgText"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="reMark">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">修改内容</h4>
				</div>
				<div class="modal-body" id="setMarkDiv"></div>
				<input type="hidden" id="reId_reDiv">
				<div class="modal-footer">
					<button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-info" data-dismiss="modal" id="reSubBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
</div>

<script src="${GmBase}/assets/js/datatables/js/jquery.dataTables.min.js"></script>
<script src="${GmBase}/assets/js/datatables/dataTables.bootstrap.js"></script>
<script src="${GmBase}/assets/js/datatables/yadcf/jquery.dataTables.yadcf.js"></script>
<script src="${GmBase}/assets/js/datatables/tabletools/dataTables.tableTools.min.js"></script>
<script src="${GmBase}/assets/js/tagsinput/bootstrap-tagsinput.min.js"></script>
<script src="${GmBase}/assets/js/inputmask/jquery.inputmask.bundle.js"></script>
<script src="${GmBase}/assets/js/select2/select2.min.js"></script>
<script src="${GmBase}/assets/js/multiselect/js/jquery.multi-select.js"></script>
<script src="${GmBase}/assets/js/jquery-ui/jquery-ui.min.js"></script>

<script src="${GmBase}/assets/js/bootstrap.min.js"></script>
<script src="${GmBase}/assets/js/TweenMax.min.js"></script>
<script src="${GmBase}/assets/js/resizeable.js"></script>
<script src="${GmBase}/assets/js/joinable.js"></script>
<script src="${GmBase}/assets/js/xenon-api.js"></script>
<script src="${GmBase}/assets/js/xenon-toggles.js"></script>
<script src="${GmBase}/assets/js/devexpress-web-14.1/js/globalize.min.js"></script>
<script src="${GmBase}/assets/js/devexpress-web-14.1/js/dx.chartjs.js"></script>
<script src="${GmBase}/assets/js/xenon-custom.js"></script>
</body>
</html>