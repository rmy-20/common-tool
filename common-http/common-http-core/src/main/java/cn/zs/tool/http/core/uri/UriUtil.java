package cn.zs.tool.http.core.uri;

import cn.zs.tool.core.text.CharacterUtil;
import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;

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
     * 截取 path
     */
    public static List<String> splitPathSegments(String path) {
        if (StringUtil.isBlank(path)) {
            return Collections.emptyList();
        }
        List<String> pathSegments = new ArrayList<>();
        StringBuilder pathSegmentBuilder = new StringBuilder();
        int length = path.length(), lastIndex = length - 1;
        for (int i = 0; i < length; i++) {
            char c = path.charAt(i);
            if (c == '/') {
                if (i == 0) {
                    continue;
                }
                pathSegments.add(pathSegmentBuilder.toString());
                pathSegmentBuilder.setLength(0);
                if (i == lastIndex) {
                    pathSegments.add(StringPool.EMPTY);
                }
            } else {
                pathSegmentBuilder.append(c);
            }
        }
        if (pathSegmentBuilder.length() > 0) {
            pathSegments.add(pathSegmentBuilder.toString());
        }
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
}
