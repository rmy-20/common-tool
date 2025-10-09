package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.crypto.constant.AlgorithmEnum;
import io.github.rmy20.tool.crypto.constant.CryptoConstant;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.Getter;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Objects;

/**
 * sign支持的算法
 *
 * @author sheng
 */
@Getter
public enum SignedAlgorithm {
    /**
     * SM3WithSM2
     */
    SM3WithSM2(AlgorithmEnum.SM3WithSM2, AsymmetricCryptoAlgorithmEnum.SM2) {
        @Override
        public PublicKey toPublicKey(byte[] publicKeyArr) {
            try {
                int length = publicKeyArr.length, len = length / 2;
                byte[] xBytes = new byte[len];
                System.arraycopy(publicKeyArr, length - len * 2, xBytes, 0, len);
                byte[] yBytes = new byte[len];
                System.arraycopy(publicKeyArr, length - len, yBytes, 0, len);

                BigInteger x = BigIntegers.fromUnsignedByteArray(xBytes);
                BigInteger y = BigIntegers.fromUnsignedByteArray(yBytes);
                ECPoint point = CryptoConstant.SM2_CURVE.createPoint(x, y);

                // 构建公钥规范
                ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(point, CryptoConstant.EC_NAMED_CURVE_PARAMETER_SPEC);
                // 使用KeyFactory生成PublicKey
                KeyFactory keyFactory = KeyFactory.getInstance(getAsymmetricAlgorithm().getAlgorithm().getAlgorithm(),
                        getAlgorithm().getProvider().getProvider());
                return keyFactory.generatePublic(pubKeySpec);
            } catch (Exception e) {
                throw new CryptoException("摘要签名算法[" + name() + "]转换公钥异常", e);
            }
        }
    },

    /**
     * MD5WithRSA
     */
    MD5WithRSA(AlgorithmEnum.MD5WithRSA, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA1WithRSA
     */
    SHA1WithRSA(AlgorithmEnum.SHA1WithRSA, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA256WithRSA
     */
    SHA256WithRSA(AlgorithmEnum.SHA256WithRSA, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA384withRSA
     */
    SHA384WithRSA(AlgorithmEnum.SHA384WithRSA, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA512WithRSA
     */
    SHA512WithRSA(AlgorithmEnum.SHA512WithRSA, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA256WithRSA/PSS
     */
    SHA256WithRSA_PSS(AlgorithmEnum.SHA256WithRSA_PSS, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA384WithRSA/PSS
     */
    SHA384WithRSA_PSS(AlgorithmEnum.SHA384WithRSA_PSS, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA512WithRSA/PSS
     */
    SHA512WithRSA_PSS(AlgorithmEnum.SHA512WithRSA_PSS, AsymmetricCryptoAlgorithmEnum.RSA),

    /**
     * SHA256withECDSA
     */
    SHA256WithECDSA(AlgorithmEnum.SHA256WithECDSA, AsymmetricCryptoAlgorithmEnum.ECDSA),

    /**
     * SHA384WithECDSA
     */
    SHA384WithECDSA(AlgorithmEnum.SHA384WithECDSA, AsymmetricCryptoAlgorithmEnum.ECDSA),

    /**
     * SHA512WithECDSA
     */
    SHA512WithECDSA(AlgorithmEnum.SHA512WithECDSA, AsymmetricCryptoAlgorithmEnum.ECDSA),
    ;

    /**
     * 获取签名实例
     */
    public Signature createSignature() {
        try {
            AlgorithmEnum algorithm = getAlgorithm();
            Provider provider = algorithm.provider();
            return Objects.nonNull(provider) ? Signature.getInstance(algorithm.getAlgorithm(), provider)
                    : Signature.getInstance(algorithm.getAlgorithm());
        } catch (Exception e) {
            throw new CryptoException("构建签名算法[" + getAlgorithm() + "]实例异常", e);
        }
    }

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public KeyPair generateKeyPair(int keySize) {
        return getAsymmetricAlgorithm().generateKeyPair(keySize);
    }

    /**
     * 生成公私钥
     *
     * @param keySize 密钥长度
     */
    public AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
        return getAsymmetricAlgorithm().generateKeyPairInfo(keySize);
    }

    /**
     * 转换私钥
     */
    public PrivateKey toPrivateKey(byte[] privateKeyArr) {
        return getAsymmetricAlgorithm().toPrivateKey(privateKeyArr);
    }

    /**
     * 转换公钥
     */
    public PublicKey toPublicKey(byte[] publicKeyArr) {
        return getAsymmetricAlgorithm().toPublicKey(publicKeyArr);
    }

    SignedAlgorithm(AlgorithmEnum algorithm, AsymmetricCryptoAlgorithmEnum asymmetricAlgorithm) {
        this.algorithm = algorithm;
        this.asymmetricAlgorithm = asymmetricAlgorithm;
    }

    /**
     * 算法
     */
    private final AlgorithmEnum algorithm;

    /**
     * 非对称算法
     */
    private final AsymmetricCryptoAlgorithmEnum asymmetricAlgorithm;
}
