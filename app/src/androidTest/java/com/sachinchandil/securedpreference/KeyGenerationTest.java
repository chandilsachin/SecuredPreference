package com.sachinchandil.securedpreference;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import com.sachinchandil.securedpreference.demo.SecuredPreference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class KeyGenerationTest {


    private final String PASSWORD = "12345678@asdf";
    private SecuredPreference securedPreference;
    private KeyChainEncryptedPreference keyChainEncryptedPreference;

    @Before
    public void setup(){
        securedPreference = new SecuredPreference(InstrumentationRegistry.getTargetContext(), "test1");
        keyChainEncryptedPreference = new KeyChainEncryptedPreference(InstrumentationRegistry.getTargetContext(), "test1");
    }

    @After
    public void tearDown(){
        keyChainEncryptedPreference.flushData();
    }

    @Test
    public void shouldStoreSecureKeyInPreference() throws Exception {

        securedPreference.setLoginPin(PASSWORD);
        assertEquals(true, securedPreference.loginPinMatches(PASSWORD));
    }

    @Test
    public void shouldFail() throws Exception {

        securedPreference.setLoginPin(PASSWORD);
        assertEquals(false, securedPreference.loginPinMatches("secureKey"));
    }

    @Test
    public void shouldPassSaveKeyValue(){
        keyChainEncryptedPreference.write("key", "value");
        assertEquals("value", keyChainEncryptedPreference.read("key"));
    }

    @Test
    public void shouldFailSaveKeyValue(){
        keyChainEncryptedPreference.write("key", "value");
        assertNotEquals("valu", keyChainEncryptedPreference.read("key"));
    }

}