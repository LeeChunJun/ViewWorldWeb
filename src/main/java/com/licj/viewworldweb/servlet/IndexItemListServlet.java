package com.licj.viewworldweb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.licj.viewworldweb.model.Item;
import com.licj.viewworldweb.model.table.ItemTable;

/**
 * Servlet implementation class IndexItemListServlet
 */
@WebServlet("/IndexItemListServlet")
public class IndexItemListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexItemListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		int start = 0;
        int count = 12;
        int total;
        /* 取得查询关键字 */
        String queryString = request.getParameter("query");
        String startStr = request.getParameter("start");
    	if(startStr != null) {
    		start = Integer.parseInt(startStr);
    	}
        
        int next = start + count;
        int pre = start - count;
        
        if(queryString == null || queryString.equals("")){
        	total = new ItemTable().getTotal();
        } else {
        	total = new ItemTable().getAttrTotal(queryString);
        }
        
        int last;
        if (0 == total % count){
        	// 假设总数是50，是能够被5整除的，那么最后一页的开始就是45
        	last = total - count;
        } else {
        	// 假设总数是51，不能够被5整除的，那么最后一页的开始就是50
        	last = total - total % count;
        }
        
        pre = pre < 0 ? 0 : pre;
        next = next > last ? last : next;
 
        request.setAttribute("next", next);
        request.setAttribute("pre", pre);
        request.setAttribute("last", last);
        
        List<Item> items = null;
        if(queryString == null || queryString.equals("")){
        	items = new ItemTable().list(start, count);
        } else {
        	items = new ItemTable().listByAttr(start, count, queryString);
        	request.setAttribute("query", queryString);
        }
        request.setAttribute("items", items);
 
        request.getRequestDispatcher("/jsp/view_world_index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
