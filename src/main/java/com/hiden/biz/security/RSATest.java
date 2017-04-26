package com.hiden.biz.security;

import java.nio.charset.Charset;

import com.hiden.biz.common.ResourcesConstants;


public class RSATest {
	public static void main(String[] args) {
		
		String publickey="D:\\public.key";
		String privatekey="D:\\private.key";
        RSAEncrypt rsaEncrypt = new RSAEncrypt();
        //加载公钥
        try {
            //rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
            rsaEncrypt.loadPublicKeyFromFile(publickey);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }
        //加载私钥
        try {
            //rsaEncrypt.loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
            rsaEncrypt.loadPrivateKeyFromFile(privatekey);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }
        //测试字符串
        String toEncryptStr = "sfdfrerewref";
        try {
            //加密
            byte[] toEncryptBytes = toEncryptStr.getBytes();// toEncryptStr.getBytes();//原始字符串
            //原始字符串的16进制字符串
            String originHexStr = AESUtil.parseByte2HexStr(toEncryptBytes);//原始字符串转hex string

            //加密
            byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), originHexStr.getBytes());//rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), toEncryptBytes);
            String encryptedHexString = AESUtil.parseByte2HexStr(cipher);//传输前做16进制字符串处理
            System.out.println("传输前，中的16进制：" + encryptedHexString);
            System.out.println("传输前，中的16进制len：" + encryptedHexString.length());
            System.out.println("加密后16进制字符串:");
            System.out.println(encryptedHexString);
            System.out.println("加密后数据：" + new String(cipher, "UTF-8"));
            System.out.println("加密前数据:" + toEncryptStr);
            //解密
            byte[] hexedCipher = AESUtil.parseHexStr2Byte(encryptedHexString);
            byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), hexedCipher);
            
            //解密后是原始密码的16进制字符串
            //16进制解密
            String ends = new String(plainText, "UTF-8");
            
            byte[] parseHexStr2Byte = AESUtil.parseHexStr2Byte(ends);
            
            ends = new String(parseHexStr2Byte, "UTF-8");
            
            System.out.println("最终解密结果：" + new String(plainText));
            System.out.println("最终解密结果len：" + new String(plainText).length());
        	
        	/*byte[] cipher = rsaEncrypt.encrypt(rsaEncrypt.getPublicKey(), toEncryptStr.getBytes());
        	String encodepw = new String(cipher, "UTF-8");
        	
        	byte[] plainText = rsaEncrypt.decrypt(rsaEncrypt.getPrivateKey(), encodepw.getBytes());
        	String decodepw = new String(plainText, "UTF-8");
        	System.out.println(encodepw);
        	System.out.println(decodepw);*/
        	
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
