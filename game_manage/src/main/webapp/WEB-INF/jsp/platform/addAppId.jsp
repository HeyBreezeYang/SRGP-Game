<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>设置APP-ID</h1>
<hr>
<div class="row-fluid">
    <div class="span12">
        <div class="widget-box">
            <div class="widget-title"> <span class="icon"> <i class="icon-info-sign"></i> </span>
                <h5>设置ID信息</h5>
            </div>
            <div class="widget-content nopadding">
                <form class="form-horizontal" method="post" action="platform/sub"  id="appForm" novalidate="novalidate">
                    <div class="control-group">
                        <label class="control-label">游戏版本</label>
                        <div class="controls">
                            <select name="versionID">
                                <c:if test="${! empty versionList }">
                                    <c:forEach items="${versionList}" var="vs">
                                        <option value="${vs.id}">${vs.versionName}</option>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty versionList }">
                                    <option value="-1">无可使用版本</option>
                                </c:if>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">APP-ID</label>
                        <div class="controls">
                            <input type="text" name="appID" id="APP"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">OPEN-ID</label>
                        <div class="controls">
                            <input type="text" name="openID" id="OPEN"/>
                        </div>
                    </div>
                    <div class="form-actions">
                        <input type="button" onclick="subAddAppID();" value="提交" class="btn btn-success">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="resMsg"></div>

<script type="text/javascript">
    function subAddAppID() {
        $("#appForm").ajaxSubmit({
            dataType:'json',
            success:function (data) {
               var res =JSON.parse(data);
               if(res['res']===1){
                   forwardPage('platform/set',6);
               }else{
                   $("#resFail").html("ERROR:"+res['msg']);
                   $("#resMsg").html($("#resB").html());
               }
            }
        });
    }

</script>