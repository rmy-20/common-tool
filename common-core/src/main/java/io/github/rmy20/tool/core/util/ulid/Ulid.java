package io.github.rmy20.tool.core.util.ulid;

import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.core.util.RandomUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * ulid，48位时间戳 + 80位随机数
 *
 * @author sheng
 */
public final class Ulid implements Serializable, Comparable<Ulid> {
    private static final long serialVersionUID = -3638881342689331691L;

    /**
     * ULID随机数字节长度，10字节 = 80位
     */
    private static final int RANDOM_BYTES_LENGTH = 10;

    /**
     * 最高有效位，64位：48位时间戳 + 16位随机数
     */
    private final long msb;

    /**
     * 最低有效位，64位：64位随机数
     */
    private final long lsb;

    /**
     * 创建 ULID
     */
    public static Ulid createUlid() {
        return new Ulid();
    }

    /**
     * 将传入的字符串解析为 ULID
     */
    public static Ulid createFrom(String str) {
        char[] charArray = UlidUtil.toCharArray(str);
        byte[] alphabetValues = UlidUtil.ALPHABET_VALUES;
        // 48位时间戳 → 50位 → 前10个字符
        long timestamp = ((alphabetValues[charArray[0]] & 0x1FL) << 45)
                | ((alphabetValues[charArray[1]] & 0x1FL) << 40)
                | ((alphabetValues[charArray[2]] & 0x1FL) << 35)
                | ((alphabetValues[charArray[3]] & 0x1FL) << 30)
                | ((alphabetValues[charArray[4]] & 0x1FL) << 25)
                | ((alphabetValues[charArray[5]] & 0x1FL) << 20)
                | ((alphabetValues[charArray[6]] & 0x1FL) << 15)
                | ((alphabetValues[charArray[7]] & 0x1FL) << 10)
                | ((alphabetValues[charArray[8]] & 0x1FL) << 5)
                | ((alphabetValues[charArray[9]] & 0x1FL));

        // 随机数高40位 → 中间8个字符
        long random0 = ((alphabetValues[charArray[10]] & 0x1FL) << 35)
                | ((alphabetValues[charArray[11]] & 0x1FL) << 30)
                | ((alphabetValues[charArray[12]] & 0x1FL) << 25)
                | ((alphabetValues[charArray[13]] & 0x1FL) << 20)
                | ((alphabetValues[charArray[14]] & 0x1FL) << 15)
                | ((alphabetValues[charArray[15]] & 0x1FL) << 10)
                | ((alphabetValues[charArray[16]] & 0x1FL) << 5)
                | (alphabetValues[charArray[17]] & 0x1FL);

        // 随机数低40位 → 最后8个字符
        long random1 = ((alphabetValues[charArray[18]] & 0x1FL) << 35)
                | ((alphabetValues[charArray[19]] & 0x1FL) << 30)
                | ((alphabetValues[charArray[20]] & 0x1FL) << 25)
                | ((alphabetValues[charArray[21]] & 0x1FL) << 20)
                | ((alphabetValues[charArray[22]] & 0x1FL) << 15)
                | ((alphabetValues[charArray[23]] & 0x1FL) << 10)
                | ((alphabetValues[charArray[24]] & 0x1FL) << 5)
                | (alphabetValues[charArray[25]] & 0x1FL);

        // msb：48位时间戳 + 16位随机数
        long msb = (timestamp << 16) | (random0 >>> 24);
        // lsb：64位随机数
        long lsb = (random0 << 40) | (random1 & 0xFFFFFFFFFFL);
        return new Ulid(msb, lsb);
    }

    /**
     * 创建传入时间的最小 ULID
     */
    public static Ulid createMin(long timestamp) {
        return new Ulid(timestamp << 16, 0L);
    }

    /**
     * 创建传入时间的最大 ULID
     */
    public static Ulid createMax(long timestamp) {
        return new Ulid((timestamp << 16) | 0xFFFFL, 0xFFFFFFFFFFFFFFFFL);
    }

    /**
     * 校验传入的字符串是否是 ULID
     */
    public static boolean validUlid(String str) {
        return StringUtil.isNotBlank(str) && UlidUtil.validChars(str.toCharArray());
    }

    public Ulid() {
        this(System.currentTimeMillis(), RandomUtil.entropyRandomBytes(RANDOM_BYTES_LENGTH));
    }

    public Ulid(long msb, long lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }

    public Ulid(long timestamp, byte[] randomBytes) {
        // 48 位时间戳 + 80 位随机数
        Assert.isTrue((timestamp & 0xFFFF000000000000L) == 0, "timestamp must be less than 2^48");
        Assert.isTrue(Objects.nonNull(randomBytes) && randomBytes.length == RANDOM_BYTES_LENGTH, "randomBytes must be 10 byte length");

        // msb：48位时间戳 + 16位随机数
        this.msb = (timestamp << 16)
                | ((randomBytes[0] & 0xFFL) << 8)
                | ((randomBytes[1] & 0xFFL));

        // lsb：64位随机数
        this.lsb = ((randomBytes[2] & 0xFFL) << 56)
                | ((randomBytes[3] & 0xFFL) << 48)
                | ((randomBytes[4] & 0xFFL) << 40)
                | ((randomBytes[5] & 0xFFL) << 32)
                | ((randomBytes[6] & 0xFFL) << 24)
                | ((randomBytes[7] & 0xFFL) << 16)
                | ((randomBytes[8] & 0xFFL) << 8)
                | (randomBytes[9] & 0xFFL);
    }

