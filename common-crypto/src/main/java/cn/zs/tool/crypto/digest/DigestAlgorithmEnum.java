package cn.zs.tool.crypto.digest;

import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;

import java.security.MessageDigest;
import java.security.Provider;
import java.util.Objects;

/**
 * 摘要算法枚举
 *
 * @author sheng
 */
@Getter
public enum DigestAlgorithmEnum {
    /**
     * SM3
     */
    SM3(AlgorithmEnum.SM3),

    /**
     * MD5
     */
    MD5(AlgorithmEnum.MD5),

    /**
     * SHA-1
     */
    SHA_1(AlgorithmEnum.SHA_1),

    /**
     * SHA-256
     */
    SHA_256(AlgorithmEnum.SHA_256),

    /**
     * SHA-384
     */
    SHA_384(AlgorithmEnum.SHA_384),

    /**
     * SHA-512
     */
    SHA_512(AlgorithmEnum.SHA_512),
    ;

    /**
     * 获取摘要算法实例
     */
    public MessageDigest createMessageDigest() {
        try {
            AlgorithmEnum algorithm = getAlgorithm();
            Provider provider = algorithm.provider();
            return Objects.nonNull(provider) ? MessageDigest.getInstance(algorithm.getAlgorithm(), provider)
                    : MessageDigest.getInstance(algorithm.getAlgorithm());
        } catch (Exception e) {
            throw new CryptoException("构建摘要算法" + getAlgorithm().getName() + "实例异常", e);
        }
    }

    DigestAlgorithmEnum(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 算法
     */
    private final AlgorithmEnum algorithm;
}
