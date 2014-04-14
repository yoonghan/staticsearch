package com.timwe.util.celcom.my.cache.impl;

import java.util.concurrent.TimeUnit;

public interface SearchSettingsImpl {
	/** Partitioning size to search **/
	final static int PARTITION_SIZE = 20;
	
	/** Estimated result return per site **/
	final static int EST_RESULT_MAX_SIZE = 50;
	
	/** Default sleep time for thread **/
	final static int SLEEP_MILISEC_TIME = 10 * 1000;
	
	/** Maximum number of sites it can take for guava cache. **/
	final static int MAX_SIZE = 10;
	
	/** Span unit for guava cache. **/
	final static TimeUnit SPAN_TIME_UNIT = TimeUnit.HOURS;
	
	/** Span time for guava cache. **/
	final static int SPAN_TIME = 24;
	
	/** The FQN for this delegate for memcached */
	final static String FQN = "MY_CELCOM_SEARCH";
	
	/** Default thread size */
	final static int DEFAULT_THREAD_SIZE = 10;
	
	static final int RETRY_INTERVAL_TIME = 15;//in minutes
	
	static final TimeUnit RETRY_INTERVAL_UNIT = TimeUnit.MINUTES;//in minutes
}
