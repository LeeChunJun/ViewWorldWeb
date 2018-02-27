/**
 * register controller
 */
$(function() {

	// 待上传的注册信息
	var resultObj = {
		"result" : false,
		"message" : "",
		"email" : "",
		"password" : "",
		"nick" : "",
		"phone" : "",
		"yzm" : ""
	};

	// 接收验证结果
	var resultInfo = {
		"result" : false,
		"message" : ""
	};

	$("#account_email").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["email"] = resultInfo["message"];
		}
	});

	$("#account_pwd").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["password"] = resultInfo["message"];
		}
	});

	$("#account_pwd").keyup(function() {
		checkIntensity(this.value);
	});

	$("#account_nick").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["nick"] = resultInfo["message"];
		}
	});

	$("#account_phone").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["phone"] = resultInfo["message"];
		}
	});

	$("#account_phone_vertify_input").click(function() {
		var resultArr = phoneVertify($("#account_phone").val());
		if (resultArr["result"] == true) {
			setTime(this, 60);
		} else {
			$("#phone_info").html(resultArr["message"]);
		}

	});

	$("#account_phone_vertify").blur(function() {
		resultInfo = inputDataValid(this);
		if (resultInfo["result"] == true) {
			resultObj["yzm"] = resultInfo["message"];
		}
	});

	// 表单提交
	$("#submit_user_info").click(
			function() {
				if (resultObj["email"] != "" && resultObj["password"] != ""
						&& resultObj["nick"] != "" && resultObj["phone"] != ""
						&& resultObj["yzm"] != "") {
					resultObj["result"] = true;
					resultObj["message"] = "ok";
					// ajax上传数据
					submitUserInfo(resultObj);

				} else {
					resultObj["result"] = false;
					resultObj["message"] = "fail";
					inputDataValid(document.getElementById('account_email'));
					inputDataValid(document.getElementById('account_pwd'));
					inputDataValid(document.getElementById('account_nick'));
					inputDataValid(document.getElementById('account_phone'));
					inputDataValid(document
							.getElementById('account_phone_vertify'));
				}

				return false;
			});
	
	$("#clickDownApp").click(function(){
		$("#myModal").modal('show');
		return false;
	});

});

