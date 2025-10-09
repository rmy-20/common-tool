package io.github.rmy20.tool.crypto.util;


import io.github.rmy20.tool.crypto.constant.CryptoConstant;
import io.github.rmy20.tool.crypto.constant.ProviderEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;

import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Objects;

/**
 * 密钥工具类
 *
 * @author sheng
 */
public class KeyUtil {
    /**
     * 转换私钥
     *
     * @param privateKey 私钥
     * @param provider   算法提供者，默认使用 JDK
     */
    public static ECPrivateKey loadPkcs8EcPrivateKey(byte[] privateKey, ProviderEnum provider) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = Objects.isNull(provider) ? KeyFactory.getInstance(CryptoConstant.ALGORITHM_EC)
                    : KeyFactory.getInstance(CryptoConstant.ALGORITHM_EC, provider.getProvider());
            return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new CryptoException("EC转换私钥异常", e);
        }
    }
}
