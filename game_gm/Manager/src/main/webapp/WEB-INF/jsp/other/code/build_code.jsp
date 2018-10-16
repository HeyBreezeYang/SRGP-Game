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
            <h3 class="panel-title">兑换码编辑</h3>
        </div>
        <div class="panel-body">
            <form action="code/addCode" role="form" class="form-horizontal" method="post" id="prizeForm">
                <div class="form-group">
                    <label class="col-sm-1 control-label">使用类型</label>
                    <div class="col-sm-2">
                        <select class="form-control" name="type">
                            <option value="1">一次使用</option>
                            <option value="-1">重复使用</option>
                            <option value="-2">单服使用</option>
                        </select>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label" >兑换码类型</label>
                    <div class="col-sm-3">
                        <div class="form-block">
                            <label>
                                <input type="radio" name="specialType" class="cbr cbr-gray" value="0" checked="checked" onclick="setBindingCode();">
                                普通类
                            </label>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <div class="form-block">
                            <label>
                                <input type="radio" name="specialType" class="cbr cbr-danger" value="1" onclick="setBindingCode();">
                                同批次类
                            </label>
                        </div>
                    </div>
                    <div class="col-sm-3">
                        <div class="form-block">
                            <label>
                                <input type="radio" name="specialType" class="cbr cbr-orange" id="bdc" value="2" onclick="setBindingCode();">
                                批次绑定
                            </label>
                        </div>
                    </div>
                </div>
                <div class="form-group" id="bindingDIV" style="display: none;">
                    <label class="col-sm-1 control-label" for="select_bId">批次</label>
                    <div class="col-sm-3">
                        <select class="form-control" multiple="multiple" id="select_bId" name="bindingId">
                            <c:forEach items="${bdCode}" var="code">
                                <option value="${code.id}">${code.dsp}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label" for="dsp">兑换码名称</label>
                    <div class="col-sm-2 " >
                        <input type="text" name="dsp" class="form-control" id="dsp" />
                    </div>
                    <label class="col-sm-1 control-label" >截止日期</label>
                    <div class="col-sm-3 " >
                        <div class="date-and-time">
                            <input type="text" class="form-control datepicker" name="endTimeForConfig" data-format="yyyy-mm-dd">
                            <input type="text" class="form-control timepicker" name="endTimeForConfig" data-template="dropdown" data-show-seconds="true" data-default-time="11:25 AM"
                                   data-show-meridian="true" data-minute-step="1" data-second-step="1" />
                        </div>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">设置</label>
                    <div class="col-sm-2">
                        <select class="form-control" onchange="changeCodeType(this);">
                            <option value="1" selected="selected">随机生成</option>
                            <option value="-1">设置</option>
                        </select>
                    </div>
                </div>
                <div class="form-group" id="numPamrae">
                    <label class="col-sm-1 control-label" for="prizeNum">数量</label>
                    <div class="col-sm-2">
                        <input type="text" name="num" class="form-control" value="0" id="prizeNum"/>
                    </div>
                </div>
                <div class="form-group" id="codePamrae" style="display: none;">
                    <label class="col-sm-1 control-label" for="prizeCode">兑换码</label>
                    <div class="col-sm-6">
                        <input type="text" name="prizeCode" class="form-control myParmas" id="prizeCode"/>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">邮件标题</label>
                    <div class="col-sm-4">
                        <input type="text" name="mailTitle" class="form-control"/>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">邮件内容</label>
                    <div class="col-sm-6">
                        <textarea class="form-control" rows="8" name="mailText" style="resize: none;" ></textarea>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label">邮件配置</label>
                    <div class="col-sm-6">
                        <textarea class="form-control" rows="8" name="func" style="resize: none;" ></textarea>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label" for="select_sId">服务器</label>
                    <div class="col-sm-3">
                        <select class="form-control" multiple="multiple" id="select_sId" name="sid">
                                <c:forEach items="${serverConfig}" var="config">
                                    <option value="${config.serverID}">${config.serverName}</option>
                                </c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <label>
                            <input type="checkbox" class="cbr cbr-info" name="isAllServer" value="all">
                            是否全服
                        </label>
                    </div>
                    <label class="col-sm-1 control-label" for="select_plfId">渠道</label>
                    <div class="col-sm-3">
                        <select class="form-control" multiple="multiple" id="select_plfId" name="channel">
                            <c:forEach items="${channl}" var="config">
                                <option value="${config.name}">${config.dsp}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <label>
                            <input type="checkbox" class="cbr cbr-seccess" name="isAllChannel" value="all">
                            是否全渠道
                        </label>
                    </div>
                </div>
                <div class="form-group-separator"></div>
                <div class="form-group">
                    <label class="col-sm-1 control-label"></label>
                    <div class="col-sm-6">
                        <button class="btn btn-purple btn-block" onclick="upPrize();" type="button">确定编辑</button>
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
    function setBindingCode(){
        var k =document.getElementById("bdc");
        if(k.checked){
            $("#bindingDIV").show(170);
        }else{
            $("#bindingDIV").hide();
        }
    }


    function changeCodeType(vr){
        var j=$(vr).val();
        if(j==1){
            $("#numPamrae").show();
            $("#codePamrae").hide();
            $("#prizeCode").tagsinput('removeAll');
        }else{
            $("#codePamrae").show();
            $("#numPamrae").hide();
            $("#prizeNum").val(0);
        }
    }

    function upPrize(){
        $("#setMarkDiv").html("确定兑换配置内容？");
        $("#reSubBtn").attr("onclick","subPrize();");
        jQuery('#reMark').modal('show',{backdrop: 'fade'});
    }
    function subPrize(){
        $("#prizeForm").submit();
    }

    // Just for demo purpose
    function shuffleArray(array){
        for (var i = array.length - 1; i > 0; i--)
        {
            var j = Math.floor(Math.random() * (i + 1));
            var temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    jQuery(document).ready(function($)
    {
        var i = -1, colors = ['primary', 'secondary', 'red', 'blue', 'warning', 'success', 'purple'];
        colors = shuffleArray(colors);
        $(".myParmas").tagsinput({
            tagClass: function()
            {
                i++;
                return "label label-" + colors[i%colors.length];
            }
        });
        $("#select_sId").multiSelect({
            afterInit: function()
            {
                // Add alternative scrollbar to list
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar();
            },
            afterSelect: function()
            {
                // Update scrollbar size
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar('update');
            }
        });
        $("#select_bId").multiSelect({
            afterInit: function()
            {
                // Add alternative scrollbar to list
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar();
            },
            afterSelect: function()
            {
                // Update scrollbar size
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar('update');
            }
        });
        $("#select_plfId").multiSelect({
            afterInit: function()
            {
                // Add alternative scrollbar to list
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar();
            },
            afterSelect: function()
            {
                // Update scrollbar size
                this.$selectableContainer.add(this.$selectionContainer).find('.ms-list').perfectScrollbar('update');
            }
        });
    });
</script>
