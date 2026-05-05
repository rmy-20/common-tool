package io.github.rmy20.tool.core.util.ulid;

import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.util.RandomUtil;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有序序列ulid，48位时间戳 + 24位序列 + 56位随机数
 *
 * @author sheng
 */
public class UlidSequence implements Serializable, Comparable<UlidSequence> {
    private static final long serialVersionUID = 3765446611249683372L;

    /**
     * 毫秒内序列
     */
    private static long SEQUENCE = 0L;

    /**
     * 最后一次生成 ID 的时间戳
     */
    private static long LAST_TIMESTAMP = -1L;

    /**
     * 序列位数
     */
    private static final long SEQUENCE_BITS = 24L;

    /**
     * 序列最大值
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 随机数部分长度
     */
    private static final int RANDOM_BYTES_LENGTH = 7;

    /**
     * 随机数最小值
     */
    private static final byte[] MIN_RANDOM = new byte[]{0, 0, 0, 0, 0, 0, 0};

    /**
     * 随机数最大值
     */
    private static final byte[] MAX_RANDOM = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};

    /**
     * 锁
     */
    private static final ReentrantLock LOCK = new ReentrantLock();

    private static final String TIMESTAMP_ERR_MSG = "timestamp must be less than 2^48";
    private static final String SEQUENCE_ERR_MSG = "sequence must between 0 and " + MAX_SEQUENCE;
    private static final String RANDOM_ERR_MSG = "randomBytes must be 7 byte length";

    /**
     * 时间戳
     */
    private final long timestamp;

    /**
     * 序列
     */
    @Getter
    private final long sequence;

    /**
     * 随机数
     */
    private final byte[] randomBytes;

    /**
     * 高位，48位时间戳 + 16位高位序列号
     */
    private final long msb;

    /**
     * 低位，8位低位序列号 + 56位随机数
     */
    private final long lsb;

    /**
     * 对应的 ULID
     */
    private final Ulid ulid;

    /**
     * 创建 ULID
     */
    public static UlidSequence createNextUlid() {
        try {
            LOCK.lock();
            long now = now();
            if (now == LAST_TIMESTAMP) {
                if (SEQUENCE++ <= MAX_SEQUENCE) {
                    SEQUENCE &= MAX_SEQUENCE;
                    if (SEQUENCE == 0) {
                        now = getNextMilli(LAST_TIMESTAMP);
                    }
                } else {
                    SEQUENCE = 0L;
                }
            } else {
                SEQUENCE = 0L;
            }
            LAST_TIMESTAMP = now;

            return new UlidSequence(now, SEQUENCE, RandomUtil.entropyRandomBytesAndScramble(RANDOM_BYTES_LENGTH));
        } catch (Throwable e) {
            throw new RuntimeException("生成序列ulid异常", e);
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 从传入的字符串中解析 ULID
     */
    public static UlidSequence createFrom(String str) {
        Ulid ulid = Ulid.createFrom(str);
        // 高位，48位时间戳 + 16位高位序列
        long msb = ulid.getMostSignificantBits();
        // 低位，8位低位序列 + 56为随机数
        long lsb = ulid.getLeastSignificantBits();

        byte[] randomBytes = new byte[RANDOM_BYTES_LENGTH];
        randomBytes[0] = (byte) (lsb >>> 48);
        randomBytes[1] = (byte) (lsb >>> 40);
        randomBytes[2] = (byte) (lsb >>> 32);
        randomBytes[3] = (byte) (lsb >>> 24);
        randomBytes[4] = (byte) (lsb >>> 16);
        randomBytes[5] = (byte) (lsb >>> 8);
        randomBytes[6] = (byte) lsb;

        return new UlidSequence(msb >>> 16, ((msb & 0xFFFFL) << 8) | ((lsb >>> 56) & 0xFFL), randomBytes);
    }

    /**
     * 创建传入时间的最小 ULID
     */
    public static UlidSequence createMin(long timestamp) {
        return new UlidSequence(timestamp, 0L, MIN_RANDOM);
    }

    /**
     * 创建传入时间的最大 ULID
     */
    public static UlidSequence createMax(long timestamp) {
        return new UlidSequence(timestamp, MAX_SEQUENCE, MAX_RANDOM);
    }

    /**
     * 阻塞获取下一毫秒数
     */
    private static long getNextMilli(long lastTimestamp) {
        long now;
        do {
            now = now();
        } while (now <= lastTimestamp);
        return now;
    }

    /**
     * 当前毫秒数
     */
    private static long now() {
        return System.currentTimeMillis();
    }

    public UlidSequence(long timestamp, long sequence, byte[] randomBytes) {
        // 48位时间戳 + 24位序列 + 56位随机数（7byte）
        Assert.isTrue((timestamp & 0xFFFF000000000000L) == 0, TIMESTAMP_ERR_MSG);
        Assert.isTrue(sequence >= 0 && sequence <= MAX_SEQUENCE, SEQUENCE_ERR_MSG);
        Assert.isTrue(Objects.nonNull(randomBytes) && randomBytes.length == RANDOM_BYTES_LENGTH, RANDOM_ERR_MSG);
        this.timestamp = timestamp;
        this.sequence = sequence;
        this.randomBytes = Arrays.copyOf(randomBytes, RANDOM_BYTES_LENGTH);

        // msb：时间戳48位 + 序列高16位
        this.msb = (timestamp << 16) | (sequence >>> 8);

        // lsb：序列低8位 + 56 位随机数
        this.lsb = ((sequence & 0xFFL) << 56)
                | ((randomBytes[0] & 0xFFL) << 48)
                | ((randomBytes[1] & 0xFFL) << 40)
                | ((randomBytes[2] & 0xFFL) << 32)
                | ((randomBytes[3] & 0xFFL) << 24)
                | ((randomBytes[4] & 0xFFL) << 16)
                | ((randomBytes[5] & 0xFFL) << 8)
                | (randomBytes[6] & 0xFFL);
        this.ulid = new Ulid(this.msb, this.lsb);
    }

    /**
     * 获取时间戳
     */
    public long getTime() {
        return this.timestamp;
    }

    /**
     * 随机数
     */
    public byte[] getRandom() {
        return this.randomBytes;
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
        return this.ulid.toString();
    }

    /**
     * 小写结果
     */
    public String toLowerCase() {
        return this.ulid.toLowerCase();
    }

    @Override
    public int hashCode() {
        return ulid.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (Objects.isNull(other) || !UlidSequence.class.equals(other.getClass())) {
            return false;
        }
        UlidSequence that = (UlidSequence) other;
        return (this.timestamp == that.timestamp)
                && (this.sequence == that.sequence)
                && (Arrays.equals(this.randomBytes, that.randomBytes));
    }

    @Override
    public int compareTo(UlidSequence other) {
        if (this.timestamp != other.timestamp) {
            return Long.compareUnsigned(this.timestamp, other.timestamp);
        }
        if (this.sequence != other.sequence) {
            return Long.compareUnsigned(this.sequence, other.sequence);
        }
        return Long.compareUnsigned(this.lsb, other.lsb);
    }
}
