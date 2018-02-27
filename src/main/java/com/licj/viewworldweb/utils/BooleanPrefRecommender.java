package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

class BooleanPrefRecommender {

	private BooleanPrefRecommender() {
	}

	public static void main(String[] args) throws Exception {
		DataModel dataModel = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel
				.toDataMap(new FileDataModel(new File("E:/12/ViewWorldWeb/src/main/java/resource/music_rate.csv"))));
		UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
		Recommender recommender = new GenericBooleanPrefUserBasedRecommender(dataModel, neighborhood, similarity);
		
//		ItemSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
//		Recommender recommender = new GenericBooleanPrefItemBasedRecommender(dataModel, similarity);
		
		List<RecommendedItem> recommendations = recommender.recommend(280190780, 12);
		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}
	}
}