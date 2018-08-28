package com.licj.viewworldweb.recommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.licj.viewworldweb.model.table.ItemTable;

public class BaseItemRecommender extends ItemRecommender {

	/* GenericItemBasedRecommender对象 */
	private final GenericItemBasedRecommender mItemBasedRecommender;
	/* 输入参数 */
	private final DataModel mDataModel;
	private final ItemSimilarity mItemSimilarity;

	private BaseItemRecommender(Builder builder) {
		this.mDataModel = builder.mDataModel;
		this.mItemSimilarity = builder.mItemSimilarity;
		this.mItemBasedRecommender = new GenericItemBasedRecommender(mDataModel, mItemSimilarity);
	}

	public GenericItemBasedRecommender getmItemBasedRecommender() {
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

	// 返回最相似的items
	@Override
	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany) throws TasteException {
		List<RecommendedItem> result = new ArrayList<>();

		List<Long> items = new ItemTable().getMostSimilarItems(String.valueOf(itemID));
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
		mItemBasedRecommender.refresh(alreadyRefreshed);
	}

	@Override
	public DataModel getDataModel() {
		return mItemBasedRecommender.getDataModel();
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany);
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
		return mItemBasedRecommender.recommend(userID, howMany, rescorer);
	}
	
	@Override
	public float estimatePreference(long userID, long itemID) throws TasteException {
		return mItemBasedRecommender.estimatePreference(userID, itemID);
	}

	@Override
	public void removePreference(long userID, long itemID) throws TasteException {
		mItemBasedRecommender.removePreference(userID, itemID);
	}

	@Override
	public void setPreference(long userID, long itemID, float rating) throws TasteException {
		mItemBasedRecommender.setPreference(userID, itemID, rating);
	}

	@Override
	public String toString() {
		return "ItemBasedRecommender[recommender:" + mItemBasedRecommender + "]";
	}

}
