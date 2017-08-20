package com.sachinchandil.securedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.sachinchandil.securedpreference.demo.SecuredPreference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class KeyGenerationTest {


    private final String PASSWORD = "12345678@asdf";
    private SecuredPreference securedPreference;

    private SharedPreferences sharedPreferences;
    private Context context;

    @Before
    public void setup() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        sharedPreferences = Mockito.mock(SharedPreferences.class);
        context = Mockito.mock(Context.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        KeyStore keyStore = Mockito.mock(KeyStore.class);
        when(KeyStore.getInstance(anyString())).thenReturn(keyStore);
        Mockito.doNothing().when(keyStore).load(any(InputStream.class), Mockito.any(char[].class));
        Certificate cert = Mockito.mock(Certificate.class);
        when(keyStore.getCertificate(anyString())).thenReturn(cert);

    }

    @After
    public void tearDown() {

    }

    @Test
    public void shouldStoreSecureKeyInPreference() throws Exception {

        securedPreference = new SecuredPreference(context, "test");
        securedPreference.setLoginPin(PASSWORD);
        assertEquals(true, securedPreference.loginPinMatches(PASSWORD));
    }

    @Test
    public void shouldFail() throws Exception {

        securedPreference = new SecuredPreference(context, "test");
        securedPreference.setLoginPin(PASSWORD);
        assertEquals(false, securedPreference.loginPinMatches("secureKey"));
    }

}