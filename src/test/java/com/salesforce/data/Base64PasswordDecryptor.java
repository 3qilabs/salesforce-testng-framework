
package com.salesforce.data;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;



public class Base64PasswordDecryptor implements PasswordDecryptor {


	@Override
	public String getDecryptedPassword(String encriptedPassword) {
		byte[] decoded = Base64.decodeBase64(encriptedPassword);
		String decrypted = null;
		try {
			decrypted = new String(decoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
//			throw new AutomationError("Unable to decrypt password", e);
		}
		return decrypted;
	}

	public static String getEncryptedPassword(String plainPassword) {
		String encryptedPassword = "";
		try {
			encryptedPassword = Base64.encodeBase64String(plainPassword.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedPassword;
	}

}
