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
            <h3 class="panel-title">兑换码列表</h3>
        </div>
        <div class="panel-body">
            <table id="codTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
                <thead>
                    <tr>
                        <th>兑换码</th>
                        <th>服务器</th>
                        <th>渠道</th>
                        <th>物品设置</th>
                        <th>结束时间</th>
                        <th>使用类型</th>
                        <th>兑换码类型</th>
                        <th>数量</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${! empty AttrList}">
                        <c:forEach items="${AttrList}" var="AttrData">
                            <tr>
                                <td>${AttrData.dsp}</td>
                                <td>${AttrData.sid}</td>
                                <td>${AttrData.channel}</td>
                                <td>
                                    <textarea style="resize: none;height: 100%;width: 100%;border: none;" readonly="readonly">${AttrData.func}</textarea>
                                </td>
                                <td>${AttrData.endTime}</td>
                                <td>${AttrData.type}</td>
                                <td>${AttrData.specialType}</td>
                                <td>${AttrData.num}</td>
                                <td>
                                    <form action="code/exportCode" id="exportCodeForm" method="post">
                                        <input type="hidden" id="exportCodeId" name="id" value="${AttrData.id}">
                                        <%--<a href="#" class="btn btn-info btn-sm btn-icon icon-leaf" onclick="submit">导出</a>--%>
                                        <button type="submit" class="btn btn-info btn-sm btn-icon icon-leaf" >导出</button>
                                    </form>
                                    <a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick="delCode(${AttrData.id});">删除</a>
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
        $("#codTable").dataTable({
            aLengthMenu: [
                [10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]
            ]
        });
    });

    function subDelCode(){
        var tbId=$("#delId_delDiv").val();
        $.ajax({
            url:'code/delCode',
            data:{"id":tbId},
            dataType:'text',
            type: 'post',
            success:function(data){
                forwardPage('code/queryCode','${thisMenuID}');
            }
        });
    }
    function delCode(deltbId){
        $("#delId_delDiv").val(deltbId);
        $("#delText").html("确定删除该批兑换码？");
        $("#detSubBtn").attr("onclick","subDelCode();");
        jQuery('#delMake').modal('show',{backdrop: 'fade'});
    }

   /* function exportCodeAjax() {
        var codeId=$("#exportCodeId").val();
        $.ajax({
            url:'code/exportCode',
            data:{"id":codeId},
            dataType:'text',
            type: 'post',
            success:function(data){
                forwardPage('code/queryCode','${thisMenuID}');
            }
        });
    }

    function exportCode(deltbId){
        $("#exportCodeId").val(deltbId);
        $("#delText").html("确定导出该批兑换码？");
        $("#detSubBtn").attr("onclick","exportCodeAjax();");
        jQuery('#delMake').modal('show',{backdrop: 'fade'});
    }*/
</script>