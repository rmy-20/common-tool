package io.github.rmy20.tool.httpclient4;

import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.httpclient4.request.HttpClient4FormRequest;
import io.github.rmy20.tool.httpclient4.request.HttpClient4MultipartRequest;
import io.github.rmy20.tool.httpclient4.request.HttpClient4Request;

import java.net.URI;

/**
 * HttpClient4 工具类
 *
 * @author sheng
 */
public class HttpClient4Tool {
    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request get(String url) {
        return HttpClient4Request.create(url, HttpMethodEnum.GET);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request get(URI url) {
        return get(url.toString());
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request post(String url) {
        return HttpClient4Request.create(url, HttpMethodEnum.POST);
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request post(URI url) {
        return post(url.toString());
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request put(String url) {
        return HttpClient4Request.create(url, HttpMethodEnum.PUT);
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request put(URI url) {
        return put(url.toString());
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request delete(String url) {
        return HttpClient4Request.create(url, HttpMethodEnum.DELETE);
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static HttpClient4Request delete(URI url) {
        return delete(url.toString());
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static HttpClient4Request request(String url, HttpMethodEnum method) {
        return HttpClient4Request.create(url, method);
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static HttpClient4Request request(URI url, HttpMethodEnum method) {
        return request(url.toString(), method);
    }

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    public static HttpClient4FormRequest form(String url) {
        return HttpClient4FormRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    public static HttpClient4FormRequest form(String url, HttpMethodEnum method) {
        return HttpClient4FormRequest.create(url, method);
    }

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    public static HttpClient4MultipartRequest multipart(String url) {
        return HttpClient4MultipartRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static HttpClient4MultipartRequest multipart(String url, HttpMethodEnum method) {
        return HttpClient4MultipartRequest.create(url, method);
    }
}
