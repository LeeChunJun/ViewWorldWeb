package com.licj.viewworldweb.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String uri = request.getRequestURI();
		if (uri.endsWith("register.jsp") || uri.endsWith("login.jsp") || uri.endsWith("LoginServlet") || uri.endsWith("RegisterServlet")) {
			chain.doFilter(request, response);
			return;
		} else if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || uri.endsWith(".jpg")) {
			chain.doFilter(request, response);
			return;
		}

		String userName = (String) request.getSession().getAttribute("userName");
		if (null == userName) {
			response.sendRedirect("/ViewWorldWeb/jsp/login.jsp");
			return;
		} else {
			chain.doFilter(request, response);
		}


	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
