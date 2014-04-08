package search;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import search.init.SearchGuavaCache;

import com.timwe.util.celcom.my.search.util.SearchUtility;

public class TestConcurrentRuns {
	private final String SEARCH_KEY_1 = "99"; //minimum 3 characters
	private final double THINKTIME=1000;
	private final int USER_COUNT=10;
	//Loading into the memory.
		
	static SearchGuavaCache cacheSearcher = new SearchGuavaCache();
	
	@Test
	public void basicConcurrentRunValid(){
		startUserExecution();
		waitSomeTime();
	}
	
	
	/**
	 * Java program that is to be run manually to determine the performance and test the program on how it should run.
	 */
	//@Test
	public void startPerformanceTest(){
		System.out.println("Starting ...");
		System.out.println("Pause 5 seconds for JVM loading ...");
		pauseProgram();	//may need the server to refresh.
		System.out.println("Start users with random think time, less than "+THINKTIME+" miliseconds...");
		
		startUserExecution(); 
		printServerInfo();
				
		waitSomeTime();
		
		System.out.println("\n\nLet's do it again...");
		
		printServerInfo();
		
		startUserExecution();
		
		waitSomeTime();
		
		printServerInfo();
				
		waitSomeTime();
		
		System.out.println("\n\nImagine GC ran.");
		
		System.gc();
				
		System.out.println("\n\nLet's do it again...");
		
		printServerInfo();
		
		startUserExecution();
		
		waitSomeTime();
		
		printServerInfo();
		
		System.out.println("\n\nEnd - no join...");
	}
	
	private void startUserExecution(){
		for(int loop = 0; loop < USER_COUNT; loop++){
			SimulateMoreUsers smu = new SimulateMoreUsers(loop);
			smu.start();
		}
	}
	
	private void waitSomeTime(){
		try{
			Thread.sleep(10000);
		}catch(Exception e){
			
		}
	}
	
	private void printServerInfo(){
		int mb = 1024*1024;
		
		//Getting the runtime reference from system
				Runtime runtime = Runtime.getRuntime();
				
				System.out.println("##### Heap utilization statistics [MB] #####");
				
				//Print used memory
				System.out.println("Used Memory:" 
					+ (runtime.totalMemory() - runtime.freeMemory()) / mb);

				//Print free memory
				System.out.println("Free Memory:" 
					+ runtime.freeMemory() / mb);
				
				//Print total available memory
				System.out.println("Total Memory:" + runtime.totalMemory() / mb);

				//Print Maximum available memory
				System.out.println("Max Memory:" + runtime.maxMemory() / mb);
	}
	
	private void validateResult(List<String> searchResults, int userThreadId, String controller){
		Assert.assertEquals(searchResults.size(), 11);
	}
	
	private void endTimer(long startTime, int userThreadId, String controller) {
		long executedTime = System.currentTimeMillis() - startTime;
		System.out.println("Executed Time:" + executedTime+"-user-"+userThreadId);
	}

	private long startTimer(){
		return System.currentTimeMillis();
	}
	
	private void pauseProgram(){
		//let the system cool down.
		try{
			Thread.sleep(5000);
		}catch(Exception e){
			//do nothing.
		}
	}
	
	class SimulateMoreUsers extends Thread{
		
		private int userThreadID;
		
		public SimulateMoreUsers(int userThreadID){
			this.userThreadID = userThreadID;
		}
		
		public void run(){
			//List<SearchRequest> searchValue = cacheSearcher.loadSite_100K_Example();
			executeThinkTime();
			long startTime = startTimer(); //start timer.
			
			List<String> searchResults = SearchUtility.searchInputWithSiteAndKey(cacheSearcher, "example", SEARCH_KEY_1);
			
			validateResult(searchResults, userThreadID, "first");

			endTimer(startTime, userThreadID, "second");
		}
		
		private void executeThinkTime(){
			int thinkTime = (int)(Math.random()*THINKTIME);
			try{
				Thread.sleep(thinkTime);
			}catch(Exception e){
				System.err.println("Interrupted Run");
			}
		}
	}
	
	public static void main(String args[]){
		TestConcurrentRuns tcr = new TestConcurrentRuns();
		tcr.startPerformanceTest();
	}
}
