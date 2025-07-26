package cn.zs.tool.crypto.digest;

import cn.zs.tool.crypto.common.BaseCrypto;
import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * Hmac 实现
 *
 * @author sheng
 */
@Slf4j
public class HMac implements BaseCrypto {
    /**
     * 当前算法枚举
     */
    private final HMacAlgorithmEnum algorithm;

    /**
     * 读取流缓存区大小
     */
    private final int bufferSize;

    /**
     * 创建{@link  HMac}
     *
     * @param algorithm 算法枚举
     */
    public static HMac create(HMacAlgorithmEnum algorithm) {
        return new HMac(algorithm);
    }

    /**
     * 创建{@link  HMac}
     *
     * @param algorithm  算法枚举
     * @param bufferSize 读取流缓存区大小
     */
    public static HMac create(HMacAlgorithmEnum algorithm, int bufferSize) {
        return new HMac(algorithm, bufferSize);
    }

    public HMac(HMacAlgorithmEnum algorithm) {
        this(algorithm, 256);
    }

    public HMac(HMacAlgorithmEnum algorithm, int bufferSize) {
        this.algorithm = Objects.requireNonNull(algorithm, "algorithm must not be null");
        this.bufferSize = Math.max(128, bufferSize);
    }

    @Override
    public final AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public byte[] mac(byte[] data, SecretKey key) {
        return mac(new ByteArrayInputStream(data), key);
    }

    /**
     * mac
     *
     * @param data 待mac数据
     * @param key  密钥
     */
    public byte[] mac(InputStream data, SecretKey key) {
        try {
            Mac mac = algorithm.createMac(key);
            final byte[] buffer = new byte[bufferSize];
            int readLen;
            while ((readLen = data.read(buffer)) > -1) {
                mac.update(buffer, 0, readLen);
            }
            return mac.doFinal();
        } catch (Exception e) {
            log.error("[{}]散列计算异常", algorithm(), e);
            throw new CryptoException(e);
        }
    }
}
