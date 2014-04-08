package com.timwe.util.celcom.my.cache.data;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.timwe.util.celcom.my.cache.impl.MultiSearchCache;
import com.timwe.util.celcom.my.cache.impl.SearchSettingsImpl;
import com.timwe.util.celcom.my.search.data.SearchRequest;

/**
 * Explicit loading data into guava. This class is not finalized.
 * @author yoong.han
 *
 */
public abstract class AbstractSearchGuavaCache<T> 
	implements MultiSearchCache<T>{
	
	private final LoadingCache<String,List<SearchRequest<T>>> cacheHolder = getCacheBuilder().build(new SearchCacheLoader());
	
	public List<SearchRequest<T>> getByValuesBySite(String site){
		List<SearchRequest<T>> value = null;
		try {
			value = cacheHolder.get(site);
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getCause());
		}
		return value;
	}
	
	protected  LoadingCache<String,List<SearchRequest<T>>>  getCache(){
		return cacheHolder;
	}
	
	
	/**
	 * Creates a cache builder object.
	 */
	protected CacheBuilder<Object, Object> getCacheBuilder() {
		return CacheBuilder.newBuilder()
		.maximumSize(SearchSettingsImpl.MAX_SIZE)
		.expireAfterAccess(SearchSettingsImpl.SPAN_TIME, SearchSettingsImpl.SPAN_TIME_UNIT);
	}
			
	/**
	 * The key here will be reference as a site.
	 * @author yoong.han
	 *
	 */
	class SearchCacheLoader extends CacheLoader<String, List<SearchRequest<T>>>{
		public List<SearchRequest<T>> load(String site) throws Exception {
			List<SearchRequest<T>> loadedValues = loadValues(site);
			return (loadedValues == null?null:Collections.unmodifiableList(loadedValues));
		}
	}
}
