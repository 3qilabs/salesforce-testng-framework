
package com.salesforce.utilities;

public class ArrayUtils {
	@SuppressWarnings("unchecked")
	public static final <T> T[] set(T[] array, T val, int pos) {
		if ((array != null) && (array.length > pos)) {
			array[pos] = val;
			return array;
		}
		T[] extended = (T[]) new Object[pos + 1];
		extended[pos] = val;
		if (array != null) {
			System.arraycopy(array, 0, extended, 0, array.length);
		}
		return extended;
	}

}
