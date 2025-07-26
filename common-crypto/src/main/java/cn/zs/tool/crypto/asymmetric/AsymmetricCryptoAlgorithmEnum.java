package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.constant.CryptoConstant;
import cn.zs.tool.crypto.constant.KeySizeEnum;
import cn.zs.tool.crypto.constant.ProviderEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

/**
 * 非对称加密算法枚举
 *
 * @author sheng
 */
@Getter
public enum AsymmetricCryptoAlgorithmEnum {
    /**
     * 国密 SM2 非对称加密和签名算法
     */
    SM2(AlgorithmEnum.SM2) {
        @Override
        public KeyPair generateKeyPair(int keySize) {
            try {
                AlgorithmEnum algorithm = AlgorithmEnum.SM2;
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm.getAlgorithm(), algorithm.getProvider().getProvider());
                keyPairGenerator.initialize(CryptoConstant.SM2_P256_V1, new SecureRandom());
                return keyPairGenerator.generateKeyPair();
            } catch (Exception e) {
                throw new CryptoException("SM2生成密钥对异常", e);
            }
        }

        @Override
        public AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
            KeyPair keyPair = generateKeyPair(keySize);
            PrivateKey pairPrivate = keyPair.getPrivate();
            byte[] privateKeyArr = ((BCECPrivateKey) pairPrivate).getD().toByteArray();

            PublicKey pairPublic = keyPair.getPublic();
            byte[] publicKeyArr = ((BCECPublicKey) pairPublic).getQ().getEncoded(false);
            return AsymmetricCryptoKeyPairDTO.builder()
                    .keyPair(keyPair)
                    .publicKey(publicKeyArr)
                    .privateKey(privateKeyArr)
                    .build();
        }
    },

    /**
     * RSA
     */
    RSA(AlgorithmEnum.RSA),

    /**
     * ECDSA
     */
    ECDSA(AlgorithmEnum.ECDSA) {
        @Override
        public AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
            return super.generateKeyPairInfo(KeySizeEnum.KEY_SIZE_256.getSize());
        }
    },
    ;

    /**
     * 生成公私钥
     *
     * @param keySize 密钥长度
     */
    public KeyPair generateKeyPair(int keySize) {
        try {
            AlgorithmEnum algorithm = getAlgorithm();
            ProviderEnum provider = algorithm.getProvider();
            KeyPairGenerator keyPairGenerator = Objects.isNull(provider) ? KeyPairGenerator.getInstance(algorithm.getAlgorithm())
                    : KeyPairGenerator.getInstance(algorithm.getAlgorithm(), provider.getProvider());
            keyPairGenerator.initialize(keySize, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(name() + "生成密钥对异常", e);
        }
    }

    /**
     * 生成公私钥
     *
     * @param keySize 密钥长度
     */
    public AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
        KeyPair keyPair = generateKeyPair(keySize);
        return AsymmetricCryptoKeyPairDTO.builder()
                .keyPair(keyPair)
                .publicKey(keyPair.getPublic().getEncoded())
                .privateKey(keyPair.getPrivate().getEncoded())
                .build();
    }

    /**
     * 转换公钥
     *
     * @param publicKeyArr 公钥
     */
    public PublicKey toPublicKey(byte[] publicKeyArr) {
        AlgorithmEnum algorithm = getAlgorithm();
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyArr);
            ProviderEnum provider = algorithm.getProvider();
            KeyFactory keyFactory = Objects.isNull(provider) ? KeyFactory.getInstance(algorithm.getAlgorithm())
                    : KeyFactory.getInstance(algorithm.getAlgorithm(), provider.getProvider());
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new CryptoException(algorithm + "转换公钥异常", e);
        }
    }

    /**
     * 转换私钥
     *
     * @param privateKey 私钥
     */
    public PrivateKey toPrivateKey(byte[] privateKey) {
        AlgorithmEnum algorithm = getAlgorithm();
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
            ProviderEnum provider = algorithm.getProvider();
            KeyFactory keyFactory = Objects.isNull(provider) ? KeyFactory.getInstance(algorithm.getAlgorithm())
                    : KeyFactory.getInstance(algorithm.getAlgorithm(), provider.getProvider());
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new CryptoException(algorithm + "转换私钥异常", e);
        }
    }

    AsymmetricCryptoAlgorithmEnum(AlgorithmEnum algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 算法
     */
    private final AlgorithmEnum algorithm;
}
