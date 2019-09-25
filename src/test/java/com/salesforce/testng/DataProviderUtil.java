/*******************************************************************************
 * Copyright (c) 2019 Infostretch Corporation
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.salesforce.testng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.salesforce.core.ConfigurationManager;

/**
 * Utility class for TestNG data providers.
 * <ul>
 * <li>Example Usage: @Test(dataProvider = "csvDataProvider", dataProviderClass
 * = DataProviderUtil.class)
 * <li>Excel Data provider:
 * <ol>
 * <li>excelDataProvider, excelDataProviderParallel <br>
 * Required properties:
 * <ol>
 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;optional sheet
 * name&gt; If the data is not in in first sheet of workbook then provide
 * sheet-name with datafile separated by comma
 * <li>test.&lt;method name&gt;.data.hasheader=true/false set true to skip
 * header row
 * </ol>
 * <li>excelTableDataProvider, excelTableDataProviderParallel<br>
 * provides data from excel sheet marked with data set name<br>
 * Required properties:
 * <ol>
 * <li>test.&lt;method name&gt;.datafile=&lt;datafile URL&gt;,&lt;data set
 * name&gt;,&lt;optional sheet name&gt; If the data is not in in first sheet of
 * workbook then provide sheet-name with datafile separated by comma
 * </ol>
 * </ol>
 * <li>CSV Data provider:
 * <ol>
 * <li>csvDataProvider, csvDataProviderParallel <br>
 * Required property
 * <ol>
 * <li>test.&lt;method_name&gt;.datafile= &lt;file URL&gt;
 * </ol>
 * </ol>
 * </ul>
 *
 */
public class DataProviderUtil {
	private static final Log logger = LogFactoryImpl.getLog(DataProviderUtil.class);

	/**
	 * Blank values will be considered as null as type is not be identifiable from blank
	 * @param key
	 * @param
	 * @return
	 */
	public static List<Object[]> getDataSetAsMap(String key) {
		Configuration config = ConfigurationManager.getBundle().subset(key);
		ArrayList<Object[]> dataset = new ArrayList();
		if (config.isEmpty()) {
			return dataset;
		} else {
			int size = config.getList(config.getKeys().next().toString()).size();

			for(int i = 0; i < size; ++i) {
				Map<String, String> map = new LinkedHashMap();
				Iterator iter = config.getKeys();

				while(iter.hasNext()) {
					String dataKey = String.valueOf(iter.next());

					try {
						map.put(dataKey, config.getStringArray(dataKey)[i]);
					} catch (ArrayIndexOutOfBoundsException var9) {
						throw var9;
					}
				}

				dataset.add(new Object[]{map});
			}
			return dataset;
		}
	}
}
