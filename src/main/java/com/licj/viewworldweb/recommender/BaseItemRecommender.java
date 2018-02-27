package com.licj.viewworldweb.recommender;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.LongPair;

public class BaseItemRecommender extends ItemRecommender {

	/* GenericItemBasedRecommender对象 */
	private final GenericBooleanPrefItemBasedRecommender mItemBasedRecommender; 
	/* 输入参数*/
	private final DataModel mDataModel;
	private final ItemSimilarity mItemSimilarity;

	private BaseItemRecommender(Builder builder) {
		this.mDataModel = builder.mDataModel;
		this.mItemSimilarity = builder.mItemSimilarity;
		this.mItemBasedRecommender = new GenericBooleanPrefItemBasedRecommender(mDataModel, mItemSimilarity);
	}
	
	public GenericBooleanPrefItemBasedRecommender getmItemBasedRecommender() {
		return mItemBasedRecommender;
	}

	public DataModel getmDataModel() {
		return mDataModel;
	}

	public ItemSimilarity getmItemSimilarity() {
		return mItemSimilarity;
	}

	public static class Builder {
		private DataModel mDataModel;
		private ItemSimilarity mItemSimilarity;

		public Builder() {
		}

		public Builder dataModel(DataModel dataModel) {
			this.mDataModel = dataModel;
			return this;
		}

		public Builder itemSimilarity(ItemSimilarity itemSimilarity) {
			this.mItemSimilarity = itemSimilarity;
			return this;
		}

		public BaseItemRecommender build() {
			return new BaseItemRecommender(this);
		}

	}

	/* 业务操作 */
	
	
	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		mItemBasedRecommender.refresh(alreadyRefreshed);
	}

	@Override
	public float estimatePreference(long userID, long itemID) throws TasteException {
		return mItemBasedRecommender.estimatePreference(userID, itemID);
	}

	@Override
	public DataModel getDataModel() {
		return mItemBasedRecommender.getDataModel();
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany);
	}

/*	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, boolean includeKnownItems) throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany, includeKnownItems);
	}*/

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany, rescorer);
	}

/*	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer, boolean includeKnownItems)
			throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany, rescorer, includeKnownItems);
	}*/

	@Override
	public void removePreference(long userID, long itemID) throws TasteException {
		mItemBasedRecommender.removePreference(userID, itemID);
	}

	@Override
	public void setPreference(long userID, long itemID, float rating) throws TasteException {
		mItemBasedRecommender.setPreference(userID, itemID, rating);		
	}
	
	@Override
	public ItemSimilarity getItemSimilarity() {
		return mItemBasedRecommender.getSimilarity();
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany) throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemID, howMany);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany, Rescorer<LongPair> rescorer)
			throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemID, howMany, rescorer);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany) throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemIDs, howMany);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, Rescorer<LongPair> rescorer)
			throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemIDs, howMany, rescorer);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, boolean excludeItemIfNotSimilarToAll)
			throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemIDs, howMany, excludeItemIfNotSimilarToAll);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, Rescorer<LongPair> rescorer,
			boolean excludeItemIfNotSimilarToAll) throws TasteException {
		return mItemBasedRecommender.mostSimilarItems(itemIDs, howMany, rescorer, excludeItemIfNotSimilarToAll);
	}

	@Override
	public List<RecommendedItem> recommendedBecause(long userID, long itemID, int howMany) throws TasteException {
		return mItemBasedRecommender.recommendedBecause(userID, itemID, howMany);
	}
	
	@Override
	public String toString(){
		return "ItemBasedRecommender[recommender:" + mItemBasedRecommender + "]";
	}
	

}
