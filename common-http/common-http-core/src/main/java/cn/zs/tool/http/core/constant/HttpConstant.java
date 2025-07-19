package cn.zs.tool.http.core.constant;

import java.util.function.Predicate;

/**
 * http 常量
 *
 * @author sheng
 */
public class HttpConstant {
    /**
     * 响应状态码成功判断
     */
    public static final Predicate<Integer> HTTP_OK_PREDICATE = statusCode -> statusCode >= 200 && statusCode < 300;
}
