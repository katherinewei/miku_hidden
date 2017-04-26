
package com.hiden.biz.wechat.common.util.crypto;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class WxCryptUtil {
    private static final Base64 base64 = new Base64();
    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final ThreadLocal<DocumentBuilder> builderLocal = new ThreadLocal() {
        protected DocumentBuilder initialValue() {
            try {
                return DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch (ParserConfigurationException var2) {
                throw new IllegalArgumentException(var2);
            }
        }
    };
    protected byte[] aesKey;
    protected String token;
    protected String appidOrCorpid;

    public WxCryptUtil() {
    }

    public WxCryptUtil(String token, String encodingAesKey, String appidOrCorpid) {
        this.token = token;
        this.appidOrCorpid = appidOrCorpid;
        this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }

    public String encrypt(String plainText) {
        String encryptedXml = this.encrypt(this.genRandomStr(), plainText);
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000L);
        String nonce = this.genRandomStr();

        try {
            String e = SHA1.gen(new String[]{this.token, timeStamp, nonce, encryptedXml});
            String result = this.generateXml(encryptedXml, e, timeStamp, nonce);
            return result;
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException(var7);
        }
    }

    protected String encrypt(String randomStr, String plainText) {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStringBytes = randomStr.getBytes(CHARSET);
        byte[] plainTextBytes = plainText.getBytes(CHARSET);
        byte[] bytesOfSizeInNetworkOrder = this.number2BytesInNetworkOrder(plainTextBytes.length);
        byte[] appIdBytes = this.appidOrCorpid.getBytes(CHARSET);
        byteCollector.addBytes(randomStringBytes);
        byteCollector.addBytes(bytesOfSizeInNetworkOrder);
        byteCollector.addBytes(plainTextBytes);
        byteCollector.addBytes(appIdBytes);
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);
        byte[] unencrypted = byteCollector.toBytes();

        try {
            Cipher e = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(this.aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(this.aesKey, 0, 16);
            e.init(1, keySpec, iv);
            byte[] encrypted = e.doFinal(unencrypted);
            String base64Encrypted = base64.encodeToString(encrypted);
            return base64Encrypted;
        } catch (Exception var15) {
            throw new RuntimeException(var15);
        }
    }

    public String decrypt(String msgSignature, String timeStamp, String nonce, String encryptedXml) {
        String cipherText = extractEncryptPart(encryptedXml);

        String result;
        try {
            result = SHA1.gen(new String[]{this.token, timeStamp, nonce, cipherText});
            if(!result.equals(msgSignature)) {
                throw new RuntimeException("加密消息签名校验失败");
            }
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException(var7);
        }

        result = this.decrypt(cipherText);
        return result;
    }

    public String decrypt(String cipherText) {
        byte[] original;
        byte[] networkOrder;
        try {
            Cipher xmlContent = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec from_appid = new SecretKeySpec(this.aesKey, "AES");
            IvParameterSpec e = new IvParameterSpec(Arrays.copyOfRange(this.aesKey, 0, 16));
            xmlContent.init(2, from_appid, e);
            networkOrder = Base64.decodeBase64(cipherText);
            original = xmlContent.doFinal(networkOrder);
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        }

        String xmlContent1;
        String from_appid1;
        try {
            byte[] e1 = PKCS7Encoder.decode(original);
            networkOrder = Arrays.copyOfRange(e1, 16, 20);
            int xmlLength = this.bytesNetworkOrder2Number(networkOrder);
            xmlContent1 = new String(Arrays.copyOfRange(e1, 20, 20 + xmlLength), CHARSET);
            from_appid1 = new String(Arrays.copyOfRange(e1, 20 + xmlLength, e1.length), CHARSET);
        } catch (Exception var8) {
            throw new RuntimeException(var8);
        }

        if(!from_appid1.equals(this.appidOrCorpid)) {
            throw new RuntimeException("AppID不正确");
        } else {
            return xmlContent1;
        }
    }

    private byte[] number2BytesInNetworkOrder(int number) {
        byte[] orderBytes = new byte[]{(byte)(number >> 24 & 255), (byte)(number >> 16 & 255), (byte)(number >> 8 & 255), (byte)(number & 255)};
        return orderBytes;
    }

    private int bytesNetworkOrder2Number(byte[] bytesInNetworkOrder) {
        int sourceNumber = 0;

        for(int i = 0; i < 4; ++i) {
            sourceNumber <<= 8;
            sourceNumber |= bytesInNetworkOrder[i] & 255;
        }

        return sourceNumber;
    }

    private String genRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < 16; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }

    private String generateXml(String encrypt, String signature, String timestamp, String nonce) {
        String format = "<xml>\n<Encrypt><![CDATA[%1$s]]></Encrypt>\n<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n<TimeStamp>%3$s</TimeStamp>\n<Nonce><![CDATA[%4$s]]></Nonce>\n</xml>";
        return String.format(format, new Object[]{encrypt, signature, timestamp, nonce});
    }

    static String extractEncryptPart(String xml) {
        try {
            DocumentBuilder e = (DocumentBuilder)builderLocal.get();
            Document document = e.parse(new InputSource(new StringReader(xml)));
            Element root = document.getDocumentElement();
            return root.getElementsByTagName("Encrypt").item(0).getTextContent();
        } catch (Exception var4) {
            throw new RuntimeException(var4);
        }
    }
}
