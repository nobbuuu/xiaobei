package com.tcl.base.utils.encipher;

import com.blankj.utilcode.util.LogUtils;

import org.apaches.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {

    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
    public static final String KEY_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCmgxORkcmxF0LzGmqFVTqnX02dGFnIyDoggRY4G7bZVw3zbWsbPFAJIbzbm3hVfFm75xDu3uCda3qBiXKTeTsOXcpopypmcv3yK6YWBGuVkrZqNVNwGmk+HmLCyr5mDdlcESX2ywpCjALwljA4CfSPsiE8rouQLlAuhUAFMhszwIDAQAB";

    public static final String DEFAULT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIKaDE5GRybEXQvMaaoVVOqdfTZ0YWcjIOiCBFjgbttlXDfNtaxs8UAkhvNubeFV8WbvnEO7e4J1reoGJcpN5Ow5dyminKmZy_fIrphYEa5WStmo1U3AaaT4eYsLKvmYN2VwRJfbLCkKMAvCWMDgJ9I-yITyui5AuUC6FQAUyGzPAgMBAAECgYA4vSRLfUy8EuM5lPzy34Gcy_7yAW2vmRta5-Frm1WOC2Mc-0nKNyo4a6YVbAxgsS3HQqHo6zKlYurDmRgh4-Vn-lmBg1ikuUO_5i8JbtRkZONH7m08ngpLEKb0VRQn3tSJAUxIQtP1D9khX--BoS1c8KZBKhR_iPtyoyF8BZTWwQJBANXku-P9t97LPzBopN8T0Zi-tZNX0IiBDTk1BeXNjEqtHXeZ_pWYbm5y0OBNRz3Kgcm2C4uYpA8wDNdhGdJyG5UCQQCcT8lXGACaV5o5DAz0g_u7Gs6v8zYxlqd5MHqJMyjMYriP0P7Ya7yWk5JEzBcnzeb0EoekXV8O1rYFMTWtuK3TAkEAl4LSETrM_zVrikfl2dr5EkCs8DU5EyiPDGHhzpmecdtUzz63FpKGTxhnmytCdqO28gJRBU5XkCaSQhKXLvoDTQJBAJKdxB0NRoN-6AnaY8x2gAhgfL5NQugAh9YEwusuMXaEotlDD8ewRpR2lxww0wdi-t61AsYJTsB8ZP85yT_JmkECQBDiHZZ29pyjQKgTAE_fOWfvC4Dlt9cGoQ1GvQeEoVPksJ_k8PA5qu_Dc9o_qwe43GJvuibUypJeVfdipDB2DnE";

    public static Map<String, String> createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such compute-->[" + RSA_ALGORITHM + "]");
        }

        // 初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize);
        // 生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        // 得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
        // 得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);

        return keyPairMap;
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥加密
     *
     * @return
     */
    public static String publicEncrypt(String key, String message){
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            PublicKey publicKey = decodePublicKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] miwen = cipher.doFinal(message.getBytes());
            return Base64.encodeBase64String(miwen);
        }catch (Exception e){
            throw new RuntimeException("加密字符串[" + message + "]时遇到异常", e);
        }
    }

    /**
     * 通过字符串转为公钥
     */
    public static PublicKey decodePublicKey(String publicKeyData) {
        PublicKey publicKey = null;
        try {
            byte[] decodeKey = Base64.decodeBase64(publicKeyData);
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodeKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(x509);
        } catch (Exception e) {
            LogUtils.e("通过字符串转为公钥异常,publicKeyData=" + publicKeyData, e);
        }
        return publicKey;
    }

    public static void test() throws Exception {
        String mobile = "13631919041";
        String pwd = "txk@123456";
        String enCodeMobile = RSAUtil.publicEncrypt(mobile,RSAUtil.getPublicKey(DEFAULT_PUBLIC_KEY));//公钥加密
        String enCodePwd = RSAUtil.publicEncrypt(pwd,RSAUtil.getPublicKey(DEFAULT_PUBLIC_KEY));//公钥加密
        LogUtils.dTag("asaqdsdas",enCodeMobile);

        String account = publicEncrypt(mobile, getPublicKey(RSAUtil.DEFAULT_PUBLIC_KEY));
        String psw = publicEncrypt(pwd, getPublicKey(RSAUtil.DEFAULT_PUBLIC_KEY));

    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), publicKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        /*Map<String, String> keyMap = createKeys(1024);
        String  publicKey = keyMap.get("publicKey");
        String  privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n" + publicKey);
        System.out.println("私钥： \n" + privateKey);

        System.out.println("公钥加密——私钥解密");
        String str = "hello word!";
        System.out.println("明文：\n" + str);
        System.out.println("明文大小：\n" + str.getBytes().length);
        String encodedData = publicEncrypt(str, getPublicKey(publicKey));
        System.out.println("密文：\n" + encodedData);
        String decodedData = privateDecrypt(encodedData, getPrivateKey(privateKey));
        System.out.println("解密后文字: \n" + decodedData);*/
        System.out.println(publicEncrypt("tjtjfb025", getPublicKey(DEFAULT_PUBLIC_KEY)));
        System.out.println(publicEncrypt("tjtjfb025CRM", getPublicKey(DEFAULT_PUBLIC_KEY)));
        System.out.println(publicEncrypt("tjtjfb026", getPublicKey(DEFAULT_PUBLIC_KEY)));
        System.out.println(publicEncrypt("123456", getPublicKey(DEFAULT_PUBLIC_KEY)));
        //System.out.println(privateDecrypt("V1ZsYWVWL3RtdzhZZEpoS09YUHN2YytyTFRlOUJIQk1kS1ZEcVVVb1hsRnRsNEI5L3JpbjFhQi8xZ2NwRmxmZWlwQkp0YVBBbW5aQWtFSkxQSnhNeWR6VGsyNHg5VzIzUWZjNTVoOVY5UVh3REovbmRBb01xck93TUdpdkdEZGovdFpLaGgrN1I2dXZNMmJmQ0ZueEcxS3M1c1FGb0YvekRPdS9jcnc5UWhJPQ", getPrivateKey(DEFAULT_PRIVATE_KEY)));
        //System.out.println(URLDecoder.decode("V1ZsYWVWL3RtdzhZZEpoS09YUHN2YytyTFRlOUJIQk1kS1ZEcVVVb1hsRnRsNEI5L3JpbjFhQi8xZ2NwRmxmZWlwQkp0YVBBbW5aQWtFSkxQSnhNeWR6VGsyNHg5VzIzUWZjNTVoOVY5UVh3REovbmRBb01xck93TUdpdkdEZGovdFpLaGgrN1I2dXZNMmJmQ0ZueEcxS3M1c1FGb0YvekRPdS9jcnc5UWhJPQ","Unicode"));
    }
}