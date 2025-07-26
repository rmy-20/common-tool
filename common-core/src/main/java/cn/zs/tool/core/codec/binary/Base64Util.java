package cn.zs.tool.core.codec.binary;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64 相关工具类
 *
 * @author sheng
 */
public class Base64Util {
    // region 标准 base64

    /**
     * base64编码，默认使用UTF-8编码
     *
     * @param data 待编码数据
     */
    public static String encodeToString(byte[] data) {
        return encodeToString(data, StandardCharsets.UTF_8);
    }

    /**
     * base64编码
     *
     * @param data    待编码数据
     * @param charset 编码
     */
    public static String encodeToString(byte[] data, Charset charset) {
        return new String(encode(data), charset);
    }

    /**
     * base64编码
     *
     * @param data 待编码数据
     */
    public static byte[] encode(byte[] data) {
        try {
            return Base64.getEncoder().encode(data);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * base64编码
     *
     * @param byteBuffer 待编码数据
     */
    public static ByteBuffer encode(ByteBuffer byteBuffer) {
        try {
            return Base64.getEncoder().encode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * base64解码，默认使用UTF-8编码
     *
     * @param data 待解码数据
     */
    public static byte[] decode(String data) {
        return decode(data, StandardCharsets.UTF_8);
    }

    /**
     * base64解码
     *
     * @param data    待解码数据
     * @param charset 编码
     */
    public static byte[] decode(String data, Charset charset) {
        return decode(data.getBytes(charset));
    }

    /**
     * base64解码
     *
     * @param data 待解码数据
     */
    public static byte[] decode(byte[] data) {
        try {
            return Base64.getDecoder().decode(data);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    /**
     * base64解码
     *
     * @param byteBuffer 待解码数据
     */
    public static ByteBuffer decode(ByteBuffer byteBuffer) {
        try {
            return Base64.getDecoder().decode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    // endregion

    // region url safe base64

    /**
     * url safe base64编码，默认使用UTF-8编码
     *
     * @param data 待编码数据
     */
    public static String encodeUrlSafeToString(byte[] data) {
        return encodeUrlSafeToString(data, StandardCharsets.UTF_8);
    }

    /**
     * url safe base64编码
     *
     * @param data    待编码数据
     * @param charset 编码
     */
    public static String encodeUrlSafeToString(byte[] data, Charset charset) {
        return new String(encodeUrlSafe(data), charset);
    }

    /**
     * url safe base64编码
     *
     * @param data 待编码数据
     */
    public static byte[] encodeUrlSafe(byte[] data) {
        try {
            return Base64.getUrlEncoder().encode(data);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * url safe base64编码
     *
     * @param byteBuffer 待编码数据
     */
    public static ByteBuffer encodeUrlSafe(ByteBuffer byteBuffer) {
        try {
            return Base64.getUrlEncoder().encode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * url safe base64解码，默认使用UTF-8编码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeUrlSafe(String data) {
        return decodeUrlSafe(data, StandardCharsets.UTF_8);
    }

    /**
     * url safe base64解码
     *
     * @param data    待解码数据
     * @param charset 编码
     */
    public static byte[] decodeUrlSafe(String data, Charset charset) {
        return decodeUrlSafe(data.getBytes(charset));
    }

    /**
     * url safe base64解码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeUrlSafe(byte[] data) {
        try {
            return Base64.getUrlDecoder().decode(data);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    /**
     * url safe base64解码
     *
     * @param byteBuffer 待解码数据
     */
    public static ByteBuffer decodeUrlSafe(ByteBuffer byteBuffer) {
        try {
            return Base64.getUrlDecoder().decode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    // endregion

    // region mime base64

    /**
     * mime base64编码，默认使用UTF-8编码
     *
     * @param data 待编码数据
     */
    public static String encodeMimeToString(byte[] data) {
        return encodeMimeToString(data, StandardCharsets.UTF_8);
    }

    /**
     * mime base64编码
     *
     * @param data    待编码数据
     * @param charset 编码
     */
    public static String encodeMimeToString(byte[] data, Charset charset) {
        return new String(encodeMime(data), charset);
    }

    /**
     * mime base64编码
     *
     * @param data 待编码数据
     */
    public static byte[] encodeMime(byte[] data) {
        try {
            return Base64.getMimeEncoder().encode(data);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * mime base64编码
     *
     * @param byteBuffer 待编码数据
     */
    public static ByteBuffer encodeMime(ByteBuffer byteBuffer) {
        try {
            return Base64.getMimeEncoder().encode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64编码异常", e);
        }
    }

    /**
     * mime base64解码，默认使用UTF-8编码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeMime(String data) {
        return decodeMime(data, StandardCharsets.UTF_8);
    }

    /**
     * mime base64解码
     *
     * @param data    待解码数据
     * @param charset 编码
     */
    public static byte[] decodeMime(String data, Charset charset) {
        return decodeMime(data.getBytes(charset));
    }

    /**
     * mime base64解码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeMime(byte[] data) {
        try {
            return Base64.getMimeDecoder().decode(data);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    /**
     * mime base64解码
     *
     * @param byteBuffer 待解码数据
     */
    public static ByteBuffer decodeMime(ByteBuffer byteBuffer) {
        try {
            return Base64.getMimeDecoder().decode(byteBuffer);
        } catch (Exception e) {
            throw new BinaryException("base64解码异常", e);
        }
    }

    // endregion
}
