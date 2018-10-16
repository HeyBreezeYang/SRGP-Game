<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<div id="roleInfoDiv">
			<div class="breadcrumb-env">
				<ol class="breadcrumb bc-1">
					<li>
						<a href="#" onclick="moveMain();">
							<i class="fa-home"></i>回到主页
						</a>
					</li>
				</ol>
			</div>

			<div class="col-md-12" >
				<div>
					<div>
						<label>角色名：</label><input type="text" id="queryrolename"><label>${itemsInfo["status"]}</label>

					</div>
					<div>
						<button type="button" onclick="queryByrolename();">查询</button>

					</div>
				</div>
			</div>
			<div>
				<table>
					<div>
						<label>角色名: </label>&nbsp;<label id="rolename">${itemsInfo["rolename"]}</label>
					</div>
					<div>
						<label>角色ID: </label>&nbsp;<label id="playerId" >${itemsInfo["pid"]}</label>
					</div>
					<div>
						<label>货币信息</label>
					</div>
					<div>
						<thead>
							<tr>
								<th>类型</th>
								<th>数量</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<th>金币</th>
								<%--<th>80400001</th>--%>
								<td>${cur["80400001"]}</td>
								<td><input id="gold" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400001','gold')">修改</button></td>
							</tr>
							<tr>
								<th>钻石</th>
								<%--<th>80400002</th>--%>
								<td>${cur["80400002"]}</td>
								<td><input id="diamond" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400002','diamond')">修改</button></td>
							</tr>
							<tr>
								<th>人脉</th>
								<%--<th>80400003</th>--%>
								<td>${cur["80400003"]}</td>
								<td><input id="contacts" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400003','contacts')">修改</button></td>
							</tr>
							<tr>
								<th>机会</th>
								<%--<th>80400004</th>--%>
								<td>${cur["80400004"]}</td>
								<td><input id="chance" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400004','chance')">修改</button></td>
							</tr>
							<tr>
								<th>工会贡献</th>
								<%--<th>80400005</th>--%>
								<td>${cur["80400005"]}</td>
								<td><input id="laborUnion" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400005','laborUnion')">修改</button></td>
							</tr>
							<tr>
								<th>酒会积分</th>
								<%--<th>80400006</th>--%>
								<td>${cur["80400006"]}</td>
								<td><input id="wineParty" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400006','wineParty')">修改</button></td>
							</tr>
							<tr>
								<th>学霸经验</th>
								<%--<th>80400007</th>--%>
								<td>${cur["80400007"]}</td>
								<td><input id="studyBully" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400007','studyBully')">修改</button></td>
							</tr>
							<tr>
								<th>辩论货币</th>
								<%--<th>80400008</th>--%>
								<td>${cur["80400008"]}</td>
								<td><input id="debateCur" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400008','debateCur')">修改</button></td>
							</tr>
							<tr>
								<th>辩论积分</th>
								<%--<th>80400009</th>--%>
								<td>${cur["80400009"]}</td>
								<td><input id="debateIntegral" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400009','debateIntegral')">修改</button></td>
							</tr>
							<tr>
								<th>BOSS积分</th>
								<%--<th>80400011</th>--%>
								<td>${cur["80400011"]}</td>
								<td><input id="boss" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400011','boss')">修改</button></td>
							</tr>
							<tr>
								<th>挖宝积分</th>
								<%--<th>80400012</th>--%>
								<td>${cur["80400012"]}</td>
								<td><input id="digTreasure" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400012','digTreasure')">修改</button></td>
							</tr>
							<tr>
								<th>大丰收积分</th>
								<%--<th>80400013</th>--%>
								<td>${cur["80400013"]}</td>
								<td><input id="bumperCrop" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400013','bumperCrop')">修改</button></td>
							</tr>
							<tr>
								<th>剿土匪积分</th>
								<%--<th>80400014</th>--%>
								<td>${cur["80400014"]}</td>
								<td><input id="suppressBandits" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400014','suppressBandits')">修改</button></td>
							</tr>
							<tr>
								<th>抓壮丁积分</th>
								<%--<th>80400015</th>--%>
								<td>${cur["80400015"]}</td>
								<td><input id="pressGang" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400015','pressGang')">修改</button></td>
							</tr>
							<tr>
								<th>妖后</th>
								<%--<th>80400016</th>--%>
								<td>${cur["80400015"]}</td>
								<td><input id="demonQueen" type="text" size="5" /></td>
								<td><button type="button" onclick="updateCur('80400016','demonQueen')">修改</button></td>
							</tr>
						</tbody>
					</div>

					<div>
						<div>
							<label>计数类型物品</label>
						</div>
						<thead>
							<tr>
								<th>goodsID</th>
								<th>数量</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${itemsInfo['gl']}" var="gl">
							<tr>
								<td>${gl["name"]}</td>
								<td>${gl["num"]}</td>
								<td><input id="${gl["gid"]}" type="text" size="5" /></td>
								<td><button type="button" onclick="updateGoods('${gl["gid"]}')">修改</button></td>
							</tr>
						</c:forEach>
						</tbody>
					</div>
				</table>
			</div>
				<div>
				</div>
<script type="text/javascript">

    /*laydate.render({
        elem: '#startDate', //指定元素
        showBottom: false
    });
    laydate.render({
        elem: '#endDate',
        showBottom: false

    });*/
	function queryByrolename(){
        var rolename = $("#queryrolename").val();
        jQuery.ajax({

            url: "itemsManage/queryRoleItems",
            type:"POST",
            async: false,
            data:{"rolename":rolename},
            success: function(response)
            {
                jQuery('#roleInfoDiv').html(response);
            }
        });
	}
	
	function updateCur(goodsId,changeType) {
        var pid = $("#playerId").html().trim();
        var changeNum = $("#"+changeType+"").val();
        jQuery.ajax({
            url: "itemsManage/changeCurrency",
            type:"POST",
            data:{"pid":pid,"goodsId":goodsId,"changeNum":changeNum},
            success: function(res)
            {
                alert(res);
                var rolename = $("#rolename").html().trim();
                jQuery.ajax({
                    url: "itemsManage/queryRoleItems",
                    type:"POST",
                    async: false,
                    data:{"rolename":rolename},
                    success: function(response)
                    {
                        jQuery('#roleInfoDiv').html(response);
                    }
                });
            }
        });
    }

    function updateGoods(goodsId) {
        var pid = $("#playerId").html().trim();
        var changeNum = $("#"+goodsId+"").val();
        jQuery.ajax({
            url: "itemsManage/changeGoods",
            type:"POST",
            data:{"pid":pid,"goodsId":goodsId,"changeNum":changeNum},
            success: function(res)
            {
                alert(res);
                var rolename = $("#rolename").html().trim();
                jQuery.ajax({
                    url: "itemsManage/queryRoleItems",
                    type:"POST",
                    async: false,
                    data:{"rolename":rolename},
                    success: function(response)
                    {
                        jQuery('#roleInfoDiv').html(response);
                    }
                });
            }
        });
    }
	
	/*function changeCurrency() {
        var playerId = $("#playerId").val();
        var currency = $("#currency").val();
        var changeNum = $("#changeNum").val();
        var changeType = $("#changeType").val();

        jQuery.ajax({

            url: "itemsManage/updateItemsInfo",
            type:"POST",
            async: false,
            data:{"playerId":playerId,"currency":currency,"changeNum":changeNum,"changeType":changeType},
            success: function(response)
            {
                jQuery('#insideLog').html(response);
            }
        });
    }*/

</script>