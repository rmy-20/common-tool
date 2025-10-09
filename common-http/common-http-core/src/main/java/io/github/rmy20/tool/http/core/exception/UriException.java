package io.github.rmy20.tool.http.core.exception;

/**
 * uri异常
 *
 * @author sheng
 */
public class UriException extends RuntimeException {
    private static final long serialVersionUID = 1337505246012144911L;

    public UriException() {
    }

    public UriException(String message) {
        super(message);
    }

    public UriException(String message, Throwable cause) {
        super(message, cause);
    }
}
