package com.licj.viewworldweb.servlet;

public class Parameters {
	public static final String EMAIL = "email";//(selectA)
	public static final String PHONE = "phone";//(selectB)
	public static final String PASSWORD = "password";//(require)
	public static final String USER_ID = "userID";//(require)
	public static final String ITEM_ID = "itemID";
	
	public static final String COUNT = "count";//(option->10)
	public static final String FORMAT = "format";//(option->text)
	
	/**
	 * Abstract-{}:聚焦实现具体功能函数的抽象类
	 * Caching-{}:缓存相对应的类
	 * Generic-{}:常规实现相应类
	 */
	public static final String RECOMMENDER_TYPE = "recommenderType";//(option->@UserBased_REC)
	public static final String UserBased_REC = "GenericUserBasedRecommender";//(default)
	public static final String ItemBased_REC = "GenericItemBasedRecommender";
	public static final String SVD_REC = "SVDRecommender";
	public static final String BooleanPrefUser_REC = "GenericBooleanPrefUserBasedRecommender";
	public static final String BooleanPrefItem_REC = "GenericBooleanPrefItemBasedRecommender";
	public static final String ItemAverage_REC = "ItemAverageRecommender";
	public static final String ItemUserAverage_REC = "ItemUserAverageRecommender";
	public static final String Random_REC = "RandomRecommender";/* 参考类型 */
	
	public static final String UserNeighborhood_TYPE = "UserNeighborhoodType";//(option|require<-@UserBased_REC)
	public static final String NearestNUser_NHD = "NearestNUserNeighborhood";//(default)
	public static final String ThresholdUser_NHD = "ThresholdUserNeighborhood";
	
	public static final String Similarity_TYPE = "SimilarityType";//(require)
	public static final String GenericUser_SIM = "GenericUserSimilarity";
	public static final String GenericItem_SIM = "GenericItemSimilarity";
	public static final String FileItem_SIM = "FileItemSimilarity";
	public static final String MBI_SIM = "MultithreadedBatchItemSimilarities";
	public static final String CityBlock_SIM = "CityBlockSimilarity";
	public static final String PearsonCorrelation_SIM = "PearsonCorrelationSimilarity";//(default|require<-@UserBased_REC)
	public static final String EuclideanDistance_SIM = "EuclideanDistanceSimilarity";
	public static final String SpearmanCorrelation_SIM = "SpearmanCorrelationSimilarity";
	public static final String LogLikelihood_SIM = "LogLikelihoodSimilarity";
	public static final String TanimotoCoefficient_SIM = "TanimotoCoefficientSimilarity";
	public static final String UncenteredCosine_SIM = "UncenteredCosineSimilarity";
	
	public static final String DataModel_TYPE = "DataModelType";
	public static final String File_DM = "FileDataModel";
	public static final String GenericBooleanPref_DM = "GenericBooleanPrefDataModel";
	public static final String Generic_DM = "GenericDataModel";
	public static final String PACU_DM = "PlusAnonymousConcurrentUserDataModel";
	public static final String PAU_DM = "PlusAnonymousUserDataModel";
	
	public static final String IS_WEIGHTED = "Weight";//(option:true|false)
	public static final String NearestN = "NearestN";//(option:int)
	public static final String Threshold = "Threshold";//(option:double{value < 1.0})
	
	
}
