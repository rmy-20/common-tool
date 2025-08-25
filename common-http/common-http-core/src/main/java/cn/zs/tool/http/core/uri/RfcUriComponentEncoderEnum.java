package cn.zs.tool.http.core.uri;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.CharacterUtil;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.util.UriUtil;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * RFC 3986 URI Component Encoder
 *
 * @author sheng
 */
public enum RfcUriComponentEncoderEnum {
    /**
     * 协议
     */
    SCHEME() {
        @Override
        public boolean isAllowed(int c) {
            return CharacterUtil.isAlpha(c) || Character.isDigit(c) || '+' == c || '.' == c || '-' == c;
        }
    },

    /**
     * 权限
     */
    AUTHORITY() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isUnreservedOrSubDelimiter(c) || ':' == c || '@' == c;
        }
    },

    /**
     * 用户信息
     */
    USER_INFO() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isUnreservedOrSubDelimiter(c) || ':' == c;
        }
    },

    /**
     * 主机 IPV4
     */
    HOST_IPV4() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isUnreservedOrSubDelimiter(c);
        }
    },

    /**
     * 主机 IPV6
     */
    HOST_IPV6() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isUnreservedOrSubDelimiter(c) || '[' == c || ']' == c || ':' == c;
        }
    },

    /**
     * 端口
     */
    PORT() {
        @Override
        public boolean isAllowed(int c) {
            return Character.isDigit(c);
        }
    },

    /**
     * 路径
     */
    PATH() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isPathChar(c) || '/' == c;
        }
    },

    /**
     * 路径段
     */
    PATH_SEGMENT() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isPathChar(c);
        }
    },

    /**
     * 查询参数
     */
    QUERY() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isPathChar(c) || '/' == c || '?' == c;
        }
    },

    /**
     * 查询参数组成部分
     */
    QUERY_PARAM() {
        @Override
        public boolean isAllowed(int c) {
            return '=' != c && '&' != c && (UriUtil.isPathChar(c) || '/' == c || '?' == c);
        }
    },

    /**
     * 片段
     */
    FRAGMENT() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isPathChar(c) || '/' == c || '?' == c;
        }
    },

    /**
     * uri
     */
    URI() {
        @Override
        public boolean isAllowed(int c) {
            return UriUtil.isUnreserved(c);
        }
    },
    ;

    /**
     * 给定字符在当前 uri 组件中是否合法
     */
    public abstract boolean isAllowed(int c);

    /**
     * 对传入组件进行编码
     *
     * @param source  组件
     * @param charset 默认字符集
     */
    public String encode(String source, Charset charset) {
        if (StringUtil.isEmpty(source)) {
            return source;
        }
        Assert.nonNull(charset, "Charset must be not null");
        byte[] sourceBytes = source.getBytes(charset);
        // 检查给定字符串是否合法
        boolean isAllowed = true;
        for (byte sourceByte : sourceBytes) {
            if (!isAllowed(sourceByte)) {
                isAllowed = false;
                break;
            }
        }
        if (isAllowed) {
            return source;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream(sourceBytes.length);
        for (byte sourceByte : sourceBytes) {
            if (isAllowed(sourceByte)) {
                stream.write(sourceByte);
            } else {
                // 不合法字符进行编码
                stream.write('%');
                stream.write(Character.toUpperCase(Character.forDigit((sourceByte >> 4) & 0xF, 16)));
                stream.write(Character.toUpperCase(Character.forDigit(sourceByte & 0xF, 16)));
            }
        }
        return new String(stream.toByteArray(), charset);
    }
}
