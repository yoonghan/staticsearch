package com.timwe.util.celcom.my.search.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.timwe.util.celcom.my.cache.impl.MultiSearchCache;
import com.timwe.util.celcom.my.cache.impl.SearchSettingsImpl;
import com.timwe.util.celcom.my.concurrency.util.ConcurrencyUtil;
import com.timwe.util.celcom.my.search.data.FilterPageResult;
import com.timwe.util.celcom.my.search.data.SearchRequest;
import com.timwe.util.celcom.my.search.data.SearchResultList;
import com.timwe.util.celcom.my.search.data.SearchWorker;

public class SearchUtility {
	
	//Starts a thread executor.
	private final static ExecutorService threadPool = ConcurrencyUtil.getSimpleService(SearchSettingsImpl.DEFAULT_THREAD_SIZE);
		
	/**
	 * Search based on site and return result
	 * @param cacheInput
	 * @param site
	 * @param searchKeys
	 * @return
	 */
	public static <T> List<T> searchInputWithSiteAndKey(MultiSearchCache<T> cacheInput, String site, String...searchKeys){
		return searchInputWithKey(cacheInput.getByValuesBySite(site), searchKeys);
	}
	
	/**
	 * Search based on site and return result with a range and compared based on a comparator.
	 * @param cacheInput
	 * @param site
	 * @param searchKeys
	 * @return
	 */
	public static <T> FilterPageResult<T> searchInputWithSiteAndKey(MultiSearchCache<T> cacheInput, Comparator<T> cmp, String site, int start, int range, String...searchKeys){
		List<T> listReturned =  searchInputWithKey(cacheInput.getByValuesBySite(site), searchKeys);
		Collections.sort(listReturned, cmp);
		FilterPageResult<T> filteredResult = new FilterPageResult<T>(listReturned, start, range);
		return filteredResult;
	}
	
	/**
	 * 
	 * @param input
	 * @param searchKey
	 * @return
	 */
	public static <T> List<T> searchInputWithKey(List<SearchRequest<T>> input,String...searchKeys) {
		Set<T> returnedList = null;

		try{
			validateInput(input, searchKeys);
				
			CompletionService<SearchResultList<T>>  completionService
			= new ExecutorCompletionService<SearchResultList<T>>(threadPool);;
			
			List<SearchRequest<T>> keyList = input;
			
			//split into smaller list and segregate to processors core.
			List<List<SearchRequest<T>>> partitionList = Lists.partition(keyList, SearchSettingsImpl.PARTITION_SIZE);
			int leafCount = partitionList.size(); 
	        for(List<SearchRequest<T>> subList : partitionList){
	        	completionService.submit(createWorker(subList, searchKeys));
			}
	        
	        //remove duplicates.
	        returnedList = new SearchResultList<T>(SearchSettingsImpl.EST_RESULT_MAX_SIZE); 
	        for(int i =1;i<=leafCount;i++){
	        	try {
	        		Future<SearchResultList<T>> workerResultFut   = completionService.take();
	        		SearchResultList<T>         workerResult      = workerResultFut.get();
	        		//get all the returned results.
	        		returnedList.addAll(workerResult);
				} catch (Exception e) {
					//do nothing if exception.
					e.printStackTrace();
				}
	        }
		}catch(IllegalArgumentException iae){
			//do nothing if caught this exception.
			iae.printStackTrace();
		}
        
		//returned results are never a duplicate.
        return transformReturnedResult(returnedList);
	}
	
	private static <T> ArrayList<T> transformReturnedResult(Set<T> resultList){
		if(resultList != null && resultList.size() > 0){
			return new ArrayList<T>(resultList);
		}
		return Lists.newArrayList();
	}
	
	protected static <T> Callable<SearchResultList<T>> createWorker(List<SearchRequest<T>>subList, String...searchKeys){
		return new SearchWorker<T>(subList, searchKeys); 
	}
	
	/**
	 * Validate all input are true
	 * @param input
	 * @param searchKeys
	 * @return true if valid
	 * @throws IllegalArgumentException
	 */
	protected static <T> boolean validateInput(List<SearchRequest<T>> input, String...searchKeys) throws IllegalArgumentException{
		Preconditions.checkNotNull(searchKeys);
		Preconditions.checkNotNull(input);
		return true;
	}
	
}
