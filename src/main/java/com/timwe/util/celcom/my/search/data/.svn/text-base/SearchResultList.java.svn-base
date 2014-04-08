package com.timwe.util.celcom.my.search.data;

import java.util.HashSet;

import com.google.common.base.Optional;

/**
 * Controls the returned list type.
 * @author yoong.han
 *
 */
public class SearchResultList<T> extends HashSet<T>{
	
	/**
	 * Serial Id Generated List
	 */
	private static final long serialVersionUID = 3084406823492987879L;
	
	public SearchResultList(int size){
		super(size);
	}
	
	@Override
	/*
	 * Add values only if not null.
	 */
	public boolean add(T value) {
		boolean addedValue=false;
		if(Optional.fromNullable(value).isPresent())
			addedValue = super.add(value);
		return addedValue;
	}
}
