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
     * `: `编码
     */
    public static final byte[] FIELD_SEP_ENCODED = StringUtil.encoded(": ");

    /**
     * 回车换行编码
     */
    public static final byte[] CR_LF_ENCODED = StringUtil.encoded("\r\n");

    /**
     * 两个横线编码
     */
    public static final byte[] TWO_HYPHENS_ENCODED = StringUtil.encoded("--");

    /**
     * 内容描述编码
     */
    public static final byte[] CONTENT_DISPOSITION_ENCODED = StringUtil.encoded("Content-Disposition");

    /**
     * 内容类型编码
     */
    public static final byte[] CONTENT_TYPE_ENCODED = StringUtil.encoded("Content-Type");

    /**
     * 边界
     */
    public static final String BOUNDARY = "boundary";
}
