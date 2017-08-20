package com.sachinchandil.securedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class KeyChainEncryptedPreference {

    private SharedPreferences preferences;
    private KeyChainManager keyChainManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public KeyChainEncryptedPreference(Context context, String sharedPrefName) {
        preferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        keyChainManager = new KeyChainManager(context);
    }

    /**
     * Writes a encrypted value against key
     */
    public void write(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        String k = keyChainManager.base64Encode(key);
        String v = encrypt(value);
        editor.putString(k, v);
        editor.apply();
    }

    /**
     * Fetches stored encrypted value against key and converts in plain text.
     */
    public String read(String key) {
        String k = keyChainManager.base64Encode(key);
        String v = preferences.getString(k, "");
        if (v.length() < 1) return "";
        return decrypt(v);
    }

    /**
     * Encrypts data
     */
    public String encrypt(String data) {
        return keyChainManager.encrypt(data);
    }

    /**
     * Decrypts data
     */
    public String decrypt(String data) {
        return keyChainManager.decrypt(data);
    }


    public void flushData(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
