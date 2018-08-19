package com.licj.viewworldweb.utils;

import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.model.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.cf.taste.similarity.*;
import java.io.*;
import java.util.*;

public class RecommenderIntro {

	public static void main(String[] args) {
		RecommenderIntro recommenderIntro = new RecommenderIntro();
		try {
			recommenderIntro.entry(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void entry(String[] args) throws Exception {
		File modelFile = null;
		if (args.length > 0)
			modelFile = new File(args[0]);
		if (modelFile == null || !modelFile.exists())
			modelFile = new File("D:/12-licj/eclipse-workspace/ViewWorldWeb/src/main/java/resource/ratings.csv");
//		if (!modelFile.exists()) {
//			System.err.println("Please, specify name of file, or put file 'input.csv' into current directory!");
//			System.exit(1);
//		}
		
		DataModel model = new FileDataModel(modelFile);// 加载数据文件

//		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
//		UserNeighborhood neighborhood = new NearestNUserNeighborhood(12, similarity, model);
//		Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);// 创建推荐引擎

		ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
		Recommender recommender = new GenericItemBasedRecommender(model, similarity);
		
		List<RecommendedItem> recommendations = recommender.recommend(102, 130);// 对于用户12，推荐12个项目

		for (RecommendedItem recommendation : recommendations) {
			System.out.println(recommendation);
		}

	}

}
