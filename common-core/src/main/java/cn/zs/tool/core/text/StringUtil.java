package cn.zs.tool.core.text;

import cn.zs.tool.core.array.ArrayUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 字符串工具类
 *
 * @author sheng
 */
public class StringUtil {
    /**
     * 若输入字符串为 null 或 "" 则返回 true
     *
     * @param str 待校验字符串
     * @return 是否为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 若输入字符串不为 null 或 "" 则返回 true
     *
     * @param str 待校验字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return str != null && str.length() > 0;
    }

    /**
     * 校验输入字符串数组中是否有空字符串（null、""）
     *
     * @param arr 待校验字符串数组
     * @return 是否存在为空
     */
    public static boolean hasEmpty(CharSequence... arr) {
        if (ArrayUtil.isEmpty(arr)) {
            return true;
        }
        for (CharSequence str : arr) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验输入字符串数组中是否全是空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param arr 待校验字符串数组
     * @return 是否全都为空
     */
    public static boolean isAllEmpty(CharSequence... arr) {
        if (ArrayUtil.isEmpty(arr)) {
            return true;
        }
        for (CharSequence str : arr) {
            if (isNotEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验输入字符串是否为空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param str 待校验字符串
     * @return 是否空白
     */
    public static boolean isBlank(CharSequence str) {
        if (isEmpty(str)) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验输入字符串是否不为空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param str 待校验字符串
     * @return 是否不为空白
     */
    public static boolean isNotBlank(CharSequence str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验输入字符串数组是否包含空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param arr 待校验字符串数组
     * @return 是否包含空白
     */
    public static boolean hasBlank(CharSequence... arr) {
        if (ArrayUtil.isEmpty(arr)) {
            return true;
        }
        for (CharSequence str : arr) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 校验输入字符串数组是否全为空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param arr 待校验字符串数组
     * @return 是否包含空白
     */
    public static boolean isAllBlank(CharSequence... arr) {
        if (ArrayUtil.isEmpty(arr)) {
            return true;
        }
        for (CharSequence str : arr) {
            if (isNotBlank(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验输入字符串数组是否不包含空白（null、""、如"  "等不可见字符组成的字符串）
     *
     * @param arr 待校验字符串数组
     * @return 是否不包含空白
     */
    public static boolean isNoneBlank(CharSequence... arr) {
        return !hasBlank(arr);
    }

    /**
     * 获取字符串长度，当字符串为null时返回0
     *
     * @param str 字符串
     * @return 字符串长度
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    public static String trim(CharSequence str) {
        return isBlank(str) ? StringPool.EMPTY : str.toString().trim();
    }

    /**
     * 替换字符串中所有匹配的子串
     *
     * @param text        原始字符串
     * @param regex       待替换字符串正则
     * @param replacement 替换字符串
     * @return 替换后的字符串
     */
    public static String replaceAll(CharSequence text, String regex, String replacement) {
        return isNotBlank(text) ? text.toString().replaceAll(regex, replacement) : StringPool.EMPTY;
    }

    /**
     * 编码，默认使用UTF-8编码
     *
     * @param text 文本
     */
    public static byte[] encoded(CharSequence text) {
        return encoded(StandardCharsets.UTF_8, text);
    }

    /**
     * 编码
     *
     * @param charset 编码字符集
     * @param text    文本
     */
    public static byte[] encoded(Charset charset, CharSequence text) {
        ByteBuffer buffer = charset.encode(CharBuffer.wrap(text));
        int remaining = buffer.remaining();
        byte[] array = new byte[remaining];
        System.arraycopy(buffer.array(), 0, array, 0, remaining);
        return array;
    }

    /**
     * 去除字符串中的换行符
     */
    public static CharSequence stripLineBreaks(final CharSequence text) {
        if (Objects.isNull(text)) {
            return null;
        }
        boolean requiresRewrite = false;
        int n = 0;
        for (; n < text.length(); n++) {
            final char ch = text.charAt(n);
            if (CharacterUtil.isLineBreak(ch)) {
                requiresRewrite = true;
                break;
            }
        }
        if (!requiresRewrite) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(text, 0, n);
        for (; n < text.length(); n++) {
            final char ch = text.charAt(n);
            if (CharacterUtil.isLineBreak(ch)) {
                builder.append(' ');
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}
