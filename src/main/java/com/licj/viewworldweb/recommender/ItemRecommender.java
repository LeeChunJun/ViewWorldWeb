package com.licj.viewworldweb.recommender;

import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.LongPair;

public abstract class ItemRecommender {

	/* Recommender API(9) */

	public void refresh(Collection<Refreshable> alreadyRefreshed) {
	}

	public float estimatePreference(long userID, long itemID) throws TasteException {
		return 0;
	}

	public DataModel getDataModel() {
		return null;
	}

	public List<RecommendedItem> recommend(long userID, int howMany) throws TasteException {
		return null;
	}

	public List<RecommendedItem> recommend(long userID, int howMany, boolean includeKnownItems) throws TasteException {
		return null;
	}

	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer) throws TasteException {
		return null;
	}

	public List<RecommendedItem> recommend(long userID, int howMany, IDRescorer rescorer, boolean includeKnownItems)
			throws TasteException {
		return null;
	}

	public void removePreference(long userID, long itemID) throws TasteException {
	}

	public void setPreference(long userID, long itemID, float rating) throws TasteException {
	}

	/* UserBasedRecommender API(4) */
	
	public List<RecommendedItem> mostHotItems(long itemID, int howMany) {
		return null;
	}

	public UserSimilarity getUserSimilarity() {
		return null;
	}

	public long[] mostSimilarUserIDs(long userID, int howMany) throws TasteException {
		return null;
	}

	public long[] mostSimilarUserIDs(long userID, int howMany, Rescorer<LongPair> rescorer) throws TasteException {
		return null;
	}

	@Override
	public String toString() {
		return null;
	}

	/* ItemBasedRecommender API(8) */

	public ItemSimilarity getItemSimilarity() {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany) throws TasteException {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany, Rescorer<LongPair> rescorer)
			throws TasteException {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany) throws TasteException {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, Rescorer<LongPair> rescorer)
			throws TasteException {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, boolean excludeItemIfNotSimilarToAll)
			throws TasteException {
		return null;
	}

	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany, Rescorer<LongPair> rescorer,
			boolean excludeItemIfNotSimilarToAll) throws TasteException {
		return null;
	}

	public List<RecommendedItem> recommendedBecause(long userID, long itemID, int howMany) throws TasteException {
		return null;
	}

}
