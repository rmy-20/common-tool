package cn.zs.tool.http.core.body;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.core.util.RandomUtil;
import cn.zs.tool.http.core.body.multipart.BaseMultipart;
import cn.zs.tool.http.core.body.multipart.ByteArrayMultipart;
import cn.zs.tool.http.core.body.multipart.FileMultipart;
import cn.zs.tool.http.core.body.multipart.InputStreamMultipart;
import cn.zs.tool.http.core.body.multipart.StringMultipart;
import cn.zs.tool.http.core.constant.HttpConstant;
import lombok.Getter;

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
public class MultipartFormBody implements Body {
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
    @Getter
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

    public MultipartFormBody() {
        this(generateBoundary());
    }

    public MultipartFormBody(String boundary) {
        this.multipartList = new ArrayList<>();
        Assert.isTrue(StringUtil.isNotBlank(boundary), "boundary must not be empty");
        this.boundary = boundary;
    }

    /**
     * 生成随机边界
     */
    public static String generateBoundary() {
        return "--------------------" + RandomUtil.generateUuid();
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
            doWriteHeaders(part, outputStream);
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

    protected void doWriteHeaders(BaseMultipart<?> part, OutputStream outputStream) throws IOException {
        String fileName = part.getFileName();
        boolean hasFileName = StringUtil.isNotBlank(fileName);
        StringBuilder builder = new StringBuilder(part.getContentDisposition())
                .append("; name=\"")
                .append(part.getName())
                .append('"');
        if (StringUtil.isNotBlank(fileName)) {
            builder.append("; filename=\"").append(fileName).append('"');
        }
        // Content-Disposition
        outputStream.write(HttpConstant.CONTENT_DISPOSITION_ENCODED);
        outputStream.write(HttpConstant.FIELD_SEP_ENCODED);
        outputStream.write(StringUtil.encoded(getDefaultCharset(), StringUtil.stripLineBreaks(builder.toString())));
        outputStream.write(HttpConstant.CR_LF_ENCODED);
        // Content-Type
        if (hasFileName) {
            outputStream.write(HttpConstant.CONTENT_TYPE_ENCODED);
            outputStream.write(HttpConstant.FIELD_SEP_ENCODED);
            outputStream.write(StringUtil.encoded(getDefaultCharset(), StringUtil.stripLineBreaks(part.getContentType().toString())));
            outputStream.write(HttpConstant.CR_LF_ENCODED);
        }
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
        multipartList.add(new StringMultipart(name, value, getDefaultCharset()));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addText(String name, String value, Charset charset) {
        multipartList.add(new StringMultipart(name, value, charset));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, File file) {
        multipartList.add(new FileMultipart(name, file));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, File file) {
        multipartList.add(new FileMultipart(name, filename, file));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, byte[] bytes) {
        multipartList.add(new ByteArrayMultipart(name, filename, bytes));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addBinary(String name, String filename, InputStream stream) {
        multipartList.add(new InputStreamMultipart(name, filename, stream));
        return this;
    }

    /**
     * 添加表单数据
     */
    public MultipartFormBody addPart(BaseMultipart<?> part) {
        multipartList.add(Objects.requireNonNull(part, "part must not be null"));
        return this;
    }
}
