import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.support.annotation.RequiresApi;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;


public class KeyChainManager {
    private static final String ALIAS = "key";
    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String PROVIDER_AndroidOpenSSL = "AndroidOpenSSL";
    private static final String PROVIDER_AndroidKeyStoreBCWorkaround = "AndroidKeyStoreBCWorkaround";


    private KeyStore keyStore;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public KeyChainManager(Context context) {
        try {
            init();
            if (!keyStore.containsAlias(ALIAS))
                createKeys(ALIAS, context);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private void init() throws KeyStoreException {
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up public and private keys for encryption and decryption purpose.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void createKeys(String alias, Context context) {
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + ALIAS + ", O=Organization"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", AndroidKeyStore);
            generator.initialize(spec);

            KeyPair keyPair = generator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private String getProvider() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return PROVIDER_AndroidOpenSSL;
        else return PROVIDER_AndroidKeyStoreBCWorkaround;
    }

    /**
     * Encrypts given data
     */
    public String encrypt(String data) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, null);
            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();
            Cipher inputCipher = Cipher.getInstance(RSA_MODE, getProvider());

            inputCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            CipherOutputStream cipherOut = new CipherOutputStream(out, inputCipher);
            cipherOut.write(Base64.decode(data, Base64.DEFAULT));
            cipherOut.close();

            return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypts encryptedData 
     */
    public String decrypt(String encryptedData) {
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, null);
            PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();

            Cipher output = Cipher.getInstance(RSA_MODE, getProvider());
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            CipherInputStream inputStream = new CipherInputStream(new ByteArrayInputStream(Base64.decode(encryptedData, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = inputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }
            return Base64.encodeToString(bytes, Base64.DEFAULT).trim();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
