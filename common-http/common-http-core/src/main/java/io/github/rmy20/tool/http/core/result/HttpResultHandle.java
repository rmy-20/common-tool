package io.github.rmy20.tool.http.core.result;

import io.github.rmy20.tool.core.function.throwing.ThrowingFunc;

import java.io.InputStream;

/**
 * http结果处理器
 *
 * @author sheng
 */
public interface HttpResultHandle<R> extends ThrowingFunc<InputStream, R, Throwable> {

}
