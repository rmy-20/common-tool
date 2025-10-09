package io.github.rmy20.tool.crypto.symmetric;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import io.github.rmy20.tool.crypto.constant.ModeEnum;
import io.github.rmy20.tool.crypto.constant.PaddingEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Consumer;

/**
 * 对称加密器
 *
 * @author sheng
 */
public interface Encrypt {

    // region 加密为base64字符串

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     */
    default String encryptBase64(String plainText, SecretKey secretKey) {
        return encryptBase64(plainText, secretKey, null);
    }

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     */
    default String encryptBase64(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec) {
        return encryptBase64(plainText, secretKey, paramsSpec, null, null, null);
    }

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     * @param mode      分组模式
     * @param padding   填充模式
     */
    default String encryptBase64(String plainText, SecretKey secretKey, ModeEnum mode, PaddingEnum padding) {
        return encryptBase64(plainText, secretKey, null, null, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText  待被加密的字符串
     * @param secretKey  密钥
     * @param paramsSpec 算法参数
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String encryptBase64(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 ModeEnum mode, PaddingEnum padding) {
        return encryptBase64(plainText, secretKey, paramsSpec, null, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText        待被加密的字符串
     * @param secretKey        密钥
     * @param paramsSpec       算法参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    default String encryptBase64(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return encryptBase64(plainText, StandardCharsets.UTF_8, secretKey, paramsSpec, cipherAdditional, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText  待被加密的字符串
     * @param charset    字符集
     * @param paramsSpec 算法参数
     * @param secretKey  密钥
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String encryptBase64(String plainText, Charset charset, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        try {
            return Base64Util.encodeToString(encrypt(plainText.getBytes(charset), secretKey, paramsSpec, cipherAdditional, mode, padding));
        } catch (Exception e) {
            throw new CryptoException("对称加密异常", e);
        }
    }
    // endregion

    // region 加密为hex字符串

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     */
    default String encryptHex(String plainText, SecretKey secretKey) {
        return encryptHex(plainText, secretKey, null);
    }

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     */
    default String encryptHex(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec) {
        return encryptHex(plainText, secretKey, paramsSpec, null, null, null);
    }

    /**
     * 加密
     *
     * @param plainText 待被加密的字符串
     * @param secretKey 密钥
     * @param mode      分组模式
     * @param padding   填充模式
     */
    default String encryptHex(String plainText, SecretKey secretKey, ModeEnum mode, PaddingEnum padding) {
        return encryptHex(plainText, secretKey, null, null, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText  待被加密的字符串
     * @param paramsSpec 算法参数
     * @param secretKey  密钥
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String encryptHex(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                              ModeEnum mode, PaddingEnum padding) {
        return encryptHex(plainText, secretKey, paramsSpec, null, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText        待被加密的字符串
     * @param paramsSpec       算法参数
     * @param secretKey        密钥
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    default String encryptHex(String plainText, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                              Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return encryptHex(plainText, StandardCharsets.UTF_8, secretKey, paramsSpec, cipherAdditional, mode, padding);
    }

    /**
     * 加密
     *
     * @param plainText  待被加密的字符串
     * @param charset    字符集
     * @param paramsSpec 算法参数
     * @param secretKey  密钥
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String encryptHex(String plainText, Charset charset, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                              Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        try {
            return HexUtil.encodeHexString(encrypt(plainText.getBytes(charset), secretKey, paramsSpec, cipherAdditional, mode, padding));
        } catch (Exception e) {
            throw new CryptoException("对称加密异常", e);
        }
    }
    // endregion

    /**
     * 加密
     *
     * @param data             待被加密的bytes
     * @param paramsSpec       算法参数
     * @param secretKey        密钥
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    byte[] encrypt(byte[] data, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                   Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding);
}
