package io.github.rmy20.tool.crypto.symmetric;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import lombok.Builder;
import lombok.Data;

import javax.crypto.SecretKey;

/**
 * 对称加密生成的密钥信息
 *
 * @author sheng
 */
@Data
@Builder
public class SymmetricCryptoSecretKeyDTO {
    /**
     * 密钥
     */
    private SecretKey secretKey;

    /**
     * 密钥
     */
    private byte[] secretKeyByte;

    /**
     * 初始向量iv
     */
    private byte[] ivByte;

    /**
     * 密钥的十六进制字符串
     */
    public String getSecretKeyHex() {
        return HexUtil.encodeHexString(secretKeyByte);
    }

    /**
     * 密钥的base64字符串
     */
    public String getSecretKeyBase64() {
        return Base64Util.encodeToString(secretKeyByte);
    }

    /**
     * 十六进制 iv
     */
    public String getIvHex() {
        return HexUtil.encodeHexString(ivByte);
    }

    /**
     * base64 iv
     */
    public String getIvBase64() {
        return Base64Util.encodeToString(ivByte);
    }
}
