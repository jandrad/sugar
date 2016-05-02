package com.orm.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by jandrad on 2/05/16.
 */
public final class SecureHelper {

	private Context context;

	public SecureHelper(Context context) {
		this.context = context;
	}

	private SecretKey generateKey() throws NoSuchAlgorithmException {
		// Generate a 256-bit key
		final int outputKeyLength = 256;

		SecureRandom secureRandom = new SecureRandom();
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(outputKeyLength, secureRandom);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}

	public void storeKey(String key) {
		SharedPreferences prefs = new SecurePreferences(context);
		prefs.edit().putString("key", key.toString());
	}

	public String getKey() {
		SharedPreferences prefs = new SecurePreferences(context);
		String key = prefs.getString("key", null);
		if (key == null) {
			try {
				SecretKey secretKey = generateKey();
				key = secretKey.toString();
			} catch (Exception e) {
				e.printStackTrace();
				SecureRandom random = new SecureRandom();
				key = new BigInteger(130, random).toString(32);
			}

			storeKey(key);
		}

		return key;
	}
}
