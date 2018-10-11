package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecommenderIntro {
	public final static Logger logger = LoggerFactory.getLogger(RecommenderIntro.class);
	
	public final static int NEIGHBORHOOD_NUM = 2;// define neighborhood num
    public final static int RECOMMEND_NUM = 3;// define recommend num

	public static void main(String[] args) {
		RecommenderIntro recommenderIntro = new RecommenderIntro();
		try {
			recommenderIntro.entry(args);
		} catch (Exception e) {
			logger.error("RecommenderIntro error!!!", e);
		}
	}

	public void entry(String[] args) throws Exception {
		File modelFile = null;
//        String dataDir = RecommenderIntro.class.getClassLoader().getResource("").getPath();
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

//		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
//		UserNeighborhood neighborhood = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, similarity, model);
//		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);// 创建推荐引擎

		ItemSimilarity similarity = new EuclideanDistanceSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model, similarity);

		LongPrimitiveIterator iter = model.getUserIDs();
		int index = 12;
        while (iter.hasNext()&& index > 0) {
            long uid = iter.nextLong();
            List<RecommendedItem> list = recommender.recommend(uid, RECOMMEND_NUM);
            System.out.printf("uid:%s", uid);
            for (RecommendedItem ritem : list) {
                System.out.printf("(%s,%f)", ritem.getItemID(), ritem.getValue());
            }
            System.out.println();
            index--;
        }

	}

}
