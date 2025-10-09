package io.github.rmy20.tool.crypto.symmetric;

import io.github.rmy20.tool.crypto.constant.KeySizeEnum;
import lombok.Getter;

/**
 * 密钥长度
 *
 * @author sheng
 */
@Getter
public enum SymmetricCryptoKeySizeEnum {
    /**
     * 128位长度
     */
    KEY_SIZE_128(KeySizeEnum.KEY_SIZE_128),

    /**
     * 192位长度
     */
    KEY_SIZE_192(KeySizeEnum.KEY_SIZE_192),

    /**
     * 256位长度
     */
    KEY_SIZE_256(KeySizeEnum.KEY_SIZE_256),
    ;

    SymmetricCryptoKeySizeEnum(KeySizeEnum keySize) {
        this.keySize = keySize;
        this.size = keySize.getSize();
    }

    /**
     * 长度
     */
    private final KeySizeEnum keySize;

    /**
     * 长度
     */
    private final int size;
}
