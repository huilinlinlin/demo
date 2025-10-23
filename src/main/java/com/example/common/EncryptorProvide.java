package com.example.common;


public class EncryptorProvide {	
	
	private static Encryptor encryptor = null;
	
	public static Encryptor getEncryptor() {
		if (encryptor != null) {
			return encryptor;
		}
		//WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		//encryptor = (Encryptor) ctx.getBean("D067FEBF453EEEAE7547E78ABF8CFCCF5C72F51A855DCE7C9BB9F69BB64173B4");
		return encryptor;
	}
	
	public static String decrypt(String value) throws Exception {
		return getEncryptor().decrypt(value);
	}
	
	public static String encrypt(String value) throws Exception {
		return getEncryptor().encrypt(value);
	}	
	
	
}
