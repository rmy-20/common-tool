package io.github.rmy20.tool.crypto.digest;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.codec.binary.HexUtil;
import io.github.rmy20.tool.core.text.StringPool;
import lombok.Getter;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 摘要签名工具
 *
 * @author sheng
 */
@Getter
public enum DigestTool {
    /**
     * SM3
     */
    SM3(DigestAlgorithmEnum.SM3),

    /**
     * MD5
     */
    MD5(DigestAlgorithmEnum.MD5),

    /**
     * SHA-1
     */
    SHA_1(DigestAlgorithmEnum.SHA_1),

    /**
     * SHA-256
     */
    SHA_256(DigestAlgorithmEnum.SHA_256),

    /**
     * SHA-384
     */
    SHA_384(DigestAlgorithmEnum.SHA_384),

    /**
     * SHA-512
     */
    SHA_512(DigestAlgorithmEnum.SHA_512),
    ;

    // region String散列

    // region hex

    /**
     * 计算散列值，结果为 hex
     *
     * @param data 待散列数据
     */
    public String digestHex(String data) {
        return digestHex(data, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data        待散列数据
     * @param digestCount 散列次数
     */
    public String digestHex(String data, int digestCount) {
        return digestHex(data, null, 0, digestCount);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data    待散列数据
     * @param charset 待散列数据编码
     */
    public String digestHex(String data, Charset charset) {
        return digestHex(data, charset, null, 0, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data 待散列数据
     * @param salt 盐值，放在最后
     */
    public String digestHex(String data, String salt) {
        return digestHex(data, salt, Integer.MAX_VALUE);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     */
    public String digestHex(String data, String salt, int saltPosition) {
        return digestHex(data, salt, saltPosition, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestHex(String data, String salt, int saltPosition, int digestCount) {
        return digestHex(data, StandardCharsets.UTF_8, salt, saltPosition, digestCount);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestHex(String data, Charset charset, String salt, int saltPosition, int digestCount) {
        return HexUtil.encodeHexString(digest(data, charset, salt, saltPosition, digestCount));
    }

    // endregion

    // region base64

    /**
     * 计算散列值，结果为 base64
     *
     * @param data 待散列数据
     */
    public String digestBase64(String data) {
        return digestBase64(data, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data        待散列数据
     * @param digestCount 散列次数
     */
    public String digestBase64(String data, int digestCount) {
        return digestBase64(data, null, 0, digestCount);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data    待散列数据
     * @param charset 待散列数据编码
     */
    public String digestBase64(String data, Charset charset) {
        return digestBase64(data, charset, null, 0, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data 待散列数据
     * @param salt 盐值，放在最后
     */
    public String digestBase64(String data, String salt) {
        return digestBase64(data, salt, Integer.MAX_VALUE);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     */
    public String digestBase64(String data, String salt, int saltPosition) {
        return digestBase64(data, salt, saltPosition, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestBase64(String data, String salt, int saltPosition, int digestCount) {
        return digestBase64(data, StandardCharsets.UTF_8, salt, saltPosition, digestCount);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestBase64(String data, Charset charset, String salt, int saltPosition, int digestCount) {
        return Base64Util.encodeToString(digest(data, charset, salt, saltPosition, digestCount));
    }

    // endregion

    /**
     * 计算散列值
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public byte[] digest(String data, Charset charset, String salt, int saltPosition, int digestCount) {
        return getDigest().digest(Objects.toString(data, StringPool.EMPTY).getBytes(charset),
                Objects.nonNull(salt) ? salt.getBytes(charset) : null, saltPosition, digestCount);
    }

    // endregion

    // region InputStream散列

    // region hex

    /**
     * 计算散列值，结果为 hex
     *
     * @param data 待散列数据
     */
    public String digestHex(InputStream data) {
        return digestHex(data, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data        待散列数据
     * @param digestCount 散列次数
     */
    public String digestHex(InputStream data, int digestCount) {
        return digestHex(data, null, 0, digestCount);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data    待散列数据
     * @param charset 待散列数据编码
     */
    public String digestHex(InputStream data, Charset charset) {
        return digestHex(data, null, charset, 0, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data 待散列数据
     * @param salt 盐值，放在最后
     */
    public String digestHex(InputStream data, String salt) {
        return digestHex(data, salt, Integer.MAX_VALUE);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     */
    public String digestHex(InputStream data, String salt, int saltPosition) {
        return digestHex(data, salt, saltPosition, 1);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestHex(InputStream data, String salt, int saltPosition, int digestCount) {
        return digestHex(data, salt, StandardCharsets.UTF_8, saltPosition, digestCount);
    }

    /**
     * 计算散列值，结果为 hex
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestHex(InputStream data, String salt, Charset charset, int saltPosition, int digestCount) {
        return HexUtil.encodeHexString(digest(data, salt, charset, saltPosition, digestCount));
    }

    // endregion

    // region base64

    /**
     * 计算散列值，结果为 base64
     *
     * @param data 待散列数据
     */
    public String digestBase64(InputStream data) {
        return digestBase64(data, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data        待散列数据
     * @param digestCount 散列次数
     */
    public String digestBase64(InputStream data, int digestCount) {
        return digestBase64(data, null, 0, digestCount);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data    待散列数据
     * @param charset 待散列数据编码
     */
    public String digestBase64(InputStream data, Charset charset) {
        return digestBase64(data, null, charset, 0, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data 待散列数据
     * @param salt 盐值，放在最后
     */
    public String digestBase64(InputStream data, String salt) {
        return digestBase64(data, salt, Integer.MAX_VALUE);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     */
    public String digestBase64(InputStream data, String salt, int saltPosition) {
        return digestBase64(data, salt, saltPosition, 1);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestBase64(InputStream data, String salt, int saltPosition, int digestCount) {
        return digestBase64(data, salt, StandardCharsets.UTF_8, saltPosition, digestCount);
    }

    /**
     * 计算散列值，结果为 base64
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public String digestBase64(InputStream data, String salt, Charset charset, int saltPosition, int digestCount) {
        return Base64Util.encodeToString(digest(data, salt, charset, saltPosition, digestCount));
    }

    // endregion

    /**
     * 计算散列值
     *
     * @param data         待散列数据
     * @param charset      待散列数据编码
     * @param salt         盐值
     * @param saltPosition 加盐位置
     * @param digestCount  散列次数
     */
    public byte[] digest(InputStream data, String salt, Charset charset, int saltPosition, int digestCount) {
        return getDigest().digest(data, Objects.nonNull(salt) ? salt.getBytes(charset) : null, saltPosition, digestCount);
    }

    // endregion

    DigestTool(DigestAlgorithmEnum algorithm) {
        this.algorithm = algorithm;
        this.digest = Digest.create(algorithm);
    }

    /**
     * Digest 算法
     */
    private final DigestAlgorithmEnum algorithm;

    /**
     * 实际 Digest 算法
     */
    private final Digest digest;
}
