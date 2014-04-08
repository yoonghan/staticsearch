package search;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import search.init.SearchGuavaCache;
import search.init.SearchMultiObjectGuavaCache;
import search.init.SimpleDataType;

import com.timwe.util.celcom.my.search.data.FilterPageResult;
import com.timwe.util.celcom.my.search.util.SearchUtility;

public class TestCases {
		
	static SearchGuavaCache cacheSearcher = new SearchGuavaCache();
	static SearchMultiObjectGuavaCache cacheSearcherMultiObject = new SearchMultiObjectGuavaCache();
	
	/**
	 * Test for valid returned value.
	 */
	@Test
	public void searchValidCounts(){
		//List<SearchRequest> defaultSearch = cacheSearcher.loadSite_10K_Example();
		List<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, "site","999");
		Assert.assertEquals(values.size(), 18);
	}
	
	/**
	 * Test for invalid returned value.
	 */
	@Test
	public void searchErrorCounts(){
		List<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, "site", "RASHIDI");
		Assert.assertEquals(values.size(), 0);
	}
	
	/**
	 * Test for two keys.
	 */
	@Test
	public void searchThreeKeysWithValidCounts(){
		List<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, "site", "RASHIDI", "100","999");
		Assert.assertEquals(values.size(), 38);
	}
	
	/**
	 * Test to search for only first 20
	 */
	@Test
	public void searchFirst20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 0, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 20);
		Assert.assertEquals(values.getTotalRecords(), 38);
	}
	
	/**
	 * Test to search for only middle 20
	 */
	@Test
	public void searchMiddle20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 5, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 20);
		Assert.assertEquals(values.getTotalRecords(), 38);
	}
	
	/**
	 * Test to search for only last 20
	 */
	@Test
	public void searchLast20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 19, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 19);
		Assert.assertEquals(values.getTotalRecords(), 38);
	}
	
	/**
	 * Test to search for over last 20
	 */
	@Test
	public void searchOverLast20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 37, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 1);
		Assert.assertEquals(values.getTotalRecords(), 38);
	}
	
	/**
	 * Test to search for over the last record
	 */
	@Test
	public void searchLastRecord(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 38, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 0);
		Assert.assertEquals(values.getTotalRecords(), 38);
	}
	
	/**
	 * Test to search for over the last record
	 */
	@Test
	public void searchObjectTypeLastRecord(){
		FilterPageResult<SimpleDataType> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcherMultiObject, new SimpleDataOrderComparison(), "site", 0, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 1);
		Assert.assertEquals(values.getTotalRecords(), 1);
	}
}

class StringOrderComparison implements java.util.Comparator<String>{

	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}
	
}

class SimpleDataOrderComparison implements java.util.Comparator<SimpleDataType>{

	public int compare(SimpleDataType o1, SimpleDataType o2) {
		return o1.getValue1().compareTo(o2.getValue1());
	}
	
}