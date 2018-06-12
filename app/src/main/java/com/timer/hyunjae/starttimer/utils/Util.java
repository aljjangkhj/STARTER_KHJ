package com.timer.hyunjae.starttimer.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.timer.hyunjae.starttimer.common.log;
import com.timer.hyunjae.starttimer.config.AppPosPreferenceManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by bible5 on 2018-04-19.
 */

public class Util {

    public static String createAccessToken(Context context){
        String akID = null;
        akID = AppPosPreferenceManager.getAppKeyId(context);

        return akID + "_" + getFormattedDateTime();
    }

    // YYYYMMddhhmmss 형의 날짜 포맷 String 반환
    public static String getFormattedDateTime()
    {
        TimeZone tz = null;
        tz = TimeZone.getTimeZone("Asia/Seoul");
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(tz);
        String strNow = df.format(Calendar.getInstance().getTime());

        String appID = strNow;

        return appID;
    }

    // YYYYSMMRddPhhAmmYss 형의 appID String 반환
    public static String getAppID(Context context)
    {
        TimeZone tz = null;
        tz = TimeZone.getTimeZone("Asia/Seoul");
        DateFormat df = new SimpleDateFormat("yyyy'M'MM'O'dd'D'HH'N'mm'P'ss");
        df.setTimeZone(tz);
        String strNow = df.format(Calendar.getInstance().getTime());

        String appID = strNow;

        return appID;
    }

    public static String encRSA(String expStr, byte[] publicKeyMod, String plainText)
            throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidKeyException,
            UnsupportedEncodingException,
            BadPaddingException,
            IllegalBlockSizeException {
        byte[] publicKeyExp = Base64.decode(expStr, Base64.NO_WRAP);// .decode(expStr);
        BigInteger exp = new BigInteger(publicKeyExp);
        BigInteger mod = new BigInteger(publicKeyMod);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(mod, exp);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey pubKey = fact.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(plainText.getBytes("UTF-8"));
        return new String( Base64.encodeToString(cipherData, Base64.NO_WRAP));
    }

    public static String getHashValue(Context context) {
        String returnVal = "";
        InputStream input = null;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo data = pm.getApplicationInfo(
//                    context.getPackageName(), 128);
                    context.getPackageName(), ApplicationInfo.FLAG_UPDATED_SYSTEM_APP);//0x80

            input = new FileInputStream(data.publicSourceDir);
            byte[] buffer = new byte[1024];
            MessageDigest md5Hash = MessageDigest.getInstance("SHA-512");
            int numRead = 0;
            while (numRead != -1) {
                numRead = input.read(buffer);
                if (numRead > 0) {
                    md5Hash.update(buffer, 0, numRead);
                }
            }

            byte[] md5Bytes = md5Hash.digest();
            for (int i = 0; i < md5Bytes.length; i++) {
                returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
            }
        } catch (Throwable t) {
            log.vlog(2, "에러 : " + t.getMessage()); // error
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException e) {
                log.vlog(2, "에러 : " + e.getMessage()); // error
            }
        }

        return returnVal.toUpperCase();
    }

    public static String getHmac2(String key, String data) {

        String hash = null;

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(),
                    "HmacSHA256");
            sha256_HMAC.init(secret_key);
            hash = new String(Base64.encodeToString(sha256_HMAC.doFinal(data.getBytes()), Base64.NO_WRAP));
        } catch (NoSuchAlgorithmException e) {
            log.vlog(2, "에러 : " + e.getMessage()); // error
        } catch (InvalidKeyException e) {
            log.vlog(2, "에러 : " + e.getMessage()); // error
        }
        return hash;
    }

    public static String GetDevicesUUID(Context mContext){
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return deviceId;
    }

    public static String getUniqueDeviceID(Context c) {
        final TelephonyManager tm = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
        final WifiManager wm = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);

        final String androidId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID); //Settings.Secure.ANDROID_ID; // 2.2 이상에서는 Unique한 ID를 제공

        String ret = null;

        if(tm != null) {

            // Phones
            final String tmDevice, tmSerial;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            ret = deviceUuid.toString();

        } else if(wm != null) {

            // Tablet 등 Telephony 없는 Device
            WifiInfo wi = wm.getConnectionInfo();
            String mac = wi.getMacAddress();
            UUID deviceUuid = new UUID(androidId.hashCode(), mac.hashCode());
            ret = deviceUuid.toString();

        } else {

            // Telephony도 없고, WIFI도 없는 경우
            UUID deviceUuid = new UUID(androidId.hashCode(), "UNKNOWNDEVICE".hashCode());
            ret = deviceUuid.toString();

        }

        return ret;
    }

    public static void loadWebSite(Context context, String webURL) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
        context.startActivity(intent);
    }

    public static String yyyyMMdd() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getFinName(String type) {
        if(type == null)
            return "";

        String finName ="";

        if("KB".equals(type))
            finName = "KB국민은행";
        else if("KC".equals(type))
            finName = "KB국민카드";
        else if("NB".equals(type))
            finName = "NH농협은행";
        else if("SC".equals(type))
            finName = "신한카드";

        return finName;
    }

    public static String getPayState(int type) {
        String payState = "";
        switch (type) {
            case 0:
                payState = "미결제";
                break;
            case 1:
                payState = "결제성공";
                break;
            case 2:
                payState = "결제실패";
                break;
            case 3:
                payState = "결제취소성공";
                break;
            case 4:
                payState = "결제취소실패";
                break;
        }
        return payState;
    }

    public static String getInstallmentStr(int type) {
        String returnData = "";
        switch (type) {
            case 0:
            case 1:
                returnData = "일시불";
                break;

            default:
                returnData = String.valueOf(type) + " 개월";
                break;
        }

        return returnData;
    }

    public static String getSeqNumFormat(String seqNum) {
        if(seqNum.length() < 16)
            return "";
        StringBuffer stringBuffer = new StringBuffer(seqNum);
        for(int i=0 ; i<3 ; i++) {
            stringBuffer = stringBuffer.insert(5*(1+i), "-");
        }

        return stringBuffer.toString();
    }

    public static String getAmountStr(double amount) {
        String priceStr = String.format("%,.0f", amount);
        return priceStr += "원";
    }
}
