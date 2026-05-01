package io.github.rmy20.tool.http.core.request;

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
     * @param condition 是否添加
     * @param name      key
     * @param value     value
     */
    default T addText(boolean condition, String name, Object value) {
        return condition ? addText(name, value) : self();
    }

    /**
     * 添加表单
     *
     * @param name  key
     * @param value value
     */
    T addTextEncoded(String name, Object value);

    /**
     * 添加表单
     *
     * @param condition 是否添加
     * @param name      key
     * @param value     value
     */
    default T addTextEncoded(boolean condition, String name, Object value) {
        return condition ? addTextEncoded(name, value) : self();
    }
}
