package com.timwe.util.celcom.my.cache.impl;

import java.util.List;

import com.timwe.util.celcom.my.search.data.SearchRequest;

public interface MultiSearchCache<T> {
	public List<SearchRequest<T>> getByValuesBySite(String site);
	
	public abstract List<SearchRequest<T>> loadValues(String site);
}
