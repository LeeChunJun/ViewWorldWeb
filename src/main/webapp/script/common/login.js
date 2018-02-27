/**
 * login controller
 */
$(function() {

	// 待上传的登录信息
	var resultObj = {
		"result" : false,
		"message" : "",
		"account" : "",
		"password" : "",
	};

	// 接收验证结果
	var resultInfo = {
		"result" : false,
		"message" : ""
	};

	$("#account_id").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["account"] = resultInfo["message"];
		}
	});

	$("#account_id").blur(function() {
		if (this.value == "") {
			this.value = "邮箱/手机号"
		}
	});

	$("#account_id").focus(function() {
		if (this.value == "邮箱/手机号") {
			this.value = ""
		}
	});

	$("#account_pwd").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["password"] = resultInfo["message"];
		}
	});

	// 表单提交
	$("#user_login").click(function() {
		if (resultObj["account"] != "" && resultObj["password"] != "") {
			resultObj["result"] = true;
			resultObj["message"] = "ok";
			// ajax上传数据
			loginUserInfo(resultObj);

		} else {
			resultObj["result"] = false;
			resultObj["message"] = "fail";
			inputDataValid(document.getElementById('account_id'));
			inputDataValid(document.getElementById('account_pwd'));
		}

		return false;
		
	});
	
	$("#clickDownApp").click(function(){
		$("#myModal").modal('show');
		return false;
	});

});

var loginUserInfo = function(resultObj) {
	$.ajax({
		cache : true,
		type : "POST",
		url : "http://localhost:8080/ViewWorldWeb/LoginServlet",
		// 相当于 data : "{'str1':'foovalue','str2':'barvalue'}",
		data : JSON.stringify({
			"result" : resultObj["result"],
			"message" : resultObj["message"],
			"account" : resultObj["account"],
			"password" : resultObj["password"],
		}),

		async : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
		success : function(data) {
			if (data != "登录成功") {
				alert("用户名或密码错误");
				window.location.reload();
			} else {
				// 改为主页view_world_index.jsp
				window.location.href = "/ViewWorldWeb/IndexItemListServlet"
			}
		}
	});
}

var inputDataValid = function(inputNode) {

	var id = inputNode.id;
	var value = inputNode.value;
	value = $.trim(value);
	inputNode.value = value; // 去除两端空格

	var resultArr = {
		"result" : false,
		"message" : ""
	};// 接收验证结果

	if (id == "account_id") {
		resultArr = pwdAccount(value);
		if (resultArr["result"] == false) {
			$("#id_info").html(resultArr["message"]);
		} else {
			$("#id_info").html("");
		}
	} else if (id == "account_pwd") {
		resultArr = pwdVertify(value);
		if (resultArr["result"] == false) {
			$("#pwd_info").html(resultArr["message"]);
		} else {
			$("#pwd_info").html("");
		}
	}
	return resultArr;

}

/**
 * 验证账号是否为空
 * 
 * @param str
 */
var pwdAccount = function(str) {
	var parttenPhone = /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/; // /^1[3,5]\d{9}$/;
	var patternEmail = /^[\w.+-]+@(?:[-a-z0-9]+\.)+[a-z]{2,4}$/i;// 正则/\b[^\s\@]+\@(?:[a-z\d-]+\.)+(?:com|net|org|info|cn|jp|gov|aero)\b/;
	if (str == "") {
		return {
			"result" : false,
			"message" : "账号不能为空"
		};
	} else if (parttenPhone.test(str) || patternEmail.test(str)) {
		return {
			"result" : true,
			"message" : str
		};
	} else {
		return {
			"result" : false,
			"message" : "请输入邮箱或者电话"
		};
	}
}

/**
 * 验证密码是否为空
 * 
 * @param str
 */
var pwdVertify = function(str) {
	if (str == "") {
		return {
			"result" : false,
			"message" : "密码不能为空"
		};
	} else {
		return {
			"result" : true,
			"message" : str
		};
	}
}

