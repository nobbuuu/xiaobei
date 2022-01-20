package com.tcl.base.utils.encipher;


import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DES加解密工具类
 * <p>
 * Created by yaojt on 2020/12/14.
 */

public class DesUtil {
    public static String DES3KEY = "k3pkOQE7q7OpJ9avDLH3gny9/EX4MWF7uUuf0xwxGvwTTwCWlUAP5Q+PUpk+K3j9"; //默认3des密钥

//    public static final String ALGORITHM_DES = "desede/CBC/PKCS5Padding"; //DES/CBC/PKCS5Padding
//    public static final String ivStr = "";

    /* used **************************************************************/

    // 向量
//    private final static String iv = "";

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return String
     * @throws Exception Exception
     */
    public static String encrypt(String plainText)
            throws Exception {
        return encrypt(plainText, DES3KEY);
    }

    public static String decrypt(String encryptText)
            throws Exception {
        return decrypt(encryptText, DES3KEY);
    }

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return String
     * @throws Exception Exception
     */
    public static String encrypt(String plainText, String secretKey)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec("01234567".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return String
     * @throws Exception Exception
     */
    public static String decrypt(String encryptText, String secretKey)
            throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec("01234567".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
        return new String(decryptData, encoding);
    }

}
