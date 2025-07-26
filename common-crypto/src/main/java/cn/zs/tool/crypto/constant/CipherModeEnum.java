package cn.zs.tool.crypto.constant;

import lombok.Getter;

import javax.crypto.Cipher;

/**
 * {@link Cipher Cipher 模式}
 *
 * @author sheng
 */
@Getter
public enum CipherModeEnum {
    /**
     * 加密模式
     */
    ENCRYPT_MODE(Cipher.ENCRYPT_MODE),

    /**
     * 解密模式
     */
    DECRYPT_MODE(Cipher.DECRYPT_MODE),

    /**
     * 包装模式
     */
    WRAP_MODE(Cipher.WRAP_MODE),

    /**
     * 拆包模式
     */
    UNWRAP_MODE(Cipher.UNWRAP_MODE),
    ;

    CipherModeEnum(int mode) {
        this.mode = mode;
    }

    /**
     * 模式
     */
    private final int mode;
}
