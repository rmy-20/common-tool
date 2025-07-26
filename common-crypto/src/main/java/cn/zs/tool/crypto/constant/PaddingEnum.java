package cn.zs.tool.crypto.constant;

import lombok.Getter;

/**
 * 补码方式
 *
 * <p>
 * 补码方式是在分组密码中，当明文长度不是分组长度的整数倍时，需要在最后一个分组中填充一些数据使其凑满一个分组的长度。
 *
 * @author sheng
 */
@Getter
public enum PaddingEnum {
    /**
     * 无补码
     */
    NoPadding("NoPadding"),

    /**
     * 0补码，即不满block长度时使用0填充
     */
    ZeroPadding("ZeroPadding"),

    /**
     * 1PKCS1中定义的最优非对称加密填充方案
     */
    OAEPPadding("OAEPPadding"),

    /**
     * PKCS#1中描述的填充方案，和 RSA 算法一起使用
     */
    PKCS1Padding("PKCS1Padding"),

    /**
     * RSA实验室“PKCS#5：基于密码的加密标准”1.5版中描述的填充方案，1993年11月。
     */
    PKCS5Padding("PKCS5Padding"),

    /**
     * RSA实验室“PKCS#7：基于密码的加密标准”1.5版中描述的填充方案，1997年2月。
     */
    PKCS7Padding("PKCS7Padding"),

    /**
     * The padding scheme defined in the SSL Protocol Version 3.0, November 18, 1996, section 5.2.3.2 (CBC block cipher)
     */
    SSL3Padding("SSL3Padding"),
    ;

    PaddingEnum(String padding) {
        this.padding = padding;
    }

    /**
     * 补码方式名称
     */
    private final String padding;
}
