
package com.salesforce.data;

import com.salesforce.keys.ApplicationProperties;

/**
 * This interface should be implemented to support encrypted password. You
 * should use any algorithm of your choice to encrypt password and implement
 * this interface to provide decrypted password. You need to register your
 * implementation with {@link ApplicationProperties#PASSWORD_DECRYPTOR_IMPL} property. Configuration manager will use registered implementation to store decrypted value for key starting with prefix dec
 * 
 *
 */
public interface PasswordDecryptor {
	/**
	 * 
	 * @param encriptedPassword
	 * @return
	 */
	public String getDecryptedPassword(String encriptedPassword);
}
