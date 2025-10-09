package io.github.rmy20.tool.crypto.digest;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import lombok.Getter;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Hmac 工具类
 *
 * @author sheng
 */
@Getter
public enum HMacTool {
    /**
     * HmacMD5 算法
     */
    HmacMD5(HMacAlgorithmEnum.HmacMD5),

    /**
     * HmacSHA1 算法
     */
    HmacSHA1(HMacAlgorithmEnum.HmacSHA1),

    /**
     * HmacSHA256 算法
     */
    HmacSHA256(HMacAlgorithmEnum.HmacSHA256),

    /**
     * HmacSHA384 算法
     */
    HmacSHA384(HMacAlgorithmEnum.HmacSHA384),

    /**
     * HmacSHA512 算法
     */
    HmacSHA512(HMacAlgorithmEnum.HmacSHA512),

    /**
     * HmacSM3 算法
     */
    HmacSM3(HMacAlgorithmEnum.HmacSM3),
    ;

    /**
     * 将 byte[] 密钥转换为 SecretKey
     *
     * @param key 密钥
     */
    public SecretKey toSecretKey(final byte[] key) {
        return algorithm.toSecretKey(key);
    }

    // region String mac

    // region hex

    /**
     * mac
     *
     * @param plainText 待mac数据
     * @param key       密钥
     */
    public String macHex(String plainText, SecretKey key) {
        return macHex(plainText, StandardCharsets.UTF_8, key);
    }

    /**
     * mac
     *
     * @param plainText 待mac数据
     * @param key       密钥
     */
    public String macHex(String plainText, Charset charset, SecretKey key) {
        return macHex(plainText.getBytes(charset), key);
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public String macHex(byte[] data, SecretKey key) {
        return HexUtil.encodeHexString(mac(data, key));
    }

    // endregion

    // region base64

    /**
     * mac
     *
     * @param plainText 待mac数据
     * @param key       密钥
     */
    public String macBase64(String plainText, SecretKey key) {
        return macBase64(plainText, StandardCharsets.UTF_8, key);
    }

    /**
     * mac
     *
     * @param plainText 待mac数据
     * @param key       密钥
     */
    public String macBase64(String plainText, Charset charset, SecretKey key) {
        return macBase64(plainText.getBytes(charset), key);
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public String macBase64(byte[] data, SecretKey key) {
        return Base64Util.encodeToString(mac(data, key));
    }

    //endregion

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public byte[] mac(byte[] data, SecretKey key) {
        return mac.mac(data, key);
    }

    // endregion

    // region InputStream mac

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public String macHex(InputStream data, SecretKey key) {
        return HexUtil.encodeHexString(mac(data, key));
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public String macBase64(InputStream data, SecretKey key) {
        return Base64Util.encodeToString(mac(data, key));
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public byte[] mac(InputStream data, SecretKey key) {
        return mac.mac(data, key);
    }

    // endregion

    HMacTool(HMacAlgorithmEnum algorithm) {
        this.algorithm = algorithm;
        this.mac = HMac.create(algorithm);
    }

    /**
     * 算法
     */
    private final HMacAlgorithmEnum algorithm;

    /**
     * 算法实例
     */
    private final HMac mac;
}
