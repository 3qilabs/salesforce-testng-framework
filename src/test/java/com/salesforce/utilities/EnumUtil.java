package com.salesforce.utilities;
import java.lang.reflect.Array;

/**
 * set get object array by enumeration
 * 
 */
public class EnumUtil {

	@SuppressWarnings("unchecked")
	public static final <T> Object[] set(Enum<?> arg, T val, T... args) {
		if ((args != null) && (args.length > arg.ordinal())) {
			args[arg.ordinal()] = val;
			return args;
		}
		T[] extended = (T[]) Array.newInstance(Object.class, arg.ordinal());// new
		extended[arg.ordinal()] = val;
		if (args != null) {
			System.arraycopy(args, 0, extended, 0, args.length);
		}
		return extended;

	}

	public static final Object getFrom(Enum<?> arg, Object... args) {
		if ((args != null) && (args.length > arg.ordinal())) {
			return args[arg.ordinal()];
		}
		return null;
	}

	public static final Object[] setIfNull(Enum<?> arg, Object val, Object... args) {
		if (null == getFrom(arg, args)) {
			return set(arg, val, args);
		}
		return args;
	}

}
