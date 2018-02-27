<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../script/jquery/2.0.0/jquery.min.js"></script>
<link href="../style/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="../script/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="../style/common/login.css" rel="stylesheet" type="text/css">
<script src="../script/common/login.js"></script>
<title>登录视界</title>
</head>
<body>
	<div class="div_content">

		<div>
			<img src="../images/logo.png" alt="ViewWorld_logo" width="322"
				height="73">
		</div>

		<div class="div_login">
			<div style="margin-left: 36px">
				<h3>
					<strong>登录视界</strong>
				</h3>
			</div>
			<br>
			<form action="" method="post" class="form-inline"
				name="loginAccountForm">
				<div style="margin-left: 60px;">
					<label for="account_id">账号</label> <input type="text"
						class="form-control" id="account_id" value="邮箱/手机号">&nbsp;<span
						class="text-danger" id="id_info" style="font-size: smaller;"></span>
				</div>
				<br>
				<div style="margin-left: 60px;">
					<label for="account_pwd">密码</label> <input type="password"
						class="form-control" id="account_pwd"> <span
						class="text-danger" id="pwd_info" style="font-size: smaller;"></span>
				</div>
				<br>
				<div style="margin-left: 90px;">
					<button class="btn btn-success" id="user_login">登录</button>
				</div>
			</form>
		</div>

		<div class="div_notice">
			<div class="div_notice_detail">
				<ul>
					<li style="list-style-type: none;">&gt;&nbsp;<span
						class="text-muted">还没有视界帐号？</span><a href="register.jsp">立即注册</a></li>
					<li style="list-style-type: none;">&gt;&nbsp;<a
						href="#" id="clickDownApp">点击下载视界移动应用</a></li>
				</ul>
			</div>
		</div>

	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button data-dismiss="modal" class="close" type="button">
						<span aria-hidden="true">×</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title">视界，看见不同的世界</h4>
				</div>
				<div class="modal-body">
					<span><img src="../images/view_world_app.png" alt="视界手机端二维码下载"></span>
				</div>
				<div class="modal-footer">
					<button data-dismiss="modal" class="btn btn-success" type="button">关闭</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>

	<div class="div_foot">

		<%@ include file="foot.jsp"%>

	</div>

</body>
</html>