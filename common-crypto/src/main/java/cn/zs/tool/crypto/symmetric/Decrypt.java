package cn.zs.tool.crypto.symmetric;

import cn.zs.tool.core.codec.binary.Base64Util;
import cn.zs.tool.core.codec.binary.HexUtil;
import cn.zs.tool.crypto.constant.ModeEnum;
import cn.zs.tool.crypto.constant.PaddingEnum;
import cn.zs.tool.crypto.exception.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.function.Consumer;

/**
 * 对称解密器接口
 *
 * @author sheng
 */
public interface Decrypt {
    // region 解密base64字符串

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     */
    default String decryptBase64(String encrypt, SecretKey secretKey) {
        return decryptBase64(encrypt, secretKey, null);
    }

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     */
    default String decryptBase64(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec) {
        return decryptBase64(encrypt, secretKey, paramsSpec, null, null, null);
    }

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     * @param mode      分组模式
     * @param padding   填充模式
     */
    default String decryptBase64(String encrypt, SecretKey secretKey, ModeEnum mode, PaddingEnum padding) {
        return decryptBase64(encrypt, secretKey, null, null, mode, padding);
    }

    /**
     * 解密
     *
     * @param encrypt    待待被解密的密文
     * @param secretKey  密钥
     * @param paramsSpec 加密参数
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String decryptBase64(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 ModeEnum mode, PaddingEnum padding) {
        return decryptBase64(encrypt, secretKey, paramsSpec, null, mode, padding);
    }

    /**
     * 解密
     *
     * @param encrypt          待待被解密的密文
     * @param secretKey        密钥
     * @param paramsSpec       加密参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    default String decryptBase64(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return decryptBase64(encrypt, secretKey, paramsSpec, cipherAdditional, mode, padding, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param encrypt    待待被解密的密文
     * @param secretKey  密钥
     * @param paramsSpec 加密参数
     * @param mode       分组模式
     * @param padding    填充模式
     * @param charset    字符集
     */
    default String decryptBase64(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                                 Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding, Charset charset) {
        try {
            return new String(decrypt(Base64Util.decode(encrypt), secretKey, paramsSpec, cipherAdditional, mode, padding), charset);
        } catch (Exception e) {
            throw new CryptoException("对称解密异常", e);
        }
    }
    // endregion

    // region 解密hex字符串

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     */
    default String decryptHex(String encrypt, SecretKey secretKey) {
        return decryptHex(encrypt, secretKey, null);
    }

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     */
    default String decryptHex(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec) {
        return decryptHex(encrypt, secretKey, paramsSpec, null, null, null);
    }

    /**
     * 解密
     *
     * @param encrypt   待待被解密的密文
     * @param secretKey 密钥
     * @param mode      分组模式
     * @param padding   填充模式
     */
    default String decryptHex(String encrypt, SecretKey secretKey, ModeEnum mode, PaddingEnum padding) {
        return decryptHex(encrypt, secretKey, null, null, mode, padding);
    }

    /**
     * 解密
     *
     * @param encrypt    待待被解密的密文
     * @param secretKey  密钥
     * @param paramsSpec 加密参数
     * @param mode       分组模式
     * @param padding    填充模式
     */
    default String decryptHex(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec, ModeEnum mode, PaddingEnum padding) {
        return decryptHex(encrypt, secretKey, paramsSpec, null, mode, padding);
    }

    /**
     * 解密
     *
     * @param encrypt          待待被解密的密文
     * @param secretKey        密钥
     * @param paramsSpec       加密参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    default String decryptHex(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                              Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return decryptHex(encrypt, secretKey, paramsSpec, cipherAdditional, mode, padding, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param encrypt          待待被解密的密文
     * @param secretKey        密钥
     * @param paramsSpec       加密参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     * @param charset          字符集
     */
    default String decryptHex(String encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                              Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding, Charset charset) {
        try {
            return new String(decrypt(HexUtil.decodeHex(encrypt), secretKey, paramsSpec, cipherAdditional, mode, padding), charset);
        } catch (Exception e) {
            throw new CryptoException("对称解密异常", e);
        }
    }
    // endregion

    /**
     * 解密
     *
     * @param encrypt          待待被解密的byte[]
     * @param secretKey        密钥
     * @param paramsSpec       加密参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    byte[] decrypt(byte[] encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                   Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding);
}
