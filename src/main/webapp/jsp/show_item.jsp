<%@page import="com.licj.viewworldweb.model.table.RateTable"%>
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

	<div class="div_content">

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
						%><%=userID%></li>
					<li>&nbsp;</li>
					<li>&nbsp;</li>
					<li>物品ID:</li>
					<li id="pageItemID"><%=itemID%></li>
				</ul>
			</div>

			<div class="div_head_right">

				<div class="dropdown" style="float: right;">

					<button type="button" class="btn btn-sm"
						onclick="playMusic(<%=itemID%>)"
						style="background-color: transparent; border: 1px solid #FFF; color: white;">
						<span class="glyphicon glyphicon-play"></span>&nbsp;&nbsp;&nbsp;播放音乐
					</button>

					<button type="button" class="btn dropdown-toggle btn-sm"
						id="dropdownMenu1" data-toggle="dropdown"
						style="background-color: transparent; border: 1px solid #FFF; color: white;">
						<span class="glyphicon glyphicon-list"></span>&nbsp;&nbsp;&nbsp;选择推荐系统&nbsp;&nbsp;&nbsp;<span
							class="caret"></span>
					</button>

					<ul class="dropdown-menu" role="menu"
						aria-labelledby="dropdownMenu1">

						<li class="dropdown-header">协同过滤</li>
						<li role="presentation"><a href="#"
							id="base_user_recommender">依据用户的推荐</a></li>
						<li role="presentation"><a href="#"
							id="base_item_recommender">依据音乐的推荐</a></li>

						<li role="presentation" class="divider"></li>

						<li class="dropdown-header">基于内容的推荐</li>
						<li role="presentation"><a href="#"
							id="base_content_recommender">依据音乐属性相似度的推荐</a></li>

						<li role="presentation" class="divider"></li>

						<li class="dropdown-header">自定义推荐</li>
						<li role="presentation"><a href="#"
							id="base_distribute_recommender">分布式推荐</a></li>
 							
					</ul>

				</div>

			</div>

		</div>

	</div>

	<!-- 基于用户推荐弹窗 -->
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
					<br>
					<div style="margin-left: 90px;">
						<input type="text" class="form-control" id="format"><label
							for="format">&nbsp;FORMAT</label>
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

	<div class="div_item">
		<div class="div_item_desc">
			<%
				Item item = new ItemTable().get(Long.parseLong(itemID));
				RateTable rateTable = new RateTable();
			%>
			<h3 style="height: 16px"><%=item.getPublish_time()%>发布
			</h3>
			<!-- 应该改为歌曲发布时间 -->
			<h3 style="height: 16px"><%=item.getTags().get(0)%></h3>
			<!-- 应该改为歌曲最显著标志 -->
			<hr style="margin-right: 36px">
			<h3 style="height: 20px"><%=item.getSong_name()%></h3>
			<!-- 应该改为歌曲名字 -->
			<h5 style="height: 20px"><%=item.getSinger_name()%></h5>
			<!-- 应该改为歌曲演唱者 -->
			<h5 style="height: 16px">
				<%=rateTable.getAVGPreferByItemID(itemID)%>分&nbsp;&nbsp;
				<%=rateTable.getRatesNumByItemID(itemID)%>人评价
			</h5>
			<div id="play_music_div" style="height: 16px;"></div>
			<!--应该改为歌曲副歌部分
			<h5 style="height: 16px">
				<span style="color: #AEE5E4">Lift your eyes and see the glory
					Where the circle of life is drawn See the never-ending story Come
					with me to the gates of dawn</span>
			</h5>
			-->
		</div>
		<div class="div_item_cover">
			<div class="div_item_cover_detail">
				<img alt="" src="<%=item.getPic_url()%>>" width="132px"
					height="132px" id="currentSongImage">
				<h5>
					<span id="currentSong"><%=item.getSong_name()%></span>
					<!-- 应该改为歌曲名字 -->
				</h5>
				<h5>
					<span id="currentSinger"><%=item.getSinger_name()%></span>
					<!-- 应该改为歌曲演唱者 -->
				</h5>
			</div>
			<div class="div_item_cover_tag">

				<%
					for (String tag : item.getTags()) {
				%>
				<button class="tag btn" onclick="toggleTag(this)"><%=tag%></button>
				<%
					}
				%>

			</div>
		</div>
	</div>

	<div class="div_item_recommender">
		<h4>
			<span class="text-success">喜欢该音乐的人也喜欢：</span>
		</h4>
		<!-- <h5 id="recommender_info">显示推荐系统名称，输入推荐参数，提交推荐；生成推荐精度、覆盖率和推荐评估分数</h5> -->
		<h5>
			<span class="text-muted" id="recommender_list"></span>
		</h5>
	</div>

	<div class="div_recommender_item_list" id="recommender_item_table">

	</div>


</body>
</html>