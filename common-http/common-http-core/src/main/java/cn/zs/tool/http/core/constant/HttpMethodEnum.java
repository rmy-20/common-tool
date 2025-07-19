package cn.zs.tool.http.core.constant;

import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.exception.HttpException;
import lombok.Getter;

/**
 * http请求方式
 *
 * @author sheng
 */
@Getter
public enum HttpMethodEnum {
    /**
     * get请求
     */
    GET("GET", "get请求", false),

    /**
     * post请求
     */
    POST("POST", "post请求", true),

    /**
     * put请求
     */
    PUT("PUT", "put请求", true),

    /**
     * delete请求（http规范允许但通常不携带请求体）
     */
    DELETE("DELETE", "delete请求", false),

    /**
     * head请求，类似与get请求，但只获取响应的请求头信息
     */
    HEAD("HEAD", "head请求", false),

    /**
     * patch请求，用于更新已存在的资源（部分更新）
     */
    PATCH("PATCH", "patch请求", true),

    /**
     * trace请求，用于诊断和调试
     */
    TRACE("TRACE", "trace请求", false),

    /**
     * options请求，获取目标资源或服务器所支持的通信选项
     */
    OPTIONS("OPTIONS", "options请求", false),
    ;

    /**
     * 根据方法获取枚举
     */
    public static HttpMethodEnum getByMethod(String method) {
        if (StringUtil.isNotBlank(method)) {
            for (HttpMethodEnum value : values()) {
                if (value.getMethod().equalsIgnoreCase(method)) {
                    return value;
                }
            }
        }
        throw new HttpException("不支持的http请求方式[" + method + "]");
    }

    HttpMethodEnum(String method, String desc, boolean needBody) {
        this.method = method;
        this.desc = desc;
        this.needBody = needBody;
    }

    /**
     * 编码
     */
    private final String method;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 是否需要body
     */
    private final boolean needBody;
}
