package cn.zs.tool.http.core.constant;

import cn.zs.tool.core.text.StringUtil;

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

    /**
     * 回车换行编码
     */
    public static final byte[] CR_LF_ENCODED = StringUtil.encoded("\r\n");

    /**
     * 两个横线编码
     */
    public static final byte[] TWO_HYPHENS_ENCODED = StringUtil.encoded("--");

    /**
     * boundary
     */
    public static final String BOUNDARY = "boundary";

    /**
     * charset
     */
    public static final String CHARSET = "charset";
}
