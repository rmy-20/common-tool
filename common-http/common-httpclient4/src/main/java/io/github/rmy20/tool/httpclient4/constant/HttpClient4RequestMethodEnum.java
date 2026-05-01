package io.github.rmy20.tool.httpclient4.constant;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.core.exception.HttpException;
import lombok.Getter;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;

import java.net.URI;

/**
 * httpclient4 请求方法
 *
 * @author sheng
 */
@Getter
public enum HttpClient4RequestMethodEnum {
    /**
     * get请求
     */
    GET(HttpMethodEnum.GET) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpGet(uri);
        }
    },

    /**
     * post请求
     */
    POST(HttpMethodEnum.POST) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpPost(uri);
        }
    },

    /**
     * put请求
     */
    PUT(HttpMethodEnum.PUT) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpPut(uri);
        }
    },

    /**
     * delete请求（http规范允许但通常不携带请求体）
     */
    DELETE(HttpMethodEnum.DELETE) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpDelete(uri);
        }
    },

    /**
     * head请求，类似与get请求，但只获取响应的请求头信息
     */
    HEAD(HttpMethodEnum.HEAD) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpHead(uri);
        }
    },

    /**
     * patch请求，用于更新已存在的资源（部分更新）
     */
    PATCH(HttpMethodEnum.PATCH) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpPatch(uri);
        }
    },

    /**
     * trace请求，用于诊断和调试
     */
    TRACE(HttpMethodEnum.TRACE) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpTrace(uri);
        }
    },

    /**
     * options请求，获取目标资源或服务器所支持的通信选项
     */
    OPTIONS(HttpMethodEnum.OPTIONS) {
        @Override
        public HttpRequestBase create(URI uri) {
            return new HttpOptions(uri);
        }
    },
    ;

    /**
     * 创建 #{@link HttpRequestBase}
     *
     * @param uri uri
     */
    public abstract HttpRequestBase create(URI uri);

    /**
     * 根据 #{@link HttpMethodEnum} 获取枚举
     */
    public static HttpClient4RequestMethodEnum getByMethod(HttpMethodEnum method) {
        for (HttpClient4RequestMethodEnum value : values()) {
            if (value.method == method) {
                return value;
            }
        }
        throw new HttpException("不支持的方法[" + method + "]");
    }

    HttpClient4RequestMethodEnum(HttpMethodEnum method) {
        this.method = method;
    }

    /**
     * 请求方法
     */
    private final HttpMethodEnum method;
}
