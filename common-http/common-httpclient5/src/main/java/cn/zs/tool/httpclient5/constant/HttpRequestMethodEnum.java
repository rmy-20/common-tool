package cn.zs.tool.httpclient5.constant;

import cn.zs.tool.http.core.constant.HttpMethodEnum;
import lombok.Getter;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import java.net.URI;
import java.util.Objects;

/**
 * HttpClient5 请求方法枚举
 *
 * @author sheng
 */
@Getter
public enum HttpRequestMethodEnum {
    /**
     * get请求
     */
    GET(HttpMethodEnum.GET),

    /**
     * post请求
     */
    POST(HttpMethodEnum.POST),

    /**
     * put请求
     */
    PUT(HttpMethodEnum.PUT),

    /**
     * delete请求（http规范允许但通常不携带请求体）
     */
    DELETE(HttpMethodEnum.DELETE),

    /**
     * head请求，类似与get请求，但只获取响应的请求头信息
     */
    HEAD(HttpMethodEnum.HEAD),

    /**
     * patch请求，用于更新已存在的资源（部分更新）
     */
    PATCH(HttpMethodEnum.PATCH),

    /**
     * trace请求，用于诊断和调试
     */
    TRACE(HttpMethodEnum.TRACE),

    /**
     * options请求，获取目标资源或服务器所支持的通信选项
     */
    OPTIONS(HttpMethodEnum.OPTIONS),
    ;

    /**
     * 根据 #{@link HttpMethodEnum} 获取枚举
     */
    public static HttpRequestMethodEnum getByMethod(HttpMethodEnum method) {
        if (Objects.nonNull(method)) {
            for (HttpRequestMethodEnum value : values()) {
                if (value.method == method) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * 创建 #{@link HttpUriRequestBase}
     *
     * @param uri uri
     */
    public HttpUriRequestBase create(URI uri) {
        return new HttpUriRequestBase(method.getMethod(), uri);
    }

    HttpRequestMethodEnum(HttpMethodEnum method) {
        this.method = method;
    }

    /**
     * 请求方法
     */
    private final HttpMethodEnum method;
}
