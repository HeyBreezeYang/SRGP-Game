<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="rtmDiv">
<div class="breadcrumb-env">
    <ol class="breadcrumb bc-1">
        <li>
            <a href="#" onclick="moveMain();">
                <i class="fa-home"></i>回到主页
            </a>
        </li>
    </ol>
</div>
<div style="width: 100%;" >
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">实时在线监控</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">–</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <div class="form-group">
                <form role="form" class="form-horizontal" id="query-2">
                    <input type="hidden" name="other" value="27">

                    <label>服务器ID：</label>
                    <select id="serverId" name="serverId">

                        <c:forEach items="${allServer}" var="serverObj">
                            <option value="all">汇总</option>
                            <option value="${serverObj.serverid}">${serverObj.servername}</option>
                        </c:forEach>

                    </select>

                    <label>开始时间：</label>
                    <input type="text" id="startDate" name="startDate" autocomplete="off">


                    <label>结束时间：</label>
                    <input type="text" id="endDate" name="endDate" autocomplete="off">





                    <button type="button" onclick="queryRTM();">查询</button>
                </form>
            </div>
            <table class="table table-striped table-bordered" id="example-2">
                <thead>
                <tr class="replace-inputs">
                    <th abbr="logTime" class="table-2">日期</th>

                    <th abbr="logTime" class="table-2">服务器ID</th>

                    <th abbr="createTeam" class="table-2">总角色数</th>

                    <th abbr="createPlayer" class="table-2">新增注册数</th>

                    <th abbr="actor" class="table-2">日活跃角色数</th>

                    <th abbr="actor" class="table-2">日充值人数</th>

                    <th abbr="actor" class="table-2">日充值金额</th>

                    <th abbr="actor" class="table-2">日活跃arpu</th>

                    <th abbr="actor" class="table-2">日付费arpu</th>


                </tr>
                </thead>
                <tbody id="tb-2">
                <c:forEach items="${allData['listDay']}" var="day" >
                    <tr>
                        <td>${day}</td>
                        <td>${allData[day]['serverId']}</td>
                        <td>${allData[day]['roleCount']}</td>
                        <td>${allData[day]['newRoleCount']}</td>
                        <td>${allData[day]['loginCount']}</td>
                        <td>${allData[day]['payNum']}</td>
                        <td>${allData[day]['payCountPrice']}</td>
                        <td>${allData[day]['loginArpu']}%</td>
                        <td>${allData[day]['payArpu']}%</td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
       <%-- <div id="onlineRole" style="height:400px">></div>--%>
    </div>
</div>
</div>
<script src="${GmBase}/assets/js/echarts/echarts.js"></script>
<script type="text/javascript">

    laydate.render({
        elem: '#startDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#endDate',
        showBottom: false

    });

    jQuery(document).ready(function($)
    {
        //加载多选框
        $("#sid").select2({
            placeholder: '选择需要发送的服务器',
            allowClear: true
        }).on('select2-open', function()
        {
            // Adding Custom Scrollbar
            $(this).data('select2').results.addClass('overflow-hidden').perfectScrollbar();
        });
    });

    function queryRTM() {
        var serverid = $("#serverId").val();
        var startdate = $("#startDate").val();
        var enddate = $("#endDate").val();

        jQuery.ajax({
            url: "realTimeMonitoring/queryRealTimeMonitoring",
            type:"POST",
            async: false,
            data:{"sid":serverid,"startDate":startdate,"endDate":enddate},
            success: function(response)
            {
                jQuery('#rtmDiv').html(response);
            }
        });
    }
</script>