package com.salesforce.utilities;
/**
 * <dl>
 * Usage:
 * <dt>
 * </dl>
 * 
 */
public enum StringComparator {
	/**
	 * to compare s1 with s2.
	 */
	Exact("", ""),
	/**
	 * to compare s1 for s2 as prefix.
	 */
	Prefix("", ".*"),
	/**
	 * to compare s1 for s2 as suffix.
	 */
	Suffix(".*", ""),
	/**
	 * to check whether s1 contains s2?
	 */
	In(".*", ".*"),
	/**
	 * compare s1 with regexp, s2 will be treated as regexp
	 */
	RegExp("", "");
	String p, s;

	private StringComparator(String p, String s) {
		this.p = p;
		this.s = s;
	}

	public boolean compare(String s1, String s2) {
		if (RegExp.equals(this)) {
			return s1.matches(s2);
		}
		return s1.matches(p + s2.replaceAll("([\\]\\[\\\\{\\}$\\(\\)\\|\\^\\+.])", "\\\\$1") + s);
	}

	public boolean compareIgnoreCase(String s1, String s2) {
		return compare(s1.toUpperCase(), s2.toUpperCase());
	}

}
