package com.licj.viewworldweb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.licj.viewworldweb.model.User;
import com.licj.viewworldweb.model.table.UserTable;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String email = request.getParameter(Parameters.EMAIL);
		String phone = request.getParameter(Parameters.PHONE);
		User user = null;
		if (email != null) {
			user = new UserTable().getUserByEmail(email);
		} else if (phone != null) {
			user = new UserTable().getUserByPhone(phone);
		} else {
			throw new ServletException("user was not specified");
		}
		if (user != null) {
			try {
				writeJSON(response, user);
			} catch (IOException e) {
				LOGGER.error("doGet() error!", e);
			}
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private static void writeJSON(HttpServletResponse response, User user) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer.print(user.toJSON());
	}

}
