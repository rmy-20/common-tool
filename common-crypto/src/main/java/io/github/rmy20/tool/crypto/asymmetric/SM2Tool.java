package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import io.github.rmy20.tool.crypto.constant.CryptoConstant;
import io.github.rmy20.tool.crypto.constant.KeySizeEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.PlainDSAEncoding;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.BigIntegers;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

/**
 * 国密SM2非对称算法工具
 *
 * @author sheng
 */
@Slf4j
public class SM2Tool {

    /**
     * 国密SM2算法实例
     */
    public static final SM2 sm2 = SM2.create();

    /**
     * 生成{@link KeyPair 公私钥}
     */
    public static KeyPair generateKeyPair() {
        return sm2.getAlgorithm().generateKeyPair(KeySizeEnum.KEY_SIZE_256.getSize());
    }

    /**
     * 获取{@link KeyPair 公私钥信息}
     */
    public static AsymmetricCryptoKeyPairDTO generateKeyPairInfo() {
        return sm2.getAlgorithm().generateKeyPairInfo(KeySizeEnum.KEY_SIZE_256.getSize());
    }

    // region 转换私钥

    /**
     * 私钥 D 值转私钥参数
     *
     * @param privateKey 私钥 D 值
     */
    public static ECPrivateKeyParameters dValueToEcPrivateKeyParameters(byte[] privateKey) {
        BigInteger privateKeyBigInteger = BigIntegers.fromUnsignedByteArray(privateKey);
        return new ECPrivateKeyParameters(privateKeyBigInteger, CryptoConstant.SM2_DOMAIN_PARAMS);
    }

    /**
     * 转换PKCS8格式私钥
     *
     * @param privateKeyArr 私钥
     */
    public static ECPrivateKeyParameters toEcPrivateKeyParameters(byte[] privateKeyArr) {
        PrivateKey privateKey = sm2.getAlgorithm().toPrivateKey(privateKeyArr);
        return toEcPrivateKeyParameters(privateKey);
    }

    /**
     * 转换私钥
     *
     * @param privateKey 私钥
     */
    public static ECPrivateKeyParameters toEcPrivateKeyParameters(PrivateKey privateKey) {
        if (privateKey instanceof BCECPrivateKey) {
            BCECPrivateKey ecPrivateKey = (BCECPrivateKey) privateKey;
            // ECParameterSpec ecParameterSpec = ecPrivateKey.getParameters();
            // ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
            //         ecParameterSpec.getG(), ecParameterSpec.getN(), ecParameterSpec.getH());
            return new ECPrivateKeyParameters(ecPrivateKey.getD(), CryptoConstant.SM2_DOMAIN_PARAMS);
        }
        throw new CryptoException("SM2转换私钥异常");
    }

    // endregion

    // region 转换公钥

    /**
     * 公钥 Q 值转公钥参数
     *
     * @param publicKey 公钥 Q 值，长度为65或64字节，取后64字节
     */
    public static ECPublicKeyParameters qValueToEcPublicKeyParameters(byte[] publicKey) {
        int length = publicKey.length, len = length / 2;
        byte[] xBytes = new byte[len];
        System.arraycopy(publicKey, length - len * 2, xBytes, 0, len);
        byte[] yBytes = new byte[len];
        System.arraycopy(publicKey, length - len, yBytes, 0, len);
        return qValueToEcPublicKeyParameters(xBytes, yBytes);
    }

    /**
     * 公钥 Q 值转公钥参数
     *
     * @param publicKeyPointX 公钥点X
     * @param publicKeyPointY 公钥点Y
     */
    public static ECPublicKeyParameters qValueToEcPublicKeyParameters(byte[] publicKeyPointX, byte[] publicKeyPointY) {
        BigInteger x = BigIntegers.fromUnsignedByteArray(publicKeyPointX);
        BigInteger y = BigIntegers.fromUnsignedByteArray(publicKeyPointY);
        ECPoint point = CryptoConstant.SM2_CURVE.createPoint(x, y);
        return new ECPublicKeyParameters(point, CryptoConstant.SM2_DOMAIN_PARAMS);
    }

    /**
     * 转换X509格式公钥
     */
    public static ECPublicKeyParameters toEcPublicKeyParameters(byte[] publicKeyArr) {
        PublicKey publicKey = sm2.getAlgorithm().toPublicKey(publicKeyArr);
        return toEcPublicKeyParameters(publicKey);
    }

