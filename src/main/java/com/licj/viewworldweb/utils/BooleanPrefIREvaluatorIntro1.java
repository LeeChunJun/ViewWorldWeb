package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.io.File;

class BooleanPrefIREvaluatorIntro1 {

	private BooleanPrefIREvaluatorIntro1() {
	}

	public static void main(String[] args) throws Exception {
		// Use GenericBooleanPrefDataModel, based on same data
		DataModel model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel
				.toDataMap(new FileDataModel(new File("E:/12/ViewWorldWeb/src/main/java/resource/music_rate.csv"))));

		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
//		RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
		
//		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
//			@Override
//			public Recommender buildRecommender(DataModel model) throws TasteException {
//				UserSimilarity similarity = new LogLikelihoodSimilarity(model);
//				UserNeighborhood neighborhood = new NearestNUserNeighborhood(1, similarity, model);
//				return new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
//			}
//		};
		
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			@Override
			public Recommender buildRecommender(DataModel model) throws TasteException {
				ItemSimilarity similarity = new TanimotoCoefficientSimilarity(model);
				return new GenericBooleanPrefItemBasedRecommender(model, similarity);
			}
		};

		DataModelBuilder modelBuilder = new DataModelBuilder() {
			@Override
			public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
				// Build a GenericBooleanPrefDataModel here too
				return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
			}
		};

		double score = evaluator.evaluate(recommenderBuilder, modelBuilder, model, 0.9, 1.0);
		System.out.println(score);
	}

}