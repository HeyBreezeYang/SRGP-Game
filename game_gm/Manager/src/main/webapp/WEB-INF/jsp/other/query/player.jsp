<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<script type="text/javascript">
		 var zrCache=new Array();
         var hero=JSON.parse('${heroMsg}');
         var goods=JSON.parse('${goodsMsg}');
         var skill=JSON.parse('${skillMsg}');

		 function coverCH(msg){
			 var i=1;
			 var ht="";
             $.each(msg,function(key,value){
                 ht=$("#js"+i).html();
                 ht+="<li>"+value['cid']+"</li>";
                 $("#js"+i).html(ht);
                 i++;
                 if(i===2){
                     i=1;
				 }
             });
		 }
         function coverJSGoods(msg){
             var i=1;
             var ht="";
             $.each(msg,function(key,value){
                 ht=$("#bb"+i).html();
                 ht+="<li>"+value['name'];
                 ht+="<span class='label label-white pull-right'>× "+value['num'];
                 ht+="</span></li>";
                 $("#bb"+i).html(ht);
                 i++;
                 if(i===7){
                     i=1;
                 }
             });
         }
		 function coverGoods(id,msg,K,kv){
			 var ht="";
			 $.each(msg,function(key,value){
				 ht+="<tr>";
				 for(var j=0;j< K.length;j++){
				     if(K[j]==='name'){
                         ht+="<td>"+kv[value[K[0]]]+"</td>";
					 }else{
                         ht+="<td>"+value[K[j]]+"</td>";
					 }
				 }
				 ht+="</tr>";
			 });
			 $("#"+id).html(ht);
		 }
		 function getMsg(){
			$("#userSub").ajaxSubmit({
				dataType:'json',
				success:function(data){
					$.each(data,function(key,msg){
						if(key=='mnList'){
                            var k=new Array();
                            k[0]="cid";
                            k[1]='pname';
                            k[2]="sexp";
                            k[3]="charm";
                            k[4]="itmy";
                            k[5]="cldNum";
                            coverGoods("skMsg",msg,k,skill);
						}else if(key=='bei_bao'){
                            coverJSGoods(msg);
                        }else if(key=='xdList'){
                            var k=new Array();
                            k[0]="cid";
                            k[1]='pname';
                            k[2]="lv";
                            k[3]="exp";
                            k[4]="kc";
                            k[5]="zs";
                            k[6]="qs";
                            k[7]="yg";
                            coverGoods("bskMsg",msg,k,skill);
						}else if(key=='chInfo'){
                            coverCH(msg);
						}else{
						    if(key == 'crtDt' || key == 'lgnDt'){
								msg = fmtDate(msg);
							}
                            $("#"+key).html(msg+"");
						}
					});
				 }
			});
		 }

         function fmtDate(obj){

             var date = new Date();
             date.setTime(obj);
             var y = date.getFullYear();
             var m = date.getMonth() + 1;
             m = m < 10 ? ('0' + m) : m;
             var d = date.getDate();
             d = d < 10 ? ('0' + d) : d;
             var h = date.getHours();
             h = h < 10 ? ('0' + h) : h;
             var minute = date.getMinutes();
             var second = date.getSeconds();
             minute = minute < 10 ? ('0' + minute) : minute;
             second = second < 10 ? ('0' + second) : second;
             return y + '-' + m + '-' + d + " "+ h + ":" + minute + ":" + second;
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
		<div class="col-md-12">
			<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">查询</h3>

						<div class="panel-options">
							<a href="#" data-toggle="panel">
								<span class="collapse-icon">&ndash;</span>
								<span class="expand-icon">+</span>
							</a>
						</div>
					</div>
					<div class="panel-body">
						<form action="player/queryMsg" role="form" class="form-horizontal" method="post" id="userSub" >
							<div class="form-group">
								<label class="col-sm-1 control-label" for="qMouth">查询方式</label>
								<div class="col-sm-3">
									<select class="form-control" id="qMouth" name="type" >
										<option value="1">名称</option>
										<option value="2">PID</option>
										<option value="3">账号</option>
									</select>
								</div>
							</div>
							<div class="form-group" id="sid">
								<label class="col-sm-1 control-label" for="server">服务器</label>
								<div class="col-sm-3">
									<select class="form-control" id="server" name="server">
										<c:forEach items="${serverConfig}" var="config">
											<option value="${config.serverID}">${config.serverName}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-1 control-label" for="user">玩家</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="user" name="user"/>
								</div>
								<span class="input-group-btn">
										<button class="btn btn-blue" type="button" id="queryButton" onclick="getMsg();">获取</button>
								</span>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">基本信息</h3>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										PID
										<span class="label label-default pull-right" id="pid"></span>
									</li>
									<br/>
									<li>
										UID
										<span class="label label-primary pull-right" id="aId"></span>
									</li>
									<br/>
									<li>
										名称
										<span class="label label-secondary pull-right" id="nm"></span>
									</li>
									<br/>
									<li>
										公会ID
										<span class="label label-warning pull-right" id="guildId"></span>
									</li>
									<br/>
									<li>
										公会名字
										<span class="label label-warning pull-right" id="guinm"></span>
									</li>
									<br/>
									<li>
										注册时间
										<span class="label label-purple pull-right" id="crtDt"></span>
									</li>
								</ul>
							</div>
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										玩家等级
										<span class="label label-warning pull-right" id="lv"></span>
									</li>
									<br/>
									<li>
										玩家经验
										<span class="label label-success pull-right" id="exp"></span>
									</li>
									<br/>
									<li>
										宴会积分
										<span class="label label-purple pull-right" id="ptySco"></span>
									</li>
									<br/>
									<li>
										VIP
										<span class="label label-primary pull-right" id="vip"></span>
									</li>
									<br/>
									<li>
										活跃度
										<span class="label label-danger pull-right" id="an"></span>
									</li>
									<br/>
									<li>
										最后登录
										<span class="label label-purple pull-right" id="lgnDt"></span>
									</li>
								</ul>
							</div>
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										口才
										<span class="label label-default pull-right" id="kou_cai"></span>
									</li>
									<br/>
									<li>
										智商
										<span class="label label-primary pull-right" id="zhi_shang"></span>
									</li>
									<br/>
									<li>
										情商
										<span class="label label-secondary pull-right" id="qing_shang"></span>
									</li>
									<br/>
									<li>
										眼光
										<span class="label label-warning pull-right" id="yan_guang"></span>
									</li>
									<li>
										富豪势力
										<span class="label label-warning pull-right" id="fu_hao"></span>
									</li>
									<br/>
									<%--<li>
										暴击率
										<span class="label label-danger pull-right" id="bao_ji"></span>
									</li>
									<br/>
									<li>
										暴击伤
										<span class="label label-info pull-right" id="bao_shang"></span>
									</li>--%>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">货币信息</h3>
					</div>
					<div class="panel-body">
						<div class="row">
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										金币
										<span class="label label-default pull-right" id="80400001"></span>
									</li>
									<br/>
									<li>
										钻石
										<span class="label label-primary pull-right" id="80400002"></span>
									</li>
									<br/>
									<li>
										人脉
										<span class="label label-secondary pull-right" id="80400003"></span>
									</li>
									<br/>
									<li>
										机会
										<span class="label label-warning pull-right" id="80400004"></span>
									</li>
									<br/>
									<li>
										剿土匪积分
										<span class="label label-warning pull-right" id="80400014"></span>
									</li>
								</ul>
							</div>
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										贡献
										<span class="label label-warning pull-right" id="80400005"></span>
									</li>
									<br/>
									<li>
										酒会
										<span class="label label-success pull-right" id="80400006"></span>
									</li>
									<br/>
									<li>
										辩论币
										<span class="label label-purple pull-right" id="80400008"></span>
									</li>
									<br/>
									<li>
										挖宝积分
										<span class="label label-purple pull-right" id="80400012"></span>
									</li>
									<br/>
									<li>
										抓壮丁积分
										<span class="label label-info pull-right" id="80400015"></span>
									</li>
								</ul>
							</div>
							<div class="col-sm-4">
								<ul class="list-unstyled line-height-default">
									<li>
										BOSS积分
										<span class="label label-warning pull-right" id="80400011"></span>
									</li>
									<br/>
									<li>
										辩论积分
										<span class="label label-info pull-right" id="80400009"></span>
									</li>
									<br/>
									<li>
										学霸经验
										<span class="label label-danger pull-right" id="80400007"></span>
									</li>
									<br/>
									<li>
										大丰收积分
										<span class="label label-danger pull-right" id="80400013"></span>
									</li>
									<br/>
									<li>
										妖后
										<span class="label label-danger pull-right" id="80400016"></span>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">已有称号</h3>
					</div>
					<div class="panel-body">
						<div class="row" id="jsMsg">
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js1"></ul></div>
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js2"></ul></div>
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js3"></ul></div>
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js4"></ul></div>
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js5"></ul></div>
							<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="js6"></ul></div>
						</div>
					</div>
				</div>
			</div>
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">背包</h3>
				</div>
				<div class="panel-body">
					<div class="row" id="bbMsg">
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb1"></ul></div>
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb2"></ul></div>
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb3"></ul></div>
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb4"></ul></div>
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb5"></ul></div>
						<div class="col-sm-2"><ul class="list-unstyled line-height-default" id="bb6"></ul></div>
					</div>
				</div>
			</div>
		</div>
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<h3 class="panel-title">员工</h3>
					</div>
					<div class="panel-body">
						<table class="table responsive">
							<thead>
							<tr>
								<th>CID</th>
								<th>NAME</th>
								<th>等级</th>
								<th>经验</th>
								<th>口才</th>
								<th>智商</th>
								<th>情商</th>
								<th>眼光</th>
							</tr>
							</thead>
							<tbody id="bskMsg"></tbody>
						</table>
					</div>
				</div>
			</div>
		<div class="col-md-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title">美女</h3>
				</div>
				<div class="panel-body">
					<table class="table responsive">
						<thead>
						<tr>
							<th>CID</th>
							<th>NAME</th>
							<th>经验</th>
							<th>魅力</th>
							<th>亲密度</th>
							<th>子嗣数</th>
						</tr>
						</thead>
						<tbody id="skMsg"></tbody>
					</table>
				</div>
			</div>
		</div>

		</div>