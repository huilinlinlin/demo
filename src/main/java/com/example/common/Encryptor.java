package com.example.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Encryptor
 *
 * @author david
 * @version 1.2
 */
@Component
public class Encryptor {
	//private static final Logger log = LoggerFactory.getLogger(Encryptor.class);  
	
	private String aes256Key="D067FEBF453EEEAE7547E78ABF8CFCCF5C72F51A855DCE7C9BB9F69BB64173B4";
	
	public String getAes256Key() {
		return aes256Key;
	}
	
	public void setAes256Key(String aes256Key) {
		this.aes256Key = aes256Key;
	}
	
	public String encrypt(String plainText) throws Exception {
		if (null == plainText) {
			return null;
		}
		if (StringUtils.isBlank(this.aes256Key)) {
			throw new Exception("缺少common.aes256Key配置");
		}		
		return this.encrypt(plainText, this.aes256Key);
	}
	
	public String decrypt(String cipherText) throws Exception {
		System.out.println(cipherText);
		System.out.println(this.aes256Key);
		if (null == cipherText) {
			return null;
		}
		if (StringUtils.isBlank(this.aes256Key)) {
			throw new Exception("缺少common.aes256Key配置");
		}
		return this.decrypt(cipherText, this.aes256Key);
	}
	
	/**
	 * Example for encrypt & decrypt AES 256 加密
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Encryptor encryptor = new Encryptor();
			
			// 要加密的字串
			String dataString = "HelloWorld";
			
		//	System.out.println("main MESSAGE: dataString-->{}"+ dataString);
		//	System.out.println("main MESSAGE: length-->{}"+ dataString.length());
			
			// 金鑰 需要存放起來 ( getKey() 可以重新產生，但記得存起來 )
			String key = "D067FEBF453EEEAE7547E78ABF8CFCCF5C72F51A855DCE7C9BB9F69BB64173B4";
			//System.out.println("main MESSAGE: key-->{}"+ key);
			//System.out.println("main MESSAGE: key length-->{}"+ key.length());
			
			// 加密
			//String encrypt = encryptor.encrypt(dataString, key);
			String encrypt = "355AC70443A2BBA42FE07D80D83E64A308B5248AF8A4AB";
			// 解密
			String decrypt = encryptor.decrypt(encrypt, key);
			
			//System.out.println("main MESSAGE: encrypt-->{}"+ encrypt);
			System.out.println("main MESSAGE: decrypt-->{}"+ decrypt);
			
			// Example SHA-512 ( getKey() 可以重新產生，但記得存起來 )
			//String salt = "9E756ED5B57B904390E5438694CC187675121629F62F91D9023BA3F9FC72CD22";
			//String code = encryptor.getSHA512withSalt(dataString, salt);
			
			//System.out.println("code:{} , length:{}"+code+ code.length());      
			
		} catch (Exception e) {
			System.out.println("main MESSAGE: Exception Happened ! "+ e);
		}
	}
	
	/**
	 * 取得金鑰(256)
	 * 
	 * @return AES Key(256)
	 */
	public static String getKey() {
		String result = "";
		try {			
			// 自動產生 Key
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// must be equal to 128, 192 or 256
			kgen.init(256);
			SecretKey skey = kgen.generateKey();
			
			byte[] raw = skey.getEncoded();
			System.out.println("getKey MESSAGE: raw-->{}"+ raw);
			System.out.println("getKey MESSAGE: raw length-->{}"+raw.length);
			//result = Hex.encodeHexString(raw).toUpperCase();
			result = new String(Hex.encodeHex(raw)).toUpperCase();
		} catch (Exception e) {
			System.out.println("getKey MESSAGE: Exception Happened ! "+ e);
		}
		return result;
	}
	
	/**
	 * AES 加密
	 * 
	 * @param plainText (需加密文字）
	 * @param encryptionKey (金鑰）
	 * @return 已加密文字
	 */
	public String encrypt(String plainText, String encryptionKey) {
		String result = null;
		try {
			byte[] raw = Hex.decodeHex(encryptionKey.toCharArray());
			System.out.println("encrypt MESSAGE: raw-->{}"+ raw);
			System.out.println("encrypt MESSAGE: raw length-->{}"+ raw.length);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec parameterSpec = new GCMParameterSpec(128, raw);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, parameterSpec);
			byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			//result = Hex.encodeHexString(encrypted).toUpperCase();
			result = new String(Hex.encodeHex(encrypted)).toUpperCase();
		} catch (Exception e) {
			//log.info("encrypt MESSAGE: Exception Happened ! "+ e);
		}
		return result;
	}
	
	/**
	 * AES 解密
	 * 
	 * @param cipherText (已加密文字)
	 * @param encryptionKey (金鑰)
	 * @return 已解密文字
	 */
	public String decrypt(String cipherText, String encryptionKey) {
		String result = null;
		try {
			byte[] raw = Hex.decodeHex(encryptionKey.toCharArray());
			//System.out.println("raw length : {}", raw.length);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec parameterSpec = new GCMParameterSpec(128, raw);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, parameterSpec);
			byte[] decrypted = cipher.doFinal(Hex.decodeHex(cipherText.toCharArray()));
			result = new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			//log.info("encrypt MESSAGE: Exception Happened ! ", e);
		}
		return result;
	}
	
	public String getSHA512(String cipherText) {
		return getSHA512withSalt(cipherText, null);
	}
	
	public String getSHA512withSalt(String cipherText, String salt) {
		String result = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			if (salt != null) {
				digest.update(salt.getBytes());
			}
			byte[] bytes = digest.digest(cipherText.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
