package cn.zs.tool.crypto.digest;

import cn.zs.tool.crypto.common.BaseCrypto;
import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

/**
 * 摘要签名算法
 *
 * @author sheng
 */
@Slf4j
@Getter
public class Digest implements BaseCrypto {
    /**
     * 当前算法枚举
     */
    private final DigestAlgorithmEnum algorithm;

    /**
     * 读取流缓存区大小
     */
    private final int bufferSize;

    /**
     * 获取摘要签名算法实例
     *
     * @param algorithm 算法枚举
     * @return {@link Digest}
     */
    public static Digest create(DigestAlgorithmEnum algorithm) {
        return new Digest(algorithm);
    }

    /**
     * 获取摘要签名算法实例
     *
     * @param algorithm  算法枚举
     * @param bufferSize 读取流缓存区大小
     * @return {@link Digest}
     */
    public static Digest create(DigestAlgorithmEnum algorithm, int bufferSize) {
        return new Digest(algorithm, bufferSize);
    }

    public Digest(DigestAlgorithmEnum algorithm) {
        this(algorithm, 256);
    }

    public Digest(DigestAlgorithmEnum algorithm, int bufferSize) {
        this.algorithm = Objects.requireNonNull(algorithm, "algorithm must not be null");
        this.bufferSize = Math.max(bufferSize, 128);
    }

    @Override
    public final AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
    }

    /**
     * 计算散列值
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public byte[] digest(byte[] data, byte[] salt, int saltPosition, int digestCount) {
        if (Objects.isNull(salt)) {
            salt = new byte[0];
        }
        saltPosition = Math.max(0, saltPosition);
        digestCount = Math.max(1, digestCount);
        final MessageDigest digest = getAlgorithm().createMessageDigest();
        // 单次散列
        if (digestCount == 1) {
            return digest(digest, data, salt, saltPosition);
        }
        // 多次散列
        byte[] result = data;
        for (int i = 0; i < digestCount; i++) {
            digest.reset();
            result = digest(digest, result, salt, saltPosition);
        }
        return result;
    }

    /**
     * 计算散列值
     *
     * @param data         待散列数据流
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public byte[] digest(final InputStream data, byte[] salt, int saltPosition, int digestCount) {
        if (Objects.isNull(salt)) {
            salt = new byte[0];
        }
        saltPosition = Math.max(0, saltPosition);
        digestCount = Math.max(1, digestCount);
        final MessageDigest messageDigest = getAlgorithm().createMessageDigest();
        try {
            // 加盐在开头
            if (saltPosition == 0) {
                messageDigest.update(salt);
            }
            // 散列后的位置
            int position = 0;
            final byte[] buffer = new byte[bufferSize];
            int readLen;
            while ((readLen = data.read(buffer)) > -1) {
                int updateLen = saltPosition - position;
                // 加盐在中间
                if (updateLen > 0 && updateLen <= readLen) {
                    messageDigest.update(buffer, 0, updateLen);
                    messageDigest.update(salt);
                    // 剩余长度
                    int remaining = readLen - updateLen;
                    if (remaining > 0) {
                        messageDigest.update(buffer, updateLen, remaining);
                    }
                } else {
                    messageDigest.update(buffer, 0, readLen);
                }
                position += readLen;
            }
            // 加盐在末尾
            if (position < saltPosition) {
                messageDigest.update(salt);
            }
        } catch (Exception e) {
            log.error("[{}]散列计算异常", algorithm().getName(), e);
            throw new CryptoException(e);
        }
        byte[] result = messageDigest.digest();
        // 多次散列
        if ((digestCount -= 1) > 0) {
            // 单次散列
            if (digestCount == 1) {
                messageDigest.reset();
                result = digest(messageDigest, result, salt, saltPosition);
            } else {
                for (int i = 0; i < digestCount; i++) {
                    messageDigest.reset();
                    result = digest(messageDigest, result, salt, saltPosition);
                }
            }
        }
        return result;
    }

    /**
     * 散列
     *
     * @param digest       {@link MessageDigest }
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     */
    protected byte[] digest(MessageDigest digest, byte[] data, byte[] salt, int saltPosition) {
        if (saltPosition == 0) {
            return digest(digest, salt, data);
        }
        if (saltPosition >= data.length) {
            return digest(digest, data, salt);
        }
        // 加盐在中间
        return digest(digest, Arrays.copyOfRange(data, 0, saltPosition), salt, Arrays.copyOfRange(data, saltPosition, data.length));
    }

    /**
     * 散列
     *
     * @param messageDigest {@link MessageDigest }
     * @param dataList      待散列数据
     */
    protected byte[] digest(MessageDigest messageDigest, byte[]... dataList) {
        for (byte[] data : dataList) {
            if (Objects.nonNull(data)) {
                messageDigest.update(data);
            }
        }
        return messageDigest.digest();
    }
}
