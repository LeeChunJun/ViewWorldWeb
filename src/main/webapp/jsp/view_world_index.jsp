<%@page import="com.licj.viewworldweb.model.table.UserTable"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="script/jquery/2.0.0/jquery.min.js"></script>
<link href="style/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="script/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="style/common/view_world_index.css" rel="stylesheet"
	type="text/css">
<script src="script/common/view_world_index.js"></script>
<title>视界，不一样的世界</title>
</head>
<body>

	<div class="div_content">

		<div class="div_head">

			<div class="div_head_left">
				<ul style="list-style-type: none;">
					<li>视界 · 主页</li>
					<li>&nbsp;</li>
					<li>/</li>
					<li>&nbsp;</li>
					<li>看见整个世界</li>
					<li>&nbsp;</li>
					<li>/</li>
					<li>&nbsp;</li>
					<li><%=request.getSession().getAttribute("userName")%></li>
					<li>&nbsp;</li>
					<li>&nbsp;</li>
					<li><a href="#" onclick="exitAccount()"><span
							class="glyphicon glyphicon-log-out"></span></a></li>
					<li id="pageUserID">
						<%
							String userID = "";
							UserTable userTable = new UserTable();
							String account = (String) request.getSession().getAttribute("userName");
							userID = userTable.getIDByAccount(account, userTable.hasUserByPhone(account));
						%>
					</li>
				</ul>
			</div>

			<div class="div_head_right">
				<div class="input-group">
					<input type="text" id="search_input" class="form-control"
						placeholder="Search for..." style="height: 80%;"> <span
						class="input-group-btn">
						<button class="btn btn-default btn-sm" type="button"
							id="search_btn">搜索</button>
					</span>
				</div>
			</div>

		</div>

		<div class="div_item_table">
			<table
				class="table table-striped table-bordered table-hover table-condensed"
				style="text-align: center;">
				<thead>
					<tr>
						<th style="text-align: center;">ID</th>
						<th style="text-align: center;">名称</th>
						<th style="text-align: center;">发布时间</th>
						<th style="text-align: center;">标签</th>
						<th style="text-align: center;">操作</th>
					</tr>
				</thead>
				<tbody>

					<c:forEach items="${items}" var="item" varStatus="st">
						<tr>
							<td>${item.id}</td>
							<td>${item.name}</td>
							<td>${item.publish_time}</td>
							<td>${item.tags}</td>
							<td><a
								href="jsp/show_item.jsp?itemID=${item.id}&userID=<%=userID%>">查看</a></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>
			<nav>
				<ul class="pager">

					<%
						String query = (String) request.getAttribute("query");
						if (query != null && !query.equals("")) {
					%>
					<li><a href="?start=0&query=<%=query%>">首 页</a></li>
					<li><a href="?start=${pre}&query=<%=query %>">上一页</a></li>
					<li><a href="?start=${next}&query=<%=query %>">下一页</a></li>
					<li><a href="?start=${last}&query=<%=query %>">末 页</a></li>
					<%
						} else {
					%>

					<li><a href="?start=0">首 页</a></li>
					<li><a href="?start=${pre}">上一页</a></li>
					<li><a href="?start=${next}">下一页</a></li>
					<li><a href="?start=${last}">末 页</a></li>

					<%
						}
					%>
				</ul>
			</nav>
		</div>



	</div>

</body>
</html>