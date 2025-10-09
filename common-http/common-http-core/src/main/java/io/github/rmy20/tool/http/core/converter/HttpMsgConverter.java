package io.github.rmy20.tool.http.core.converter;

import io.github.rmy20.tool.core.function.throwing.ThrowingFunc;

import java.io.InputStream;

/**
 * http消息转换器
 *
 * @author sheng
 */
public interface HttpMsgConverter<R> extends ThrowingFunc<InputStream, R, Throwable> {

}
