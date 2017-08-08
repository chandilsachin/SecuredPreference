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

    @Before
    public void setup(){

    }

    @After
    public void tearDown(){

    }

    @Test
    public void shouldStoreSecureKeyInPreference() throws Exception {

        securedPreference = new SecuredPreference(InstrumentationRegistry.getTargetContext(), "test");
        securedPreference.setLoginPin(PASSWORD);
        assertEquals(true, securedPreference.loginPinMatches(PASSWORD));
    }

    @Test
    public void shouldFail() throws Exception {

        securedPreference = new SecuredPreference(InstrumentationRegistry.getTargetContext(), "test");
        securedPreference.setLoginPin(PASSWORD);
        assertEquals(false, securedPreference.loginPinMatches("secureKey"));
    }

}