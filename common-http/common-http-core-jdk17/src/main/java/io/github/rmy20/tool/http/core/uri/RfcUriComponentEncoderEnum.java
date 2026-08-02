package io.github.rmy20.tool.http.core.uri;

import io.github.rmy20.tool.core.text.CharacterUtil;
import io.github.rmy20.tool.http.core.constant.PercentCodecEnum;

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
            return isUnreservedOrSubDelimiter(c) || ':' == c || '@' == c;
        }
    },

    /**
     * 用户信息
     */
    USER_INFO() {
        @Override
        public boolean isAllowed(int c) {
            return isUnreservedOrSubDelimiter(c) || ':' == c;
        }
    },

    /**
     * 主机 IPV4
     */
    HOST_IPV4() {
        @Override
        public boolean isAllowed(int c) {
            return isUnreservedOrSubDelimiter(c);
        }
    },

    /**
     * 主机 IPV6
     */
    HOST_IPV6() {
        @Override
        public boolean isAllowed(int c) {
            return isUnreservedOrSubDelimiter(c) || '[' == c || ']' == c || ':' == c;
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
            return isPathChar(c) || '/' == c;
        }
    },

    /**
     * 路径段
     */
    PATH_SEGMENT() {
        @Override
        public boolean isAllowed(int c) {
            return isPathChar(c);
        }
    },

    /**
     * 查询参数
     */
    QUERY() {
        @Override
        public boolean isAllowed(int c) {
            return isPathChar(c) || '/' == c || '?' == c;
        }
    },

    /**
     * 查询参数组成部分
     */
    QUERY_PARAM() {
        @Override
        public boolean isAllowed(int c) {
            return '=' != c && '&' != c && (isPathChar(c) || '/' == c || '?' == c);
        }
    },

    /**
     * 片段
     */
    FRAGMENT() {
        @Override
        public boolean isAllowed(int c) {
            return isPathChar(c) || '/' == c || '?' == c;
        }
    },

    /**
     * uri
     */
    URI() {
        @Override
        public boolean isAllowed(int c) {
            return PercentCodecEnum.RFC3986.isAllowed(c);
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
        return PercentCodecEnum.encode(source, charset, this, false);
    }

    /**
     * 未保留字符（可直接出现在 uri 中而无需编码）或子分隔符
     */
    private static final boolean[] UNRESERVED_OR_SUB_DELIMITER = new boolean[128];
    /**
     * 字符集 pchar
     */
    private static final boolean[] PATH_CHAR = new boolean[128];

    static {
        // 子分隔符
        final char[] SUB_DELIMITER_CHAR_ARRAY = {'!', '$', '&', '\'', '(', ')', '*', '+', ',', ';', '='};
        final boolean[] SUB_DELIMITER_ARRAY = new boolean[128];
        for (char ch : SUB_DELIMITER_CHAR_ARRAY) {
            SUB_DELIMITER_ARRAY[ch] = true;
        }
        // 未保留字符（可直接出现在 uri 中而无需编码）或子分隔符
        for (int i = 0; i < 128; i++) {
            boolean allowed = PercentCodecEnum.RFC3986.isAllowed(i) || SUB_DELIMITER_ARRAY[i];
            UNRESERVED_OR_SUB_DELIMITER[i] = allowed;
            PATH_CHAR[i] = allowed;
        }
        PATH_CHAR[':'] = true;
        PATH_CHAR['@'] = true;
    }

    /**
     * 判断是否是 uri 未保留字符（可直接出现在 uri 中而无需编码）或子分隔符
     */
    public static boolean isUnreservedOrSubDelimiter(int c) {
        return c >= 0 && c < UNRESERVED_OR_SUB_DELIMITER.length && UNRESERVED_OR_SUB_DELIMITER[c];
    }

    /**
     * 给定字符是否在 rfc uri 路径段允许字符集 pchar 中
     */
    public static boolean isPathChar(int c) {
        return c >= 0 && c < PATH_CHAR.length && PATH_CHAR[c];
    }
}
