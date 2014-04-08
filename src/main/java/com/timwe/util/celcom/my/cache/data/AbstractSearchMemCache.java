package com.timwe.util.celcom.my.cache.data;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.mindergy.cache.MemCacheImpl;
import com.mindergy.exception.CacheException;
import com.mindergy.interfaces.ICache;
import com.timwe.util.celcom.my.cache.impl.MultiSearchCache;
import com.timwe.util.celcom.my.cache.impl.SearchSettingsImpl;
import com.timwe.util.celcom.my.search.data.SearchRequest;

/**
 * Explicit loading data into guava. This class is not finalized.
 * @author yoong.han
 *
 */
public abstract class AbstractSearchMemCache<T>  implements MultiSearchCache<T> {
	
	private final int RESET_COUNTER=0;
	private final int FIRST_LOAD_COUNTER=1;
	private final int MAX_RETRIES = 20;
	
	/** The cache interface */
	private final ICache cache = new MemCacheImpl(AbstractSearchMemCache.class.getSimpleName());
	/** Count for releases */
	private AtomicInteger atomicCounter = new AtomicInteger(0);
	
	/**
	 * Get the value of a key from the cache
	 * @param key
	 * @return value
	 */
	private List<SearchRequest<T>> getFromCache(String site) {
		
		List<SearchRequest<T>> value = retrieveValue(site);

		if(value == null){
			int counter = atomicCounter.incrementAndGet();
			if(counter == FIRST_LOAD_COUNTER ||  counter> MAX_RETRIES){
				
				try{
					value = loadValues(site);
					putOnCache(site, value);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				atomicCounter.set(RESET_COUNTER);
				
			}else{
				sleep();
				retrieveValue(site);
			}
		}
		
		return value;
	}
	
	@SuppressWarnings("unchecked")
	private List<SearchRequest<T> > retrieveValue(String site){
		try {
			//have to be casted, this object is only List<SearchRequest>
			return (List<SearchRequest<T>>)cache.get(SearchSettingsImpl.FQN, site);
		} catch (CacheException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Put a wait
	 */
	private void sleep(){
		try{
			Thread.sleep(SearchSettingsImpl.SLEEP_MILISEC_TIME);
		}catch(Exception e){
			//do nothing.
		}
	}
	
	/**
	 * Put a key and its pair on the cache
	 * @param key
	 * @param value
	 */
	public void putOnCache(String site, List<SearchRequest<T>> value) {
		try {
			cache.put(SearchSettingsImpl.FQN, 
					site, 
					value, 
					getExpiryDate(new Date()));
		} catch (CacheException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calculate the expiry date
	 * @param dt
	 * @return
	 */
	private Date getExpiryDate(Date dt) {
		long spanTime = TimeUnit.MILLISECONDS.convert(SearchSettingsImpl.SPAN_TIME,SearchSettingsImpl.SPAN_TIME_UNIT);
		return new Date(dt.getTime()+ spanTime);
	}

	/*
	 * Return the values from cached site.
	 * (non-Javadoc)
	 * @see com.timwe.util.celcom.my.cache.impl.MultiSearchCache#getByValuesBySite(java.lang.String)
	 */
	public List<SearchRequest<T>> getByValuesBySite(String site) {
		return getFromCache(site);
	};
}
