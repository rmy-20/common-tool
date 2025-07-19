package cn.zs.tool.http.core.converter;

import cn.zs.tool.core.fuction.throwing.ThrowingFunc;

import java.io.InputStream;

/**
 * http消息转换器
 *
 * @author sheng
 */
public interface HttpMsgConverter<R> extends ThrowingFunc<InputStream, R, Throwable> {

}
