<%@page import="com.licj.viewworldweb.model.Item"%>
<%@page import="com.licj.viewworldweb.model.table.ItemTable"%>
<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="../script/jquery/2.0.0/jquery.min.js"></script>
<link href="../style/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="../script/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="../style/common/show_item.css" rel="stylesheet"
	type="text/css">
<script src="../script/common/show_item.js"></script>
<title>推荐系统</title>
</head>

<body style="background-image: url('../images/show_item_page_bg.jpg');">

	<div class="div_head">

		<div class="div_head_left">
			<ul style="list-style-type: none;">
				<li>视界 · 音乐</li>
				<li>&nbsp;</li>
				<li>/</li>
				<li>&nbsp;</li>
				<li>音乐推荐</li>
				<li>&nbsp;</li>
				<li>/</li>
				<li>&nbsp;</li>
				<li><%=request.getSession().getAttribute("userName")%></li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li><a href="#" onclick="exitAccount()"><span
						class="glyphicon glyphicon-log-out"></span></a></li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>用户ID:</li>
				<li id="pageUserID">
					<%
						String userID = request.getParameter("userID");
						String itemID = request.getParameter("itemID");
					%> <%=userID%>
				</li>
				<li>&nbsp;</li>
				<li>&nbsp;</li>
				<li>物品ID:</li>
				<li id="pageItemID"><%=itemID%></li>
			</ul>
		</div>

		<div class="div_head_right">

			<div class="dropdown" style="float: right;">

				<button type="button" class="btn dropdown-toggle btn-sm"
					id="dropdownMenu2" data-toggle="dropdown"
					style="background-color: transparent; border: 1px solid #FFF; color: white;">
					<span class="glyphicon glyphicon-cloud-download"></span>&nbsp;&nbsp;&nbsp;数据源处理&nbsp;&nbsp;&nbsp;<span
						class="caret"></span>
				</button>

				<ul class="dropdown-menu" role="menu"
					aria-labelledby="dropdownMenu2">

					<li class="dropdown-header">获取数据</li>
					<li role="presentation"><a href="#" id="fetch_data_net_easy">从网易云获取数据</a>
					</li>
					<li role="presentation" class="divider"></li>
					<li class="dropdown-header">处理数据</li>
					<li role="presentation"><a href="#" id="write_data_mysqldb">写入mysql数据库</a>
					</li>
					<li role="presentation" class="divider"></li>
					<li class="dropdown-header">分析数据</li>
					<li role="presentation"><a href="#" id="analys_data">分析项目相关性</a></li>

				</ul>
			</div>

			<div class="dropdown" style="float: right;">

				<!--<button type="button" class="btn btn-sm" onclick="playMusic(<%=itemID%>)" style="background-color: transparent; border: 1px solid #FFF; color: white;">
						<span class="glyphicon glyphicon-play"></span>&nbsp;&nbsp;&nbsp;播放音乐
					</button>-->

				<button type="button" class="btn dropdown-toggle btn-sm"
					id="dropdownMenu1" data-toggle="dropdown"
					style="background-color: transparent; border: 1px solid #FFF; color: white;">
					<span class="glyphicon glyphicon-list"></span>&nbsp;&nbsp;&nbsp;选择推荐系统&nbsp;&nbsp;&nbsp;<span
						class="caret"></span>
				</button>

				<ul class="dropdown-menu" role="menu"
					aria-labelledby="dropdownMenu1">

					<li class="dropdown-header">协同过滤</li>
					<li role="presentation"><a href="#" id="base_user_recommender">依据用户的推荐</a>
					</li>
					<li role="presentation"><a href="#" id="base_item_recommender">依据物品的推荐</a>
					</li>
					<li role="presentation" class="divider"></li>
					<li class="dropdown-header">基于内容的推荐</li>
					<li role="presentation"><a href="#"
						id="base_content_recommender">依据物品属性相似度的推荐</a></li>
					<li role="presentation" class="divider"></li>
					<li class="dropdown-header">自定义推荐</li>
					<li role="presentation"><a href="#"
						id="base_distribute_recommender">分布式推荐</a></li>

				</ul>

			</div>

		</div>

	</div>

	<!-- 推荐弹窗参数设定 -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button data-dismiss="modal" class="close" type="button">
						<span aria-hidden="true">×</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="recommender_name_info"></h4>
				</div>

				<div class="modal-body">
					<!-- 推荐系统输入参数选择 -->
					<div style="margin-left: 90px;">
						<input type="text" class="form-control" id="count"><label
							for="count">&nbsp;COUNT</label>
					</div>
					<div style="margin-left: 90px;">
						<select class="form-control" id="format">
							<option>json</option>
							<option>xml</option>
							<option>text</option>
						</select> <label for="format">&nbsp;FORMAT</label>
					</div>
					<div style="margin-left: 90px;">
						<select class="form-control" id="evaluator">
							<option>no</option>
							<option>yes</option>
						</select> <label for="evaluator">&nbsp;EVALUATOR</label>
					</div>
				</div>

				<div class="modal-footer">
					<span id="wait_info" style="float: left;"></span>
					<button data-dismiss="modal" class="btn btn-default" type="button">关闭</button>
					<button class="btn btn-success" id="recommender_submit"
						type="button">进行推荐</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<!-- 当前选择的歌曲项目详情 -->
	<div class="div_item">
		<div class="div_item_desc">
			<%
				Item item = new ItemTable().get(Long.parseLong(itemID));
			%>
			<h3 style="height: 12px">
				<%=item.getPublish_time()%>
				发布
			</h3>
			<h3 style="height: 12px">
				<%=item.getName()%>
			</h3>
			<hr style="margin-right: 36px">
			<div class="div_item_tag">

				<%
					for (String tag : item.getTags()) {
				%>
				<button class="tag btn btn-success"><%=tag%></button>
				<%
					}
				%>

			</div>
			<div class="div_item_play">
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-repeat"></span>&nbsp;&nbsp;&nbsp;单曲循环
				</button>
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-cloud"></span>&nbsp;&nbsp;&nbsp;分享
				</button>
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-star"></span>&nbsp;&nbsp;&nbsp;收藏
				</button>
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-music"></span>&nbsp;&nbsp;&nbsp;顺序播放
				</button>
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-forward"></span>&nbsp;&nbsp;&nbsp;跳过歌曲
				</button>
				<button type="button" class="btn btn-sm"
					style="background-color: transparent; border: 1px solid #FFF; color: white; margin: 2px;">
					<span class="glyphicon glyphicon-send"></span>
				</button>
			</div>
			<div class="div_item_dispaly">
				<iframe frameborder="no" border="0" marginwidth="0" marginheight="0"
					width=298 height=52
					src="http://music.163.com/outchain/player?type=2&id=<%=itemID%>&auto=1&height=32"></iframe>
			</div>

		</div>

		<div class="div_item_recommender" id="div_item_recommender_id">
			<h5>
				<br> <br> <br> <br> <br> <br> <br>
				<br> <br> <br> <br> <br> <br> <br>
				<br> <br> <br> <br> <br> <br> <br>
				<br> <br> <br> <br> <br> <br> <br>
				<br> <br> <br> <br> <br> <br> <br>
				<br>
			</h5>

		</div>

		<div class="div_recommender_analys">
			<h5>
				显示推荐系统名称，输入推荐参数；<br>生成推荐精度、覆盖率和推荐评估分数
			</h5>
			<h5>
				<span class="text-muted" id="recommender_info">
					id="recommender_info" </span>
			</h5>
		</div>

	</div>

</body>

</html>