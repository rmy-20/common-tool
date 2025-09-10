package cn.zs.tool.urlconnection.request;

import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.body.MultipartFormBody;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.constant.MediaTypeEnum;
import cn.zs.tool.http.core.request.BaseMultipartRequest;

import java.io.File;
import java.io.InputStream;

/**
 * url connection multipart request
 *
 * @author sheng
 */
public class UrlConnectionMultipartRequest extends UrlConnectionBaseRequest<UrlConnectionMultipartRequest>
        implements BaseMultipartRequest<UrlConnectionMultipartRequest> {
    /**
     * 表单数据
     */
    private final MultipartFormBody formBody;

    /**
     * 创建#{@link UrlConnectionMultipartRequest}
     */
    public static UrlConnectionMultipartRequest create(String url, HttpMethodEnum method) {
        return new UrlConnectionMultipartRequest(url, method);
    }

    public UrlConnectionMultipartRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBody = MultipartFormBody.create();
        super.body = this.formBody;
    }

    @Override
    public UrlConnectionMultipartRequest addText(String name, String value) {
        return null;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, File file) {
        return null;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, File file) {
        return null;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, byte[] bytes) {
        return null;
    }

    @Override
    public UrlConnectionMultipartRequest addBinary(String name, String filename, InputStream stream) {
        return null;
    }

    @Override
    public UrlConnectionMultipartRequest self() {
        return this;
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        String contentType = getHeaders().getContentType();
        if (StringUtil.isBlank(contentType)) {
            getHeaders().setContentType(MediaTypeEnum.MULTIPART_FORM_DATA.getMediaType());
        }
    }
}
