package com.licj.viewworldweb.servlet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.licj.viewworldweb.recommender.BaseItemRecommender;
import com.licj.viewworldweb.recommender.BaseUserRecommender;
import com.licj.viewworldweb.recommender.ItemJDBCDataModel;
import com.licj.viewworldweb.recommender.ItemRecommender;

public class RecommenderFactory {
	private static final Logger LOGGER = Logger.getLogger(RecommenderFactory.class);
	private HttpServletRequest request;

	public RecommenderFactory(HttpServletRequest request) {
		this.request = request;
	}

	public ItemRecommender getItemRecommender(String recommenderClassName) {
		try {
			if (recommenderClassName.equals(Parameters.UserBased_REC)) {
				return getBaseUserRecommender(request);
			} else if (recommenderClassName.equals(Parameters.ItemBased_REC)) {
				return getBaseItemRecommender(request);
			}
		} catch (TasteException e) {
			LOGGER.error("getItemRecommender() error!", e);
		}
		return null;
	}

	@SuppressWarnings("unused")
	public ItemRecommender getBaseUserRecommender(HttpServletRequest request) throws TasteException {
		DataModel dataModel = new ItemJDBCDataModel().getDataModel();
//		DataModel dataModel = new ItemFileDataModel().getDataModel();
		UserSimilarity similarity = null;
		UserNeighborhood neighborhood = null;
		ItemRecommender recommender = null;

		String similarityString = request.getParameter(Parameters.Similarity_TYPE);
		String neighborhoodString = request.getParameter(Parameters.UserNeighborhood_TYPE);
		String weightedString = request.getParameter(Parameters.IS_WEIGHTED);
		String nearestNString = request.getParameter(Parameters.NearestN);
		String thresholdString = request.getParameter(Parameters.Threshold);

		if (similarityString == null && neighborhoodString == null) {
			similarity = new LogLikelihoodSimilarity(dataModel);
			neighborhood = new NearestNUserNeighborhood(12, similarity, dataModel);
			recommender = new BaseUserRecommender.Builder().dataModel(dataModel).userNeighborhood(neighborhood)
					.userSimilarity(similarity).build();
		}

		return recommender;
	}

	@SuppressWarnings("unused")
	public ItemRecommender getBaseItemRecommender(HttpServletRequest request) throws TasteException {
		DataModel dataModel = new ItemJDBCDataModel().getDataModel();
//		DataModel dataModel = new ItemFileDataModel().getDataModel();
		ItemSimilarity similarity = null;
		ItemRecommender recommender = null;

		String similarityString = request.getParameter(Parameters.Similarity_TYPE);
		String weightedString = request.getParameter(Parameters.IS_WEIGHTED);

		if (similarityString == null) {
			similarity = new LogLikelihoodSimilarity(dataModel);
			recommender = new BaseItemRecommender.Builder().dataModel(dataModel).itemSimilarity(similarity).build();
		}

		return recommender;
	}

}
