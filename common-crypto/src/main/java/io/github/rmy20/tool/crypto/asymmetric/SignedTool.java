package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import io.github.rmy20.tool.crypto.constant.KeySizeEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;

/**
 * 签名工具
 *
 * @author sheng
 */
@Slf4j
@Getter
public enum SignedTool {
    /**
     * SM3WithSM2
     */
    SM3WithSM2(SignedAlgorithm.SM3WithSM2),

    /**
     * MD5WithRSA
     */
    MD5WithRSA(SignedAlgorithm.MD5WithRSA),

    /**
     * SHA1WithRSA
     */
    SHA1WithRSA(SignedAlgorithm.SHA1WithRSA),

    /**
     * SHA256WithRSA
     */
    SHA256WithRSA(SignedAlgorithm.SHA256WithRSA),

    /**
     * SHA384withRSA
     */
    SHA384withRSA(SignedAlgorithm.SHA384WithRSA),

    /**
     * SHA512WithRSA
     */
    SHA512WithRSA(SignedAlgorithm.SHA512WithRSA),

    /**
     * SHA256WithRSA/PSS
     */
    SHA256WithRSA_PSS(SignedAlgorithm.SHA256WithRSA_PSS),

    /**
     * SHA384WithRSA/PSS
     */
    SHA384WithRSA_PSS(SignedAlgorithm.SHA384WithRSA_PSS),

    /**
     * SHA512WithRSA/PSS
     */
    SHA512WithRSA_PSS(SignedAlgorithm.SHA512WithRSA_PSS),

    /**
     * SHA256WithECDSA
     */
    SHA256WithECDSA(SignedAlgorithm.SHA256WithECDSA),

    /**
     * SHA384WithECDSA
     */
    SHA384WithECDSA(SignedAlgorithm.SHA384WithECDSA),

