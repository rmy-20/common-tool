package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.crypto.constant.AlgorithmEnum;
import io.github.rmy20.tool.crypto.constant.CipherModeEnum;
import io.github.rmy20.tool.crypto.constant.ModeEnum;
import io.github.rmy20.tool.crypto.constant.PaddingEnum;
import io.github.rmy20.tool.crypto.exception.CryptoException;
import lombok.Getter;

import javax.crypto.Cipher;
import java.security.Key;
import java.util.Objects;

/**
 * RSA算法类型
 *
 * @author sheng
 */
@Getter
public enum RSAAlgorithmEnum {
    /**
     * RSA
     */
    RSA(null, null),

    /**
     * RSA/ECB/PKCS1Padding
     */
    RSA_ECB_PKCS1Padding(ModeEnum.ECB, PaddingEnum.PKCS1Padding),

    /**
     * RSA/ECB
     */
    RSA_ECB_NoPadding(ModeEnum.ECB, PaddingEnum.NoPadding),

    /**
     * RSA/NONE/NoPadding
     */
    RSA_None_NoPadding(ModeEnum.NONE, PaddingEnum.NoPadding),

    /**
     * RSA/NONE/OAEPPadding
     */
    RSA_None_OAEPPadding(ModeEnum.NONE, PaddingEnum.OAEPPadding),
    ;

    /**
     * 构建{@link Cipher 非对称算法实例}
     */
    public Cipher createCipher(CipherModeEnum mode, Key key) {
        try {
            AlgorithmEnum algorithm = getAlgorithm().getAlgorithm();
            Cipher cipher = Cipher.getInstance(getAlgorithmName(), algorithm.getProvider().getProvider());
            cipher.init(mode.getMode(), key);
            return cipher;
        } catch (Exception e) {
            throw new CryptoException("构建RSA非对称加密算法[" + getAlgorithmName() + "]实例异常", e);
        }
    }

    RSAAlgorithmEnum(ModeEnum mode, PaddingEnum padding) {
        this.algorithm = AsymmetricCryptoAlgorithmEnum.RSA;
        this.mode = mode;
        this.padding = padding;
        this.algorithmName = algorithmName();
    }

    /**
     * 拼接算法名称
     */
    private String algorithmName() {
        ModeEnum mode = getMode();
        if (Objects.nonNull(mode)) {
            StringBuilder builder = new StringBuilder(getAlgorithm().getAlgorithm().getAlgorithm());
            builder.append(StringPool.SLASH_SIGN).append(mode.getName());
            PaddingEnum padding = getPadding();
            if (Objects.nonNull(padding)) {
                builder.append(StringPool.SLASH_SIGN).append(padding.getPadding());
            }
            return builder.toString();
        }
        return getAlgorithm().getAlgorithm().getAlgorithm();
    }

    /**
     * 算法
     */
    private final AsymmetricCryptoAlgorithmEnum algorithm;

    /**
     * 模式
     */
    private final ModeEnum mode;

    /**
     * 补码
     */
    private final PaddingEnum padding;

    /**
     * 算法名称
     */
    private final String algorithmName;
}
