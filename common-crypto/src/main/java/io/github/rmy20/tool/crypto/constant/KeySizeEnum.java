package io.github.rmy20.tool.crypto.constant;

import lombok.Getter;

/**
 * 密钥长度
 *
 * @author sheng
 */
@Getter
public enum KeySizeEnum {
    /**
     * 128位长度
     */
    KEY_SIZE_128(128),

    /**
     * 192位长度
     */
    KEY_SIZE_192(192),

    /**
     * 256位长度
     */
    KEY_SIZE_256(256),

    /**
     * 1024
     */
    KEY_SIZE_1024(1024),

    /**
     * 2048
     */
    KEY_SIZE_2048(2048),

    /**
     * 3072
     */
    KEY_SIZE_3072(3072),

    /**
     * 4096
     */
    KEY_SIZE_4096(4096),
    ;

    KeySizeEnum(int size) {
        this.size = size;
    }

    /**
     * 长度
     */
    private final int size;
}
