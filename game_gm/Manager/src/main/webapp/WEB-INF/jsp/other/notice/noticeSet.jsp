<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<div  id="noticeDiv">
		<div class="breadcrumb-env">
			<ol class="breadcrumb bc-1">
				<li>
					<a href="#" onclick="moveMain();">
						<i class="fa-home"></i>回到主页
					</a>
				</li>
			</ol>
		</div>
		<div class="noticePageVid">
		<c:forEach items="${noticeSets}" var="noticeSet">
			<div class="col-md-12" >
				${noticeSet.setting}:
				<input type="hidden" id="noticeSetId" name="noticeSetId" value="${noticeSet.id}">
				<c:if test="${noticeSet.value == '0'}">
					<input type="checkbox" class="cbr cbr-info" id="noticeSetOnOff" name="noticeSetOnOff" value="0" >
				</c:if>
				<c:if test="${noticeSet.value == '1'}">
					<input type="checkbox" class="cbr cbr-info" id="noticeSetOnOff" name="noticeSetOnOff" value="1" checked>
				</c:if>
				<div>
					<a href="#" class="btn btn-danger btn-sm btn-icon icon-left" onclick='upNoticeSet("${noticeSet.id}","${noticeSet.value}");'>修改</a>
				</div>
			</div>
		</c:forEach>
		</div>

		<div class="form-group">
			<label class="col-sm-1 control-label" for="ImageDiv">公告图片</label>
			<div class="col-sm-3" id="ImageDiv">
				<input type="text" id="noticeImage"  name="noticeImage" class="form-control"  />
				<button type="button" onclick="uploadImge()">更新</button>
			</div>
		</div>
	</div>
<script type="text/javascript">
	function upNoticeSet(id,noticeSetValue) {

        jQuery.ajax({
            url: "notice/updateNoticeSet",
            async: false,
            data:{"noticeSetId":id,"noticeSetValue":noticeSetValue},
            success: function(res)
            {
                alert(res);
                jQuery.ajax({
                    url: "notice/noticeSet",
                    async: false,
                    success: function(res)
                    {
                		jQuery('#noticeDiv').html(res);
                    }
                });
            }
        });
    }
    
    function uploadImge() {
	    var imgeId = $("#noticeImage").val();
        jQuery.ajax({
            url: "notice/updateNoticeImage",
            async: false,
            data:{"imageId":imgeId},
            success: function(res)
            {
                alert(res);
                jQuery.ajax({
                    url: "notice/noticeSet",
                    async: false,
                    success: function(res)
                    {
                        jQuery('#noticeDiv').html(res);
                    }
                });
            }
        });
    }


</script>
		