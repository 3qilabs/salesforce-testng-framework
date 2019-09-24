package com.salesforce.data;


/*import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.keys.ApplicationProperties;
import com.qmetry.qaf.automation.testng.RetryAnalyzer;
import com.qmetry.qaf.automation.testng.pro.DataProviderUtil;*/

import com.salesforce.core.ConfigurationManager;
import com.salesforce.keys.ApplicationProperties;
import com.salesforce.testng.DataProviderUtil;
import com.salesforce.utilities.*;
import com.salesforce.utilities.JSONUtil;
import com.salesforce.utilities.RandomStringGenerator.RandomizerTypes;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.SkipException;

public abstract class BaseDataBean {
//    protected final transient Log logger = LogFactory.getLog(this.getClass());

    public BaseDataBean() {
    }

    public String toCSV() {
        StringBuffer sb = new StringBuffer();
        Field[] flds = this.getClass().getDeclaredFields();
        Field[] var6 = flds;
        int var5 = flds.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Field fld = var6[var4];

            try {
                fld.setAccessible(true);
                if (fld.getDeclaringClass().equals(this.getClass()) && fld.get(this) != null) {
                    sb.append(fld.get(this).toString());
                }

                sb.append(",");
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        return sb.toString();
    }

    public void fillData(Object obj) {
        if (obj instanceof Map) {
            this.fillData((Map)obj);
        } else {
            if (obj instanceof String) {
                String str = String.valueOf(obj);
                if (JSONUtil.isValidJsonString(str)) {
                    this.fillFromJsonString(str);
                    return;
                }

                /*if (str.startsWith("select")) {
                    this.fillDataFromDB(str);
                    return;
                }*/

                /*if (!ConfigurationManager.getBundle().subset(str).isEmpty()) {
                    this.fillFromConfig(str);
                    return;
                }*/
            }

            throw new SkipException("Unable to fill data with unknown object. It must be either Map or String: valid json / property key / sql statement." + obj);
        }
    }

    protected Field[] getFields() {
        return ClassUtil.getAllFields(this.getClass(), BaseDataBean.class);
    }

    public void fillFromJsonString(String jsonstr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonstr);
            String[] keys = JSONObject.getNames(jsonObject);
            String[] var7 = keys;
            int var6 = keys.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                String key = var7[var5];
                this.fillData(key, jsonObject.getString(key));
            }
        } catch (JSONException var8) {
//            this.logger.error(var8);
        }

    }

    public void fillFromConfig(String datakey) {
        List<Object[]> set = DataProviderUtil.getDataSetAsMap(datakey);
        if (!set.isEmpty()) {
            int index = 0;
            if (set.size() > 1) {
                if (ApplicationProperties.BEAN_POPULATE_RANDOM.getBoolenVal(new boolean[]{false})) {
                    index = RandomUtils.nextInt(set.size());
                } else {
                    int cindex = ConfigurationManager.getBundle().getInt("retry.invocation.count", 0);
                    index = cindex % set.size();
                }
            }

            this.fillData(((Object[])set.get(index))[0]);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Field[] flds = this.getFields();
        Field[] var6 = flds;
        int var5 = flds.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Field fld = var6[var4];

            try {
                fld.setAccessible(true);
                if (fld.get(this) != null) {
                    sb.append(fld.getName() + " : " + fld.get(this).toString());
                    sb.append(",");
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }
        }

        sb.deleteCharAt(sb.length() - 1);
        if (sb.length() > 0) {
            sb.append("]");
        }

        return sb.toString();
    }

    public String toCSV(String csvNames) {
        if (csvNames != null && !csvNames.equalsIgnoreCase("all") && !csvNames.equalsIgnoreCase("*")) {
            StringBuffer sb = new StringBuffer();
            StringTokenizer fldNames = new StringTokenizer(csvNames, ",");

            while(fldNames.hasMoreTokens()) {
                try {
                    Field fld = this.getClass().getDeclaredField(fldNames.nextToken());
                    fld.setAccessible(true);
                    if (fld.get(this) != null) {
                        sb.append(fld.get(this).toString());
                    }

                    sb.append(",");
                } catch (Exception var5) {
                    var5.printStackTrace();
                }
            }

            return sb.toString();
        } else {
            return this.toCSV();
        }
    }

    public String getCSVLabel() {
        StringBuffer sb = new StringBuffer();
        Field[] flds = this.getClass().getDeclaredFields();
        Field[] var6 = flds;
        int var5 = flds.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Field fld = var6[var4];

            try {
                if (fld.getDeclaringClass().equals(this.getClass())) {
                    sb.append(fld.getName());
                    sb.append(",");
                }
            } catch (Exception var8) {
//                this.logger.error(var8);
            }
        }

        return sb.toString();
    }

    public void fillData(Map<String, String> map) {
        Iterator var3 = map.keySet().iterator();

        while(var3.hasNext()) {
            String key = (String)var3.next();
            this.fillData(key, String.valueOf(map.get(key)));
        }

    }

   /* public void fillDataFromDB(String query) {
        this.fillData((Map)DatabaseUtil.getRecordDataAsMap(query)[0][0]);
    }*/

    public void fillData(String fieldName, String value) {
        Field[] fields = this.getFields();
        Field[] var7 = fields;
        int var6 = fields.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Field field2 = var7[var5];
            if (field2.getName().equalsIgnoreCase(fieldName)) {
                if (!Modifier.isFinal(field2.getModifiers())) {
                    this.setField(field2, value);
                }

                return;
            }
        }

    }

    public void fillRandomData() {
        Field[] fields = this.getFields();
        Field[] var5 = fields;
        int var4 = fields.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Field field = var5[var3];
//            this.logger.debug("NAME :: " + field.getName());
            if (!Modifier.isFinal(field.getModifiers())) {
                RandomizerTypes type = RandomizerTypes.MIXED;
//                int len = true;
                long min = 0L;
                long max = 0L;
                String prefix = "";
                String suffix = "";
                String format = "";
                String[] list = new String[0];
                Randomizer randomizer = (Randomizer)field.getAnnotation(Randomizer.class);
                if (randomizer != null && !randomizer.skip()) {
                    type = field.getType() == Date.class ? RandomizerTypes.DIGITS_ONLY : randomizer.type();
                    int len = randomizer.length();
                    prefix = randomizer.prefix();
                    suffix = randomizer.suffix();
                    min = randomizer.minval();
                    max = min > randomizer.maxval() ? min : randomizer.maxval();
                    format = randomizer.format();
                    list = randomizer.dataset();
                    String str = "";
                    if (list != null && list.length != 0) {
                        str = getRandomValue(list);
                    } else {
                        str = StringUtil.isBlank(format) ? RandomStringUtils.random(len, !type.equals(RandomizerTypes.DIGITS_ONLY), !type.equals(RandomizerTypes.LETTERS_ONLY)) : StringUtil.getRandomString(format);
                    }

                    try {
                        field.setAccessible(true);
                        Method setter = null;

                        try {
                            setter = this.getClass().getMethod("set" + StringUtil.getTitleCase(field.getName()), String.class);
                        } catch (Exception var21) {
                        }

                        String rStr;
                        if (field.getType() != String.class && setter == null) {
                            rStr = "";
                            if (min == max && min == 0L) {
                                rStr = RandomStringUtils.random(len, false, true);
                            } else {
                                rStr = String.valueOf((long)((int)(Math.random() * (double)(max - min + 1L))) + min);
                            }

                            if (field.getType() == Integer.TYPE) {
                                field.setInt(this, Integer.parseInt(rStr));
                            } else if (field.getType() == Float.TYPE) {
                                field.setFloat(this, Float.parseFloat(rStr));
                            } else if (field.getType() == Double.TYPE) {
                                field.setDouble(this, Double.parseDouble(rStr));
                            } else if (field.getType() == Long.TYPE) {
                                field.setLong(this, Long.parseLong(rStr));
                            } else if (field.getType() == Short.TYPE) {
                                field.setShort(this, Short.parseShort(rStr));
                            } else if (field.getType() == Date.class) {
//                                this.logger.info("filling date " + rStr);
                                int days = Integer.parseInt(rStr);
                                field.set(this, DateUtil.getDate(days));
                            } else if (field.getType() == Boolean.TYPE) {
                                field.setBoolean(this, RandomUtils.nextBoolean());
                            }
                        } else {
                            if (list == null || list.length == 0) {
                                if (min == max && min == 0L) {
                                    str = StringUtil.isBlank(format) ? RandomStringUtils.random(len, !type.equals(RandomizerTypes.DIGITS_ONLY), !type.equals(RandomizerTypes.LETTERS_ONLY)) : StringUtil.getRandomString(format);
                                } else {
                                    str = String.valueOf((long)((int)(Math.random() * (double)(max - min + 1L))) + min);
                                }
                            }

                            rStr = prefix + str + suffix;
                            if (setter != null) {
                                setter.setAccessible(true);
                                setter.invoke(this, rStr);
                            } else {
                                field.set(this, rStr);
                            }
                        }
                    } catch (IllegalArgumentException var22) {
//                        this.logger.error("Unable to fill random data in field " + field.getName(), var22);
                    } catch (IllegalAccessException var23) {
//                        this.logger.error("Unable to Access " + field.getName(), var23);
                    } catch (InvocationTargetException var24) {
//                        this.logger.error("Unable to Access setter for " + field.getName(), var24);
                    }
                }
            }
        }

    }

    protected void setField(Field field, String val) {
        try {
            field.setAccessible(true);
            if (field.getType() == String.class) {
                field.set(this, val);
            } else {
                Method setter = null;

                try {
                    setter = this.getClass().getMethod("set" + StringUtil.getTitleCase(field.getName()), String.class);
                } catch (Exception var7) {
                }

                if (setter != null) {
                    setter.setAccessible(true);
                    setter.invoke(this, val);
                } else if (field.getType() == Integer.TYPE) {
                    field.setInt(this, Integer.parseInt(val));
                } else if (field.getType() == Float.TYPE) {
                    field.setFloat(this, Float.parseFloat(val));
                } else if (field.getType() == Double.TYPE) {
                    field.setDouble(this, Double.parseDouble(val));
                } else if (field.getType() == Long.TYPE) {
                    field.setLong(this, Long.parseLong(val));
                } else if (field.getType() == Boolean.TYPE) {
                    Boolean bval = StringUtils.isBlank(val) ? null : NumberUtils.isNumber(val) ? Integer.parseInt(val) != 0 : Boolean.parseBoolean(val) || val.equalsIgnoreCase("T") || val.equalsIgnoreCase("Y") || val.equalsIgnoreCase("YES");
                    field.setBoolean(this, bval);
                } else if (field.getType() == Short.TYPE) {
                    field.setShort(this, Short.parseShort(val));
                } else if (field.getType() == Date.class) {
                    Date dVal = null;

                    try {
                        dVal = StringUtils.isBlank(val) ? null : (NumberUtils.isNumber(val) ? DateUtil.getDate(Integer.parseInt(val)) : DateUtil.parseDate(val, "MM/dd/yyyy"));
                    } catch (ParseException var6) {
//                        this.logger.error("Expected date in MM/dd/yyyy format.", var6);
                    }

                    field.set(this, dVal);
                }
            }
        } catch (IllegalArgumentException var8) {
//            this.logger.error("Unable to fill random data in field " + field.getName(), var8);
        } catch (IllegalAccessException var9) {
//            this.logger.error("Unable to Access " + field.getName(), var9);
        } catch (InvocationTargetException var10) {
//            this.logger.error("Unable to invoke setter for " + field.getName(), var10);
        }

    }

    public static String getRandomValue(String... values) {
        if (values != null && values.length != 0) {
            if (values.length == 1 && ConfigurationManager.getBundle().containsKey(values[0])) {
                values = ConfigurationManager.getBundle().getStringArray(values[0], new String[]{values[0]});
            }

            Random rand = new Random();
            int r = rand.nextInt(values.length);
            return values[r];
        } else {
            return "";
        }
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new RuntimeException(var2);
        }
    }

    /*public <T extends BaseDataBean> T deepClone() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            T copied = (BaseDataBean)in.readObject();
            return copied;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }*/
}
