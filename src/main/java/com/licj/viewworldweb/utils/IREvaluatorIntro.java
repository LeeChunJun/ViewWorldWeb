package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;

public class IREvaluatorIntro {
	public final static int NEIGHBORHOOD_NUM = 3;
    public final static int PRECISION_RECALL_NUM = 2;
    public final static long ENTRIES_MAX = 10000000L;

	private IREvaluatorIntro() {
	}

	public static void main(String[] args) throws Exception {
//		RandomUtils.useTestSeed();
		
		File modelFile = null;
//        String dataDir = IREvaluatorIntro.class.getClassLoader().getResource("").getPath();
		String dataDir = "D:/AppsData/2/eclipse-workspace/ViewWorldWeb/src/main/java/";
        if (args.length > 0)
            modelFile = new File(args[0]);
        if (modelFile == null || !modelFile.exists())
            modelFile = new File(dataDir + "resource/neteasy_rates.csv");
        if (!modelFile.exists()) {
            System.err.println("Please, specify name of file, or put file 'resource/neteasy_rate.csv' into current directory!");
            System.exit(1);
        }

        DataModel model = new FileDataModel(modelFile);

		RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
		
		// Build the same recommender for testing that we did last time:
		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model) throws TasteException {
				UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
				UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, model);
				return new GenericUserBasedRecommender(model, neighborhood, similarity);
			}
		};
		
		@SuppressWarnings("unused")
		RecommenderBuilder slopOneRecommenderBuilder = new RecommenderBuilder() {
			public Recommender buildRecommender(DataModel model) throws TasteException {
				DiffStorage diffStorage = new MemoryDiffStorage(
		                model, Weighting.WEIGHTED, ENTRIES_MAX);
		        return new SlopeOneRecommender(
		                model, Weighting.WEIGHTED, Weighting.WEIGHTED, diffStorage);
			}
		};
		
		// Evaluate precision and recall "at 2":
		IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, PRECISION_RECALL_NUM,
				GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);// Evaluate precision and recall at 12
		
		System.out.println(stats.getPrecision());
		System.out.println(stats.getRecall());
	}
}
