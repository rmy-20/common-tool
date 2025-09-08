package cn.zs.tool.httpclient5;

import cn.zs.tool.httpclient5.constant.HttpRequestMethodEnum;
import cn.zs.tool.httpclient5.request.HttpClient5FormRequest;
import cn.zs.tool.httpclient5.request.HttpClient5MultipartRequest;
import cn.zs.tool.httpclient5.request.HttpClient5Request;

import java.net.URI;

/**
 * HttpClient5 工具类
 *
 * @author sheng
 */
public class HttpClient5Tool {

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request get(String url) {
        return HttpClient5Request.create(url, HttpRequestMethodEnum.GET);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request get(URI url) {
        return get(url.toString());
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request post(String url) {
        return HttpClient5Request.create(url, HttpRequestMethodEnum.POST);
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request post(URI url) {
        return post(url.toString());
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request put(String url) {
        return HttpClient5Request.create(url, HttpRequestMethodEnum.PUT);
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request put(URI url) {
        return put(url.toString());
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request delete(String url) {
        return HttpClient5Request.create(url, HttpRequestMethodEnum.DELETE);
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static HttpClient5Request delete(URI url) {
        return delete(url.toString());
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static HttpClient5Request request(String url, HttpRequestMethodEnum method) {
        return HttpClient5Request.create(url, method);
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static HttpClient5Request request(URI url, HttpRequestMethodEnum method) {
        return request(url.toString(), method);
    }

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    public static HttpClient5FormRequest form(String url) {
        return HttpClient5FormRequest.create(url, HttpRequestMethodEnum.POST);
    }

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    public static HttpClient5FormRequest form(String url, HttpRequestMethodEnum method) {
        return HttpClient5FormRequest.create(url, method);
    }

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    public static HttpClient5MultipartRequest multipart(String url) {
        return HttpClient5MultipartRequest.create(url, HttpRequestMethodEnum.POST);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static HttpClient5MultipartRequest multipart(String url, HttpRequestMethodEnum method) {
        return HttpClient5MultipartRequest.create(url, method);
    }
}
