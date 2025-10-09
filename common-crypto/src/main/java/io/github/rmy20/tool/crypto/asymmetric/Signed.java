package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.crypto.common.BaseCrypto;
import io.github.rmy20.tool.crypto.constant.AlgorithmEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;

/**
 * 签名算法
 *
 * @author sheng
 */
@Slf4j
@Getter
public class Signed implements BaseCrypto {
    /**
     * 当前实例支持的算法
     */
    private final SignedAlgorithm algorithm;

    /**
     * 读取流缓存区大小
     */
    private final int bufferSize;

    /**
     * 构造签名实例
     *
     * @param algorithm 当前实例支持的算法
     * @return {@link Signed}
     */
    public static Signed create(SignedAlgorithm algorithm) {
        return new Signed(algorithm);
    }

    /**
     * 构造签名实例
     *
     * @param algorithm  当前实例支持的算法
     * @param bufferSize 读取流缓存区大小
     * @return {@link Signed}
     */
    public static Signed create(SignedAlgorithm algorithm, int bufferSize) {
        return new Signed(algorithm, bufferSize);
    }

    public Signed(SignedAlgorithm algorithm) {
        this(algorithm, 256);
    }

    public Signed(SignedAlgorithm algorithm, int bufferSize) {
        this.algorithm = algorithm;
        this.bufferSize = bufferSize;
    }

    @Override
    public final AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return 签名
     */
    public byte[] sign(final byte[] data, final PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        return sign(new ByteArrayInputStream(data), privateKey, parameterSpec);
    }

    /**
     * 生成签名
     *
     * @param data       {@link InputStream} 数据流
     * @param privateKey 私钥
     */
    public byte[] sign(final InputStream data, PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        try {
            Signature signature = algorithm.createSignature();
            if (Objects.nonNull(parameterSpec)) {
                signature.setParameter(parameterSpec);
            }
            final byte[] buffer = new byte[bufferSize];
            signature.initSign(privateKey);
            int read;
            while ((read = data.read(buffer)) > -1) {
                signature.update(buffer, 0, read);
            }
            return signature.sign();
        } catch (final Exception e) {
            log.error("签名算法[{}]签名异常", algorithm, e);
            throw new CryptoException("签名算法[" + algorithm + "]签名异常", e);
        }
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data 数据
     * @param sign 签名
     * @return 是否验证通过
     */
    public boolean verify(final byte[] data, final PublicKey publicKey, AlgorithmParameterSpec parameterSpec, final byte[] sign) {
        try {
            Signature signature = algorithm.createSignature();
            if (Objects.nonNull(parameterSpec)) {
                signature.setParameter(parameterSpec);
            }
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            log.error("签名算法[{}]验签异常", algorithm, e);
            return false;
        }
    }

    /**
     * 用公钥证书检验数字签名的合法性
     *
     * @param data 数据
     * @param sign 签名
     * @return 是否验证通过
     */
    public boolean verify(final byte[] data, final Certificate certificate, AlgorithmParameterSpec parameterSpec, final byte[] sign) {
        try {
            Signature signature = algorithm.createSignature();
            if (Objects.nonNull(parameterSpec)) {
                signature.setParameter(parameterSpec);
            }
            signature.initVerify(certificate);
            signature.update(data);
            return signature.verify(sign);
        } catch (Exception e) {
            log.error("签名算法[{}]验签异常", algorithm, e);
            return false;
        }
    }
}
