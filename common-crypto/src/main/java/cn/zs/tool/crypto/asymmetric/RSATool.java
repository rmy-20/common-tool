package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.core.codec.binary.Base64Util;
import cn.zs.tool.core.codec.binary.HexUtil;
import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.constant.KeySizeEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Objects;

/**
 * RSA 算法工具
 *
 * @author sheng
 */
@Getter
public enum RSATool {
    /**
     * RSA
     */
    RSA(RSAAlgorithmEnum.RSA),

    /**
     * RSA/ECB/PKCS1Padding
     */
    RSA_ECB_PKCS1Padding(RSAAlgorithmEnum.RSA_ECB_PKCS1Padding),

    /**
     * RSA/ECB
     */
    RSA_ECB_NoPadding(RSAAlgorithmEnum.RSA_ECB_NoPadding),

    /**
     * RSA/NONE/NoPadding
     */
    RSA_None_NoPadding(RSAAlgorithmEnum.RSA_None_NoPadding),

    /**
     * RSA/NONE/OAEPPadding
     */
    RSA_None_OAEPPadding(RSAAlgorithmEnum.RSA_None_OAEPPadding),
    ;

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public static KeyPair generateKeyPair() {
        return generateKeyPair(KeySizeEnum.KEY_SIZE_2048.getSize());
    }

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public static KeyPair generateKeyPair(int keySize) {
        return AsymmetricCryptoAlgorithmEnum.RSA.generateKeyPair(keySize);
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public static AsymmetricCryptoKeyPairDTO generateKeyPairInfo() {
        return generateKeyPairInfo(KeySizeEnum.KEY_SIZE_2048.getSize());
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public static AsymmetricCryptoKeyPairDTO generateKeyPairInfo(int keySize) {
        return AsymmetricCryptoAlgorithmEnum.RSA.generateKeyPairInfo(keySize);
    }

    /**
     * 转换公钥
     *
     * @param publicKey 公钥
     */
    public static PublicKey toPublicKey(byte[] publicKey) {
        return AsymmetricCryptoAlgorithmEnum.RSA.toPublicKey(publicKey);
    }

    /**
     * 转换ASN1格式公钥
     *
     * @param publicKey ASN1格式公钥
     */
    public static PublicKey toPublicKeyAsn1(byte[] publicKey) {
        AlgorithmEnum algorithm = AlgorithmEnum.RSA;
        try {
            ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(publicKey);
            BigInteger modulus = ASN1Integer.getInstance(asn1Sequence.getObjectAt(0)).getPositiveValue();
            BigInteger publicExponent = ASN1Integer.getInstance(asn1Sequence.getObjectAt(1)).getPositiveValue();
            Provider provider = algorithm.provider();
            KeyFactory keyFactory = Objects.isNull(provider) ? KeyFactory.getInstance(algorithm.getAlgorithm())
                    : KeyFactory.getInstance(algorithm.getAlgorithm(), provider);
            return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
        } catch (Exception e) {
            throw new CryptoException(algorithm + "转换ASN1格式公钥异常", e);
        }
    }

    /**
     * 转换私钥
     *
     * @param privateKey 私钥
     */
    public static PrivateKey toPrivateKey(byte[] privateKey) {
        return AsymmetricCryptoAlgorithmEnum.RSA.toPrivateKey(privateKey);
    }

    // region 公钥加密，私钥解密

    // region 公钥加密

    /**
     * 公钥加密，默认UTF-8编码
     *
     * @param data      待被加密的字符串
     * @param publicKey hex编码的公钥
     * @return 加密后的16进制字符串
     */
    public String encryptHex(String data, PublicKey publicKey) {
        return encryptHex(data, StandardCharsets.UTF_8, publicKey);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param publicKey hex编码的公钥
     * @param charset   编码
     * @return 加密后的16进制字符串
     */
    public String encryptHex(String data, Charset charset, PublicKey publicKey) {
        try {
            return HexUtil.encodeHexString(encrypt(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            throw new CryptoException("非对称加密-公钥加密异常", e);
        }
    }

    /**
     * 公钥加密，默认UTF-8编码
     *
     * @param data      待被加密的字符串
     * @param publicKey base64编码的公钥
     * @return 加密后的base64字符串
     */
    public String encryptBase64(String data, PublicKey publicKey) {
        return encryptBase64(data, StandardCharsets.UTF_8, publicKey);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param charset   编码
     * @param publicKey base64编码的公钥
     * @return 加密后的base64字符串
     */
    public String encryptBase64(String data, Charset charset, PublicKey publicKey) {
        try {
            return Base64Util.encodeToString(encrypt(data.getBytes(charset), publicKey));
        } catch (Exception e) {
            throw new CryptoException("RSA公钥加密异常", e);
        }
    }

    /**
     * 公钥加密
     *
     * @param data      数据
     * @param publicKey 公钥
     */
    public byte[] encrypt(byte[] data, PublicKey publicKey) {
        return getRsa().encrypt(data, publicKey);
    }

    // endregion

    // region 私钥解密

    /**
     * 私钥解密为字符串，默认字符集为UTF-8
     *
     * @param encrypt    待解密hex字符串
     * @param privateKey hex格式 私钥
     */
    public String decryptHex(String encrypt, PrivateKey privateKey) {
        return decryptHex(encrypt, privateKey, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密为字符串
     *
     * @param encrypt    待解密hex字符串
     * @param privateKey hex格式 私钥
     * @param charset    字符集
     */
    public String decryptHex(String encrypt, PrivateKey privateKey, Charset charset) {
        try {
            return new String(decrypt(HexUtil.decodeHex(encrypt), privateKey), charset);
        } catch (Exception e) {
            throw new CryptoException("非对称加密-hex解密异常", e);
        }
    }

    /**
     * 私钥解密为字符串，默认字符集为UTF-8
     *
     * @param encrypt    待解密base64字符串
     * @param privateKey base64格式 私钥
     */
    public String decryptBase64(String encrypt, PrivateKey privateKey) {
        return decryptBase64(encrypt, privateKey, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密为字符串
     *
     * @param encrypt    待解密base64字符串
     * @param privateKey base64格式 私钥
     * @param charset    字符集
     */
    public String decryptBase64(String encrypt, PrivateKey privateKey, Charset charset) {
        try {
            return new String(decrypt(Base64Util.decode(encrypt), privateKey), charset);
        } catch (Exception e) {
            throw new CryptoException("RSA私钥解密异常", e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data       密文
     * @param privateKey 私钥
     */
    public byte[] decrypt(byte[] data, PrivateKey privateKey) {
        return getRsa().decrypt(data, privateKey);
    }

    // endregion

    // endregion

    // region 私钥加密，公钥解密（一般不使用）

    // region 私钥加密

    /**
     * 私钥加密，默认UTF-8编码，一般不使用
     *
     * @param data 待被加密的字符串
     * @return 加密后的16进制字符串
     */
    public String encryptHex(String data, PrivateKey privateKey) {
        return encryptHex(data, StandardCharsets.UTF_8, privateKey);
    }

    /**
     * 私钥加密，一般不使用
     *
     * @param data       待被加密的字符串
     * @param charset    编码
     * @param privateKey 私钥
     * @return 加密后的16进制字符串
     */
    public String encryptHex(String data, Charset charset, PrivateKey privateKey) {
        try {
            return HexUtil.encodeHexString(encrypt(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            throw new CryptoException("RSA私钥加密异常", e);
        }
    }

    /**
     * 私钥加密，默认UTF-8编码，一般不使用
     *
     * @param data       待被加密的字符串
     * @param privateKey 私钥
     * @return 加密后的 base64 字符串
     */
    public String encryptBase64(String data, PrivateKey privateKey) {
        return encryptBase64(data, StandardCharsets.UTF_8, privateKey);
    }

    /**
     * 私钥加密，一般不使用
     *
     * @param data       待被加密的字符串
     * @param charset    编码
     * @param privateKey 私钥
     * @return 加密后的 base64 字符串
     */
    public String encryptBase64(String data, Charset charset, PrivateKey privateKey) {
        try {
            return Base64Util.encodeToString(encrypt(data.getBytes(charset), privateKey));
        } catch (Exception e) {
            throw new CryptoException("RSA私钥加密异常", e);
        }
    }

    /**
     * 私钥加密，一般不使用
     *
     * @param data       数据
     * @param privateKey 私钥
     */
    public byte[] encrypt(byte[] data, PrivateKey privateKey) {
        return getRsa().encrypt(data, privateKey);
    }

    // endregion

    // region 公钥解密

    /**
     * 公钥解密为字符串，默认字符集为UTF-8，一般不使用
     *
     * @param encrypt   待解密hex字符串
     * @param publicKey hex 格式公钥
     */
    public String decryptHex(String encrypt, PublicKey publicKey) {
        return decryptHex(encrypt, publicKey, StandardCharsets.UTF_8);
    }

    /**
     * 公钥解密为字符串
     *
     * @param encrypt   待解密hex字符串
     * @param charset   字符集
     * @param publicKey hex 格式公钥
     */
    public String decryptHex(String encrypt, PublicKey publicKey, Charset charset) {
        try {
            return new String(decrypt(HexUtil.decodeHex(encrypt), publicKey), charset);
        } catch (Exception e) {
            throw new CryptoException("RSA非对称加密-hex解密异常", e);
        }
    }

    /**
     * 公钥解密为字符串，默认字符集为UTF-8
     *
     * @param encrypt   待解密hex字符串
     * @param publicKey base64 格式公钥
     */
    public String decryptBase64(String encrypt, PublicKey publicKey) {
        return decryptBase64(encrypt, publicKey, StandardCharsets.UTF_8);
    }

    /**
     * 公钥解密为字符串
     *
     * @param encrypt   待解密hex字符串
     * @param charset   字符集
     * @param publicKey base64 格式公钥
     */
    public String decryptBase64(String encrypt, PublicKey publicKey, Charset charset) {
        try {
            return new String(decrypt(Base64Util.decode(encrypt), publicKey), charset);
        } catch (Exception e) {
            throw new CryptoException("RSA公钥解密异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data      密文
     * @param publicKey 公钥
     */
    public byte[] decrypt(byte[] data, PublicKey publicKey) {
        return getRsa().decrypt(data, publicKey);
    }

    // endregion

    // endregion

    RSATool(RSAAlgorithmEnum algorithm) {
        this.algorithm = algorithm;
        this.rsa = new RSA(algorithm);
    }

    /**
     * 算法
     */
    private final RSAAlgorithmEnum algorithm;

    /**
     * RSA 算法实例
     */
    private final RSA rsa;
}
