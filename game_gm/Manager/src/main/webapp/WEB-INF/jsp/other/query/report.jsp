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
            <h3 class="panel-title">综合查询</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <form action="report/data" role="form" class="form-horizontal" method="post" id="sqlQuery"  target="_blank">
                <div class="form-group">
                    <div class="col-sm-4">
                        <select class="form-control" name="qid" id="queryType">
                            <option value="1">综合统计数据</option>
                            <option value="2">留存率</option>
                            <option value="4">LTV数据(比值)</option>
                            <option value="7">LTV数据(金额)</option>
                        </select>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">服务器</label>
                        <select class="form-control" name="serverId" >
                            <option value="all">全部</option>
                            <option value="all_count">全服统计</option>
                            <c:forEach items="${serverConfig}" var="config">
                                <option value="${config.serverID}">${config.serverName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <label class="control-label">渠道</label>
                        <select class="form-control" name="other">
                            <option value="all">全部</option>
                            <option value="all_count">全渠道统计</option>
                            <c:forEach items="${channl}" var="config">
                                <option value="${config.name}">${config.dsp}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-1">
                        <label class="control-label">开始</label>
                        <input type="text" class="form-control datepicker" name="start" data-format="yyyy-mm-dd">
                    </div>
                    <div class="col-sm-1">
                        <label class="control-label">截止</label>
                        <input type="text" class="form-control datepicker" name="end" data-format="yyyy-mm-dd">
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <div class="col-sm-4">
                        <button class="btn btn-info btn-block" type="submit" >获取查询结果</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<link rel="stylesheet" href="${GmBase}/assets/js/daterangepicker/daterangepicker-bs3.css">
<script src="${GmBase}/assets/js/daterangepicker/daterangepicker.js"></script>
<script src="${GmBase}/assets/js/datepicker/bootstrap-datepicker.js"></script>
<script src="${GmBase}/assets/js/timepicker/bootstrap-timepicker.min.js"></script>
<script src="${GmBase}/assets/js/colorpicker/bootstrap-colorpicker.min.js"></script>
<script src="${GmBase}/assets/js/jquery-ui/jquery-ui.min.js"></script>
<script src="${GmBase}/assets/js/xenon-custom.js"></script>

<script type="text/javascript">

</script>
