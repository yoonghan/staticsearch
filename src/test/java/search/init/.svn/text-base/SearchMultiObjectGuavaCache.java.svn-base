package search.init;

import java.util.ArrayList;
import java.util.List;

import com.timwe.util.celcom.my.cache.data.AbstractSearchGuavaCache;
import com.timwe.util.celcom.my.search.data.SearchRequest;

public class SearchMultiObjectGuavaCache extends AbstractSearchGuavaCache<SimpleDataType> {

	int MAX_LOAD = 10000;
	
	public List<SearchRequest<SimpleDataType>> loadSite_100K_Example(){
		MAX_LOAD = 100000;
		return loadValues("example");
	}
	
	public List<SearchRequest<SimpleDataType>> loadSite_10K_Example(){
		MAX_LOAD = 10000;
		return loadValues("example");
	}
	
	public List<SearchRequest<SimpleDataType>> loadValues(String site) {
		//load all values into memory
		List<SearchRequest<SimpleDataType>> tempArrayList = new ArrayList<SearchRequest<SimpleDataType>>(MAX_LOAD);
		for(int i=0; i<MAX_LOAD; i++){
			String searchKey = null;
			searchKey = null; 
			searchKey = String.format("A very long senten3ce like etc. etc. etc. %s %s%s",i,(char)i,(char)i);
			SimpleDataType searchPath = null;
			if(i % 9999 != 0){ //make some missing values
				searchPath =new SimpleDataType("1","location");
				//it will have the same data
			}
							
			tempArrayList.add(new SearchRequest<SimpleDataType>(searchKey,searchPath));
		}
		return tempArrayList;
	}

}
