package io.github.rmy20.tool.http.jdkclient;

import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.constant.HttpMethodEnum;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientFormRequest;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientMultipartRequest;
import io.github.rmy20.tool.http.jdkclient.request.JdkClientRequest;

import java.net.URI;

/**
 * JDK 客户端工具类
 *
 * @author sheng
 */
public class JdkClientTool {
    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest get(String url) {
        return JdkClientRequest.create(url, HttpMethodEnum.GET);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest get(URI url) {
        return get(url.toString());
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest post(String url) {
        return JdkClientRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest post(URI url) {
        return post(url.toString());
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest put(String url) {
        return JdkClientRequest.create(url, HttpMethodEnum.PUT);
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest put(URI url) {
        return put(url.toString());
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest delete(String url) {
        return JdkClientRequest.create(url, HttpMethodEnum.DELETE);
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static JdkClientRequest delete(URI url) {
        return delete(url.toString());
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static JdkClientRequest request(String url, HttpMethodEnum method) {
        return JdkClientRequest.create(url, method);
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static JdkClientRequest request(URI url, HttpMethodEnum method) {
        return request(url.toString(), method);
    }

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    public static JdkClientFormRequest form(String url) {
        return JdkClientFormRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    public static JdkClientFormRequest form(String url, HttpMethodEnum method) {
        return JdkClientFormRequest.create(url, method);
    }

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    public static JdkClientMultipartRequest multipart(String url) {
        return JdkClientMultipartRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static JdkClientMultipartRequest multipart(String url, HttpMethodEnum method) {
        return JdkClientMultipartRequest.create(url, method);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static JdkClientMultipartRequest multipart(String url, HttpMethodEnum method, MediaType contentType) {
        return JdkClientMultipartRequest.create(url, method, contentType);
    }
}
