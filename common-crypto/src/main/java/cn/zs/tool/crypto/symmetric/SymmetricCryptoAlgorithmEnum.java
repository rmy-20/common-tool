package cn.zs.tool.crypto.symmetric;

import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.constant.CipherModeEnum;
import cn.zs.tool.crypto.constant.CryptoConstant;
import cn.zs.tool.crypto.constant.ModeEnum;
import cn.zs.tool.crypto.constant.PaddingEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 对称加密算法枚举
 *
 * @author sheng
 */
@Slf4j
@Getter
public enum SymmetricCryptoAlgorithmEnum {
    /**
     * 国密 SM4 对称加密算法
     */
    SM4(AlgorithmEnum.SM4, 16),

    /**
     * AES 对称加密算法，JDK 默认实现为 AES/ECB/PKCS5Padding
     */
    AES(AlgorithmEnum.AES, 16) {
        @Override
        public SecretKey generateKey(SymmetricCryptoKeySizeEnum keySize) {
            try {
                AlgorithmEnum algorithm = AlgorithmEnum.AES;
                KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.getAlgorithm(), algorithm.getProvider().getProvider());
                keyGenerator.init(keySize.getSize());
                return keyGenerator.generateKey();
            } catch (Exception e) {
                throw new CryptoException("AES生成密钥异常", e);
            }
        }
    },

    /**
     * DES 对称加密算法，JDK 默认实现为 DES/ECB/PKCS5Padding
     * <p>
     * 已被 AES 取代
     */
    DES(AlgorithmEnum.DES, 8) {
        @Override
        public SecretKey toSecretKey(byte[] key) {
            try {
                AlgorithmEnum algorithm = getAlgorithm();
                KeySpec keySpec = new DESKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm.getAlgorithm(), algorithm.getProvider().getProvider());
                return keyFactory.generateSecret(keySpec);
            } catch (Exception e) {
                throw new CryptoException("DES转换密钥异常", e);
            }
        }
    },

    /**
     * DESede 对称加密算法，JDK 默认实现为 DESede/ECB/PKCS5Padding
     * <p>
     * AES 更安全
     */
    DESede(AlgorithmEnum.DESede, 8) {
        @Override
        public SecretKey toSecretKey(byte[] key) {
            try {
                AlgorithmEnum algorithm = getAlgorithm();
                KeySpec keySpec = new DESedeKeySpec(key);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm.getAlgorithm(), algorithm.getProvider().getProvider());
                return keyFactory.generateSecret(keySpec);
            } catch (Exception e) {
                throw new CryptoException("DESede转换密钥异常", e);
            }
        }
    },

    /**
     * ChaCha20 对称加密算法，流密码算法，不使用填充模式
     */
    ChaCha20(AlgorithmEnum.ChaCha20, 12),

    /**
     * RC5
     */
    RC5(AlgorithmEnum.RC5, 8),
    ;

    /**
     * 获取{@link Cipher 对称算法实例}
     *
     * @param cipherMode       {@link Cipher Cipher 模式}
     * @param secretKey        密钥
     * @param paramsSpec       算法参数
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    public Cipher createCipher(CipherModeEnum cipherMode, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                               Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        String algorithmName = joinAlgorithmName(mode, padding);
        try {
            Provider provider = algorithm.provider();
            Cipher cipher = Objects.nonNull(provider) ? Cipher.getInstance(algorithmName, provider) : Cipher.getInstance(algorithmName);
            if (Objects.nonNull(paramsSpec)) {
                cipher.init(cipherMode.getMode(), secretKey, paramsSpec);
            } else {
                cipher.init(cipherMode.getMode(), secretKey);
            }
            if (Objects.nonNull(cipherAdditional)) {
                cipherAdditional.accept(cipher);
            }
            return cipher;
        } catch (Exception e) {
            log.error("构建对称加密算法[{}]实例异常", algorithmName, e);
            throw new CryptoException("构建对称加密算法" + algorithmName + "实例异常", e);
        }
    }

    /**
     * 将 byte[] 密钥转换为 SecretKey
     *
     * @param key 密钥
     */
    public SecretKey toSecretKey(final byte[] key) {
        return new SecretKeySpec(key, algorithm.getAlgorithm());
    }

    /**
     * 拼接算法名称
     *
     * @param mode    分组模式
     * @param padding 填充模式
     */
    public String joinAlgorithmName(final ModeEnum mode, final PaddingEnum padding) {
        AlgorithmEnum algorithm = getAlgorithm();
        return Objects.isNull(mode) || Objects.isNull(padding) ? algorithm.getAlgorithm() :
                String.format(CryptoConstant.ALGORITHM_TEMPLATE, algorithm.getAlgorithm(), mode.name(), padding.getPadding());
    }

    /**
     * 生成密钥
     */
    public SecretKey generateKey() {
        return generateKey(SymmetricCryptoKeySizeEnum.KEY_SIZE_128);
    }

    /**
     * 生成密钥
     *
     * @param keySize 密钥长度
     */
    public SecretKey generateKey(SymmetricCryptoKeySizeEnum keySize) {
        try {
            AlgorithmEnum algorithm = getAlgorithm();
            Provider provider = algorithm.provider();
            KeyGenerator keyGenerator = Objects.nonNull(provider) ? KeyGenerator.getInstance(algorithm.getAlgorithm(), provider)
                    : KeyGenerator.getInstance(algorithm.getAlgorithm());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new CryptoException(name() + "生成密钥异常", e);
        }
    }

    /**
     * 生成 iv
     */
    public byte[] generateIv() {
        return generateIv(getIvSize());
    }

    /**
     * 生成 iv
     *
     * @param ivSize iv长度
     */
    public byte[] generateIv(int ivSize) {
        byte[] iv = new byte[ivSize];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        return iv;
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public SymmetricCryptoSecretKeyDTO generateSecretKeyInfo(SymmetricCryptoKeySizeEnum keySize) {
        SecretKey secretKey = generateKey(keySize);
        return SymmetricCryptoSecretKeyDTO.builder()
                .secretKey(secretKey)
                .secretKeyByte(secretKey.getEncoded())
                .ivByte(generateIv())
                .build();
    }

    SymmetricCryptoAlgorithmEnum(AlgorithmEnum algorithm, int ivSize) {
        this.algorithm = algorithm;
        this.ivSize = ivSize;
    }

    /**
     * 算法
     */
    private final AlgorithmEnum algorithm;

    /**
     * iv长度
     */
    private final int ivSize;
}
