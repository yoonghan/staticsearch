package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.junit.Assert;
import org.junit.Test;

import search.init.SearchGuavaCache;
import search.init.SearchMultiObjectGuavaCache;
import search.init.SimpleDataType;

import com.timwe.util.celcom.my.search.data.FilterPageResult;
import com.timwe.util.celcom.my.search.data.SearchRequest;
import com.timwe.util.celcom.my.search.data.SearchResultList;
import com.timwe.util.celcom.my.search.data.SearchWorker;
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
		Assert.assertEquals(values.size(), 10);
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
		Assert.assertEquals(values.size(), 21);
	}
	
	/**
	 * Test to search for only first 20
	 */
	@Test
	public void searchFirst20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 0, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 20);
		Assert.assertEquals(values.getTotalRecords(), 21);
	}
	
	/**
	 * Test to search for only middle 20
	 */
	@Test
	public void searchMiddle20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 5, 6, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 6);
		Assert.assertEquals(values.getTotalRecords(), 21);
	}
	
	/**
	 * Test to search for only last 20
	 */
	@Test
	public void searchLast20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 19, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 2);
		Assert.assertEquals(values.getTotalRecords(), 21);
	}
	
	/**
	 * Test to search for over last 20
	 */
	@Test
	public void searchOverLast20Records(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 20, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 1);
		Assert.assertEquals(values.getTotalRecords(), 21);
	}
	
	/**
	 * Test to search for over the last record
	 */
	@Test
	public void searchLastRecord(){
		FilterPageResult<String> values = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, new StringOrderComparison(), "site", 38, 20, "RASHIDI", "100","999");
		Assert.assertEquals(values.getRecords().size(), 0);
		Assert.assertEquals(values.getTotalRecords(), 21);
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
	
	/**
	 * Test regular expression is searching right keywords.
	 */
	@Test
	public void testKeywordSearches(){
		List<SearchRequest<String>> keywordsToSearch = new ArrayList<SearchRequest<String>>(6);
		keywordsToSearch.add(new SearchRequest<String>("indonesia", "1"));
		keywordsToSearch.add(new SearchRequest<String>("one malaysia", "2"));
		keywordsToSearch.add(new SearchRequest<String>("malaysia one", "3"));
		keywordsToSearch.add(new SearchRequest<String>("one", "4"));
		keywordsToSearch.add(new SearchRequest<String>("malaysia is one community", "5"));
		keywordsToSearch.add(new SearchRequest<String>("malaysia is a friends to indonesia", "6"));
		keywordsToSearch.add(new SearchRequest<String>("one can see that from indonesia", "7"));
		keywordsToSearch.add(new SearchRequest<String>("one can see that from indonesia.", "8"));
		keywordsToSearch.add(new SearchRequest<String>("malaysia one.", "9"));
		keywordsToSearch.add(new SearchRequest<String>("one, malaysia", "10"));
		
		SearchWorker<String> searchWorker = new SearchWorker<String>(keywordsToSearch, "one");
		SearchResultList<String> results = searchWorker.call();
		
		ArrayList<String> listOfResults = new ArrayList<String>(10);
		for(String result: results){
			listOfResults.add(result);
		}
		Collections.sort(listOfResults);
		Assert.assertEquals("10",listOfResults.get(0));
		Assert.assertEquals("2",listOfResults.get(1));
		Assert.assertEquals("3",listOfResults.get(2));
		Assert.assertEquals("4",listOfResults.get(3));
		Assert.assertEquals("5",listOfResults.get(4));
		Assert.assertEquals("7",listOfResults.get(5));
		Assert.assertEquals("8",listOfResults.get(6));
		Assert.assertEquals("9",listOfResults.get(7));
	}
	
	/**
	 * Disallow all special characters
	 */
	@Test
	public void disallowSpecialCharacters(){
		List<SearchRequest<String>> keywordsToSearch = new ArrayList<SearchRequest<String>>(6);
		keywordsToSearch.add(new SearchRequest<String>(".*all.* one", "1"));
		keywordsToSearch.add(new SearchRequest<String>("\\one\\", "2"));
		keywordsToSearch.add(new SearchRequest<String>(".*", "3"));
		
		SearchWorker<String> searchWorker = new SearchWorker<String>(keywordsToSearch, "one");
		SearchResultList<String> results = searchWorker.call();
		
		Assert.assertEquals(1,results.size()); //because of first
	}
}

class StringOrderComparison implements java.util.Comparator<String>{

	public int compare(String o1, String o2) {
		return o1.compareTo(o2);
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
			Function<? super T, ? extends U> arg0) {
		return null;
	}

	public static <T, U> Comparator<T> comparing(
			Function<? super T, ? extends U> arg0, Comparator<? super U> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingDouble(
			ToDoubleFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsFirst(Comparator<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsLast(Comparator<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<String> reversed() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<String> thenComparing(Comparator<? super String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U extends Comparable<? super U>> Comparator<String> thenComparing(
			Function<? super String, ? extends U> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U> Comparator<String> thenComparing(
			Function<? super String, ? extends U> arg0,
			Comparator<? super U> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<String> thenComparingDouble(
			ToDoubleFunction<? super String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<String> thenComparingInt(
			ToIntFunction<? super String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<String> thenComparingLong(
			ToLongFunction<? super String> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class SimpleDataOrderComparison implements java.util.Comparator<SimpleDataType>{

	public int compare(SimpleDataType o1, SimpleDataType o2) {
		return o1.getValue1().compareTo(o2.getValue1());
	}

	public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
			Function<? super T, ? extends U> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T, U> Comparator<T> comparing(
			Function<? super T, ? extends U> arg0, Comparator<? super U> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingDouble(
			ToDoubleFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingInt(ToIntFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> comparingLong(ToLongFunction<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsFirst(Comparator<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> Comparator<T> nullsLast(Comparator<? super T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T extends Comparable<? super T>> Comparator<T> reverseOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<SimpleDataType> reversed() {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<SimpleDataType> thenComparing(
			Comparator<? super SimpleDataType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U extends Comparable<? super U>> Comparator<SimpleDataType> thenComparing(
			Function<? super SimpleDataType, ? extends U> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public <U> Comparator<SimpleDataType> thenComparing(
			Function<? super SimpleDataType, ? extends U> arg0,
			Comparator<? super U> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<SimpleDataType> thenComparingDouble(
			ToDoubleFunction<? super SimpleDataType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<SimpleDataType> thenComparingInt(
			ToIntFunction<? super SimpleDataType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Comparator<SimpleDataType> thenComparingLong(
			ToLongFunction<? super SimpleDataType> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}