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
            <h3 class="panel-title">留存率查询</h3>
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
                            <option value="${serverObj.serverID}">${serverObj.serverName}</option>
                        </c:forEach>

                    </select>

                    <label>渠道：</label>
                    <select id="channel" name="channel">

                        <c:forEach items="${allChannel}" var="channel">
                            <option value="${channel.name}">${channel.dsp}</option>
                        </c:forEach>

                    </select>

                    <label>开始时间：</label>
                    <input type="text" id="startDate" name="startDate" autocomplete="off">


                    <label>结束时间：</label>
                    <input type="text" id="endDate" name="endDate" autocomplete="off">





                    <button type="button" onclick="queryRTM();">查询</button>
                </form>

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
        var channel = $("#channel").val();
        var startdate = $("#startDate").val();
        var enddate = $("#endDate").val();

        jQuery.ajax({
            url: "retentionRates/queryRetentionRates",
            type:"POST",
            async: false,
            data:{"sid":serverid,"channel":channel,"startDate":startdate,"endDate":enddate},
            success: function(response)
            {
                jQuery('#rtmDiv').html(response);
            }
        });
    }
</script>