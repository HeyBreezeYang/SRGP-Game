<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
    var t;
    jQuery(document).ready(function($)
    {
       t= $("#ltvTable").DataTable({
            aLengthMenu: [
                [10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
            ]
        });
    });
    var chartMsg;
    function queryLTV(){
        t.rows('#ltvMsg tr').remove().draw();
        $("#ltvForm").ajaxSubmit({
            dataType:'json',
            type:'post',
            url:'analysis/ltvMsg',
            success:function(data){
                chartMsg=data;
                $.each(data,function(i,dcem){
                    for(i=0;i<dcem.length;i++){
                        t.row.add([
                            dcem[i]['cTime'],
                            dcem[i]['pTime'],
                            dcem[i]['cNum'],
                            dcem[i]['pNum'],
                            dcem[i]['pay'],
                            "<a href='#ltvChart' class='btn btn-blue btn-sm btn-icon icon-left' onclick='setDataChart(\""+dcem[i]['cTime']+"\",\""+dcem[i]['cNum']+"\");'>图详</a>"
                        ]).draw();
                    }
                });
            }
        });
    }
    function setDataChart(dataKey,people){
        $("#bar-ltv").dxChart({
            dataSource: chartMsg[dataKey],
            commonSeriesSettings:{
                argumentField: "pTime"
            },
            panes: [{
                name: "topPane"
            }, {
                name: "bottomPane"
            }],
            defaultPane: "bottomPane",
            series: [{
                pane: "topPane",
                valueField: "pNum",
                name: "充值人数",
                label: {
                    visible: true,
                    customizeText: function (){
                        return this.valueText ;
                    }
                },
                color: "#00b19d"
            }, {
                type: "bar",
                valueField: "pay",
                name: "充值金额",
                label: {
                    visible: true,
                    customizeText: function (){
                        return this.valueText ;
                    }
                },
                color: "#d5080f"
            }
            ],
            valueAxis: [{
                pane: "bottomPane",
                grid: {
                    visible: true
                },
                title: {
                    text: "每日充值金额, RMB"
                }
            }, {
                pane: "topPane",
                min: 0,
                grid: {
                    visible: true
                },
                title: {
                    text: "每日充值玩家, 人"
                }
            }],
            legend: {
                verticalAlignment: "bottom",
                horizontalAlignment: "center"
            },
            title: {
                text: dataKey+" 中新增 "+people+" 人的LTV 情况"
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
            <form class="form-horizontal"  id="ltvForm" role="form">
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
                        <label class="control-label">新增开始时间</label>
                        <input type="text" class="form-control datepicker" name="cst" data-format="yyyy-mm-dd">
                    </div>
                    <div class="col-sm-1">
                        <label class="control-label">新增截止时间</label>
                        <input type="text" class="form-control datepicker" name="ced" data-format="yyyy-mm-dd">
                    </div>
                    <div class="col-sm-1">
                        <label class="control-label">充值天数</label>
                        <input type="text" class="form-control" name="pst" title="范围1-30">
                    </div>
                    <div class="col-sm-1">
                        <label class="control-label">&nbsp;</label>
                        <input type="text" class="form-control"  name="ped" title="范围1-30">
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <div class="col-sm-4">
                        <button class="btn btn-info btn-block" onclick="queryLTV();" type="button">获取结果</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">LTV数据</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <table id="ltvTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>创建时间</th>
                        <th>充值天数</th>
                        <th>创建人数</th>
                        <th>充值人数</th>
                        <th>充值金额</th>
                        <th width="13%">操作</th>
                    </tr>
                </thead>
                <tbody id="ltvMsg">

                </tbody>
            </table>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">图表详情</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body" id="ltvChart">
            <div id="bar-ltv" style="height: 450px; width: 100%;"></div>
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


