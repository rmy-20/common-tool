package io.github.rmy20.tool.crypto.symmetric;

import io.github.rmy20.tool.crypto.common.BaseCrypto;
import io.github.rmy20.tool.crypto.constant.AlgorithmEnum;
import io.github.rmy20.tool.crypto.constant.CipherModeEnum;
import io.github.rmy20.tool.crypto.constant.ModeEnum;
import io.github.rmy20.tool.crypto.constant.PaddingEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 对称加密基类
 * <p>
 * 对称加密要求双方使用 同一个密钥 进行加密解密
 *
 * @author sheng
 */
@Getter
public class SymmetricCrypto implements BaseCrypto {
    /**
     * 当前算法枚举
     */
    private final SymmetricCryptoAlgorithmEnum algorithm;

    /**
     * 构建对称加密算法实例
     *
     * @param algorithm 算法
     * @return {@link SymmetricCrypto}
     */
    public static SymmetricCrypto create(SymmetricCryptoAlgorithmEnum algorithm) {
        return new SymmetricCrypto(algorithm);
    }

    public SymmetricCrypto(SymmetricCryptoAlgorithmEnum algorithm) {
        this.algorithm = Objects.requireNonNull(algorithm, "对称加密算法枚举不允许为空");
    }

    @Override
    public final AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
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
    public byte[] encrypt(byte[] data, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                          Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        try {
            Cipher cipher = algorithm.createCipher(CipherModeEnum.ENCRYPT_MODE, secretKey, paramsSpec, cipherAdditional, mode, padding);
            // 补零
            if (Objects.nonNull(mode) && Objects.nonNull(padding) && padding == PaddingEnum.ZeroPadding) {
                int length = data.length;
                int blockSize = cipher.getBlockSize();
                // 块拆分后数据中多余的数据
                int remainLength = length % blockSize;
                if (remainLength > 0) {
                    final byte[] newArray = new byte[length + blockSize - remainLength];
                    System.arraycopy(data, 0, newArray, 0, length);
                    data = newArray;
                }
            }
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new CryptoException("对称加密算法[" + algorithm.joinAlgorithmName(mode, padding) + "]加密异常", e);
        }
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
    public byte[] decrypt(byte[] encrypt, SecretKey secretKey, AlgorithmParameterSpec paramsSpec,
                          Consumer<Cipher> cipherAdditional, ModeEnum mode, PaddingEnum padding) {
        try {
            Cipher cipher = algorithm.createCipher(CipherModeEnum.DECRYPT_MODE, secretKey, paramsSpec, cipherAdditional, mode, padding);
            byte[] result = cipher.doFinal(encrypt);
            // 解码后的数据正好是块大小的整数倍，说明可能存在补0的情况，去掉末尾所有的0
            if (Objects.nonNull(mode) && Objects.nonNull(padding) && padding == PaddingEnum.ZeroPadding && result.length % cipher.getBlockSize() == 0) {
                int i = result.length - 1;
                while (i >= 0 && 0 == result[i]) {
                    i--;
                }
                int newSize = i + 1;
                if (newSize > 0) {
                    final byte[] newArray = new byte[newSize];
                    System.arraycopy(result, 0, newArray, 0, newSize);
                    result = newArray;
                }
            }
            return result;
        } catch (Exception e) {
            throw new CryptoException("对称加密算法[" + algorithm.joinAlgorithmName(mode, padding) + "]解密异常", e);
        }
    }
}
