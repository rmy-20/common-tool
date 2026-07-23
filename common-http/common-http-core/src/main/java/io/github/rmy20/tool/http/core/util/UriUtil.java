package io.github.rmy20.tool.http.core.util;

import io.github.rmy20.tool.core.collection.CollectionUtil;
import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.exception.UriException;

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
        boolean inValue = false;
        for (int i = 0; i < queryLen; i++) {
            char ch = query.charAt(i);
            if (ch == '&') {
                queryParameters.add(queryBuilder.toString());
                queryBuilder.setLength(0);
                inValue = false;
                if (queryParameters.size() % 2 != 0) {
                    queryParameters.add(StringPool.EMPTY);
                }
            } else if (ch == '=' && !inValue) {
                queryParameters.add(queryBuilder.toString());
                queryBuilder.setLength(0);
                inValue = true;
            } else {
                queryBuilder.append(ch);
            }
        }

        if (queryBuilder.length() > 0) {
            queryParameters.add(queryBuilder.toString());
            queryBuilder.setLength(0);
        }
        if (queryParameters.size() % 2 != 0) {
            queryParameters.add(StringPool.EMPTY);
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
}
