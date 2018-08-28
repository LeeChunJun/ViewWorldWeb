package com.licj.viewworldweb.recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.licj.viewworldweb.model.table.RateTable;

public class BaseUserRecommender extends ItemRecommender {

	/* GenericUserBasedRecommender对象 */
	private final GenericUserBasedRecommender mUserBasedRecommender; 
	/* 输入参数*/
	private final DataModel mDataModel;
	private final UserNeighborhood mNeighborhood;
	private final UserSimilarity mUserSimilarity;

	private BaseUserRecommender(Builder builder) {
		this.mDataModel = builder.mDataModel;
		this.mNeighborhood = builder.mNeighborhood;
		this.mUserSimilarity = builder.mUserSimilarity;
		this.mUserBasedRecommender = new GenericUserBasedRecommender(mDataModel, mNeighborhood, mUserSimilarity);
	}

	public GenericUserBasedRecommender getmUserBasedRecommender() {
		return mUserBasedRecommender;
	}

	public DataModel getmDataModel() {
		return mDataModel;
	}

	public UserNeighborhood getmNeighborhood() {
		return mNeighborhood;
	}

	public UserSimilarity getmUserSimilarity() {
		return mUserSimilarity;
	}

	public static class Builder {
		private DataModel mDataModel;
		private UserNeighborhood mNeighborhood;
		private UserSimilarity mUserSimilarity;

		public Builder() {
		}

		public Builder dataModel(DataModel dataModel) {
			this.mDataModel = dataModel;
			return this;
		}

		public Builder userNeighborhood(UserNeighborhood neighborhood) {
			this.mNeighborhood = neighborhood;
			return this;
		}

		public Builder userSimilarity(UserSimilarity userSimilarity) {
			this.mUserSimilarity = userSimilarity;
			return this;
		}

		public BaseUserRecommender build() {
			return new BaseUserRecommender(this);
		}

	}
	
	// 返回评价最多的Items
	@Override
	public List<RecommendedItem> mostHotItems(long itemID, int howMany) {
		List<RecommendedItem> result = new ArrayList<>();
		
		List<Long> items = new RateTable().getMostRateItems();
		for (int i = 0; i < howMany; i++) {
			long id = items.get(i);
			result.add(new RecommendedItem() {
				
				@Override
				public float getValue() {
					return 5.0f;
				}
				
				@Override
				public long getItemID() {
					return id;
				}
			});
		}
		
		return result;
	}

	/* 业务操作 */
	
	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed) {
		mUserBasedRecommender.refresh(alreadyRefreshed);
	}

	@Override
	public DataModel getDataModel() {
		return mUserBasedRecommender.getDataModel();
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
		return mUserBasedRecommender.recommend(userID, howMany);
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
		return mUserBasedRecommender.recommend(userID, howMany, rescorer);
	}
	
	@Override
	public float estimatePreference(long userID, long itemID) throws TasteException {
		return mUserBasedRecommender.estimatePreference(userID, itemID);
	}

	@Override
	public void removePreference(long userID, long itemID) throws TasteException {
		mUserBasedRecommender.removePreference(userID, itemID);
	}

	@Override
	public void setPreference(long userID, long itemID, float rating) throws TasteException {
		mUserBasedRecommender.setPreference(userID, itemID, rating);
	}

	@Override
	public String toString(){
		return "UserBasedRecommender[recommender:" + mUserBasedRecommender + "]";
	}


}
