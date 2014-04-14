package search.init;

import java.util.ArrayList;
import java.util.List;

import com.timwe.util.celcom.my.cache.data.AbstractSearchGuavaCache;
import com.timwe.util.celcom.my.search.data.SearchRequest;

public class SearchGuavaCache extends AbstractSearchGuavaCache<String> {

	int MAX_LOAD = 10000;
	
	public List<SearchRequest<String>> loadSite_100K_Example(){
		MAX_LOAD = 100000;
		return loadValues("example");
	}
	
	public List<SearchRequest<String>> loadSite_10K_Example(){
		MAX_LOAD = 10000;
		return loadValues("example");
	}
	
	public List<SearchRequest<String>> loadValues(String site) {
		//load all values into memory
		List<SearchRequest<String>> tempArrayList = new ArrayList<SearchRequest<String>>(MAX_LOAD);
		for(int i=0; i<MAX_LOAD; i++){
			String searchKey = null;
			searchKey = null; 
			//break all text into only 3 characters.
			String intVal = new String(""+i);
			intVal = intVal.length() > 3? intVal.substring(0,3)+" "+intVal.substring(3): intVal;
			
			searchKey = String.format("A very long senten3ce like etc. etc. etc. %s %s %s",intVal,(char)i,(char)i);
			String searchPath = null;
			if(i % 9999 != 0){ //make some missing values
				searchPath ="location"+i;
			}
							
			tempArrayList.add(new SearchRequest<String>(searchKey,searchPath));
		}
		return tempArrayList;
	}

}
