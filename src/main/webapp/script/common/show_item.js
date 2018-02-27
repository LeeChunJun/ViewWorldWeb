/**
 * item的推荐展示页面控制
 */
function toggleTag(obj) {
	$(obj).toggleClass("btn-success");
}

function playMusic(itemID) {
	$(play_music_div)
			.html(
					"<iframe frameborder='no' border='0' marginwidth='0' marginheight='0' width=300 height=86 src='http://music.163.com/outchain/player?type=2&id="
							+ itemID + "&auto=1&height=66'></iframe>")
}

function exitAccount() {
	$.get('http://localhost:8080/ViewWorldWeb/LogOutServlet', function() {
		window.location.reload();
	});
	return false;
}

function enableRecommenderBtn() {
	$("#wait_info").text("");
	$("#recommender_submit").attr("disabled", false);
	$("#recommender_submit").removeAttribute("class", "btn disabled");
	$("#recommender_submit").setAttribute("class", "btn btn-success");
}

function disableRecommenderBtn() {
	$("#wait_info").text("正在计算中。。。");
	$("#recommender_submit").attr("disabled", true);
	$("#recommender_submit").setAttribute("class", "btn disabled");
	$("#recommender_submit").removeAttribute("class", "btn btn-success");
}

function recommenderSubmit(recommenderType) {

	var userID = $("#pageUserID").text().trim();
	var itemID = $("#pageItemID").text().trim();
	var count = "12";
	var format = "json";

	/*
	 * var postData = { "userID" : userID, "itemID" : itemID, "count" : count,
	 * "format" : format, "recommenderType" : recommenderType, }
	 */
	var postData = "";
	if (userID != "") {
		postData = postData + "userID=" + userID;
	}
	if (itemID != "") {
		postData = postData + "&itemID=" + itemID;
	}
	if (count != "") {
		postData = postData + "&count=" + count;
	}
	if (format != "") {
		postData = postData + "&format=" + format;
	}
	if (recommenderType != "") {
		postData = postData + "&recommenderType=" + recommenderType;
	}
	

	$
			.ajax({
				cache : true,
				type : "POST",
				url : "http://localhost:8080/ViewWorldWeb/RecommenderServlet",
				data : postData,
				async : true,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
				},
				success : function(data) {
					/* 重组推荐项目表格 */
					var jsonData = $.parseJSON(data);

					if (jsonData.sum == 0) {
						$("#recommender_list").text("推荐列表为空!");
						$("#recommender_item_table").html("");
					} else {
						$("#recommender_info").html(
								"绝对平均误差为：" + jsonData.aadScore
										+ "&nbsp;&nbsp;均方根误差为"
										+ jsonData.emsScore
										+ "&nbsp;&nbsp;查准率为"
										+ jsonData.precision
										+ "&nbsp;&nbsp;查全率为" + jsonData.recall);
						$("#recommender_list").text("");
						var content = "";
						for (i = 0; i < jsonData.sum; i++) {
							content = content + "<tr><td>"
									+ jsonData.items[i].item.id + "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.song_name
									+ "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.singer_name
									+ "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.publish_time
									+ "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.tags + "</td>";
							content = content + "<td>"
									+ jsonData.items[i].score + "</td>";
							content = content
									+ "<td><a href='show_item.jsp?itemID="
									+ jsonData.items[i].item.id + "&userID="
									+ userID + "'>查看</a></td></tr>";
						}
						$("#recommender_item_table")
								.html(

										"<table class='table table-bordered table-condensed' style='text-align: center;'>"
												+ "<thead><tr>"
												+ "<th style='text-align: center;'>ID</th>"
												+ "<th style='text-align: center;'>歌曲名</th>"
												+ "<th style='text-align: center;'>歌手名</th>"
												+ "<th style='text-align: center;'>发布时间</th>"
												+ "<th style='text-align: center;'>标签</th>"
												+ "<th style='text-align: center;'>分数</th>"
												+ "<th style='text-align: center;'>操作</th>"
												+ "</tr></thead>"
												+ "<tbody>"
												+ content
												+ "</tbody>"
												+ "</table>"

								);
					}

					$("#myModal").modal('hide');
				}
			});

	return false;
}

$(function() {
	var recommender_select = "";

	$("#base_user_recommender").click(function() {
		recommender_select = "GenericBooleanPrefUserBasedRecommender";
		$("#recommender_name_info").text("依据用户的推荐");
		$("#myModal").modal('show');
		enableRecommenderBtn();
	});

	$("#base_item_recommender").click(function() {
		recommender_select = "GenericBooleanPrefItemBasedRecommender";
		$("#recommender_name_info").text("依据物品的推荐");
		$("#myModal").modal('show');
		enableRecommenderBtn();
	});

	$("#base_content_recommender").click(function() {
		$("#recommender_name_info").text("依据物品属性相似度的推荐");
		$("#myModal").modal('show');
		enableRecommenderBtn();
	});

	$("#base_distribute_recommender").click(function() {
		$("#recommender_name_info").text("分布式推荐");
		$("#myModal").modal('show');
		enableRecommenderBtn();
	});

	$("#recommender_submit").click(function() {
		recommenderSubmit(recommender_select);
		disableRecommenderBtn();
	});

});
