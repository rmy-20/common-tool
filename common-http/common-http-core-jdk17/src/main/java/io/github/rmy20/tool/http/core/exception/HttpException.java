package io.github.rmy20.tool.http.core.exception;

/**
 * http 相关异常
 *
 * @author sheng
 */
public class HttpException extends RuntimeException {
    private static final long serialVersionUID = -7038829084080778358L;

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
