package cn.zs.tool.crypto.constant;

import lombok.Getter;

/**
 * 模式
 *
 * <p>
 * 加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码）在加密时对明文分组的模式，它代表了不同的分组方式
 *
 * @author sheng
 */
@Getter
public enum ModeEnum {
    /**
     * 无模式
     */
    NONE("NONE", false),

    /**
     * 密码分组连接模式（Cipher Block Chaining），不推荐使用，安全性较低
     */
    CBC("CBC", true),

    /**
     * 密文反馈模式（Cipher Feedback），推荐使用，提供了较好的安全性。
     */
    CFB("CFB", true),

    /**
     * 计数器模式（A simplification of OFB），推荐使用，具有较好的性能和安全性。
     */
    CTR("CTR", true),

    /**
     * Cipher Text Stealing，主要用于处理不完全填充的数据块，安全性依赖于具体实现。
     */
    CTS("CTS", true),

    /**
     * 电子密码本模式（Electronic CodeBook），不推荐使用，安全性较低。
     */
    ECB("ECB", false),

    /**
     * 输出反馈模式（Output Feedback），推荐使用，提供了较好的安全性。
     */
    OFB("OFB", true),

    /**
     * GCM，全称为 Galois/Counter Mode，G是指 GMAC，C是指CTR，为 CTR 模式的变种。推荐使用，提供了加密和完整性校验。
     * <p>
     * 它在 CTR 加密的基础上增加 GMAC 的特性，解决了 CTR 不能对加密消息进行完整性校验的问题。
     */
    GCM("GCM", true),
    ;

    ModeEnum(String name, boolean needIv) {
        this.name = name;
        this.needIv = needIv;
    }

    /**
     * 模式名称
     */
    private final String name;

    /**
     * 是否需要IV向量
     */
    private final boolean needIv;
}