    /**
     * SHA512WithECDSA
     */
    SHA512WithECDSA(SignedAlgorithm.SHA512WithECDSA),
    ;

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public KeyPair generateKeyPair() {
        return algorithm.generateKeyPair(KeySizeEnum.KEY_SIZE_2048.getSize());
    }

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public KeyPair generateKeyPair(int keySize) {
        return algorithm.generateKeyPair(keySize);
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public AsymmetricCryptoKeyPairDTO generateKeyPairInfo() {
        return algorithm.generateKeyPairInfo(KeySizeEnum.KEY_SIZE_2048.getSize());
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
        return algorithm.generateKeyPairInfo(keySize);
    }

    /**
     * 转换私钥
     */
    public PrivateKey toPrivateKey(byte[] privateKeyArr) {
        return algorithm.toPrivateKey(privateKeyArr);
    }

    /**
     * 转换公钥
     */
    public PublicKey toPublicKey(byte[] publicKeyArr) {
        return algorithm.toPublicKey(publicKeyArr);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @return hex 格式签名
     */
    public String signHex(String plainText, PrivateKey privateKey) {
        return signHex(plainText, StandardCharsets.UTF_8, privateKey);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param charset    文本解码编码
     * @param privateKey 私钥
     * @return hex 格式签名
     */
    public String signHex(String plainText, Charset charset, PrivateKey privateKey) {
        return signHex(plainText, charset, privateKey, null);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @return hex 格式签名
     */
    public String signHex(String plainText, PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        return signHex(plainText, StandardCharsets.UTF_8, privateKey, parameterSpec);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param charset    文本解码编码
     * @param privateKey 私钥
     * @return hex 格式签名
     */
    public String signHex(String plainText, Charset charset, PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        try {
            return HexUtil.encodeHexString(sign(plainText, charset, privateKey, parameterSpec));
        } catch (Exception e) {
            throw new CryptoException(algorithm.name() + "签名异常", e);
        }
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     */
    public String signBase64(String plainText, PrivateKey privateKey) {
        return signBase64(plainText, StandardCharsets.UTF_8, privateKey);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @return base64 格式签名
     */
    public String signBase64(String plainText, Charset charset, PrivateKey privateKey) {
        return signBase64(plainText, charset, privateKey, null);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     */
    public String signBase64(String plainText, PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        return signBase64(plainText, StandardCharsets.UTF_8, privateKey, parameterSpec);
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @return base64 格式签名
     */
    public String signBase64(String plainText, Charset charset, PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        try {
            return Base64Util.encodeToString(sign(plainText, charset, privateKey, parameterSpec));
        } catch (Exception e) {
            throw new CryptoException(algorithm.name() + "签名异常", e);
        }
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public byte[] sign(final String plainText, Charset charset, final PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        return sign(plainText.getBytes(charset), privateKey, parameterSpec);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data          加密数据
     * @param privateKey    私钥
     * @param parameterSpec 签名参数
     * @return 签名
     */
    public byte[] sign(final byte[] data, final PrivateKey privateKey, AlgorithmParameterSpec parameterSpec) {
        return getSigned().sign(data, privateKey, parameterSpec);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText 待签名数据
     * @param publicKey 公钥
     * @param signHex   16进制 hex 签名
     */
    public boolean verifyHex(String plainText, PublicKey publicKey, String signHex) {
        return verifyHex(plainText, StandardCharsets.UTF_8, publicKey, signHex);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText 待签名数据
     * @param publicKey 公钥
     * @param signHex   16进制 hex 签名
     */
    public boolean verifyHex(String plainText, Charset charset, PublicKey publicKey, String signHex) {
        return verifyHex(plainText, charset, publicKey, null, signHex);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText 待签名数据
     * @param publicKey 公钥
     * @param signHex   16进制 hex 签名
     */
    public boolean verifyHex(String plainText, PublicKey publicKey, AlgorithmParameterSpec parameterSpec, String signHex) {
        return verifyHex(plainText, StandardCharsets.UTF_8, publicKey, parameterSpec, signHex);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText 待签名数据
     * @param publicKey 公钥
     * @param signHex   16进制 hex 签名
     */
    public boolean verifyHex(String plainText, Charset charset, PublicKey publicKey, AlgorithmParameterSpec parameterSpec, String signHex) {
        try {
            return verify(plainText.getBytes(charset), publicKey, parameterSpec, HexUtil.decodeHex(signHex));
        } catch (Exception e) {
            log.error("[{}]验签异常", algorithm, e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText  待签名数据
     * @param publicKey  公钥
     * @param signBase64 base64 签名
     */
    public boolean verifyBase64(String plainText, PublicKey publicKey, String signBase64) {
        return verifyBase64(plainText, StandardCharsets.UTF_8, publicKey, signBase64);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText  待签名数据
     * @param publicKey  公钥
     * @param signBase64 base64 签名
     */
    public boolean verifyBase64(String plainText, Charset charset, PublicKey publicKey, String signBase64) {
        return verifyBase64(plainText, charset, publicKey, null, signBase64);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText  待签名数据
     * @param publicKey  公钥
     * @param signBase64 base64 签名
     */
    public boolean verifyBase64(String plainText, PublicKey publicKey, AlgorithmParameterSpec parameterSpec, String signBase64) {
        return verifyBase64(plainText, StandardCharsets.UTF_8, publicKey, parameterSpec, signBase64);
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText  待签名数据
     * @param publicKey  公钥
     * @param signBase64 base64 签名
     */
    public boolean verifyBase64(String plainText, Charset charset, PublicKey publicKey, AlgorithmParameterSpec parameterSpec, String signBase64) {
        try {
            return verify(plainText.getBytes(charset), publicKey, parameterSpec, Base64Util.decode(signBase64));
        } catch (Exception e) {
            log.error("[{}]验签异常", algorithm, e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data          数据
     * @param publicKey     公钥
     * @param parameterSpec 签名参数
     * @param sign          签名
     * @return 是否验证通过
     */
    public boolean verify(final byte[] data, final PublicKey publicKey, AlgorithmParameterSpec parameterSpec, final byte[] sign) {
        return getSigned().verify(data, publicKey, parameterSpec, sign);
    }

    SignedTool(SignedAlgorithm algorithm) {
        this.algorithm = algorithm;
        this.signed = Signed.create(algorithm);
    }

    /**
     * 算法
     */
    private final SignedAlgorithm algorithm;

    /**
     * 非对称算法实例
     */
    private final Signed signed;
}
