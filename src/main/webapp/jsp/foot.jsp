<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../style/common/login.css" rel="stylesheet" type="text/css">
<title>底部页面</title>
<script type="text/javascript">
$(function() {
	
	$("#aboutViewWorld").click(function() {
		$("#show_message").html("关于视界&nbsp;2017－2019 viewworld.licj.com,all rights reserved");
		$("#myFootModal").modal('show');
		return false;
	});
	$("#contactUs").click(function() {
		$("#show_message").html("联系我们&nbsp;1772043675@qq.com");
		$("#myFootModal").modal('show');
		return false;
	});
	$("#mobieApp").click(function() {
		$("#show_message").html("<img src='../images/view_world_app.png' alt='视界手机端二维码下载'>");
		$("#myFootModal").modal('show');
		return false;
	});
	
});
</script>
</head>
<body>

	<div>
	
	<div class="modal fade" id="myFootModal" tabindex="-1" role="dialog"
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
					<span id="show_message">关于视界。。。</span>
				</div>
				<div class="modal-footer">
					<button data-dismiss="modal" class="btn btn-success" type="button">关闭</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	
		<div style="float: left;">
			<span class="text-muted"> &copy; 2017－2019
				viewworld.licj.com,all rights reserved </span>
		</div>
		<div style="float: right;">
			<a href="#" id="aboutViewWorld">关于视界</a> · <a
				href="#" id="contactUs">联系我们</a> · <a
				href="#" id="mobieApp">移动应用</a>
		</div>
	</div>

</body>
</html>