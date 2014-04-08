package com.timwe.util.celcom.my.search.data;

import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.base.Optional;
import com.timwe.util.celcom.my.cache.impl.SearchSettingsImpl;

/**
 * Created a thread enabled class that is able to service small instances of the
 * search partition.
 * @author yoong.han
 *
 */
public class SearchWorker<T> implements Callable<SearchResultList<T>> {

	private final String[] searchKeys;
	private final List<SearchRequest<T>> searchList;
		
	public SearchWorker(List<SearchRequest<T>> searchList, String...searchKeys){
		this.searchKeys = searchKeys;
		this.searchList = searchList;
	}
	
	/**
	 * Invoked by Callable service to search the entire list for match.
	 * @return entire matched list of only SearchResultList.
	 * @see EnumSearchResultOrder, SearchResultList
	 */
	public SearchResultList<T> call() {
		SearchResultList<T> containment = new SearchResultList<T>(SearchSettingsImpl.EST_RESULT_MAX_SIZE);
		
		for(SearchRequest<T> value: searchList){
			String keyValue = value.getKey();
			if(Optional.fromNullable(keyValue).isPresent()
					&& isMatch(keyValue, searchKeys)){
				containment.add(value.getValue());
			}
		}
		
		return containment;
	}
	
	/**
	 * Check if value searched matches within the string.
	 * @return boolean if match return true
	 */
	private boolean isMatch(String searchValue, String...searchKeys){
		boolean matching = false;

		for(String search_key: searchKeys){
			matching = searchValue.contains(search_key);
			
			if(matching)//avoid multiple loops
				break;
		}
		return matching;
	}

}
