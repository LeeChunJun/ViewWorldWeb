package com.licj.viewworldweb.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.licj.viewworldweb.utils.RecommenderIntro;

/**
 * Servlet implementation class RecommenderData
 */
@WebServlet("/RecommenderData")
public class RecommenderData extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(RecommenderData.class);
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecommenderData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/* userID */
		String requestId = request.getParameter(Parameters.REQUESTID);

		switch (requestId) {
		case "fetchData":

			break;
		case "writeData":

			break;
		case "analysData":
			try {
				RecommenderIntro re = new RecommenderIntro();
				String[] args = new String[] {};
				re.entry(args);
				writeJSON(response, "RecommenderIntro Response Content");
			} catch (Exception e) {
				LOGGER.error("analysData error", e);
			}
			break;
		default:
			break;
		}
	}
	
	private void writeJSON(HttpServletResponse response, String content) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		String result = "{\"msg\":\"OK\"";
		result = result + ",\"code\":200";
		result = result + ",\"content\":\"" + content + "\"}";
		LOGGER.info("JSON Response->" + result);
		writer.print(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
