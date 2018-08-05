/**
 * item的推荐展示页面控制
 */
function exitAccount() {
	$.get('http://localhost:8080/ViewWorldWeb/LogOutServlet', function() {
		window.location.reload();
	});
	return false;
}

function toggleTag(obj) {
	$(obj).toggleClass("btn-success");
}

function enableRecommenderBtn() {
	$("#wait_info").text("");
	$("#recommender_submit").attr("disabled", false);
}

function disableRecommenderBtn() {
	$("#wait_info").text("正在计算中。。。");
	$("#recommender_submit").attr("disabled", true);
}

function formatDuring(mss) {
	// var days = parseInt(mss / (1000 * 60 * 60 * 24));
	// var hours = parseInt((mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
	// var minutes = parseInt((mss % (1000 * 60 * 60)) / (1000 * 60));
	// return days + " 天 " + hours + " 小时 " + minutes + " 分钟 " + seconds + " 秒
	// ";
	var seconds = (mss % (1000 * 60)) / 1000;
	return seconds;
}

function handleDataSource(postData) {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/ViewWorldWeb/RecommenderData",
		data : postData,
		dataType : "json",
		success : function(data) {
			alert(data.content);
		}
	});
	return false;
}

function recommenderSubmit(recommenderType) {

	var userID = $("#pageUserID").text().trim();
	var itemID = $("#pageItemID").text().trim();
	var count = $("#count").val();
	var format = $("#format").val();
	var evaluator = $("#evaluator").val();

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
	if (evaluator != "") {
		postData = postData + "&evaluator=" + evaluator;
	}
	if (recommenderType != "") {
		postData = postData + "&recommenderType=" + recommenderType;
	}

	/* 记录提交参数时间 */
	var startTimestamp = (new Date()).getTime();

	$.ajax({
				cache : false,
				type : "POST",
				url : "http://localhost:8080/ViewWorldWeb/RecommenderServlet",
				data : postData,
				dataType : "json",
				async : false,
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert(XMLHttpRequest.status);
					alert(XMLHttpRequest.readyState);
					alert(textStatus);
				},
				success : function(data) {
					/* 获取计算耗费时间 */
					var successTimestamp = (new Date()).getTime();
					var comsumeTime = formatDuring(successTimestamp
							- startTimestamp);
					/* 重组推荐项目表格 */
					// var jsonData = $.parseJSON(data);
					var jsonData = data

					if (jsonData.sum == 0) {
						$("#recommender_info").html("推荐列表为空!");
						$("#div_item_recommender_id").html("");
					} else {
						$("#recommender_info").html(
								"绝对平均误差为：" + jsonData.aadScore + "<br\>均方根误差为："
										+ jsonData.emsScore + "<br\>查准率为："
										+ jsonData.precision + "<br\>查全率为："
										+ jsonData.recall + "<br\>计算时间(秒)："
										+ comsumeTime);
						var content = "";
						for (i = 0; i < jsonData.sum; i++) {
							content = content + "<tr><td>"
									+ jsonData.items[i].item.id + "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.name + "</td>";
							content = content + "<td>"
									+ jsonData.items[i].item.published_year
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
						$("#div_item_recommender_id")
								.html(

										"<table class='table table-bordered table-condensed' style='text-align: center;'>"
												+ "<thead><tr>"
												+ "<th style='text-align: center;'>ID</th>"
												+ "<th style='text-align: center;'>名称</th>"
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

	/* 选择推荐系统方式 */
	$("#base_user_recommender").click(function() {
		recommender_select = "GenericUserBasedRecommender";
		$("#recommender_name_info").text("依据用户的推荐");
		$("#myModal").modal('show');
		enableRecommenderBtn();
	});

	$("#base_item_recommender").click(function() {
		recommender_select = "GenericItemBasedRecommender";
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

	/* 提交推荐 */
	$("#recommender_submit").click(function() {
		recommenderSubmit(recommender_select);
		disableRecommenderBtn();
	});

	/* 数据源处理 */
	$("#fetch_data_net_easy").click(function() {
		var postData = {
			"requestID" : "fetchData"
		}
		handleDataSource(postData);
	});

	$("#write_data_mysqldb").click(function() {
		var postData = {
			"requestID" : "writeData"
		}
		handleDataSource(postData);
	});

	$("#analys_data").click(function() {
		var postData = {
			"requestID" : "analysData"
		}
		handleDataSource(postData);
	});

});