    /**
     * 转换公钥
     *
     * @param publicKey 公钥
     */
    public static ECPublicKeyParameters toEcPublicKeyParameters(PublicKey publicKey) {
        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey ecPublicKey = (BCECPublicKey) publicKey;
            ECPoint point = ecPublicKey.getQ();
            return new ECPublicKeyParameters(point, CryptoConstant.SM2_DOMAIN_PARAMS);
        }
        throw new CryptoException("SM2转换公钥异常");
    }

    // endregion

    // region 加密

    /**
     * 公钥加密，默认UTF-8编码
     *
     * @param data      待被加密的字符串
     * @param publicKey hex编码的公钥
     * @return 加密后的16进制字符串
     */
    public static String encryptHex(String data, ECPublicKeyParameters publicKey) {
        return encryptHex(data, publicKey, SM2.SM2_DEFAULT_MODE);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param publicKey 公钥
     * @param mode      加密模式
     * @return 加密后的16进制字符串
     */
    public static String encryptHex(String data, ECPublicKeyParameters publicKey, SM2Engine.Mode mode) {
        return encryptHex(data, StandardCharsets.UTF_8, publicKey, mode);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param publicKey base64 格式公钥
     * @param mode      加密模式
     * @param charset   编码
     * @return 加密后的16进制字符串
     */
    public static String encryptHex(String data, Charset charset, ECPublicKeyParameters publicKey, SM2Engine.Mode mode) {
        try {
            return HexUtil.encodeHexString(sm2.encrypt(data.getBytes(charset), publicKey, mode));
        } catch (Exception e) {
            throw new CryptoException("SM2公钥加密异常", e);
        }
    }

    /**
     * 公钥加密，默认UTF-8编码
     *
     * @param data      待被加密的字符串
     * @param publicKey base64编码的公钥
     * @return 加密后的base64字符串
     */
    public static String encryptBase64(String data, ECPublicKeyParameters publicKey) {
        return encryptBase64(data, publicKey, SM2.SM2_DEFAULT_MODE);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param publicKey base64 格式公钥
     * @param mode      加密模式
     * @return 加密后的base64字符串
     */
    public static String encryptBase64(String data, ECPublicKeyParameters publicKey, SM2Engine.Mode mode) {
        return encryptBase64(data, StandardCharsets.UTF_8, publicKey, mode);
    }

    /**
     * 公钥加密
     *
     * @param data      待被加密的字符串
     * @param publicKey 公钥
     * @param mode      加密模式
     * @param charset   编码
     * @return 加密后的base64字符串
     */
    public static String encryptBase64(String data, Charset charset, ECPublicKeyParameters publicKey, SM2Engine.Mode mode) {
        try {
            return Base64Util.encodeToString(sm2.encrypt(data.getBytes(charset), publicKey, mode));
        } catch (Exception e) {
            throw new CryptoException("SM2加密异常", e);
        }
    }

    // endregion

    // region 解密

    /**
     * 私钥解密为字符串，默认字符集为UTF-8
     *
     * @param encrypt    待解密hex字符串
     * @param privateKey hex格式 私钥
     */
    public static String decryptHex(String encrypt, ECPrivateKeyParameters privateKey) {
        return decryptHex(encrypt, privateKey, SM2.SM2_DEFAULT_MODE);
    }

    /**
     * 私钥解密
     *
     * @param encrypt    待解密 hex 字符串
     * @param privateKey hex格式私钥
     * @param mode       解密模式
     */
    public static String decryptHex(String encrypt, ECPrivateKeyParameters privateKey, SM2Engine.Mode mode) {
        return decryptHex(encrypt, privateKey, mode, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param encrypt    待解密 hex 字符串
     * @param privateKey hex格式私钥
     * @param mode       解密模式
     */
    public static String decryptHex(String encrypt, ECPrivateKeyParameters privateKey, SM2Engine.Mode mode, Charset charset) {
        try {
            return new String(sm2.decrypt(HexUtil.decodeHex(encrypt), privateKey, mode), charset);
        } catch (Exception e) {
            throw new CryptoException("SM2解密异常", e);
        }
    }

    /**
     * 私钥解密为字符串，默认字符集为UTF-8
     *
     * @param encrypt    待解密base64字符串
     * @param privateKey base64格式 私钥
     */
    public static String decryptBase64(String encrypt, ECPrivateKeyParameters privateKey) {
        return decryptBase64(encrypt, privateKey, SM2.SM2_DEFAULT_MODE);
    }

    /**
     * 私钥解密，默认 utf-8
     *
     * @param encrypt    待解密base64字符串
     * @param privateKey base64格式私钥
     * @param mode       解密模式
     */
    public static String decryptBase64(String encrypt, ECPrivateKeyParameters privateKey, SM2Engine.Mode mode) {
        return decryptBase64(encrypt, privateKey, mode, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param encrypt    待解密base64字符串
     * @param privateKey base64格式私钥
     * @param mode       解密模式
     * @param charset    编码
     */
    public static String decryptBase64(String encrypt, ECPrivateKeyParameters privateKey, SM2Engine.Mode mode, Charset charset) {
        try {
            return new String(sm2.decrypt(Base64Util.decode(encrypt), privateKey, mode), charset);
        } catch (Exception e) {
            throw new CryptoException("SM2解密异常", e);
        }
    }

    // endregion

    // region 签名

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @param withId     标识
     */
    public static String signHex(String plainText, ECPrivateKeyParameters privateKey, String withId) {
        try {
            return HexUtil.encodeHexString(sign(plainText.getBytes(StandardCharsets.UTF_8), privateKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null));
        } catch (Exception e) {
            throw new CryptoException("SM2私钥签名异常", e);
        }
    }

    /**
     * 签名
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @param withId     标识
     */
    public static String signBase64(String plainText, ECPrivateKeyParameters privateKey, String withId) {
        try {
            return Base64Util.encodeToString(sign(plainText.getBytes(StandardCharsets.UTF_8), privateKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null));
        } catch (Exception e) {
            throw new CryptoException("SM2私钥签名异常", e);
        }
    }

    /**
     * 用私钥签名，使用ASN1编码
     *
     * @param data   数据
     * @param withId 标识
     */
    public static byte[] sign(byte[] data, ECPrivateKeyParameters privateKey, byte[] withId) {
        return sm2.sign(StandardDSAEncoding.INSTANCE, data, privateKey, withId);
    }

    /**
     * 签名，明文编码
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @param withId     标识
     */
    public static String signHexWithPlainEncoding(String plainText, ECPrivateKeyParameters privateKey, String withId) {
        try {
            return HexUtil.encodeHexString(signWithPlainEncoding(plainText.getBytes(StandardCharsets.UTF_8), privateKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null));
        } catch (Exception e) {
            throw new CryptoException("SM2私钥签名异常", e);
        }
    }

    /**
     * 签名，明文编码
     *
     * @param plainText  待签名数据
     * @param privateKey 私钥
     * @param withId     标识
     */
    public static String signBase64WithPlainEncoding(String plainText, ECPrivateKeyParameters privateKey, String withId) {
        try {
            return Base64Util.encodeToString(signWithPlainEncoding(plainText.getBytes(StandardCharsets.UTF_8), privateKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null));
        } catch (Exception e) {
            throw new CryptoException("SM2私钥签名异常", e);
        }
    }

    /**
     * 签名，明文编码
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @param withId     标识
     */
    public static byte[] signWithPlainEncoding(byte[] data, ECPrivateKeyParameters privateKey, byte[] withId) {
        return sm2.sign(PlainDSAEncoding.INSTANCE, data, privateKey, withId);
    }

    // endregion

    // region 验签

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText 待签名数据
     * @param publicKey 公钥
     * @param hexWithId 标识
     * @param signHex   16进制 hex 签名
     */
    public static boolean verifyHex(String plainText, ECPublicKeyParameters publicKey, String hexWithId, String signHex) {
        try {
            return verify(plainText.getBytes(StandardCharsets.UTF_8), publicKey,
                    Objects.nonNull(hexWithId) ? hexWithId.getBytes(StandardCharsets.UTF_8) : null, HexUtil.decodeHex(signHex));
        } catch (Exception e) {
            log.error("SM2验签异常", e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param plainText  待签名数据
     * @param publicKey  公钥
     * @param withId     可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param signBase64 base64 签名
     */
    public static boolean verifyBase64(String plainText, ECPublicKeyParameters publicKey, String withId, String signBase64) {
        try {
            return verify(plainText.getBytes(StandardCharsets.UTF_8), publicKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null, Base64Util.decode(signBase64));
        } catch (Exception e) {
            log.error("SM2验签异常", e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性，使用默认编码
     *
     * @param data   数据
     * @param withId 可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param sign   签名
     * @return true 为验证通过
     */
    public static boolean verify(byte[] data, ECPublicKeyParameters publicKey, byte[] withId, byte[] sign) {
        return sm2.verify(StandardDSAEncoding.INSTANCE, data, publicKey, withId, sign);
    }

    /**
     * 用公钥检验数字签名的合法性，使用明文编码
     *
     * @param plainText 16进制 hex 待签名数据
     * @param withId    可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param signHex   16进制 hex 签名
     */
    public static boolean verifyHexWithPlainEncoding(String plainText, ECPublicKeyParameters publicKey, String withId, String signHex) {
        try {
            return verifyWithPlainEncoding(plainText.getBytes(StandardCharsets.UTF_8), publicKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null, HexUtil.decodeHex(signHex));
        } catch (Exception e) {
            log.error("SM2验签异常", e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性，使用明文编码
     *
     * @param plainText  16进制 hex 待签名数据
     *                   .
     * @param publicKey  公钥
     * @param withId     标识
     * @param signBase64 base64 签名
     */
    public static boolean verifyBase64WithPlainEncoding(String plainText, ECPublicKeyParameters publicKey, String withId, String signBase64) {
        try {
            return verifyWithPlainEncoding(plainText.getBytes(StandardCharsets.UTF_8), publicKey,
                    Objects.nonNull(withId) ? withId.getBytes(StandardCharsets.UTF_8) : null, Base64Util.decode(signBase64));
        } catch (Exception e) {
            log.error("SM2验签异常", e);
        }
        return false;
    }

    /**
     * 用公钥检验数字签名的合法性，使用明文编码
     *
     * @param data   数据
     * @param withId 可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @param sign   签名
     * @return true 为验证通过
     */
    public static boolean verifyWithPlainEncoding(byte[] data, ECPublicKeyParameters publicKey, byte[] withId, byte[] sign) {
        return sm2.verify(PlainDSAEncoding.INSTANCE, data, publicKey, withId, sign);
    }

    // endregion
}
