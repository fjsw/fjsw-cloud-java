package com.shuwang.cloud.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HmacMd5Util {
	private static final String CHARSET_UTF8 = "UTF-8";

	public static String encryptMD5(String data)
			throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] bytes = digest.digest(data.getBytes(CHARSET_UTF8));
		//
		return byte2hex(bytes);
	}

	public static String encryptMD5(byte[] data)
			throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] bytes = digest.digest(data);
		//
		return byte2hex(bytes);
	}

	public static String encryptHMAC(String data, String secret)
			throws UnsupportedEncodingException, NoSuchAlgorithmException,
			InvalidKeyException {
		SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8),
				"HmacMD5");
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		byte[] bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
		//
		return byte2hex(bytes);
	}

	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}
}
