package cn.zs.tool.http.core.request;

/**
 * form 请求
 *
 * @author sheng
 */
public interface BaseFormRequest<T extends BaseFormRequest<T>> extends BaseRequest<T> {
    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    T addText(String name, Object value);

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    T addTextEncoded(String name, Object value);
}
