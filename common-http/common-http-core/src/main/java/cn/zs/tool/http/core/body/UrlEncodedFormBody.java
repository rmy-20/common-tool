package cn.zs.tool.http.core.body;

import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.http.core.MediaType;
import cn.zs.tool.http.core.util.UriUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * form body
 *
 * @author sheng
 */
public class UrlEncodedFormBody extends Body {
    /**
     * queryParameters
     */
    private final List<String> queryParameters = new ArrayList<>();

    /**
     * 最终的表单内容
     */
    private byte[] content;

    /**
     * 编码
     */
    private final Charset charset;

    /**
     * 创建#{@link UrlEncodedFormBody}
     */
    public static UrlEncodedFormBody create() {
        return new UrlEncodedFormBody();
    }

    /**
     * 创建#{@link UrlEncodedFormBody}
     *
     * @param charset 编码
     */
    public static UrlEncodedFormBody create(Charset charset) {
        return new UrlEncodedFormBody(charset);
    }

    /**
     * 创建#{@link UrlEncodedFormBody}
     *
     * @param charset 编码
     */
    public static UrlEncodedFormBody create(Charset charset, MediaType contentType) {
        return new UrlEncodedFormBody(charset, contentType);
    }

    public UrlEncodedFormBody() {
        this(MediaType.APPLICATION_FORM_URLENCODED);
    }

    public UrlEncodedFormBody(Charset charset) {
        this(charset, MediaType.APPLICATION_FORM_URLENCODED);
    }

    public UrlEncodedFormBody(MediaType contentType) {
        this(StandardCharsets.UTF_8, contentType);
    }

    public UrlEncodedFormBody(Charset charset, MediaType contentType) {
        super(contentType);
        this.charset = Objects.requireNonNull(charset, "charset can not be null");
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    public UrlEncodedFormBody addText(String name, Object value) {
        queryParameters.add(name);
        queryParameters.add(Objects.toString(value, StringPool.EMPTY));
        return reset();
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    public UrlEncodedFormBody addTextEncoded(String name, Object value) {
        queryParameters.add(UriUtil.encode(name, charset, UriUtil::isUnreserved, true));
        queryParameters.add(UriUtil.encode(Objects.toString(value, StringPool.EMPTY), charset, UriUtil::isUnreserved, true));
        return reset();
    }

    /**
     * 重置表单内容
     */
    private UrlEncodedFormBody reset() {
        content = null;
        return this;
    }

    /**
     * 获取表单内容
     */
    public byte[] getContent() {
        if (Objects.isNull(content)) {
            content = UriUtil.stitchQueryParameters(queryParameters).getBytes(charset);
        }
        return content;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        if (!queryParameters.isEmpty()) {
            outputStream.write(getContent());
            outputStream.flush();
        }
    }

    @Override
    public long contentLength() {
        return queryParameters.isEmpty() ? 0 : getContent().length;
    }

    @Override
    public boolean repeatable() {
        return true;
    }
}
