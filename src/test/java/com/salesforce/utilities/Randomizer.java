package com.salesforce.utilities;
import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.salesforce.data.BaseDataBean;
import com.salesforce.utilities.RandomStringGenerator.RandomizerTypes;

/**
 * This annotation can be used with {@link BaseDataBean} properties. It will be
 * used by {@link BaseDataBean#fillRandomData()} method.
 * <p>
 *
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ FIELD })
public @interface Randomizer {
	/**
	 * @return
	 */
	RandomizerTypes type() default RandomizerTypes.MIXED;

	/**
	 * data length default is 10
	 * 
	 * @return
	 */
	int length() default 10;

	String prefix() default "";

	String suffix() default "";

	/**
	 * can be used with RandomizerTypes.DIGITS_ONLY to set minimum value.When
	 * you use min and max then length will not be considered
	 * 
	 * @return
	 */
	long minval() default 0;

	/**
	 * can be used with RandomizerTypes.DIGITS_ONLY to set maximum value. When
	 * you use min and max then length will not be considered. Can be used for
	 * Date type as well.
	 * 
	 * @return
	 */
	long maxval() default 0;

	boolean skip() default false;

	/**
	 * can be used to generate random data in given format. For example:
	 * aaa-999-aaa will generate random string with xJa-123-abc.
	 * 
	 * @see StringUtil#getRandomString(String)
	 * @return
	 */
	String format() default "";

	String[] dataset() default {};

}
