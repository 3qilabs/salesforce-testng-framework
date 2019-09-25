
package com.salesforce.keys;

import org.apache.commons.lang.StringUtils;

import com.salesforce.core.ConfigurationManager;
import com.salesforce.data.BaseDataBean;


/**
 * TO get properties key/value While reading value First preference will be
 * System property if set before run
 * 
 */
public enum ApplicationProperties {

	LOAD_LOCALES("env.load.locales"),

	/**
	 * <b>key</b>: <code> env.default.locale</code><br/>
	 * <b>value</b>: local name from loaded locals that need to treated as
	 * default local
	 */
	DEFAULT_LOCALE("env.default.locale"), LOCALE_CHAR_ENCODING("locale.char.encoding"),

	/**
	 * <b>key</b>: <code>bean.populate.random</code><br/>
	 * <b>value</b>: boolean value to specify whether to populate bean data
	 * randomly or in sequence. This property used by
	 * {@link BaseDataBean#fillFromConfig(String) fillFromConfig} method while
	 * populating bean from configuration, when more than one record exist in
	 * configuration .
	 */
	BEAN_POPULATE_RANDOM("bean.populate.random"),

	DRY_RUN_MODE("dryrun.mode"),

	/**
	 * @since 2.1.11 <b>key</b>: <code>rest.client.impl</code><br/>
	 *        <b>value</b>: full qualified name of the class that extends
	 *        {@link RestClientFactory}.
	 */
	REST_CLIENT_FACTORY_IMPL("rest.client.impl"),

	/**
	 * @since 2.1.13 <b>key</b>: <code>password.decryptor.impl</code><br/>
	 *        <b>value</b>: full qualified name of the class that implements
	 *        {@link PasswordDecryptor}. This implementation will be used to
	 *        decrypt password. When configuration manager found any key starts
	 *        with {@link #ENCRYPTED_PASSWORD_KEY_PREFIX}
	 */
	PASSWORD_DECRYPTOR_IMPL("password.decryptor.impl"),

	/**
	 * <b>key</b>: <code>encrypted</code><br/>
	 * <b>value</b>: property with prefix 'encrypted'. When configuration
	 * manager found any key starts with 'encrypted' prefix, for example
	 * 'encripted.db.pwd', then it will store decrypted value without prefix,
	 * 'db.pwd' in this example. So you can reference decrypted value anywhere
	 * in the code with key without this prefix ('db.pwd' in this example).
	 * 
	 * @since 2.1.13
	 */
	ENCRYPTED_PASSWORD_KEY_PREFIX("encrypted."),
	/**
	 * <p>
	 * To set default meta-data for all element. Meta-data provided with locator
	 * has higher preference than default values.
	 * </p>
	 * <b>key</b>: <code>element.default.metadata</code><br/>
	 * <b>value</b>: JSON map of meta data to be set as default for element.
	 * 
	 * @since 2.1.13
	 * 
	 */
	ELEMENT_GLOBAL_METADATA("element.default.metadata"),
	/**
	 * <p>
	 * Specify weather to attach default element listener or not.
	 * </p>
	 * <b>key</b>: <code>element.default.listener</code><br/>
	 * <b>value</b>: boolean true/false.
	 * 
	 * @since 2.1.13
	 * 
	 */
	ELEMENT_ATTACH_DEFAULT_LISTENER("element.default.listener"),
	/**
	 * <p>
	 * Set true to trust all certificates and ignore host name verification for
	 * web-services.
	 * </p>
	 * <b>key</b>: <code>https.accept.all.cert</code><br/>
	 * <b>value</b>: boolean true/false.
	 * 
	 * @since 2.1.13
	 * 
	 */
	HTTPS_ACCEPT_ALL_CERT("https.accept.all.cert"),

	/**
	 * <p>
	 * Set test case identifier meta-key which will be used to as file name of
	 * test case result json file.
	 * </p>
	 * <b>key</b>: <code>tc.identifier.key</code><br/>
	 * <b>value</b>: String test-case meta-key.
	 * 
	 * @since 2.1.13
	 * 
	 */
	TESTCASE_IDENTIFIER_KEY("tc.identifier.key"),
	/**
	 * <p>
	 * Set proxy server that needs to used by {@link UriProxySelector}
	 * </p>
	 * <b>key</b>: <code>proxy.server</code><br/>
	 * <b>value</b>: proxy server.
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_SERVER_KEY("proxy.server"),
	/**
	 * <p>
	 * Set proxy server port that needs to used by {@link UriProxySelector}.
	 * Default value is 80.
	 * </p>
	 * <b>key</b>: <code>proxy.port</code><br/>
	 * <b>value</b>: integer port of running proxy server.
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_PORT_KEY("proxy.port"),
	/**
	 * <p>
	 * Set one or more host url that needs to be proxied through given proxy server.
	 * </p>
	 * <b>key</b>: <code>host.to.proxy</code><br/>
	 * <b>value</b>: one or more host URL separated by ';'
	 * 
	 * @since 2.1.14
	 * 
	 */
	PROXY_HOSTS_KEY("host.to.proxy");

	public String key;

	private ApplicationProperties(String key) {
		this.key = key;
	}

	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public String getStringVal(String... defaultVal) {
		return System.getProperty(key, ConfigurationManager.getBundle().getString(key,
				(null != defaultVal) && (defaultVal.length > 0) ? defaultVal[0] : ""));
	}

	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public int getIntVal(int... defaultVal) {
		try {
			int val = Integer.parseInt(getStringVal());
			return val;
		} catch (Exception e) {
			// just ignore
		}
		return (null != defaultVal) && (defaultVal.length > 0) ? defaultVal[0] : 0;
	}

	/**
	 * @param defaultVal
	 *            optional
	 * @return
	 */
	public boolean getBoolenVal(boolean... defaultVal) {
		try {
			String sVal = getStringVal().trim();
			boolean val = StringUtils.isNumeric(sVal) ? (Integer.parseInt(sVal) != 0) : Boolean.parseBoolean(sVal);
			return val;
		} catch (Exception e) {
			// just ignore
		}
		return (null != defaultVal) && (defaultVal.length > 0) && defaultVal[0];
	}

	public Object getObject(Object... defaultVal) {
		Object objToReturn = ConfigurationManager.getBundle().getObject(key);
		return null != objToReturn ? objToReturn
				: (null != defaultVal) && (defaultVal.length > 0) ? defaultVal[0] : null;
	}
}
