package cn.zs.tool.crypto.symmetric;

import cn.zs.tool.crypto.constant.ModeEnum;
import cn.zs.tool.crypto.constant.PaddingEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.KeyPair;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 对称加密工具
 *
 * @author sheng
 */
@Slf4j
@Getter
public enum SymmetricCryptoTool implements Encrypt, Decrypt {
    /**
     * 国密 SM4 对称加密算法
     */
    SM4(SymmetricCryptoAlgorithmEnum.SM4),

    /**
     * AES 对称加密算法，JDK 默认实现为 AES/ECB/PKCS5Padding
     */
    AES(SymmetricCryptoAlgorithmEnum.AES),

    /**
     * DES 对称加密算法，JDK 默认实现为 DES/ECB/PKCS5Padding
     * <p>
     * 已被 AES 取代
     */
    DES(SymmetricCryptoAlgorithmEnum.DES),

    /**
     * DESede 对称加密算法，JDK 默认实现为 DESede/ECB/PKCS5Padding
     * <p>
     * AES 更安全
     */
    DESede(SymmetricCryptoAlgorithmEnum.DESede),

    /**
     * ChaCha20 对称加密算法，流密码算法，不使用填充模式
     */
    ChaCha20(SymmetricCryptoAlgorithmEnum.ChaCha20),

    /**
     * RC5
     */
    RC5(SymmetricCryptoAlgorithmEnum.RC5),
    ;

    /**
     * 生成密钥，默认为 128 位
     */
    public SecretKey generateKey() {
        return algorithm.generateKey();
    }

    /**
     * 生成密钥
     *
     * @param keySize 密钥长度
     */
    public SecretKey generateKey(SymmetricCryptoKeySizeEnum keySize) {
        return algorithm.generateKey(keySize);
    }

    /**
     * 生成 iv
     */
    public byte[] generateIv() {
        return algorithm.generateIv();
    }

    /**
     * 生成 iv
     *
     * @param ivSize iv长度
     */
    public byte[] generateIv(int ivSize) {
        return algorithm.generateIv(ivSize);
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public SymmetricCryptoSecretKeyDTO generateSecretKeyInfo() {
        return generateSecretKeyInfo(SymmetricCryptoKeySizeEnum.KEY_SIZE_128);
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     *
     * @param keySize 密钥长度
     */
    public SymmetricCryptoSecretKeyDTO generateSecretKeyInfo(SymmetricCryptoKeySizeEnum keySize) {
        return algorithm.generateSecretKeyInfo(keySize);
    }

    /**
     * 将 byte[] 密钥转换为 SecretKey
     *
     * @param key 密钥
     */
    public final SecretKey toSecretKey(final byte[] key) {
        return algorithm.toSecretKey(key);
    }

    /**
     * 将 byte[] iv 转换为 AlgorithmParameterSpec
     *
     * @param iv iv 初始化量
     */
    public AlgorithmParameterSpec toAlgorithmParameterSpec(final byte[] iv) {
        return Objects.nonNull(iv) ? new IvParameterSpec(iv) : null;
    }

    /**
     * 加密
     *
     * @param data             待被加密的bytes
     * @param paramsSpec       算法参数
     * @param secretKey        密钥
     * @param cipherAdditional 对{@link Cipher}的额外操作，如添加额外的加密数据等
     * @param mode             分组模式
     * @param padding          填充模式
     */
    @Override
    public byte[] encrypt(byte[] data, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                          Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return getSymmetric().encrypt(data, secretKey, paramsSpec, cipherAdditional, mode, padding);
    }

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
    @Override
    public byte[] decrypt(byte[] encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                          Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        return getSymmetric().decrypt(encrypt, secretKey, paramsSpec, cipherAdditional, mode, padding);
    }

    SymmetricCryptoTool(SymmetricCryptoAlgorithmEnum algorithm) {
        this.algorithm = algorithm;
        this.symmetric = SymmetricCrypto.create(algorithm);
    }

    /**
     * 算法
     */
    private final SymmetricCryptoAlgorithmEnum algorithm;

    /**
     * 对称加密实例
     */
    private final SymmetricCrypto symmetric;
}
