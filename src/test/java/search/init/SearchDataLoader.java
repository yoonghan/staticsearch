package search.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.timwe.util.celcom.my.search.data.SearchRequest;

/**
 * Created a search data loader, this is just sample. There will be need that the program
 * be reading from system delegate and load the data directly from database.
 * @author yoong.han
 *
 */
public class SearchDataLoader {
	
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	private final long INITIAL_DELAY = 0;				//initial delay.
	private final long PERIOD = 1;						//reloading everyday.
	private final TimeUnit TIME_UNIT =  TimeUnit.DAYS;	//reloading unit.
	private final DataReader dataReader;				//suppose to use cache.
	private static boolean PROD_LOADING;				//check if the data is used as prod, if yes, read from database.

	private final static class Singleton{
		private final static SearchDataLoader instance = new SearchDataLoader();
	}
	
	private final static class TestSingleton{
		private final static SearchDataLoader instance = new SearchDataLoader(false);
	}
	
	public static SearchDataLoader getInstance(){  
		return Singleton.instance;
	}
	
	public static SearchDataLoader getTestInstance(){
		return TestSingleton.instance;
	}
	
	private SearchDataLoader(){
		this(true);
	}
	
	private SearchDataLoader(boolean loadingType){
		PROD_LOADING=loadingType;
		dataReader = new DataReader();
		executor.scheduleAtFixedRate(dataReader, INITIAL_DELAY, PERIOD, TIME_UNIT);
		
	}
	
	/**
	 * Retrieve the data
	 * @return
	 */
	public List<SearchRequest<String>> getSearchData(){
		return dataReader.getData();		
	}
	
	
	class DataReader implements Runnable{		
		private volatile List<SearchRequest<String>> search_Keys = null; 
		private final int WAIT_LOADING = 500;
		
		AtomicBoolean synchronizationStarts = new AtomicBoolean(true);
		
		
		public List<SearchRequest<String>> getData(){
			if(search_Keys == null){
				if(synchronizationStarts.get()){
					try{
						Thread.sleep(WAIT_LOADING);
					}catch(InterruptedException ie){
						//do nothing.
					}
					if(synchronizationStarts.get() || search_Keys==null){
						System.err.println("Waiting time too long, end without data.");
						throw new RuntimeException("Waited too long, let's end it.");
					}
				}else{
					System.err.println("No data, but not syched.");
				}
			}
			return search_Keys;
		}
		
		public void run() {
			//run sample
			if(PROD_LOADING == false)
				loadSampleInitValues();
			else
				initValues();
		}
		
		/**
		 * Code here for data loading.
		 */
		public void initValues(){
			//TODO;
		}
		
		/*
		 * loading sample initValues()
		 */
		public void loadSampleInitValues(){
			final int MAX_LOAD = 10000;
			//load all values into memory
			List<SearchRequest<String>> tempArrayList = new ArrayList<SearchRequest<String>>(MAX_LOAD);
			for(int i=0; i<MAX_LOAD; i++){
				String searchKey = null;
				searchKey = null; 
				searchKey = String.format("A very long senten3ce like etc. etc. etc. %s %s%s",i,(char)i,(char)i);
				String searchPath = null;
				if(i % 9999 != 0){ //make some missing values
					searchPath ="location"+i;
				}
								
				tempArrayList.add(new SearchRequest<String>(searchKey,searchPath));
			}
			synchronizationStarts.set(true);
			search_Keys = Collections.unmodifiableList(tempArrayList);
			synchronizationStarts.compareAndSet(true, false);
		}
	}
}
