package cn.zs.tool.core.util;

import cn.zs.tool.core.date.DateConstants;
import cn.zs.tool.core.date.DateTool;
import cn.zs.tool.core.text.StringPool;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 *
 * @author sheng
 */
public class RandomUtil {
    /**
     * 随机数取的模板
     */
    private static final char[] RANDOM_PARAM = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
            'z', 'x', 'c', 'v', 'b', 'n', 'm',
            'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
            'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L',
            'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

    static {
        // 打乱数字，1-0，0-9
        randomScramble(RANDOM_PARAM, 0, 9);
        // 打乱小写字母，q-10，m-35
        randomScramble(RANDOM_PARAM, 10, 35);
        // 打乱大写字母，Q-36，M-61
        randomScramble(RANDOM_PARAM, 36, 61);
    }

    /**
     * 获取去除 - 的 UUID
     */
    public static String generateUuidSimple() {
        return generateUuid().replaceAll(StringPool.MINUS_SIGN, StringPool.EMPTY);
    }

    /**
     * 获取 UUID
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据指定格式的时间和字母生成特定长度的字符串。
     * <p>
     * 如 String random = generateByTimeAndLetter(16, "yyMMddHHmmss"); // f43X10Rx43452108
     *
     * @param length   生成字符串的长度，≤ format长度，以format长度为准
     * @param dateTool 指定的时间格式，用于格式化当前时间
     * @return 生成的字符串，其长度由length参数指定，内容包含当前时间信息和随机字母
     */
    public static String generateByTimeAndLetter(int length, DateTool dateTool) {
        String time = dateTool.format(ZonedDateTime.now(DateConstants.GMT_8_ZONE_ID));
        return completeAndUnset(length, time, 10, 62);
    }

    /**
     * 补全并打乱随机字符串
     * <p>
     *
     * @param length 总长度，包括预设格式字符长度
     * @param preset 预设格式字符串，如果为空，则仅生成随机字符串
     * @param seed   0 ≤ seed < bound
     * @param bound  seed < bound <= {@code RANDOM_PARAM.length}
     * @return 返回打乱顺序后，包含预设格式和随机字符的字符串。
     */
    public static String completeAndUnset(int length, String preset, int seed, int bound) {
        preset = Objects.toString(preset, StringPool.EMPTY);
        StringBuilder origin = new StringBuilder(preset);
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        // 拼接字母
        for (int i = 0; i < length - preset.length(); i++) {
            origin.append(RANDOM_PARAM[localRandom.nextInt(seed, bound)]);
        }
        // 随机打乱
        char[] charArray = origin.toString().toCharArray();
        randomScramble(charArray, 0, charArray.length);
        return new String(charArray);
    }

    /**
     * 随机打乱字符串
     *
     * @param charArray 待打乱字符串
     * @param start     索引范围开始
     * @param end       索引范围结束
     */
    public static void randomScramble(char[] charArray, int start, int end) {
        if (start < 0) {
            start = 0;
        }
        if (end >= charArray.length) {
            end = charArray.length - 1;
        }
        if (start >= end) {
            return;
        }
        int nextEnd = end + 1;
        for (int i = end; i >= start; i--) {
            int j = ThreadLocalRandom.current().nextInt(start, nextEnd);
            char temp = charArray[i];
            charArray[i] = charArray[j];
            charArray[j] = temp;
        }
    }

    /**
     * 获取 len 位数字组成的字符串，＜1返回""
     *
     * @param len 获取的字符串的位数
     * @return len 位数字组成的字符串
     */
    public static String generateRandomNum(int len) {
        return generateRandom(len, 0, 10);
    }

    /**
     * 获取 len 位大小写字母组成的字符串
     *
     * @param len 获取的字符串的位数，＜1返回""
     * @return len 位大小写字母组成的字符串
     */
    public static String generateRandomLetter(int len) {
        return generateRandom(len, 10, 62);
    }

    /**
     * 获取 len 位大小写字母、数字组成的字符串
     *
     * @param len 获取的字符串的位数，＜1返回""
     * @return len 位大小写字母、数字组成的字符串
     */
    public static String generateRandomNumAndLetter(int len) {
        return generateRandom(len, 0, 62);
    }

    /**
     * 获取 len 位长度的字符串，由数字、大小写字母的
     *
     * @param len    长度，＜1返回""
     * @param origin 随机数范围开始
     * @param bound  随机数范围结束
     * @return len 位长度的字符串
     */
    private static String generateRandom(int len, int origin, int bound) {
        if (len < 1) {
            return StringPool.EMPTY;
        }
        char[] charArray = new char[len];
        for (int i = 0; i < len; i++) {
            charArray[i] = RANDOM_PARAM[ThreadLocalRandom.current().nextInt(origin, bound)];
        }
        // 随机打乱
        randomScramble(charArray, 0, charArray.length);
        return new String(charArray);
    }
}
