package io.github.rmy20.tool.core.util.ulid;

import io.github.rmy20.tool.core.text.StringUtil;

import java.util.Arrays;
import java.util.Objects;

/**
 * 工具类
 *
 * @author sheng
 */
public class UlidUtil {
    /**
     * ULID 字符串长度
     */
    public static final int CHAR_LENGTH = 26;

    /**
     * 解码字节表
     */
    public static final byte[] ALPHABET_VALUES = new byte[256];

    /**
     * 编码表-大写字符
     */
    public static final char[] ALPHABET_UPPERCASE = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();

    /**
     * 编码表-小写字符
     */
    public static final char[] ALPHABET_LOWERCASE = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray();

    static {
        // 全部填 -1
        Arrays.fill(ALPHABET_VALUES, (byte) -1);

        // 设置大写字符索引
        for (int i = 0; i < ALPHABET_UPPERCASE.length; i++) {
            ALPHABET_VALUES[ALPHABET_UPPERCASE[i]] = (byte) i;
        }

        // 设置小写字符索引
        for (int i = 0; i < ALPHABET_LOWERCASE.length; i++) {
            ALPHABET_VALUES[ALPHABET_LOWERCASE[i]] = (byte) i;
        }

        // OIL
        ALPHABET_VALUES['O'] = 0x00;
        ALPHABET_VALUES['I'] = 0x01;
        ALPHABET_VALUES['L'] = 0x01;

        // OIL
        ALPHABET_VALUES['o'] = 0x00;
        ALPHABET_VALUES['i'] = 0x01;
        ALPHABET_VALUES['l'] = 0x01;
    }

    /**
     * 校验传入字符是否合法的 ULID，并返回字符数组
     */
    public static char[] toCharArray(String str) {
        char[] chars = StringUtil.isNotBlank(str) ? str.toCharArray() : null;
        if (!validChars(chars)) {
            throw new IllegalArgumentException(String.format("非法ULID[%s]", str));
        }
        return chars;
    }

    /**
     * 校验传入字符是否合法的 ULID
     */
    public static boolean validChars(char[] charArray) {
        if (Objects.isNull(charArray) || charArray.length != UlidUtil.CHAR_LENGTH) {
            return false;
        }

        byte[] alphabetValues = UlidUtil.ALPHABET_VALUES;
        for (char c : charArray) {
            if (c >= alphabetValues.length || alphabetValues[c] < 0) {
                return false;
            }
        }

        // ulid 时间戳 48 位，而 base32 对时间戳编码时有50位，因此前两位必须为0，最大值为 0b00111
        return (alphabetValues[charArray[0]] & 0b11000) == 0;
    }
}
