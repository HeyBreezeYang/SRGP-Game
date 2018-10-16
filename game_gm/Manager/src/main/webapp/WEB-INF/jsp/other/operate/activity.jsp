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
            <h3 class="panel-title">活动列表</h3>
            <div class="panel-options">
                <a href="#" data-toggle="panel">
                    <span class="collapse-icon">&ndash;</span>
                    <span class="expand-icon">+</span>
                </a>
            </div>
        </div>
        <div class="panel-body">
            <button class="btn btn-red" onclick="batchDelete();">批量删除</button>
            <form action="" id="subBatchDelActivity">
                <input id="activityAidAndSid" type="hidden" />
            </form>
            <table id="activityTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th class="no-sorting">
                        <input type="checkbox" class="cbr">
                    </th>
                    <th><select id="qsid" name="qsid" onchange="queryTable('qsid');">
                        <option value="all" >服务器ID</option>
                        <c:forEach items="${allServer}" var="serverObj">
                            <option value="${serverObj.serverID}">${serverObj.serverName}</option>
                        </c:forEach>
                    </select> </th>
                    <th>活动标题</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th><select id="qstatus" name="qstatus" onchange="queryTable('qstatus');">
                        <option value="all" >活动状态</option>
                        <option value="0" >未开始</option>
                        <option value="1" >进行中</option>
                        <option value="2" >已过期</option>
                    </select> </th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="activityMsg">
                <c:if test="${! empty allActivity}">
                    <c:forEach items="${allActivity}" var="theActivity">
                            <tr>
                                <td>
                                    <input type="checkbox" class="cbr aid_sid" value="${theActivity['aid']}-${theActivity['sid']}">
                                </td>
                                <td>${theActivity['sid']}</td>
                                <td>${theActivity['title']}</td>
                                <td>${theActivity['start']}</td>
                                <td>${theActivity['end']}</td>
                                <td>${theActivity['status']}</td>
                                <td>
                                    <%--<a href="#" class="btn btn-info btn-sm btn-icon icon-left" onclick="queryActivity('${activityData.content}','${activityData.conditionMsg}','${activityData.behvsMsg}')">详情</a>--%>
                                    <a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delActivity('${theActivity['aid']}','${theActivity['sid']}');">删除</a>
                                </td>
                            </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>


<script type="text/javascript">
    jQuery(document).ready(function($)
    {
        $("#activityTable").dataTable({
            aLengthMenu: [
                [10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
            ]
        });

        // Replace checkboxes when they appear
        var $state = $("#activityTable thead input[type='checkbox']");

        $("#activityTable").on('draw.dt', function()
        {
            cbr_replace();

            $state.trigger('change');
        });

        // Script to select all checkboxes
        $state.on('change', function(ev)
        {
            var $chcks = $("#activityTable tbody input[type='checkbox']");

            if($state.is(':checked'))
            {
                $chcks.prop('checked', true).trigger('change');
            }
            else
            {
                $chcks.prop('checked', false).trigger('change');
            }
        });
    });
    
    function queryTable(idname) {
        setTimeout(function(){//因为是即时查询，需要用setTimeout进行延迟，让值写入到input内，再读取
            var storeId = document.getElementById('activityTable');//获取table的id标识
            var rowsLength = storeId.rows.length;//表格总共有多少行
            var key =  $("#"+idname+"").val();;//获取输入框的值

            if(idname == 'qsid'){

                var searchCol = 1;//要搜索的哪一列，这里是第一列，从0开始数起
            }else {
                var searchCol = 5;
            }

            for(var i=1;i<rowsLength;i++){//按表的行数进行循环，本例第一行是标题，所以i=1，从第二行开始筛选（从0数起）
                var searchText = storeId.rows[i].cells[searchCol].innerHTML;//取得table行，列的值

                if(searchText.match(key)){//用match函数进行筛选，如果input的值，即变量 key的值为空，返回的是ture，
                    storeId.rows[i].style.display='';//显示行操作，
                }else if (key == 'all'){
                    storeId.rows[i].style.display='';//显示行操作，
                }else{
                    storeId.rows[i].style.display='none';//隐藏行操作
                }
            }
        },200);//200为延时时间
    }
    function batchDelete() {
        var as =$(".aid_sid");
        var vl="";
        if(as[0].checked){
            vl+=as[0].value;
        }
        for (var i=1;i<as.length;i++){
            if(as[i].checked){
                vl=vl+","+as[i].value;
            }
        }
        $("#activityAidAndSid").val(vl);
        $("#delText").html("确定删除选中的活动配置？");
        $("#detSubBtn").attr("onclick","subBatchDel();");
        jQuery('#delMake').modal('show',{backdrop: 'fade'});
    }
    function subBatchDel() {
        $.ajax({
            url:'activity/batchDelActivity?msg='+$("#activityAidAndSid").val(),
            dataType:'json',
            type: 'post',
            success:function(data){
                forwardPage('activity/getAllActivity',${thisMenuID});
            }
        });
    }
//    function queryActivity(content,condition,beh){
//        $("#msgMaket2").css("width:60%");
//        $("#msgText").html("<p>内容</p><textarea rows='5' cols='70' readonly='readonly' style='resize: none;'>"+content+"</textarea>"+"<p>条件</p><textarea rows='5' cols='70' readonly='readonly' style='resize: none;'>"+condition+"</textarea>"+"<p>奖励</p><textarea rows='5' cols='70' readonly='readonly' style='resize: none;'>"+beh+"</textarea>");
//        jQuery('#msgMaket').modal('show', {backdrop: 'static'});
//    }
    function delActivity(id,sid){
        $("#delText").html("确定删除该活动配置？");
        $("#detSubBtn").attr("onclick","subDelActivity('"+id+"','"+sid+"');");
        jQuery('#delMake').modal('show',{backdrop: 'fade'});
    }
    function subDelActivity(id,sid){
        $.ajax({
            url:'activity/deleteActivity?id='+id+"&sid="+sid,
            dataType:'json',
            type: 'post',
            success:function(data){
                forwardPage('activity/getAllActivity',${thisMenuID});
            }
        });
    }
</script>
