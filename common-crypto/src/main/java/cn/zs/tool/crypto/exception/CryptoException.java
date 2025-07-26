package cn.zs.tool.crypto.exception;

/**
 * 加密相关异常
 *
 * @author sheng
 */
public class CryptoException extends RuntimeException {
    private static final long serialVersionUID = 4771611324610950084L;

    public CryptoException() {
    }

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoException(Throwable cause) {
        super(cause);
    }
}
