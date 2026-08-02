package io.github.rmy20.tool.http.core.body;

import io.github.rmy20.tool.core.text.StringPool;
import io.github.rmy20.tool.core.text.StringUtil;
import io.github.rmy20.tool.core.util.RandomUtil;
import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.body.multipart.BaseMultipart;
import io.github.rmy20.tool.http.core.body.multipart.ByteArrayMultipart;
import io.github.rmy20.tool.http.core.body.multipart.FileMultipart;
import io.github.rmy20.tool.http.core.body.multipart.InputStreamMultipart;
import io.github.rmy20.tool.http.core.body.multipart.StringMultipart;
import io.github.rmy20.tool.http.core.constant.HttpConstant;
import io.github.rmy20.tool.http.core.constant.PercentCodecEnum;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
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
     * ISO-8859-1编码器
     */
    private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();

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
        super((contentType = checkMediaType(contentType)));
        this.multipartList = new ArrayList<>();
        this.boundary = contentType.getParameter(HttpConstant.BOUNDARY);
    }

    static MediaType checkMediaType(MediaType contentType) {
        String boundary = contentType.getParameter(HttpConstant.BOUNDARY);
        return StringUtil.isNotBlank(boundary) ? contentType
                : contentType.withParameters(HttpConstant.BOUNDARY, RandomUtil.generateUuid());
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        doWriteTo(outputStream, true);
    }

    protected void doWriteTo(OutputStream outputStream, boolean writeBody) throws IOException {
        // 序言
        if (this.preamble != null) {
            outputStream.write(StringUtil.encoded(this.defaultCharset, this.preamble));
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
        // form data
        byte[] boundaryEncoded = StringUtil.encoded(this.defaultCharset, boundary);
        for (final BaseMultipart<?> part : multipartList) {
            outputStream.write(HttpConstant.TWO_HYPHENS_ENCODED);
            outputStream.write(boundaryEncoded);
            outputStream.write(HttpConstant.CR_LF_ENCODED);

            // form header
            for (String header : part.getHeaderList()) {
                outputStream.write(StringUtil.encoded(this.defaultCharset, header));
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
            outputStream.write(StringUtil.encoded(this.defaultCharset, this.epilogue));
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
        outputStream.flush();
    }

    @Override
    public boolean repeatable() {
        return multipartList.stream().allMatch(BaseMultipart::repeatable);
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
        IOException firstException = null;
        for (BaseMultipart<?> multipart : multipartList) {
            try {
                multipart.close();
            } catch (IOException e) {
                if (Objects.isNull(firstException)) {
                    firstException = e;
                } else {
                    firstException.addSuppressed(e);
                }
            }
        }
        if (Objects.nonNull(firstException)) {
            throw firstException;
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
        this.defaultCharset = Objects.requireNonNull(defaultCharset, "charset must not be null");
        return this;
    }

    // region 添加表单数据

    /**
     * 添加表单数据
     */
    public MultipartFormBody addText(String name, String value) {
        return addPart(new StringMultipart(name, value, this.defaultCharset));
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

    // endregion

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
            String normalFileName = removeSpecialCharacters(fileName);
            builder.append("; filename=\"").append(normalFileName).append('"');
            if (!ISO_8859_1_ENCODER.canEncode(normalFileName)) {
                builder.append("; filename*=\"UTF-8''");
                PercentCodecEnum.RFC5987.encode(builder, normalFileName, StandardCharsets.UTF_8, false);
                builder.append('"');
            }
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
    protected String removeSpecialCharacters(final String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            return StringPool.EMPTY;
        }
        int firstSpecialCharIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (isSpecialChar(text.charAt(i))) {
                firstSpecialCharIndex = i;
                break;
            }
        }
        if (firstSpecialCharIndex == -1) {
            return text;
        }
        StringBuilder builder = new StringBuilder(text.length());
        builder.append(text, 0, firstSpecialCharIndex);
        for (int i = firstSpecialCharIndex; i < text.length(); i++) {
            char ch = text.charAt(i);
            builder.append(isSpecialChar(ch) ? ' ' : ch);
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