    /**
     * 获取时间戳
     */
    public long getTime() {
        return this.msb >>> 16;
    }

    /**
     * 获取{@link Instant}
     */
    public Instant getInstant() {
        return Instant.ofEpochMilli(getTime());
    }

    /**
     * 随机数
     */
    public byte[] getRandom() {
        byte[] randomBytes = new byte[RANDOM_BYTES_LENGTH];
        // 高16位随机数
        randomBytes[0] = (byte) (this.msb >>> 8);
        randomBytes[1] = (byte) this.msb;
        // 低64位随机数
        randomBytes[2] = (byte) (this.lsb >>> 56);
        randomBytes[3] = (byte) (this.lsb >>> 48);
        randomBytes[4] = (byte) (this.lsb >>> 40);
        randomBytes[5] = (byte) (this.lsb >>> 32);
        randomBytes[6] = (byte) (this.lsb >>> 24);
        randomBytes[7] = (byte) (this.lsb >>> 16);
        randomBytes[8] = (byte) (this.lsb >>> 8);
        randomBytes[9] = (byte) this.lsb;
        return randomBytes;
    }

    /**
     * 高位
     */
    public long getMostSignificantBits() {
        return this.msb;
    }

    /**
     * 低位
     */
    public long getLeastSignificantBits() {
        return this.lsb;
    }

    /**
     * 转换为 UUID
     */
    public UUID toUuid() {
        return new UUID(this.msb, this.lsb);
    }

    @Override
    public String toString() {
        return toString(UlidUtil.ALPHABET_UPPERCASE);
    }

    /**
     * 小写 ULID 字符串
     */
    public String toLowerCase() {
        return toString(UlidUtil.ALPHABET_LOWERCASE);
    }

    /**
     * Base32 编码，每5位编码为一个字符
     */
    private String toString(char[] alphabet) {
        // 48位时间戳
        long timestamp = this.msb >>> 16;
        // 80位随机数
        long random0 = ((this.msb & 0xFFFFL) << 24) | (this.lsb >>> 40);
        long random1 = (this.lsb & 0xFFFFFFFFFFL);

        final char[] chars = new char[UlidUtil.CHAR_LENGTH];
        // 时间戳 48bit → 10个字符 (5bit/个)
        chars[0] = alphabet[(int) (timestamp >>> 45 & 0b11111)];
        chars[1] = alphabet[(int) (timestamp >>> 40 & 0b11111)];
        chars[2] = alphabet[(int) (timestamp >>> 35 & 0b11111)];
        chars[3] = alphabet[(int) (timestamp >>> 30 & 0b11111)];
        chars[4] = alphabet[(int) (timestamp >>> 25 & 0b11111)];
        chars[5] = alphabet[(int) (timestamp >>> 20 & 0b11111)];
        chars[6] = alphabet[(int) (timestamp >>> 15 & 0b11111)];
        chars[7] = alphabet[(int) (timestamp >>> 10 & 0b11111)];
        chars[8] = alphabet[(int) (timestamp >>> 5 & 0b11111)];
        chars[9] = alphabet[(int) (timestamp & 0b11111)];

        // 随机数0 40bit → 8个字符
        chars[10] = alphabet[(int) (random0 >>> 35 & 0b11111)];
        chars[11] = alphabet[(int) (random0 >>> 30 & 0b11111)];
        chars[12] = alphabet[(int) (random0 >>> 25 & 0b11111)];
        chars[13] = alphabet[(int) (random0 >>> 20 & 0b11111)];
        chars[14] = alphabet[(int) (random0 >>> 15 & 0b11111)];
        chars[15] = alphabet[(int) (random0 >>> 10 & 0b11111)];
        chars[16] = alphabet[(int) (random0 >>> 5 & 0b11111)];
        chars[17] = alphabet[(int) (random0 & 0b11111)];

        // 随机数1 40bit → 8个字符
        chars[18] = alphabet[(int) (random1 >>> 35 & 0b11111)];
        chars[19] = alphabet[(int) (random1 >>> 30 & 0b11111)];
        chars[20] = alphabet[(int) (random1 >>> 25 & 0b11111)];
        chars[21] = alphabet[(int) (random1 >>> 20 & 0b11111)];
        chars[22] = alphabet[(int) (random1 >>> 15 & 0b11111)];
        chars[23] = alphabet[(int) (random1 >>> 10 & 0b11111)];
        chars[24] = alphabet[(int) (random1 >>> 5 & 0b11111)];
        chars[25] = alphabet[(int) (random1 & 0b11111)];

        return new String(chars);
    }

    @Override
    public int hashCode() {
        long bits = this.msb ^ this.lsb;
        return Long.hashCode(bits);
    }

    @Override
    public boolean equals(Object other) {
        if (Objects.isNull(other) || !Ulid.class.equals(other.getClass())) {
            return false;
        }
        Ulid that = (Ulid) other;
        return (this.msb == that.msb) && (this.lsb == that.lsb);
    }

    @Override
    public int compareTo(Ulid other) {
        if (this.msb != other.msb) {
            return Long.compareUnsigned(this.msb, other.msb);
        }
        return Long.compareUnsigned(this.lsb, other.lsb);
    }
}
