package io.github.rmy20.tool.http.core.request;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;

import java.net.URI;

/**
 * 请求
 *
 * @author sheng
 */
public interface BaseRequestTool {
    /**
     * get请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> get(String url);

    /**
     * get请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> get(URI url);

    /**
     * post请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> post(String url);

    /**
     * post请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> post(URI url);

    /**
     * put请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> put(String url);

    /**
     * put请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> put(URI url);

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> delete(String url);

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    BaseHttpRequest<?> delete(URI url);

    /**
     * 发起请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    BaseHttpRequest<?> request(String url, HttpMethodEnum method);

    /**
     * 发起请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    BaseHttpRequest<?> request(URI url, HttpMethodEnum method);

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    BaseFormRequest<?> form(String url);

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    BaseFormRequest<?> form(String url, HttpMethodEnum method);

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    BaseMultipartRequest<?> multipart(String url);

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    BaseMultipartRequest<?> multipart(String url, HttpMethodEnum method);
}
