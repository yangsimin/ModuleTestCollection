package com.example.administrator.myapplication.utils;

import android.util.Log;

import com.example.administrator.myapplication.model.DevInfo;
import com.keymantek.serialport.utils.HexUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class DevInfoUtil
{
    private static DevInfo devInfoInstance;

    private static final String strAddress = "/.test";

    private static final String strFileName = ".DevInfo.sys";

    public static DevInfo getInstance() throws Exception
    {
        if (devInfoInstance == null)
        {
            devInfoInstance = parser();
        }
        return devInfoInstance;
    }


    public static DevInfo parser() throws Exception
    {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();

        InputStream inStream = new FileInputStream(new File(strAddress, strFileName));
        xpp.setInput(decryptStream(inStream), "UTF-8");

        int eventType = xpp.getEventType();
        DevInfo devInfo = null;
        while (eventType != XmlPullParser.END_DOCUMENT)
        {
            switch (eventType)
            {
                case XmlPullParser.START_DOCUMENT:
                    devInfo = new DevInfo();
                    break;
                case XmlPullParser.START_TAG:
                    String stName = xpp.getName();
                    if ("DeviceID".equals(stName))
                    {
                        devInfo.setDeviceID(xpp.nextText());
                    } else if ("SN".equals(stName))
                    {
                        devInfo.setSN(xpp.nextText());
                    } else if ("HardVersion".equals(stName))
                    {
                        devInfo.setHardVersion(xpp.nextText());
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return devInfo;
    }

    private static final byte[] _iv = {0x46, (byte) 0xCA, (byte) 0xFF, (byte) 0xD0, (byte) 0xC6, 0x54, 0x7D, (byte) 0xCC, (byte) 0xA1, (byte) 0xD9, 0x6C, 0x5B, 0x12, (byte) 0x96, (byte) 0x94, 0x28};
    private static final byte[] _key = {0x3E, 0x58, (byte) 0xFC, 0x48, 0x33, (byte) 0xA5, 0x59, (byte) 0x99, (byte) 0xBF, 0x74, 0x10, 0x22, 0x40, (byte) 0x8C, 0x1D, (byte) 0xCB, 0x34, 0x1D, 0x32, 0x60, (byte) 0xAB, (byte) 0x9E, 0x43, 0x7C, 0x0D, 0x37, 0x63, (byte) 0xA5, (byte) 0x91, (byte) 0xFE, (byte) 0xC7, 0x31};

    //解密
    public static String Decrypt(String sSrc) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        SecretKeySpec skeySpec = new SecretKeySpec(_key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(_iv);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(HexUtils.hexStringToByte(sSrc));
        return new String(original);
    }

    //加密
    public static String Encrypt(String sSrc) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(_key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(_iv);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
        return bytes2HexString(encrypted, encrypted.length).toLowerCase();
    }


    public static String test() throws Exception
    {
        FileInputStream inStream = new FileInputStream(new File("/sdcard", "test.txt"));
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        byte[] buf = new byte[32];
        int readCount;
        while ((readCount = inStream.read(buf, 0, buf.length)) != -1)
        {
            baoStream.write(buf, 0, readCount);
        }
        buf = baoStream.toByteArray();
        String msg = bytes2HexString(buf, buf.length);
        Log.d("see", "test() returned: " + msg);
        return Decrypt(msg);
    }

    public static String bytes2HexString(byte[] bArray, int size)
    {
        StringBuffer sb = new StringBuffer(size);
        String sTemp;
        for (int i = 0; i < size; i++)
        {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static InputStream decryptStream(InputStream inStream) throws Exception
    {
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int readCount;
        while ((readCount = inStream.read(buf, 0, buf.length)) != -1)
        {
            baoStream.write(buf, 0, readCount);
        }
        buf = baoStream.toByteArray();

        //TODO 进行解密
        String msg = bytes2HexString(buf, buf.length);
        String decrypt = Decrypt(msg);
        if (decrypt != null) buf = decrypt.getBytes();
        else return null;

        return new ByteArrayInputStream(buf);
    }
}
