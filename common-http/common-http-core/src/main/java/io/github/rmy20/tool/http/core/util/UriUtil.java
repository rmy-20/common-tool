package io.github.rmy20.tool.http.core.util;

import io.github.rmy20.tool.core.collection.CollectionUtil;
import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.text.CharacterUtil;
import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.exception.UriException;
import io.github.rmy20.tool.http.core.uri.AllowedPredicate;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * uri 相关工具类
 *
 * @author sheng
 */
public class UriUtil {
    /**
     * 未保留字符（可直接出现在 uri 中而无需编码）或子分隔符
     */
    private static final boolean[] UNRESERVED_OR_SUB_DELIMITER_ARRAY = new boolean[128];

    static {
        for (int i = 0; i < 128; i++) {
            UNRESERVED_OR_SUB_DELIMITER_ARRAY[i] = isUnreserved(i) || isSubDelimiter(i);
        }
    }

    /**
     * 判断是否是 uri 未保留字符（可直接出现在 uri 中而无需编码）或子分隔符
     */
    public static boolean isUnreservedOrSubDelimiter(int c) {
        return c >= 0 && c < UNRESERVED_OR_SUB_DELIMITER_ARRAY.length && UNRESERVED_OR_SUB_DELIMITER_ARRAY[c];
    }

    /**
     * 判断是否是 uri 未保留字符（可直接出现在 uri 中而无需编码）
     */
    public static boolean isUnreserved(int c) {
        return CharacterUtil.isAlpha(c) || Character.isDigit(c) || '-' == c || '.' == c || '_' == c || '~' == c;
    }

    /**
     * 判断是否是 uri 子分隔符
     */
    public static boolean isSubDelimiter(int c) {
        return '!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c || ',' == c || ';' == c || '=' == c;
    }

    /**
     * 给定字符是否在 rfc uri 路径段允许字符集 pchar 中
     */
    public static boolean isPathChar(int c) {
        return isUnreservedOrSubDelimiter(c) || ':' == c || '@' == c;
    }

    /**
     * 根据 / 截取路径段
     */
    public static List<String> splitPathSegments(String path) {
        if (StringUtil.isBlank(path)) {
            return Collections.emptyList();
        }
        List<String> pathSegments = new ArrayList<>();
        StringBuilder pathSegmentBuilder = new StringBuilder();
        int length = path.length();
        for (int i = length > 0 && path.charAt(0) != '/' ? 0 : 1; i < length; i++) {
            char c = path.charAt(i);
            if (c == '/') {
                pathSegments.add(pathSegmentBuilder.toString());
                pathSegmentBuilder.setLength(0);
            } else {
                pathSegmentBuilder.append(c);
            }
        }
        pathSegments.add(pathSegmentBuilder.toString());
        return pathSegments;
    }

    /**
     * 将 pathSegment 中的 / 进行编码
     */
    public static String encodePathSlashSign(String segment) {
        boolean allowed = true;
        for (int i = 0; i < segment.length(); i++) {
            if (segment.charAt(i) == '/') {
                allowed = false;
                break;
            }
        }
        if (allowed) {
            return segment;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);
            if (c == '/') {
                builder.append(StringPool.SLASH_SIGN_ENCODE);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * 将 query 参数字符串转为参数列表
     *
     * @param query query 参数字符串
     * @return 参数列表，index=i 为 name，则index = i + 1 为 value
     */
    public static List<String> splitQueryParameters(String query) {
        if (StringUtil.isBlank(query)) {
            return Collections.emptyList();
        }
        int queryLen = query.length();
        List<String> queryParameters = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        for (int i = 0, current = 0, last = queryLen - 1; i < queryLen; i++) {
            boolean isGive = query.charAt(i) == '&';
            if (isGive || (i == last)) {
                // 添加 name
                for (; current < i || (!isGive && current < queryLen); current++) {
                    char c = query.charAt(current);
                    if (c == '=') {
                        queryParameters.add(queryBuilder.toString());
                        queryBuilder.setLength(0);
                        current++;
                        break;
                    }
                    queryBuilder.append(c);
                }
                // 偶数则表明没有添加 name，该键值对没有 '='
                if (queryParameters.size() % 2 == 0) {
                    queryParameters.add(queryBuilder.toString());
                    queryBuilder.setLength(0);
                }
                // 添加 value
                for (; current < i || (!isGive && current < queryLen); current++) {
                    queryBuilder.append(query.charAt(current));
                }
                queryParameters.add(queryBuilder.toString());
                queryBuilder.setLength(0);
                current = i + 1;
            }
        }
        return queryParameters;
    }

    /**
     * 拼接 query 参数
     *
     * @param queryParameters query 参数列表，索引 i 为 name，则索引 = i + 1 为 value
     */
    public static String stitchQueryParameters(List<String> queryParameters) {
        if (CollectionUtil.isEmpty(queryParameters)) {
            return StringPool.EMPTY;
        }
        Assert.isTrue(queryParameters.size() % 2 == 0, () -> new UriException("queryParameters size must be even"));
        StringBuilder builder = new StringBuilder(queryParameters.get(0));
        String firstValue = queryParameters.get(1);
        if (StringUtil.isNotEmpty(firstValue)) {
            builder.append('=').append(firstValue);
        }
        for (int i = 2; i < queryParameters.size(); i += 2) {
            builder.append('&').append(queryParameters.get(i));
            String value = queryParameters.get(i + 1);
            if (StringUtil.isNotEmpty(value)) {
                builder.append('=').append(value);
            }
        }
        return builder.toString();
    }

    /**
     * 对传入组件进行编码
     *
     * @param source      组件
     * @param charset     默认字符集
     * @param blankAsPlus 是否将空格编码为+
     */
    public static String encode(String source, Charset charset, AllowedPredicate allowed, boolean blankAsPlus) {
        if (StringUtil.isEmpty(source)) {
            return source;
        }
        Assert.nonNull(allowed, "AllowedPredicate must be not null");
        Assert.nonNull(charset, "Charset must be not null");
        byte[] sourceBytes = source.getBytes(charset);
        // 检查给定字符串是否合法
        boolean isAllowed = true;
        for (byte sourceByte : sourceBytes) {
            if (!allowed.isAllowed(sourceByte)) {
                isAllowed = false;
                break;
            }
        }
        if (isAllowed) {
            return source;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream(sourceBytes.length);
        for (byte sourceByte : sourceBytes) {
            if (allowed.isAllowed(sourceByte)) {
                stream.write(sourceByte);
            } else if (blankAsPlus && sourceByte == 32) {
                stream.write('+');
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
