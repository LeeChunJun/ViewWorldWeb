package com.licj.viewworldweb.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.licj.viewworldweb.model.table.UserTable;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
			// 登录账号判断
			String accountString = jsonObject.getString("account").trim();
			String passwordString = jsonObject.getString("password").trim();
			UserTable userTable = new UserTable();
			
			if (userTable.hasUserByEmail(accountString) && passwordString.equals(userTable.getPasswordByAccount(accountString, false))){
				request.getSession().setAttribute("userName", accountString);
				writer.print("登录成功");
			} else if (userTable.hasUserByPhone(accountString) && passwordString.equals(userTable.getPasswordByAccount(accountString, true))){
				request.getSession().setAttribute("userName", accountString);
				writer.print("登录成功");
			} else {
				writer.print("登录失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
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
