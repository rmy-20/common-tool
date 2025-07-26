package cn.zs.tool.core.codec.binary;

/**
 * 解码异常
 *
 * @author sheng
 */
public class BinaryException extends RuntimeException {
    private static final long serialVersionUID = -3758686618381321689L;

    public BinaryException() {
    }

    public BinaryException(String message) {
        super(message);
    }

    public BinaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
