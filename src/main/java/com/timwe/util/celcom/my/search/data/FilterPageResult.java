package com.timwe.util.celcom.my.search.data;

import java.util.List;

import com.google.common.collect.Lists;

public class FilterPageResult<T> {

	private int totalRecords=0;
	private List<T> resultList=null;
	
	public FilterPageResult(List<T> resultList, int start, int range){
		if(resultList != null){
			this.totalRecords = resultList.size();
			this.resultList = filterRecords(resultList,start,range);
		}
	}
	
	/**
	 * Retrieve the total records
	 * @return
	 */
	public int getTotalRecords(){
		return totalRecords;
	}
	
	/**
	 * Return the list of result.
	 * @param resultList
	 * @param start
	 * @param end
	 * @return
	 */
	public List<T> filterRecords(List<T> resultList, int start, int range){
		int totalRecords = getTotalRecords();
		
		if(start > totalRecords || start < 0){
			return Lists.newArrayList();
		}
		
		if(start+range > totalRecords){
			range = totalRecords - start ;
		}
		
		return resultList.subList(start, start+range);
	}
	
	/**
	 * Return total results
	 * @return
	 */
	public List<T> getRecords(){
		return this.resultList;
	}
}
