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
     * Writes a enctypted value agains key
     */
    public void write(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, encrypt(value));
        editor.apply();
    }

    /**
     * Fetches stored encrypted value against key and converts in plain text.
     */
    public String read(String key) {
        String v = preferences.getString(key, "");
        if (v.length() < 1) return "";
        return decrypt(v);
    }

    /**
     * Encrypts data
     */
    private String encrypt(String data) {
        return keyChainManager.encrypt(data);
    }

    /**
     * Decrypts data
    */
    private String decrypt(String data) {
        return keyChainManager.decrypt(data);
    }

}
