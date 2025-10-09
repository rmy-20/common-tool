package io.github.rmy20.tool.core.constant;

import lombok.Getter;

/**
 * 状态码
 *
 * @author sheng
 */
@Getter
public enum StatusCodeEnum {
    /**
     * 成功
     */
    SUCCESS("000000", "success"),

    /**
     * 参数不合法
     */
    BAD_INPUT("401000", "bad input"),

    /**
     * 无效数据
     */
    INVALID_DATA("401001", "invalid data"),

    /**
     * 无效配置
     */
    INVALID_CONFIG("402001", "invalid configuration"),

    /**
     * 无权限
     */
    NO_AUTH("403001", "no authorized"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR("501000", "business error"),

    /**
     * 请求第三方异常
     */
    THIRD_REQUEST_ERROR("502001", "request third-part error"),

    /**
     * 第三方响应异常
     */
    THIRD_RESPONSE_ERROR("502002", "third-part response error"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR("999999", "unknown error"),
    ;

    StatusCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 错误码
     */
    private final String code;

    /**
     * 错误描述
     */
    private final String desc;
}
