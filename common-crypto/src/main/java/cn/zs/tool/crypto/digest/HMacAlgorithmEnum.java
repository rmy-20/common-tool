package cn.zs.tool.crypto.digest;

import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Provider;
import java.util.Objects;

/**
 * Hmac 算法
 *
 * @author sheng
 */
@Getter
public enum HMacAlgorithmEnum {
    /**
     * HmacMD5 算法
     */
    HmacMD5(AlgorithmEnum.HmacMD5),

    /**
     * HmacSHA1 算法
     */
    HmacSHA1(AlgorithmEnum.HmacSHA1),

    /**
     * HmacSHA256 算法
     */
    HmacSHA256(AlgorithmEnum.HmacSHA256),

    /**
     * HmacSHA384 算法
     */
    HmacSHA384(AlgorithmEnum.HmacSHA384),

    /**
     * HmacSHA512 算法
     */
    HmacSHA512(AlgorithmEnum.HmacSHA512),

    /**
     * HmacSM3 算法
     */
    HmacSM3(AlgorithmEnum.HmacSM3),
    ;

    /**
     * 将 byte[] 密钥转换为 SecretKey
     *
     * @param key 密钥
     */
    public SecretKey toSecretKey(final byte[] key) {
        return new SecretKeySpec(key, algorithm.getAlgorithm());
    }

    /**
     * 创建 mac 对象
     *
     * @param key 密钥
     */
    public Mac createMac(SecretKey key) {
        AlgorithmEnum algorithm = getAlgorithm();
        Objects.requireNonNull(key, "MAC[" + algorithm.getAlgorithm() + "]实例密钥不允许为空");
        try {
            Provider provider = algorithm.provider();
            Mac mac = Objects.nonNull(provider) ? Mac.getInstance(algorithm.getAlgorithm(), provider)
                    : Mac.getInstance(algorithm.getAlgorithm());
            mac.init(key);
            return mac;
        } catch (Exception e) {
            throw new CryptoException("创建[" + algorithm.getAlgorithm() + "]MAC实例异常", e);
        }
    }

    HMacAlgorithmEnum(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 算法
     */
    private final AlgorithmEnum algorithm;
}
