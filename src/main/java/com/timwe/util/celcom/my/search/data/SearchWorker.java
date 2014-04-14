package com.timwe.util.celcom.my.search.data;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import com.timwe.util.celcom.my.cache.impl.SearchSettingsImpl;

/**
 * Created a thread enabled class that is able to service small instances of the
 * search partition.
 * @author yoong.han
 *
 */
public class SearchWorker<T> implements Callable<SearchResultList<T>> {

	//private final String[] searchKeys;
	private final List<SearchRequest<T>> searchList;
	private Pattern[] searchPattern;
		
	public SearchWorker(List<SearchRequest<T>> searchList, String...searchKeys){
		//this.searchKeys = searchKeys;
		this.searchList = searchList;
		this.searchPattern = buildSearchPattern(searchKeys);
	}
	
	/*
	 * Only search via word not keys.
	 */
	private Pattern[] buildSearchPattern(String...keywords) {
		Pattern[] buildKeyPatterns;
		
		if(keywords.length == 0){
			buildKeyPatterns = new Pattern[1];
			buildKeyPatterns[0] = Pattern.compile("");
			return buildKeyPatterns;
		}
		
		buildKeyPatterns = new Pattern[keywords.length];
		int loop=0;
		for(String keyword: keywords){
			keyword = Pattern.quote(keyword);	//disable all special characters
			String patternWord = String.format("%s.*|.* %s .*|.*%s[,|.|;]|.*%s|^%s$",keyword,keyword,keyword,keyword,keyword);
			buildKeyPatterns[loop] = Pattern.compile(patternWord);
			loop++;
		}
		return buildKeyPatterns;
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
					&& isMatch(keyValue, searchPattern)){
				containment.add(value.getValue());
			}
		}
		
		return containment;
	}
	
	/**
	 * Check if value searched matches within the string.
	 * @return boolean if match return true
	 */
	private boolean isMatch(String searchValue, Pattern[] searchKeys){
		for(Pattern search_key: searchKeys){
			if(search_key.matcher(searchValue).matches()){
				return true;	//avoid multiple loops
			}
		}
		return false;
	}

}
