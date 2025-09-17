package cn.zs.tool.http.core.body;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.core.util.RandomUtil;
import cn.zs.tool.http.core.MediaType;
import cn.zs.tool.http.core.body.multipart.BaseMultipart;
import cn.zs.tool.http.core.body.multipart.ByteArrayMultipart;
import cn.zs.tool.http.core.body.multipart.FileMultipart;
import cn.zs.tool.http.core.body.multipart.InputStreamMultipart;
import cn.zs.tool.http.core.body.multipart.StringMultipart;
import cn.zs.tool.http.core.constant.HttpConstant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 表单数据
 *
 * @author sheng
 */
public class MultipartFormBody extends Body {
    /**
     * 表单数据
     */
    private final List<BaseMultipart<?>> multipartList;

    /**
     * 序言
     */
    private String preamble;

    /**
     * 结束符
     */
    private String epilogue;

    /**
     * 边界
     */
    private final String boundary;

    /**
     * 默认字符集
     */
    private Charset defaultCharset = StandardCharsets.UTF_8;

    /**
     * 创建#{@link MultipartFormBody}
     */
    public static MultipartFormBody create() {
        return new MultipartFormBody();
    }

    /**
     * 创建#{@link MultipartFormBody}
     */
    public static MultipartFormBody create(String boundary) {
        return new MultipartFormBody(boundary);
    }

    /**
     * 创建#{@link MultipartFormBody}
     */
    public static MultipartFormBody create(MediaType contentType) {
        return new MultipartFormBody(contentType);
    }

    public MultipartFormBody() {
        this(RandomUtil.generateUuid());
    }

    public MultipartFormBody(String boundary) {
        this(MediaType.MULTIPART_FORM_DATA.withParameters(HttpConstant.BOUNDARY, boundary));
    }

    public MultipartFormBody(MediaType contentType) {
        super(contentType);
        this.multipartList = new ArrayList<>();
        String boundary = contentType.getParameter(HttpConstant.BOUNDARY);
        Assert.isTrue(StringUtil.isNotBlank(boundary), "contentType boundary must not be blank");
        this.boundary = boundary;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        doWriteTo(outputStream, true);
    }

    protected void doWriteTo(OutputStream outputStream, boolean writeBody) throws IOException {
        Charset charset = getDefaultCharset();
        // 序言
        if (this.preamble != null) {
            outputStream.write(StringUtil.encoded(charset, this.preamble));
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
        // form data
        byte[] boundaryEncoded = StringUtil.encoded(charset, boundary);
        for (final BaseMultipart<?> part : multipartList) {
            outputStream.write(HttpConstant.TWO_HYPHENS_ENCODED);
            outputStream.write(boundaryEncoded);
            outputStream.write(HttpConstant.CR_LF_ENCODED);

            // form header
            for (String header : part.getHeaderList()) {
                outputStream.write(StringUtil.encoded(getDefaultCharset(), header));
                outputStream.write(HttpConstant.CR_LF_ENCODED);
            }
            outputStream.write(HttpConstant.CR_LF_ENCODED);

            if (writeBody) {
                part.writeTo(outputStream);
            }
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
        outputStream.write(HttpConstant.TWO_HYPHENS_ENCODED);
        outputStream.write(boundaryEncoded);
        outputStream.write(HttpConstant.TWO_HYPHENS_ENCODED);
        outputStream.write(HttpConstant.CR_LF_ENCODED);
        // 结束符
        if (this.epilogue != null) {
            outputStream.write(StringUtil.encoded(charset, this.epilogue));
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
        outputStream.flush();
    }

    @Override
    public long contentLength() {
        long length = 0L;
        for (BaseMultipart<?> multipart : multipartList) {
            long contentLength = multipart.contentLength();
            if (contentLength >= 0) {
                length += contentLength;
            } else {
                return -1;
            }
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            doWriteTo(outputStream, false);
            return length + outputStream.size();
        } catch (IOException ignore) {
            return -1;
        }
    }

    @Override
    public void close() throws IOException {
        for (BaseMultipart<?> multipart : multipartList) {
            multipart.close();
        }
    }

    /**
     * 设置序言
     */
    public MultipartFormBody preamble(String preamble) {
        this.preamble = preamble;
        return this;
    }

    /**
     * 设置结束符
     */
    public MultipartFormBody epilogue(String epilogue) {
        this.epilogue = epilogue;
        return this;
    }

    /**
     * 设置默认编码字符集
     */
    public MultipartFormBody defaultCharset(Charset defaultCharset) {
        this.defaultCharset = defaultCharset;
        return this;
    }

    /**
     * 获取默认编码字符集
     */
    public Charset getDefaultCharset() {
        return Objects.nonNull(defaultCharset) ? defaultCharset : StandardCharsets.UTF_8;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addText(String name, String value) {
        return addPart(new StringMultipart(name, value, getDefaultCharset()));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addText(String name, String value, Charset charset) {
        return addPart(new StringMultipart(name, value, charset));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, File file) {
        return addPart(new FileMultipart(name, file));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, File file) {
        return addPart(new FileMultipart(name, filename, file));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, byte[] bytes) {
        return addPart(new ByteArrayMultipart(name, filename, bytes));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, InputStream stream) {
        return addPart(new InputStreamMultipart(name, filename, stream));
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addPart(BaseMultipart<?> part) {
        multipartList.add(addPartHeader(Objects.requireNonNull(part, "part must not be null")));
        return this;
    }

    /**
     * 添加表单数据头
     */
    protected BaseMultipart<?> addPartHeader(BaseMultipart<?> part) {
        String fileName = part.getFileName();
        boolean hasFileName = StringUtil.isNotBlank(fileName);
        StringBuilder builder = new StringBuilder("Content-Disposition: form-data; name=\"")
                .append(removeSpecialCharacters(part.getName()))
                .append('"');
        if (hasFileName) {
            builder.append("; filename=\"").append(removeSpecialCharacters(fileName)).append('"');
        }
        part.addHeader(builder.toString());
        if (hasFileName) {
            builder.setLength(0);
            builder.append("Content-Type: ").append(part.getContentType().toString());
            part.addHeader(builder.toString());
        }
        return part;
    }

    /**
     * 去除字符串中的特殊字符
     */
    protected CharSequence removeSpecialCharacters(final CharSequence text) {
        if (Objects.isNull(text)) {
            return null;
        }
        boolean requiresRewrite = false;
        int n = 0;
        for (; n < text.length(); n++) {
            final char ch = text.charAt(n);
            if (isSpecialChar(ch)) {
                requiresRewrite = true;
                break;
            }
        }
        if (!requiresRewrite) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(text, 0, n);
        for (; n < text.length(); n++) {
            final char ch = text.charAt(n);
            if (isSpecialChar(ch)) {
                builder.append(' ');
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    /**
     * 是否是特殊字符
     */
    protected boolean isSpecialChar(int ch) {
        return ch == '\r' || ch == '\n' || ch == '\f' || ch == 11 || ch == '"';
    }
}
