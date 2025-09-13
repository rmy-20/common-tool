package cn.zs.tool.core.text;

/**
 * 字符工具类
 *
 * @author sheng
 */
public class CharacterUtil {
    /**
     * 判断字符是否是英文字母
     *
     * @param c 字符
     */
    public static boolean isAlpha(int c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 判断字符是否是16进制数字
     */
    public static boolean isHexDigit(int c) {
        return ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F') || ('0' <= c && c <= '9');
    }

    /**
     * 判断字符是否是换行符
     */
    public static boolean isLineBreak(final char ch) {
        return ch == '\r' || ch == '\n' || ch == '\f' || ch == 11;
    }
}
