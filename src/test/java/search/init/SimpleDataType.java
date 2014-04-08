package search.init;

public class SimpleDataType {

	public String value1;
	public String value2;
	
	public SimpleDataType(String value1, String value2){
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public String getValue1(){
		return value1;
	}
	
	public String getValue2(){
		return value2;
	}
	
	@Override
	public boolean equals(Object obj) {
		SimpleDataType sdt = (SimpleDataType)obj;
		return sdt.getValue2().equals(getValue2());
	}
	
	/**
	 * Create a hash code
	 */
	@Override
	public int hashCode() {
		return value2.hashCode();
	}
}
