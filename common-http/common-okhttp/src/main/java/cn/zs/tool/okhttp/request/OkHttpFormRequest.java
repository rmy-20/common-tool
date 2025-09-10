package cn.zs.tool.okhttp.request;

import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.constant.HttpMethodEnum;
import cn.zs.tool.http.core.constant.MediaTypeEnum;
import cn.zs.tool.http.core.request.BaseFormRequest;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.util.Objects;

/**
 * okhtp form
 *
 * @author sheng
 */
public class OkHttpFormRequest extends OkHttpBaseRequest<OkHttpFormRequest> implements BaseFormRequest<OkHttpFormRequest> {
    /**
     * 表单
     */
    private final FormBody.Builder formBuilder;

    /**
     * 创建请求
     *
     * @param url    url
     * @param method 方法
     */
    public static OkHttpFormRequest create(String url, HttpMethodEnum method) {
        return new OkHttpFormRequest(url, method);
    }

    public OkHttpFormRequest(String url, HttpMethodEnum method) {
        super(url, method);
        this.formBuilder = new FormBody.Builder();
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    @Override
    public OkHttpFormRequest addText(String name, Object value) {
        formBuilder.add(name, Objects.toString(value, StringPool.EMPTY));
        return this;
    }

    /**
     * 添加表单，并编码
     *
     * @param name  key
     * @param value value
     */
    @Override
    public OkHttpFormRequest addTextEncoded(String name, Object value) {
        formBuilder.addEncoded(name, Objects.toString(value, StringPool.EMPTY));
        return this;
    }

    @Override
    public void executeBefore() {
        super.executeBefore();
        String contentType = getHeaders().getContentType();
        if (StringUtil.isBlank(contentType)) {
            getHeaders().setContentType(MediaTypeEnum.APPLICATION_FORM_URLENCODED.getMediaType());
        }
    }

    @Override
    public OkHttpFormRequest self() {
        return this;
    }

    @Override
    public RequestBody getRequestBody() {
        return formBuilder.build();
    }
}
