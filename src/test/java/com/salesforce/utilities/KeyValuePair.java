package com.salesforce.utilities;
import java.util.HashMap;
import java.util.Map;


public class KeyValuePair {
	private Map<Object, Object> keyMap = new HashMap<Object, Object>();

	public KeyValuePair(Object... objects) {
		for (int i = 0; i < objects.length; i += 2) {
			keyMap.put(objects[i], objects[i + 1]);
		}
	}

	public void add(Object key, Object value) {
		keyMap.put(key, value);
	}

	public Object getValue(Object key) {

		return keyMap.get(key);
	}

	public String getStringValue(Object key) {
		return keyMap.get(key).toString();
	}

	public int getIntValue(Object key) {
		return (Integer) keyMap.get(key);
	}
}
