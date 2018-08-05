package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;

public class EvaluatorIntro {

	private EvaluatorIntro() {
	}

	public static void main(String[] args) throws Exception {
//		RandomUtils.useTestSeed();// 仅用于可重复结果的示例

		File modelFile = null;
		if (args.length > 0)
			modelFile = new File(args[0]);
		if (modelFile == null || !modelFile.exists()){
			modelFile = new File("D:/12-licj/eclipse-workspace/ViewWorldWeb/src/main/java/resource/ratings.csv");
		}
//		if (!modelFile.exists()) {
//			System.err.println("Please, specify name of file, or put file 'input.csv' into current directory!");
//			System.exit(1);
//		}

		DataModel model = new FileDataModel(modelFile);

		RecommenderEvaluator adEvaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderEvaluator emsEvaluator = new RMSRecommenderEvaluator();

		// Build the same recommender for testing that we did last time:
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			// 构建与上面相同的推荐器
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(12, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};

		@SuppressWarnings("unused")
		RecommenderBuilder slopOneRecommenderBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model) throws TasteException {
				return new SlopeOneRecommender(model);
			}
		};

		// Use 70% of the data to train; test using the other 30%.
		double aadScore = adEvaluator.evaluate(recommenderBuilder, null, model, 0.8, 0.9);// 使用80％的数据来训练;测试其他20％
		double emsScore = emsEvaluator.evaluate(recommenderBuilder, null, model, 0.8, 0.9);
		System.out.println(aadScore);
		System.out.println(emsScore);
	}
}
