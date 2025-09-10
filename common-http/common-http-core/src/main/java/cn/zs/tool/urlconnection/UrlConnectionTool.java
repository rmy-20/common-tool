package cn.zs.tool.urlconnection;

import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.urlconnection.request.UrlConnectionFormRequest;
import cn.zs.tool.urlconnection.request.UrlConnectionMultipartRequest;
import cn.zs.tool.urlconnection.request.UrlConnectionRequest;

import java.net.URI;

/**
 * url connection工具类
 *
 * @author sheng
 */
public class UrlConnectionTool {
    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest get(String url) {
        return UrlConnectionRequest.create(url, HttpMethodEnum.GET);
    }

    /**
     * get请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest get(URI url) {
        return get(url.toString());
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest post(String url) {
        return UrlConnectionRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * post请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest post(URI url) {
        return post(url.toString());
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest put(String url) {
        return UrlConnectionRequest.create(url, HttpMethodEnum.PUT);
    }

    /**
     * put请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest put(URI url) {
        return put(url.toString());
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest delete(String url) {
        return UrlConnectionRequest.create(url, HttpMethodEnum.DELETE);
    }

    /**
     * delete请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionRequest delete(URI url) {
        return delete(url.toString());
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static UrlConnectionRequest request(String url, HttpMethodEnum method) {
        return UrlConnectionRequest.create(url, method);
    }

    /**
     * 请求
     *
     * @param url    请求地址
     * @param method 请求方法
     */
    public static UrlConnectionRequest request(URI url, HttpMethodEnum method) {
        return request(url.toString(), method);
    }

    /**
     * 表单post请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionFormRequest form(String url) {
        return UrlConnectionFormRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 表单请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionFormRequest form(String url, HttpMethodEnum method) {
        return UrlConnectionFormRequest.create(url, method);
    }

    /**
     * 文件上传post请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionMultipartRequest multipart(String url) {
        return UrlConnectionMultipartRequest.create(url, HttpMethodEnum.POST);
    }

    /**
     * 多媒体请求
     *
     * @param url 请求地址
     */
    public static UrlConnectionMultipartRequest multipart(String url, HttpMethodEnum method) {
        return UrlConnectionMultipartRequest.create(url, method);
    }
}
