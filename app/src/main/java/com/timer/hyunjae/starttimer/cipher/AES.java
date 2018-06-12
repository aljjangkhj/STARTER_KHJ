package com.timer.hyunjae.starttimer.cipher;

import com.timer.hyunjae.starttimer.common.log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by riverain on 2016-03-24.
 */
public class AES {
    private final static int KEY_LEN  = 32;

    private final static String PADDING_STRING = " ";

    public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

    public static String DEFAULT_KEY  = "";
    private static byte[] key;

    public static String AES_Encrypt(String keyStr, String plainText) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
        log.vlog(2, "AES_Encrypt : " + plainText);
        setKey(keyStr);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

        byte[] plain = plainText.getBytes("UTF-8");
        //return new String(Base64.encode(cipher.doFinal(plain), Base64.NO_WRAP));
        return new String(Base64Coder.encode(cipher.doFinal(plain)));
    }



    public static String AES_Decrypt(String keyStr, String encryptData)  throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException {
        setKey(keyStr);

        byte[] encrypted = Base64Coder.decode(encryptData);

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return new String(cipher.doFinal(encrypted), "UTF-8");
    }

    public static void setDefaultKey(String keyStr) {
        if(keyStr.length() >= KEY_LEN) {
            DEFAULT_KEY = keyStr.substring(0, KEY_LEN);
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(keyStr);
            for(int i=0 ; i < KEY_LEN-keyStr.length() ; i++) { sb.append(PADDING_STRING); }

            DEFAULT_KEY = sb.toString();
        }
    }

    private static void setKey(String keyStr) {
        try {
            if(keyStr == null || keyStr.length() < 1) {
                key = DEFAULT_KEY.getBytes("UTF-8");
                return;
            }

            if(keyStr.length() >= KEY_LEN)
                key = keyStr.substring(0, KEY_LEN).getBytes("UTF-8");
            else {
                StringBuffer sb = new StringBuffer();
                sb.append(keyStr);
                for(int iCnt=0;iCnt<KEY_LEN-keyStr.length();iCnt++) {
                    sb.append(PADDING_STRING);
                }
                key = sb.toString().getBytes("UTF-8");
            }
        } catch(UnsupportedEncodingException e) {
            key = new byte[KEY_LEN]; // set key to all zero
        }
    }
}
