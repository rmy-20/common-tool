package io.github.rmy20.tool.core.codec.binary;

import io.github.rmy20.tool.core.io.IOUtil;

import java.nio.ByteBuffer;

/**
 * Hex 工具类
 *
 * @author sheng
 */
public class HexUtil {

    /**
     * 用于构建十六进制字母
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于构建输出的十六进制字母
     */
    private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 选择输出字母表
     *
     * @param toLowerCase true为小写, false为大写
     */
    private static char[] toAlphabet(final boolean toLowerCase) {
        return toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;
    }

    /**
     * hex 编码
     */
    public static String encodeHexString(final byte[] data) {
        return encodeHexString(data, true);
    }

    /**
     * hex 编码
     *
     * @param data        待编码数据
     * @param toLowerCase true为小写, false为大写
     */
    public static String encodeHexString(final byte[] data, final boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    /**
     * hex 编码
     *
     * @param data 待 hex 字节数组
     */
    public static String encodeHexString(final ByteBuffer data) {
        return encodeHexString(data, true);
    }

    /**
     * hex 编码
     *
     * @param data        待 hex 字节数组
     * @param toLowerCase 是否小写
     */
    public static String encodeHexString(final ByteBuffer data, final boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    /**
     * hex 编码
     *
     * @param data 待 hex 字节数组
     */
    public static char[] encodeHex(final ByteBuffer data) {
        return encodeHex(data, true);
    }

    /**
     * hex 编码
     *
     * @param data        待 hex 字节数组
     * @param toLowerCase 是否小写
     */
    public static char[] encodeHex(final ByteBuffer data, final boolean toLowerCase) {
        return encodeHex(IOUtil.toByteArray(data), toLowerCase);
    }

    /**
     * hex 编码
     *
     * @param data 待 hex 字节数组
     */
    public static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * hex 编码
     *
     * @param data        待 hex 字节数组
     * @param toLowerCase 是否小写
     */
    public static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, 0, data.length, toLowerCase);
    }

    /**
     * hex 编码
     *
     * @param data        待 hex 字节数组
     * @param dataOffset  {@code data} 开始 hex 位置
     * @param dataLen     hex 编码字节数
     * @param toLowerCase 是否小写
     */
    public static char[] encodeHex(final byte[] data, final int dataOffset, final int dataLen, final boolean toLowerCase) {
        char[] out = new char[dataLen << 1];
        char[] digitsArr = toAlphabet(toLowerCase);
        // 两个字符构成十六进制值
        for (int i = dataOffset, j = 0; i < dataOffset + dataLen; i++) {
            out[j++] = digitsArr[(0xF0 & data[i]) >>> 4];
            out[j++] = digitsArr[0x0F & data[i]];
        }
        return out;
    }

    /**
     * hex 解码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeHex(final String data) {
        return decodeHex(data.toCharArray());
    }

    /**
     * hex 解码
     *
     * @param data 待解码数据
     */
    public static byte[] decodeHex(final char[] data) {
        final int len = data.length;
        if ((len & 0x01) != 0) {
            throw new BinaryException("字符数为奇数");
        }
        final byte[] out = new byte[len >> 1];
        // 两个字符构成十六进制值
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f |= toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }
        return out;
    }

    /**
     * 将十六进制数转换为十进制数
     *
     * @param ch    要转换为整数的字符
     * @param index 源中字符的索引
     */
    private static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new BinaryException(String.format("索引%s的%s为非法字符", index, ch));
        }
        return digit;
    }
}
