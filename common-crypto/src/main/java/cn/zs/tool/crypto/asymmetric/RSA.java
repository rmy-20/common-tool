package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.crypto.common.BaseCrypto;
import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.constant.CipherModeEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

/**
 * RSA 非对称加密算法
 *
 * @author sheng
 */
@Getter
public class RSA implements BaseCrypto {
    /**
     * 算法
     */
    private final AsymmetricCryptoAlgorithmEnum algorithm;

    /**
     * RSA算法
     */
    private final RSAAlgorithmEnum rsaAlgorithm;

    /**
     * 构建RSA实例
     *
     * @param rsaAlgorithm {@link RSAAlgorithmEnum}
     */
    public static RSA create(RSAAlgorithmEnum rsaAlgorithm) {
        return new RSA(rsaAlgorithm);
    }

    /**
     * 指定算法类型
     */
    public RSA(RSAAlgorithmEnum rsaAlgorithm) {
        this.rsaAlgorithm = Objects.requireNonNull(rsaAlgorithm, "构建RSA实例需指定算法类型");
        this.algorithm = rsaAlgorithm.getAlgorithm();
    }

    @Override
    public AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
    }

    /**
     * 公钥加密
     *
     * @param data      数据
     * @param publicKey 公钥
     */
    public byte[] encrypt(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = rsaAlgorithm.createCipher(CipherModeEnum.ENCRYPT_MODE, publicKey);
            return doFinal(cipher, data);
        } catch (Exception e) {
            throw new CryptoException("RSA公钥加密异常", e);
        }
    }

    /**
     * 私钥加密
     *
     * @param data       数据
     * @param privateKey 私钥
     */
    public byte[] encrypt(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = rsaAlgorithm.createCipher(CipherModeEnum.ENCRYPT_MODE, privateKey);
            return doFinal(cipher, data);
        } catch (Exception e) {
            throw new CryptoException("RSA私钥加密异常", e);
        }
    }

    /**
     * 加密或解密
     *
     * @param cipher {@link Cipher }
     * @param data   被加密或解密的内容数据
     * @return 加密或解密后的内容
     * @throws Exception 异常
     */
    private byte[] doFinal(Cipher cipher, byte[] data) throws Exception {
        int dataLen = data.length;
        int rsaBlockSize = cipher.getBlockSize();
        int maxBlockSize = rsaBlockSize > 0 ? rsaBlockSize : dataLen;
        // 不足分段
        if (dataLen <= maxBlockSize) {
            return cipher.doFinal(data);
        } else {
            // 分段加解密
            try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();) {
                int offset = 0, remainLen;
                int blockSize;
                while ((remainLen = dataLen - offset) > 0) {
                    blockSize = Math.min(remainLen, maxBlockSize);
                    arrayOutputStream.write(cipher.doFinal(data, offset, blockSize));
                    offset += blockSize;
                }
                return arrayOutputStream.toByteArray();
            } catch (Exception e) {
                throw new CryptoException("RSA分段加解密异常", e);
            }
        }
    }

    /**
     * 私钥解密
     *
     * @param data       密文
     * @param privateKey 私钥
     */
    public byte[] decrypt(byte[] data, PrivateKey privateKey) {
        try {
            Cipher cipher = rsaAlgorithm.createCipher(CipherModeEnum.DECRYPT_MODE, privateKey);
            return doFinal(cipher, data);
        } catch (Exception e) {
            throw new CryptoException("RSA私钥解密异常", e);
        }
    }

    /**
     * 公钥解密
     *
     * @param data      密文
     * @param publicKey 公钥
     */
    public byte[] decrypt(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = rsaAlgorithm.createCipher(CipherModeEnum.DECRYPT_MODE, publicKey);
            return doFinal(cipher, data);
        } catch (Exception e) {
            throw new CryptoException("RSA公钥解密异常", e);
        }
    }
}
