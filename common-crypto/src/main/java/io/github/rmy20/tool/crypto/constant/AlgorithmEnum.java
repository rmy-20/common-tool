package io.github.rmy20.tool.crypto.constant;

import lombok.Getter;

import java.security.Provider;
import java.util.Objects;

/**
 * 加密算法枚举类
 *
 * @author sheng
 */
@Getter
public enum AlgorithmEnum {

    // region 国密算法

    /**
     * 国密 SM2 非对称加密和签名算法
     */
    SM2("SM2", "EC", ProviderEnum.BC),

    /**
     * 国密 SM3 摘要签名算法
     */
    SM3("SM3", "SM3", ProviderEnum.BC),

    /**
     * 国密 SM4 对称加密算法
     */
    SM4("SM4", "SM4", ProviderEnum.BC),

    // endregion

    // region 国际算法

    // region 摘要算法

    /**
     * MD5 摘要签名算法，使用 JDK 自带的 MD5 实现
     */
    MD5("MD5", "MD5", null),

    /**
     * SHA-1 摘要签名算法
     */
    SHA_1("SHA-1", "SHA-1", ProviderEnum.BC),

    /**
     * SHA-256 摘要签名算法
     */
    SHA_256("SHA-256", "SHA-256", ProviderEnum.BC),

    /**
     * SHA-384 摘要签名算法
     */
    SHA_384("SHA-384", "SHA-384", ProviderEnum.BC),

    /**
     * SHA-512 摘要签名算法
     */
    SHA_512("SHA-512", "SHA-512", ProviderEnum.BC),

    // endregion

    // region 对称加密算法

    /**
     * AES 对称加密算法，JDK 默认实现为 AES/ECB/PKCS5Padding
     */
    AES("AES", "AES", ProviderEnum.BC),

    /**
     * DES 对称加密算法，JDK 默认实现为 DES/ECB/PKCS5Padding
     */
    DES("DES", "DES", ProviderEnum.BC),

    /**
     * DESede 对称加密算法，JDK 默认实现为 DESede/ECB/PKCS5Padding
     */
    DESede("DESede", "DESede", ProviderEnum.BC),

    /**
     * ChaCha20 对称加密算法，流密码算法，不使用填充模式
     */
    ChaCha20("ChaCha20", "ChaCha20", ProviderEnum.BC),

    /**
     * RC5 对称加密算法
     */
    RC5("RC5", "RC5", ProviderEnum.BC),

    // endregion

    // region 非对称加密算法

    RSA("RSA", "RSA", ProviderEnum.BC),

    // endregion

    // region 签名算法

    /**
     * SM3WithSM2 签名算法
     */
    SM3WithSM2("SM3WithSM2", "SM3WithSM2", ProviderEnum.BC),

    /**
     * MD5WithRSA 签名算法
     */
    MD5WithRSA("MD5WithRSA", "MD5WithRSA", ProviderEnum.BC),

    /**
     * SHA1WithRSA 签名算法
     */
    SHA1WithRSA("SHA1WithRSA", "SHA1WithRSA", ProviderEnum.BC),

    /**
     * SHA256WithRSA 签名算法
     */
    SHA256WithRSA("SHA256WithRSA", "SHA256WithRSA", ProviderEnum.BC),

    /**
     * SHA384WithRSA 签名算法
     */
    SHA384WithRSA("SHA384WithRSA", "SHA384WithRSA", ProviderEnum.BC),

    /**
     * SHA512WithRSA 签名算法
     */
    SHA512WithRSA("SHA512WithRSA", "SHA512WithRSA", ProviderEnum.BC),

    /**
     * SHA256WithRSA/PSS 签名算法
     */
    SHA256WithRSA_PSS("SHA256WithRSA/PSS", "SHA256WithRSA/PSS", ProviderEnum.BC),

    /**
     * SHA384WithRSA/PSS 签名算法
     */
    SHA384WithRSA_PSS("SHA384WithRSA/PSS", "SHA384WithRSA/PSS", ProviderEnum.BC),

    /**
     * SHA512WithRSA/PSS 签名算法
     */
    SHA512WithRSA_PSS("SHA512WithRSA/PSS", "SHA512WithRSA/PSS", ProviderEnum.BC),

    /**
     * ECDSA 非对称签名算法
     */
    ECDSA("ECDSA", "EC", ProviderEnum.BC),

    /**
     * SHA256WithECDSA 签名算法
     */
    SHA256WithECDSA("SHA256WithECDSA", "SHA256WithECDSA", ProviderEnum.BC),

    /**
     * SHA384WithECDSA 签名算法
     */
    SHA384WithECDSA("SHA384WithECDSA", "SHA384WithECDSA", ProviderEnum.BC),

    /**
     * SHA512withECDSA 签名算法
     */
    SHA512WithECDSA("SHA512WithECDSA", "SHA512WithECDSA", ProviderEnum.BC),

    // endregion

    // region mac 算法

    /**
     * HmacMD5 算法
     */
    HmacMD5("HmacMD5", "HmacMD5", null),

    /**
     * HmacSHA1 算法
     */
    HmacSHA1("HmacSHA1", "HmacSHA1", null),

    /**
     * HmacSHA256 算法
     */
    HmacSHA256("HmacSHA256", "HmacSHA256", null),

    /**
     * HmacSHA384 算法
     */
    HmacSHA384("HmacSHA384", "HmacSHA384", null),

    /**
     * HmacSHA512 算法
     */
    HmacSHA512("HmacSHA512", "HmacSHA512", null),

    /**
     * HmacSM3 算法
     */
    HmacSM3("HmacSM3", "HmacSM3", ProviderEnum.BC),

    /**
     * SM4 CMAC 算法
     */
    SM4CMAC("SM4CMAC", "SM4CMAC", ProviderEnum.BC),

    // endregion

    // endregion
    ;

    /**
     * 返回当前加密算法的{@link Provider 提供者}
     */
    public Provider provider() {
        ProviderEnum provider = getProvider();
        return Objects.nonNull(provider) ? provider.getProvider() : null;
    }

    AlgorithmEnum(String name, String algorithm, ProviderEnum provider) {
        this.name = name;
        this.algorithm = algorithm;
        this.provider = provider;
    }

    /**
     * 加密算法名称
     */
    private final String name;

    /**
     * 实际上的加密算法
     */
    private final String algorithm;

    /**
     * 加密算法提供者
     */
    private final ProviderEnum provider;
}
