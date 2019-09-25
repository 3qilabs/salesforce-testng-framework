package com.salesforce.utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class ListUtils {

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(Object arrayOrIterator) {
		if (arrayOrIterator instanceof Iterator)
			return toList((Iterator<T>) arrayOrIterator);
		if(arrayOrIterator.getClass().isArray()){
			return toList((T[])arrayOrIterator);
		}
		if(List.class.isAssignableFrom(arrayOrIterator.getClass()))
			return new ArrayList<T>((List<T>)arrayOrIterator);
		
		List<T> lstToReturn = new ArrayList<T>();
		lstToReturn.add((T)arrayOrIterator);
		
		return lstToReturn;
	}

	public static <T> List<T> toList(T[] array) {
		return Arrays.asList(array);
	}

	public static <T> List<T> toList(Iterator<T> iter) {
		ArrayList<T> list = new ArrayList<T>();
		if (null != iter) {
			while (iter.hasNext()) {
				list.add(iter.next());
			}
		}
		return list;
	}
	
	public static void main(String[] args) {
		Object[][] aa = {{"a",10},{"b",20}};
		Object a = aa;
		
		System.out.printf("%s" ,toList(a).get(0));
		
	}
}
