package io.github.rmy20.tool.okhttp;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.okhttp.request.OkHttpFormRequest;
import io.github.rmy20.tool.okhttp.request.OkHttpMultipartRequest;
import io.github.rmy20.tool.okhttp.request.OkHttpRequest;

import java.net.URI;

/**
 * okhttp 工具类
 *
 * @author sheng
 */
public class OkHttpTool {

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest get(String url) {
        return OkHttpRequest.create(url, HttpMethodEnum.GET);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest get(URI url) {
        return get(url.toString());
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest post(String url) {
        return OkHttpRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest post(URI url) {
        return post(url.toString());
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest put(String url) {
        return OkHttpRequest.create(url, HttpMethodEnum.PUT);
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest put(URI url) {
        return put(url.toString());
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest delete(String url) {
        return OkHttpRequest.create(url, HttpMethodEnum.DELETE);
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static OkHttpRequest delete(URI url) {
        return delete(url.toString());
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static OkHttpRequest request(String url, HttpMethodEnum method) {
        return OkHttpRequest.create(url, method);
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static OkHttpRequest request(URI url, HttpMethodEnum method) {
        return OkHttpRequest.create(url.toString(), method);
    }

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    public static OkHttpFormRequest form(String url) {
        return OkHttpFormRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    public static OkHttpFormRequest form(String url, HttpMethodEnum method) {
        return OkHttpFormRequest.create(url, method);
    }

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    public static OkHttpMultipartRequest multipart(String url) {
        return OkHttpMultipartRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static OkHttpMultipartRequest multipart(String url, HttpMethodEnum method) {
        return OkHttpMultipartRequest.create(url, method);
    }
}
