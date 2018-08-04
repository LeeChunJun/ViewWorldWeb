package com.licj.viewworldweb.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.licj.viewworldweb.model.User;
import com.licj.viewworldweb.model.table.UserTable;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		PrintWriter writer = response.getWriter();
		try {
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null) {
				responseStrBuilder.append(inputStr);
			}
			JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
			// 注册账号判断
			String emailString = jsonObject.getString("email").trim();
			String passwordString = jsonObject.getString("password").trim();
			String nameString = jsonObject.getString("nick").trim();
			String phoneString = jsonObject.getString("phone").trim();
			UserTable userTable = new UserTable();

			if (!userTable.hasUserByEmail(emailString) && !userTable.hasUserByPhone(phoneString)) {
				User user = new User();
				user.setEmail(emailString);
				user.setPassword(passwordString);
				user.setName(nameString);
				user.setPhone(phoneString);
				user.setId(userTable.getTotal() + 1);
				List<String> tags = new ArrayList<String>();
				tags.add("暂无标签");
				user.setTags(tags);
				userTable.add(user);
				writer.print("注册成功");
			} else {
				writer.print("注册失败");
			}

		} catch (Exception e) {
			LOGGER.error("doGet() error!", e);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
