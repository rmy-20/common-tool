package cn.zs.tool.core.model;

import cn.zs.tool.core.constant.StatusCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应
 *
 * @author sheng
 */
@Data
@Builder
public class Msg<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 描述信息
     */
    private String msg;

    /**
     * 错误详情
     */
    private String errorDetail;

    /**
     * 响应信息
     */
    private T data;

    /**
     * true 为成功响应
     */
    public boolean success() {
        return StatusCodeEnum.SUCCESS.getCode().equals(code);
    }

    /**
     * true为失败
     */
    public boolean fail() {
        return !success();
    }

    /**
     * 成功响应
     *
     * @param data 响应体信息
     */
    public static <T> Msg<T> success(T data) {
        return Msg.<T>builder().code(StatusCodeEnum.SUCCESS.getCode()).msg(StatusCodeEnum.SUCCESS.getDesc()).data(data).build();
    }

    /**
     * 自定义返回信息，系统未知异常
     *
     * @param errorDetail 返回信息
     */
    public static <T> Msg<T> fail(String errorDetail) {
        return fail(StatusCodeEnum.UNKNOWN_ERROR, errorDetail);
    }

    /**
     * 自定义返回信息，参数不合法
     *
     * @param errorDetail 返回信息
     */
    public static <T> Msg<T> badInput(String errorDetail) {
        return fail(StatusCodeEnum.BAD_INPUT, errorDetail);
    }

    /**
     * 自定义返回信息，无效数据
     *
     * @param errorDetail 返回信息
     */
    public static <T> Msg<T> invalidData(String errorDetail) {
        return fail(StatusCodeEnum.INVALID_DATA, errorDetail);
    }

    /**
     * 自定义返回信息，业务异常
     *
     * @param errorDetail 返回信息
     */
    public static <T> Msg<T> businessError(String errorDetail) {
        return fail(StatusCodeEnum.BUSINESS_ERROR, errorDetail);
    }

    /**
     * 自定义返回信息
     *
     * @param resultCode  {@link StatusCodeEnum 错误码}
     * @param errorDetail 错误详情
     */
    public static <T> Msg<T> fail(StatusCodeEnum resultCode, String errorDetail) {
        return fail(resultCode.getCode(), resultCode.getDesc(), errorDetail);
    }

    /**
     * 复制错误信息
     *
     * @param msg 错误信息
     */
    public static <T> Msg<T> fail(Msg<?> msg) {
        return fail(msg.getCode(), msg.getMsg(), msg.getErrorDetail());
    }

    /**
     * 自定义返回信息
     *
     * @param code 异常码
     * @param msg  返回信息
     */
    public static <T> Msg<T> fail(String code, String msg, String errorDetail) {
        return Msg.<T>builder().code(code).msg(msg).errorDetail(errorDetail).build();
    }
}