var submitUserInfo = function(resultObj) {
	if(!resultObj["phone"].endsWith(resultObj["yzm"])){
		alert("验证码输入不正确");
		return;
	}
	$.ajax({
		cache : true,
		type : "POST",
		url : "http://localhost:8080/ViewWorldWeb/RegisterServlet",
		// 相当于 data : "{'str1':'foovalue','str2':'barvalue'}",
		data : JSON.stringify({
			"result" : resultObj["result"],
			"message" : resultObj["message"],
			"email" : resultObj["email"],
			"password" : resultObj["password"],
			"nick" : resultObj["nick"],
			"phone" : resultObj["phone"],
			"yzm" : resultObj["yzm"]
		}),

		async : false,
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
		success : function(data) {
			if (data != "注册成功") {
				alert("该用户已被注册，请换个吧！");
				window.location.reload();
			} else {
				window.location.href = "/ViewWorldWeb/jsp/login.jsp";
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

	if (id == "account_email") {
		resultArr = emailVertify(value);
		if (resultArr["result"] == false) {
			$("#email_info").html(resultArr["message"]);
		} else {
			$("#email_info").html("");
		}
	} else if (id == "account_pwd") {
		resultArr = pwdVertify(value);
		if (resultArr["result"] == false) {
			$("#pwd_info").html(resultArr["message"]);
		} else {
			$("#pwd_info").html("");
		}
	} else if (id == "account_nick") {
		resultArr = chinaLetterNumberVertify(value);
		if (resultArr["result"] == false) {
			$("#nick_info").html(resultArr["message"]);
		} else {
			$("#nick_info").html("");
		}
	} else if (id == "account_phone") {
		resultArr = phoneVertify(value);
		if (resultArr["result"] == false) {
			$("#phone_info").html(resultArr["message"]);
		} else {
			$("#phone_info").html("");
		}
	} else if (id == "account_phone_vertify") {
		resultArr = yzmVertify(value);
		if (resultArr["result"] == false) {
			$("#yzm_info").html(resultArr["message"]);
		} else {
			$("#yzm_info").html("");
		}
	}
	return resultArr;

}

/**
 * 验证邮箱
 * 
 * @param str
 */
var emailVertify = function(str) {
	var pattern = /^[\w.+-]+@(?:[-a-z0-9]+\.)+[a-z]{2,4}$/i;// 正则/\b[^\s\@]+\@(?:[a-z\d-]+\.)+(?:com|net|org|info|cn|jp|gov|aero)\b/;
	if (str == "") {
		return {
			"result" : false,
			"message" : "邮箱不能为空"
		};
	} else if (pattern.test(str)) {
		return {
			"result" : true,
			"message" : str
		};
	} else {
		return {
			"result" : false,
			"message" : "邮箱格式错误"
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

/**
 * 名号
 * 
 * @param str
 * @returns
 */
var chinaLetterNumberVertify = function(str) {
	var pattern = /^[0-9a-zA-Z\u4e00-\u9fa5]+$/i;
	if (str == "") {
		return {
			"result" : false,
			"message" : "名号不能为空"
		};
	} else if (pattern.test(str)) {
		return {
			"result" : true,
			"message" : str
		};
	} else {
		return {
			"result" : false,
			"message" : "只能包含中文、字母、数字！"
		};
	}
}

/**
 * 手机
 * 
 * @param str
 */
var phoneVertify = function(str) {
	var partten = /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/; // /^1[3,5]\d{9}$/;
	if (str == "") {
		return {
			"result" : false,
			"message" : "手机号不能为空"
		};
	} else if (partten.test(str)) {
		return {
			"result" : true,
			"message" : str
		};
	} else {
		return {
			"result" : false,
			"message" : "请输入正确手机号"
		};
	}
}

/**
 * 验证密码是否为空
 * 
 * @param str
 */
var yzmVertify = function(str) {
	if (str == "") {
		return {
			"result" : false,
			"message" : "验证码不能为空"
		};
	} else {
		return {
			"result" : true,
			"message" : str
		};
	}
}

/**
 * 密码强度检测脚本
 * 
 * @param pwd
 * @returns
 */
var checkIntensity = function(pwd) {
	var Mcolor, Wcolor, Scolor, Color_Html;
	var m = 0;
	var Modes = 0;
	for (i = 0; i < pwd.length; i++) {
		var charType = 0;
		var t = pwd.charCodeAt(i);
		if (t >= 48 && t <= 57) {
			charType = 1;
		} else if (t >= 65 && t <= 90) {
			charType = 2;
		} else if (t >= 97 && t <= 122) {
			charType = 4;
		} else {
			charType = 4;
		}
		Modes |= charType;
	}
	for (i = 0; i < 4; i++) {
		if (Modes & 1) {
			m++;
		}
		Modes >>>= 1;
	}
	if (pwd.length <= 4) {
		m = 1;
	}
	if (pwd.length <= 0) {
		m = 0;
	}
	switch (m) {
	case 1:
		Wcolor = "pwd pwd_Weak_c";
		Mcolor = "pwd pwd_c";
		Scolor = "pwd pwd_c pwd_c_r";
		Color_Html = "弱";
		break;
	case 2:
		Wcolor = "pwd pwd_Medium_c";
		Mcolor = "pwd pwd_Medium_c";
		Scolor = "pwd pwd_c pwd_c_r";
		Color_Html = "中";
		break;
	case 3:
		Wcolor = "pwd pwd_Strong_c";
		Mcolor = "pwd pwd_Strong_c";
		Scolor = "pwd pwd_Strong_c pwd_Strong_c_r";
		Color_Html = "强";
		break;
	default:
		Wcolor = "pwd pwd_c";
		Mcolor = "pwd pwd_c pwd_f";
		Scolor = "pwd pwd_c pwd_c_r";
		Color_Html = "无";
		break;
	}
	document.getElementById('pwd_Weak').className = Wcolor;
	document.getElementById('pwd_Medium').className = Mcolor;
	document.getElementById('pwd_Strong').className = Scolor;
	document.getElementById('pwd_Medium').innerHTML = Color_Html;
}

/**
 * 手机验证码
 * 
 * @param obj
 * @param countdown
 * @returns
 */
var setTime = function(obj, countdown) {
	if (countdown == 0) {
		obj.value = "获取验证码";
		$("#account_phone_vertify_input").attr("disabled", false);
		obj.removeAttribute("class", "btn disabled");
		obj.setAttribute("class", "btn btn-default");
		countdown = 60;
		return;
	} else {
		obj.value = "重新发送(" + countdown + ")";
		$("#account_phone_vertify_input").attr("disabled", true);
		obj.removeAttribute("class", "btn btn-default");
		obj.setAttribute("class", "btn disabled");
		countdown--;
	}
	setTimeout(function() {
		setTime(obj, countdown);
	}, 1000)

}

