package io.github.rmy20.tool.http.core.constant;

import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.uri.AllowedPredicate;
import lombok.Getter;

import java.nio.charset.Charset;

/**
 * 百分号编码
 *
 * @author sheng
 */
@Getter
public enum PercentCodecEnum implements AllowedPredicate {
    /**
     * RFC3986
     */
    RFC3986(buildRfc3986Unreserved()),
    /**
     * RFC5987
     */
    RFC5987(buildRfc5987AttrChar()),
    ;

    @Override
    public boolean isAllowed(int ch) {
        return ch >= 0 && ch < safeChars.length && safeChars[ch];
    }

    /**
     * 对传入组件进行编码
     *
     * @param source         源字符
     * @param encodedCharset 编码
     */
    public static String encode(String source, Charset encodedCharset, AllowedPredicate allowed, boolean blankAsPlus) {
        StringBuilder builder = new StringBuilder();
        encode(builder, source, encodedCharset, allowed, blankAsPlus);
        return builder.toString();
    }

    /**
     * 对传入组件进行编码
     *
     * @param builder        目标字符
     * @param content        源字符
     * @param encodedCharset 编码
     */
    public static void encode(StringBuilder builder, String content, Charset encodedCharset, AllowedPredicate allowed, boolean blankAsPlus) {
        if (StringUtil.isNotEmpty(content)) {
            Assert.nonNull(encodedCharset, "Charset must be not null");
            byte[] sourceBytes = content.getBytes(encodedCharset);
            for (byte sourceByte : sourceBytes) {
                int unsigned = sourceByte & 0xFF;
                if (allowed.isAllowed(unsigned)) {
                    builder.append((char) unsigned);
                } else if (blankAsPlus && unsigned == ' ') {
                    builder.append('+');
                } else {
                    builder.append('%');
                    builder.append(Character.toUpperCase(Character.forDigit((unsigned >> 4) & 0xF, 16)));
                    builder.append(Character.toUpperCase(Character.forDigit(unsigned & 0xF, 16)));
                }
            }
        }
    }

    /**
     * 对传入组件进行编码
     *
     * @param source         源字符
     * @param encodedCharset 编码
     */
    public String encode(String source, Charset encodedCharset, boolean blankAsPlus) {
        return encode(source, encodedCharset, this, blankAsPlus);
    }

    /**
     * 对传入组件进行编码
     *
     * @param builder        目标字符
     * @param content        源字符
     * @param encodedCharset 编码
     */
    public void encode(StringBuilder builder, String content, Charset encodedCharset, boolean blankAsPlus) {
        encode(builder, content, encodedCharset, this, blankAsPlus);
    }

    PercentCodecEnum(boolean[] safeChars) {
        this.safeChars = safeChars;
    }

    /**
     * 安全字符
     */
    private final boolean[] safeChars;

    public static boolean[] buildRfc3986Unreserved() {
        boolean[] unreserved = new boolean[128];
        fillAlphaDigit(unreserved);
        unreserved['-'] = true;
        unreserved['.'] = true;
        unreserved['_'] = true;
        unreserved['~'] = true;
        return unreserved;
    }

    public static boolean[] buildRfc5987AttrChar() {
        boolean[] attrChar = new boolean[128];
        fillAlphaDigit(attrChar);
        char[] safeChars = {'!', '#', '$', '&', '+', '-', '.', '^', '_', '`', '|', '~'};
        for (char safeChar : safeChars) {
            attrChar[safeChar] = true;
        }
        return attrChar;
    }

    public static void fillAlphaDigit(boolean[] safeChars) {
        for (int i = 'a'; i <= 'z'; i++) {
            safeChars[i] = true;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            safeChars[i] = true;
        }
        for (int i = '0'; i <= '9'; i++) {
            safeChars[i] = true;
        }
    }
}
