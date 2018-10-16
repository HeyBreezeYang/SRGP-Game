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
            <h3 class="panel-title">LTV查询</h3>
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

                    <label>服务器ID：</label>
                    <select id="serverId" name="serverId">

                        <c:forEach items="${allServer}" var="serverObj">
                            <option value="all">全服</option>
                            <option value="${serverObj.serverID}">${serverObj.serverName}</option>
                        </c:forEach>

                    </select>
                    <label>渠道：</label>
                    <select id="channel" name="channel">

                        <c:forEach items="${allChannel}" var="channelObj">
                            <option value="${channelObj.name}">${channelObj.dsp}</option>
                        </c:forEach>

                    </select>

                    <label>LTV类型：</label>
                    <select id="ltvType" name="ltvType">

                        <option value="1">LTV比值</option>
                        <option value="2">LTV金额</option>

                    </select>
                    <label>注册开始时间：</label>
                    <input type="text" id="rstartDate" name="startDate" autocomplete="off">


                    <label>注册结束时间：</label>
                    <input type="text" id="rendDate" name="endDate" autocomplete="off">

                    <label>查询开始时间：</label>
                    <input type="text" id="qstartDate" name="startDate" autocomplete="off">


                    <label>查询结束时间：</label>
                    <input type="text" id="qendDate" name="endDate" autocomplete="off">

                    <button type="button" onclick="queryRTM();">查询</button>
                </form>

            </div>
</div>
<script src="${GmBase}/assets/js/echarts/echarts.js"></script>
<script type="text/javascript">

    laydate.render({
        elem: '#rstartDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#rendDate',
        showBottom: false

    });
    laydate.render({
        elem: '#qstartDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#qendDate',
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
        var ltvType = $("#ltvType").val();
        var rstartdate = $("#rstartDate").val();
        var renddate = $("#rendDate").val();
        var qstartdate = $("#qstartDate").val();
        var qenddate = $("#qendDate").val();

        jQuery.ajax({
            url: "LTV/queryLTV",
            type:"POST",
            async: false,
            data:{"sid":serverid,"channel":channel,"LTVType":ltvType,"rstartDate":rstartdate,"rendDate":renddate,"qstartDate":qstartdate,"qendDate":qenddate},
            success: function(response)
            {
                jQuery('#rtmDiv').html(response)
            }
        });
    }
</script>