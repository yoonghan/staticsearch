package com.timwe.util.celcom.my.search.data;

import java.io.Serializable;

public class SearchRequest<T> implements Serializable{

	/**
	 * This class is serializable so that it can be inserted into memcached.
	 */
	private static final long serialVersionUID = -3772282776819742389L;
	
	final String key;
	final T value;
	
	public SearchRequest(String key, T value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey(){
		return key;
	}
	
	public T getValue(){
		return value;
	}
}
