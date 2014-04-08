package com.timwe.util.celcom.my.concurrency.util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrencyUtil {
	public static final ExecutorService getSimpleService(int poolSize){
		final ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
		addShutdownHook(executorService);
		return executorService;
	}
	
	private static void addShutdownHook(final ExecutorService executorService ){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				shutdown(executorService);
			}
		});
	}
	
	public static void shutdown(final ExecutorService executorService){
		executorService.shutdown();
		try {
			int timeToWait = 30;
			  if (!executorService.awaitTermination(timeToWait, TimeUnit.SECONDS)) {
			    List<Runnable> executionList = executorService.shutdownNow();
			    for(Runnable runnable : executionList){
			    	System.out.println("Trying to shutdown task: "+runnable);
			    }
			  }	
			  if (!executorService.awaitTermination(timeToWait, TimeUnit.SECONDS)) {
			  }
		} catch (InterruptedException ex) {
		  executorService.shutdownNow();
		  Thread.currentThread().interrupt();
		}	
	}
}
