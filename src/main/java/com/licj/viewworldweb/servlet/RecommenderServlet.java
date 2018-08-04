package com.licj.viewworldweb.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import com.licj.viewworldweb.model.RecommendItem;
import com.licj.viewworldweb.model.RecommendItemList;
import com.licj.viewworldweb.model.table.RateTable;
import com.licj.viewworldweb.recommender.BaseItemRecommender;
import com.licj.viewworldweb.recommender.BaseUserRecommender;
import com.licj.viewworldweb.recommender.ItemRecommender;

/**
 * Servlet implementation class ItemRecommenderServlet
 */
@WebServlet("/RecommenderServlet")
public class RecommenderServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(RecommenderServlet.class);
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_HOW_MANY = 10;
	private static final String DEFAULT_RECOMMENDER = Parameters.UserBased_REC;

	private ItemRecommender recommender;
	private RecommenderFactory recommenderFactory;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RecommenderServlet() {
		super();
	}

	private void initItemRecommender(HttpServletRequest request, HttpServletResponse response)
			throws ServletException {
		recommenderFactory = new RecommenderFactory(request);
		String recommenderTypeString = request.getParameter(Parameters.RECOMMENDER_TYPE);
		recommender = recommenderTypeString == null ? recommenderFactory.getItemRecommender(DEFAULT_RECOMMENDER)
				: recommenderFactory.getItemRecommender(recommenderTypeString);
		if (recommender == null) {
			throw new ServletException("Recommender-class is not defined");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		/*
		 * 1.设定选择的推荐器类型 2.不同的推荐器需要的不同参数设定 3.评估推荐结果的好坏，精确度和范围指标
		 */
		initItemRecommender(request, response);
		/* userID*/
		String userIDString = request.getParameter(Parameters.USER_ID);
		if (userIDString == null) {
			throw new ServletException("userID was not specified");
		}
		long userID = Long.parseLong(userIDString);
		/* itemID */
		String itemIDString = request.getParameter(Parameters.ITEM_ID);
		if (itemIDString == null) {
			throw new ServletException("itemID was not specified");
		}
		long itemID = Long.parseLong(itemIDString);
		/* howMany */
		String howManyString = request.getParameter(Parameters.COUNT);
		int howMany = howManyString == null ? DEFAULT_HOW_MANY : Integer.parseInt(howManyString);
		/* format */
		String format = request.getParameter(Parameters.FORMAT);
		if (format == null) {
			format = "json";
		}

		List<RecommendedItem> items = null;
		RecommenderEvaluator adEvaluator = null;
		RecommenderEvaluator emsEvaluator = null;
		RecommenderBuilder recommenderBuilder = null;
		double aadScore = 0.0;// 绝对平均差误差（越小越好）
		double emsScore = 0.0;// 均方根误差（越小越好）
		
		RecommenderIRStatsEvaluator irStatsEvaluator = null;
		IRStatistics stats = null;
		double precision = 0.0;// 查准率
		double recall = 0.0;// 查全率
		
		try {
			// evaluator recommender to get score.
			adEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
			emsEvaluator = new RMSRecommenderEvaluator();
			irStatsEvaluator = new GenericRecommenderIRStatsEvaluator();

			// fetch List<RecommendedItem>
			if (recommender instanceof BaseUserRecommender) {
				if(!new RateTable().hasRatedByUserID(String.valueOf(userID))){
					items = recommender.mostHotItems(itemID, howMany);
				} else {
					items = recommender.recommend(userID, howMany);
				}
				
				// evaluator recommender
/*				recommenderBuilder = new RecommenderBuilder() {
					@Override
					public Recommender buildRecommender(DataModel dataModel) throws TasteException {
						UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
						UserNeighborhood neighborhood = new NearestNUserNeighborhood(12, similarity, dataModel);
						return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
					}
				};
				// Use 80% of the data to train; test using the other 20%.
				// 1.0 -> controls how much of the overall input data is used.
				aadScore = adEvaluator.evaluate(recommenderBuilder, null, ((BaseUserRecommender) recommender).getmDataModel(), 0.8, 0.2);
				emsScore = emsEvaluator.evaluate(recommenderBuilder, null, ((BaseUserRecommender) recommender).getmDataModel(), 0.8, 0.2);
				stats = irStatsEvaluator.evaluate(recommenderBuilder, null, ((BaseUserRecommender) recommender).getmDataModel(), null, 12,
						GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.2);
				precision = stats.getPrecision();
				recall = stats.getRecall();
*/
			} else if (recommender instanceof BaseItemRecommender) {

				if(!new RateTable().hasRatedByUserID(String.valueOf(userID))){
					/* 最相似项目 */
					items = recommender.mostHotItems(itemID, howMany);
				} else {
					/* 推荐项目 */
					items = recommender.recommend(userID, howMany);
				}
				
				// evaluator recommender
/*				recommenderBuilder = new RecommenderBuilder() {
					@Override
					public Recommender buildRecommender(DataModel dataModel) throws TasteException {
						ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
						return new GenericItemBasedRecommender(dataModel, similarity);
					}
				};
				// Use 80% of the data to train; test using the other 20%.
				// 1.0 -> controls how much of the overall input data is used.
				aadScore = adEvaluator.evaluate(recommenderBuilder, null, ((BaseItemRecommender) recommender).getmDataModel(), 0.8, 0.2);
				emsScore = emsEvaluator.evaluate(recommenderBuilder, null, ((BaseItemRecommender) recommender).getmDataModel(), 0.8, 0.2);
				stats = irStatsEvaluator.evaluate(recommenderBuilder, null, ((BaseItemRecommender) recommender).getmDataModel(), null, 12,
						GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 0.2);
				precision = stats.getPrecision();
				recall = stats.getRecall();
*/
			}
			
			RecommendItemList itemList = new RecommendItemList(items);
			
			if ("text".equals(format)) {
				writePlainText(response, userID, items, itemList);
			} else if ("xml".equals(format)) {
				writeXML(response, items);
			} else if ("json".equals(format)) {
				writeJSON(response, itemList, aadScore, emsScore, precision, recall);
			} else {
				throw new ServletException("Bad format parameter: " + format);
			}
		} catch (TasteException te) {
			LOGGER.error("doGet() error!", te);
			throw new ServletException(te);
		} catch (IOException ioe) {
			LOGGER.error("doGet() error!", ioe);
			throw new ServletException(ioe);
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

	private static void writeJSON(HttpServletResponse response, RecommendItemList itemList, double aadScore, double emsScore, double precision, double recall) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		String result = "{\"sum\":" + itemList.getTotalSum() + ",\"items\":";
		result = result + itemList.toJSON();
		result = result + ",\"aadScore\":" + aadScore + ",\"emsScore\":" + emsScore;
		result = result + ",\"precision\":" + precision + ",\"recall\":" + recall;
		result = result + "}";
		System.out.println(result);
		writer.print(result);
	}

	private static void writeXML(HttpServletResponse response, Iterable<RecommendedItem> items) throws IOException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><recommendedItems>");
		for (RecommendedItem recommendedItem : items) {
			writer.print("<item><value>");
			writer.print(recommendedItem.getValue());
			writer.print("</value><id>");
			writer.print(recommendedItem.getItemID());
			writer.print("</id></item>");
		}
		writer.println("</recommendedItems>");
	}

	private void writePlainText(HttpServletResponse response, long userID, Iterable<RecommendedItem> items,
			RecommendItemList itemList) throws IOException, TasteException {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter writer = response.getWriter();
		writeRecommendations(itemList, writer);
	}

	private static void writeRecommendations(RecommendItemList itemList, PrintWriter writer) {
		for (RecommendItem recommendedItem : itemList.getRecommendItems()) {
			writer.println(recommendedItem.toString());
		}
	}

}
