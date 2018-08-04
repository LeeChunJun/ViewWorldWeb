<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="../script/jquery/2.0.0/jquery.min.js"></script>
<link href="../style/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="../script/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="../style/common/register.css" rel="stylesheet"
	type="text/css">
<script src="../script/common/register.js"></script>
<title>欢迎加入视界</title>
</head>
<body>
	<div class="div_content">

		<div>
			<img src="../images/logo.png" alt="ViewWorld_logo" width="322"
				height="73">
		</div>

		<div class="div_register">
			<div style="margin-left: 36px">
				<h3>
					<strong>欢迎加入视界</strong>
				</h3>
			</div>
			<br>
			<form action="" method="post" class="form-inline"
				name="registerAccountForm">
				<div style="margin-left: 60px;">
					<label for="account_email">邮箱</label> <input type="email"
						class="form-control" id="account_email"> <span
						class="text-danger" id="email_info" style="font-size: smaller;"></span>
				</div>
				<br>
				<div style="margin-left: 60px;">
					<div style="float: left;">
						<label for="account_pwd">密码</label> <input type="password"
							class="form-control" id="account_pwd">
					</div>
					<div style="float: left; margin-left: 12px; margin-top: 6px">
						<table>
							<tr align="center">
								<td id="pwd_Weak" class="pwd pwd_c">&nbsp;</td>
								<td id="pwd_Medium" class="pwd pwd_c pwd_f">无</td>
								<td id="pwd_Strong" class="pwd pwd_c pwd_c_r">&nbsp;</td>
							</tr>
						</table>
						<span
						class="text-danger" id="pwd_info" style="font-size: smaller;"></span>
					</div>
				</div>
				<div style="margin-left: 60px;">
					<h6>&nbsp;</h6>
				</div>
				<br>
				<div style="margin-left: 60px;">
					<label for="account_nick">名号</label> <input type="text"
						class="form-control" id="account_nick"> <span
						class="text-danger" id="nick_info" style="font-size: smaller;"></span>
				</div>
				<div style="margin-left: 90px;">
					<h6>
						<span class="text-muted">"视界"那么大，取个独一无二的名号吧</span>
					</h6>
				</div>
				<div style="margin-left: 44px;">
					<label for="account_phone">手机号</label> <input type="tel"
						class="form-control" id="account_phone"> <span
						class="text-danger" id="phone_info" style="font-size: smaller;"></span>
				</div>
				<br>
				<div style="margin-left: 44px;">
					<label for="account_phone_vertify">验证码</label> <input type="text"
						class="form-control" id="account_phone_vertify"> <input
						type="button" class="btn btn-default" class="btn btn-default"
						id="account_phone_vertify_input" value="获取验证码">
						<span
						class="text-danger" id="yzm_info" style="font-size: smaller;"></span>
				</div>
				<br>
				<div style="margin-left: 90px;">
					<button class="btn btn-success" id="submit_user_info">注册</button>
				</div>
			</form>
		</div>

		<div class="div_notice">
			<div class="div_notice_detail">
				<ul>
					<li style="list-style-type: none;">&gt;&nbsp;<span
						class="text-muted">已经拥有视界帐号？</span><a href="login.jsp">直接登陆</a></li>
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