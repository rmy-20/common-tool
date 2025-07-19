package cn.zs.tool.core.lang;

/**
 * 异常类
 *
 * @author sheng
 */
public class AssertException extends RuntimeException {
    private static final long serialVersionUID = -491500968690476511L;

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }
}
