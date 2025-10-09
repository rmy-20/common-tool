package io.github.rmy20.tool.http.core;

import io.github.rmy20.tool.core.collection.CollectionUtil;
import io.github.rmy20.tool.core.map.MultiValueMap;
import io.github.rmy20.tool.core.map.MultiValueMapAdapter;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.http.core.constant.HttpHeaderConstant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 请求头
 *
 * @author sheng
 */
public class HttpHeaders implements MultiValueMap<String, String>, Serializable {
    private static final long serialVersionUID = 7768939069899008802L;

    /**
     * 空请求体
     */
    public static final HttpHeaders EMPTY_HEADERS = new HttpHeaders(MultiValueMapAdapter.empty());

    /**
     * 实际存储的请求头
     */
    private final MultiValueMap<String, String> headers;

    /**
     * 创建一个空的请求头
     */
    public static HttpHeaders create() {
        return new HttpHeaders();
    }

    public HttpHeaders() {
        this(MultiValueMapAdapter.create(new LinkedHashMap<>(8)));
    }

    public HttpHeaders(MultiValueMap<String, String> headers) {
        this.headers = Objects.requireNonNull(headers, "MultiValueMap 不允许为空");
    }

    /**
     * 设置Content-Type
     */
    public void setContentType(MediaType contentType) {
        setContentType(contentType.getMediaType());
    }

    /**
     * 设置Content-Type
     */
    public void setContentType(String contentType) {
        if (StringUtil.isNotBlank(contentType)) {
            headers.set(HttpHeaderConstant.Content_Type, contentType);
        } else {
            headers.remove(HttpHeaderConstant.Content_Type);
        }
    }

    /**
     * 获取Content-Type
     */
    public String getContentType() {
        return headers.getFirst(HttpHeaderConstant.Content_Type);
    }

    /**
     * 设置Content-Length
     */
    public void setContentLength(long contentLength) {
        headers.set(HttpHeaderConstant.Content_Length, String.valueOf(contentLength));
    }

    /**
     * 获取Content-Length
     */
    public long getContentLength() {
        String length = getFirst(HttpHeaderConstant.Content_Length);
        return StringUtil.isNotBlank(length) ? Long.parseLong(length) : -1;
    }

    /**
     * 设置Accept
     */
    public void setAccept(String... accepts) {
        remove(HttpHeaderConstant.Accept);
        if (Objects.nonNull(accepts) && accepts.length > 0) {
            List<String> acceptList = Arrays.stream(accepts).filter(StringUtil::isNotBlank).collect(Collectors.toList());
            if (!acceptList.isEmpty()) {
                put(HttpHeaderConstant.Accept, acceptList);
            }
        }
    }

    /**
     * 设置Accept
     */
    public void setAccept(MediaType... accepts) {
        remove(HttpHeaderConstant.Accept);
        if (Objects.nonNull(accepts) && accepts.length > 0) {
            List<String> acceptList = Arrays.stream(accepts).filter(Objects::nonNull).map(MediaType::getMediaType).collect(Collectors.toList());
            if (!acceptList.isEmpty()) {
                put(HttpHeaderConstant.Accept, acceptList);
            }
        }
    }

    /**
     * 设置Accept
     */
    public void setAccept(List<String> accepts) {
        remove(HttpHeaderConstant.Accept);
        if (CollectionUtil.isNotEmpty(accepts)) {
            List<String> acceptList = accepts.stream().filter(StringUtil::isNotBlank).collect(Collectors.toList());
            if (!acceptList.isEmpty()) {
                put(HttpHeaderConstant.Accept, acceptList);
            }
        }
    }

    /**
     * 获取Accept
     */
    public List<String> getAccept() {
        return headers.getOrEmpty(HttpHeaderConstant.Accept);
    }

    @Override
    public String getFirst(String key) {
        return headers.getFirst(key);
    }

    @Override
    public void add(String key, String value) {
        headers.add(key, value);
    }

    @Override
    public void addAll(String key, List<String> values) {
        headers.addAll(key, values);
    }

    @Override
    public void set(String key, String value) {
        headers.set(key, value);
    }

    @Override
    public List<String> getOrEmpty(String key) {
        return headers.getOrEmpty(key);
    }

    @Override
    public int size() {
        return headers.size();
    }

    @Override
    public boolean isEmpty() {
        return headers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return key instanceof String && headers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return headers.containsValue(value);
    }

    @Override
    public List<String> get(Object key) {
        return headers.get(key);
    }

    @Override
    public List<String> put(String key, List<String> value) {
        return headers.put(key, value);
    }

    @Override
    public List<String> remove(Object key) {
        return headers.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<String>> m) {
        headers.putAll(m);
    }

    @Override
    public void clear() {
        headers.clear();
    }

    @Override
    public Set<String> keySet() {
        return headers.keySet();
    }

    @Override
    public Collection<List<String>> values() {
        return headers.values();
    }

    @Override
    public Set<Map.Entry<String, List<String>>> entrySet() {
        return headers.entrySet();
    }

    @Override
    public String toString() {
        return headers.toString();
    }
}
