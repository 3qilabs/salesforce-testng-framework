package com.salesforce.utilities;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;


public class JSONUtil {
	private static final Log logger = LogFactoryImpl.getLog(JSONUtil.class);

	/**
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> toMap(String json) throws JSONException {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = new Gson().fromJson(json, Map.class);
		return map;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidJsonString(String str) {
		try {
			new JSONObject(str);
			return true;
		} catch (JSONException e) {
			return false;
		}

	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static JSONArray getJsonArrayOrNull(String str) {
		try {
			return new JSONArray(str);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * @param csv
	 * @return
	 */
	public static JSONArray getJsonArrayFromCsvOrNull(String csv){
		try {
			return CDL.rowToJSONArray(new JSONTokener(csv));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * This is specifically to work with GSON. for example even "some string" is
	 * not valid json but gson can considers as valid json
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isValidGsonString(String str) {
		try {
			new JsonParser().parse(str);
			return true;
		} catch (JsonParseException e) {
			return false;
		}

	}

	/**
	 * @param obj
	 * @return
	 */
	static Map<String, Object> toMap(JSONObject obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj != null) {
			Iterator<?> iter = obj.keys();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				try {
					map.put(key, obj.get(key));
				} catch (JSONException e) {

				}
			}
		}
		return map;
	}

	/**
	 * @param str
	 * @return JsonElement or null if not a valid json
	 */
	public static JsonElement getGsonElement(String str) {
		try {
			return new JsonParser().parse(str);
		} catch (JsonParseException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param file
	 * @param obj
	 */
	public static <T> void writeJsonObjectToFile(final String file, final T obj) {

		File f = new File(file);
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
			String jsonStr = gson.toJson(obj, obj.getClass());

			FileUtil.writeStringToFile(f, jsonStr, "UTF-8");
		} catch (Throwable e) {
			System.err.println("Unable to write : " + obj.getClass().getCanonicalName() + " in file: " + file + " :"
					+ e.getMessage());
			logger.error("Unable to write : " + obj.getClass().getCanonicalName() + " in file: " + file + " :"
					+ e.getMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	/*public static Object[][] getJsonArrayOfMaps(String file) {
		try {
			Gson gson = new Gson();
			final Type DATASET_TYPE = new TypeToken<List<Map<String, Object>>>() {
				private static final long serialVersionUID = 4426388930719377223L;
			}.getType();

			List<Map<String, Object>[]> mapData;
			if (file.startsWith("["))
				mapData = gson.fromJson(file, DATASET_TYPE);
			else {
				String jsonStr = FileUtil.readFileToString(new File(file),
						ApplicationProperties.LOCALE_CHAR_ENCODING.getStringVal("UTF-8"));
				mapData = gson.fromJson(jsonStr, DATASET_TYPE);
			}
			Object[][] objecttoreturn = new Object[mapData.size()][1];
			for (int i = 0; i < mapData.size(); i++) {
				objecttoreturn[i][0] = mapData.get(i);
			}
			return objecttoreturn;// mapData.toArray(new Object[][]{});

		} catch (Throwable e) {
			throw new AutomationError(e);
		}
	}*/

	/**
	 * 
	 * @param file
	 * @param cls
	 * @return
	 */
	public static <T> T getJsonObjectFromFile(final String file, final Class<T> cls) {

		File f = new File(file);
		String json;
		String defVal = List.class.isAssignableFrom(cls) || cls.isArray() ? "[]"
				: ClassUtil.isPrimitiveOrWrapperType(cls) ? "" : "{}";
		try {
			json = FileUtil.readFileToString(f, "UTF-8");
		} catch (IOException e) {
			json = defVal;
		} catch (Throwable e) {
			logger.error("unable to load [" + cls.getName() + "] from file[ " + file + "] - " + e.getMessage());
			json = defVal;
		}
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, cls);

	}

}
