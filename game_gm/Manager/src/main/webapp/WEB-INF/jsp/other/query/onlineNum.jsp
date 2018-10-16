<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    function queryOnline(){
        $("#onlineForm").ajaxSubmit({
            dataType:'json',
            type:'post',
            url:'analysis/dataForOnline',
            success:function(data){
                if(data['msg']==null){
                    setData(data['maxSource'],data['dataField'],"bar-max","时刻最多在线人数");
                    setData(data['minSource'],data['dataField'],"bar-min","时刻最少在线人数");
                }else {
                    console.log(data['msg']);
                }
            }
        });
    }
    function setData(data,field,id,title){
        $("#"+id).dxChart({
            dataSource: data,
            commonSeriesSettings: {
                argumentField: "hour"
            },
            series: field,
            argumentAxis:{
                grid:{
                    visible: true
                }
            },
            tooltip:{
                enabled: true
            },
            title: title,
            legend: {
                verticalAlignment: "bottom",
                horizontalAlignment: "right"
            },
            commonPaneSettings: {
                border:{
                    visible: true,
                    right: true
                }
            }
        });
    }
</script>
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
            <h3 class="panel-title">查询条件</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <form class="form-horizontal"  id="onlineForm" role="form">
                <div class="form-group">
                    <div class="col-sm-2">
                        <label class="control-label">服务器</label>
                        <select class="form-control" name="serverId" >
                            <c:forEach items="${serverConfig}" var="config">
                                <option value="${config.sid}">${config.dsp}</option>
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
                        <button class="btn btn-info btn-block" onclick="queryOnline();" type="button">获取结果</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">在线MAX</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <div id="bar-max" style="height: 400px; width: 100%;"></div>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">在线MIN</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <div id="bar-min" style="height: 400px; width: 100%;"></div>
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


