package cn.zs.tool.http.core.uri;

import cn.zs.tool.core.text.CharacterUtil;
import cn.zs.tool.http.core.util.UriUtil;

import java.nio.charset.Charset;

/**
 * RFC 3986 URI Component Encoder
 *
 * @author sheng
 */
public enum RfcUriComponentEncoderEnum implements AllowedPredicate {
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
     * 对传入组件进行编码
     *
     * @param source  组件
     * @param charset 默认字符集
     */
    public String encode(String source, Charset charset) {
        return UriUtil.encode(source, charset, this, false);
    }
}
