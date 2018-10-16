package com.gmdesign.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by DJL on 2017/6/2.
 *
 * @ClassName cells
 * @Description
 */
public class DESEncrypt {
    private DESEncrypt(){}
    private static byte[] coderByDES(byte[] plainText, String key, int mode) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        SecureRandom sr = new SecureRandom();
        byte[] resultKey = makeKey(key);
        DESKeySpec desSpec = new DESKeySpec(resultKey);
        SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(desSpec);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(mode, secretKey, sr);
        return cipher.doFinal(plainText);
    }

    private static byte[] makeKey(String key) throws UnsupportedEncodingException {
        byte[] keyByte = new byte[8];
        byte[] keyResult = key.getBytes("UTF-8");

        for(int i = 0; i < keyResult.length && i < keyByte.length; ++i) {
            keyByte[i] = keyResult[i];
        }

        return keyByte;
    }

    public static String encoderByDES(String privateKey, String plainText) {
        try {
            byte[] result = coderByDES(plainText.getBytes("UTF-8"), privateKey, 1);
            return byteArr2HexStr(result);
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }

    public static String decoderByDES(String privateKey, String secretText) {
        try {
            byte[] result = coderByDES(hexStr2ByteArr(secretText), privateKey, 2);
            return new String(result, "UTF-8");
        } catch (Exception var3) {
            var3.printStackTrace();
            return "";
        }
    }

    private static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        StringBuilder sb = new StringBuilder(iLen * 2);
        for (byte anArrB : arrB) {
            int intTmp;
            for (intTmp = anArrB; intTmp < 0; ) {
                intTmp += 256;
            }

            if (intTmp < 16) {
                sb.append("0");
            }

            sb.append(Integer.toString(intTmp,16));
        }

        return sb.toString();
    }

    private static byte[] hexStr2ByteArr(String strIn) throws NumberFormatException {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];

        for(int i = 0; i < iLen; i += 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte)Integer.parseInt(strTmp, 16);
        }

        return arrOut;
    }
}
