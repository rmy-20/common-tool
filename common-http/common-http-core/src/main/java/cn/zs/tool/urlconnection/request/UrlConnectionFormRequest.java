package cn.zs.tool.urlconnection.request;

import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.body.UrlEncodedFormBody;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.constant.MediaTypeEnum;
import cn.zs.tool.http.core.request.BaseFormRequest;

/**
 * form请求
 *
 * @author sheng
 */
public class UrlConnectionFormRequest extends UrlConnectionBaseRequest<UrlConnectionFormRequest>
        implements BaseFormRequest<UrlConnectionFormRequest> {
    /**
     * 表单数据
     */
    private final UrlEncodedFormBody formBody;

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static UrlConnectionFormRequest create(String url, HttpMethodEnum method) {
        return new UrlConnectionFormRequest(url, method);
    }

    public UrlConnectionFormRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBody = UrlEncodedFormBody.create();
        super.body = this.formBody;
    }

    @Override
    public UrlConnectionFormRequest addText(String name, Object value) {
        formBody.addText(name, value);
        return this;
    }

    @Override
    public UrlConnectionFormRequest addTextEncoded(String name, Object value) {
        formBody.addTextEncoded(name, value);
        return this;
    }

    @Override
    public UrlConnectionFormRequest self() {
        return this;
    }

    @Override
    protected void executeBefore() {
        super.executeBefore();
        String contentType = getHeaders().getContentType();
        if (StringUtil.isBlank(contentType)) {
            getHeaders().setContentType(MediaTypeEnum.APPLICATION_FORM_URLENCODED.getMediaType());
        }
    }
}
