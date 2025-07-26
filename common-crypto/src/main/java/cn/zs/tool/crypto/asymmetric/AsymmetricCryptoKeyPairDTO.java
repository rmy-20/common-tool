package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.core.codec.binary.Base64Util;
import cn.zs.tool.core.codec.binary.HexUtil;
import lombok.Builder;
import lombok.Data;

import java.security.KeyPair;

/**
 * 非对称加密生成的密钥对信息
 *
 * @author sheng
 */
@Data
@Builder
public class AsymmetricCryptoKeyPairDTO {
    /**
     * 密钥对
     */
    private KeyPair keyPair;

    /**
     * 公钥
     */
    private byte[] publicKey;

    /**
     * 私钥
     */
    private byte[] privateKey;

    /**
     * 公钥的十六进制字符串
     */
    public String getPublicHex() {
        return HexUtil.encodeHexString(publicKey);
    }

    /**
     * 私钥的十六进制字符串
     */
    public String getPrivateHex() {
        return HexUtil.encodeHexString(privateKey);
    }

    /**
     * 公钥的base64字符串
     */
    public String getPublicBase64() {
        return Base64Util.encodeToString(publicKey);
    }

    /**
     * 私钥的base64字符串
     */
    public String getPrivateBase64() {
        return Base64Util.encodeToString(privateKey);
    }
}
