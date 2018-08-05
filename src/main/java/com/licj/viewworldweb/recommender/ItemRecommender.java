package com.licj.viewworldweb.recommender;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

public class ItemRecommender implements Recommender {

	/* Recommender API */
	@Override
	public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
		return null;
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
		return null;
	}

	@Override
	public float estimatePreference(long userID, long itemID) throws TasteException {
		return 0;
	}

	@Override
	public void removePreference(long userID, long itemID) throws TasteException {

	}

	@Override
	public void setPreference(long userID, long itemID, float rating) throws TasteException {

	}

	@Override
	public DataModel getDataModel() {
		return null;
	}

	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed) {

	}

	/* UserBasedRecommender API */

	/**
	 * 返回评价最多的Items
	 * 
	 * @param itemID
	 * @param howMany
	 * @return
	 */

	public List<RecommendedItem> mostHotItems(long itemID, int howMany) {
		return null;
	}

	/* ItemBasedRecommender API(8) */

	/**
	 * 返回最相似的items
	 * 
	 * @param itemID
	 * @param howMany
	 * @return
	 * @throws TasteException
	 */

	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany) throws TasteException {
		return null;
	}

}
