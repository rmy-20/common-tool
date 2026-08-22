package io.github.rmy20.tool.http.core.exception;

/**
 * uri解析异常
 *
 * @author sheng
 */
public class UriParseException extends RuntimeException {
    private static final long serialVersionUID = 1337505246012144911L;

    public UriParseException() {
    }

    public UriParseException(String message) {
        super(message);
    }

    public UriParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
