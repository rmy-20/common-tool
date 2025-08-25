package cn.zs.tool.okhttp.request;

import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.constant.MediaTypeEnum;
import cn.zs.tool.http.core.exception.HttpException;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

/**
 * okhttp multipart form
 *
 * @author sheng
 */
public class OkHttpMultipartRequest extends OkHttpBaseRequest<OkHttpMultipartRequest> {
    /**
     * 表单
     */
    private final MultipartBody.Builder formBuilder;

    public OkHttpMultipartRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBuilder = new MultipartBody.Builder();
    }

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static OkHttpMultipartRequest create(String url, HttpMethodEnum method) {
        return new OkHttpMultipartRequest(url, method);
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    public OkHttpMultipartRequest addText(String name, String value) {
        formBuilder.addFormDataPart(name, Objects.toString(value, StringPool.EMPTY));
        return this;
    }

    /**
     * 添加文件
     *
     * @param name key
     * @param file 文件
     */
    public OkHttpMultipartRequest addBinary(String name, File file) {
        String fileName = file.getName();
        return addBinary(name, fileName, file);
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param file     文件
     */
    public OkHttpMultipartRequest addBinary(String name, String filename, File file) {
        RequestBody fileBody = RequestBody.create(file, null);
        return addPart(name, filename, fileBody);
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param bytes    文件
     */
    public OkHttpMultipartRequest addBinary(String name, String filename, byte[] bytes) {
        RequestBody fileBody = RequestBody.create(bytes, null);
        return addPart(name, filename, fileBody);
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param stream   文件流
     */
    public OkHttpMultipartRequest addBinary(String name, String filename, InputStream stream) {
        try (Buffer buffer = new Buffer();) {
            buffer.readFrom(stream);
            return addBinary(name, filename, buffer.readByteArray());
        } catch (Exception e) {
            throw new HttpException("okhttp multipart添加文件流异常", e);
        }
    }

    /**
     * 添加文件
     *
     * @param name     key
     * @param filename 文件名
     * @param fileBody 文件
     */
    public OkHttpMultipartRequest addPart(String name, String filename, RequestBody fileBody) {
        this.formBuilder.addFormDataPart(name, filename, fileBody);
        return this;
    }

    @Override
    public void executeBefore() {
        super.executeBefore();
        String contentType = getHeaders().getContentType();
        if (StringUtil.isBlank(contentType)) {
            getHeaders().setContentType(MediaTypeEnum.MULTIPART_FORM_DATA.getMediaType());
        }
    }

    @Override
    public OkHttpMultipartRequest self() {
        return this;
    }

    @Override
    public RequestBody getRequestBody() {
        return formBuilder.setType(MultipartBody.FORM).build();
    }
}
