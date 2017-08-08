import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import KeyChainEncryptedPreference;

import java.security.SecureRandom;

public class SecuredPreference {
    public static final String PREFERENCE_NAME = "secretKeys";

    private KeyChainEncryptedPreference preference;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public SecuredPreference(Context context){
        preference = new KeyChainEncryptedPreference(context, PREFERENCE_NAME);
    }

    /**
     * Constructor for unit testing purpose. textPreferenceName is temporary preference name for testing.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public SecuredPreference(Context context, String testPreferenceName){
        preference = new KeyChainEncryptedPreference(context, testPreferenceName);
    }
    public void setLoginPin(String pin){
        preference.write(LOGIN_PIN, pin);
    }

    public boolean isLoginPinSet(){
        return !loginPinMatches("");
    }

    public boolean loginPinMatches(String loginPin){
        String pin = preference.read(LOGIN_PIN);
        return pin.equals(loginPin);
    }

    private static final String LOGIN_PIN = "loginPin";
}
