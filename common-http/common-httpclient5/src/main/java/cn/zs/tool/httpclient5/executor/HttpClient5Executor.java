package cn.zs.tool.httpclient5.executor;

import cn.zs.tool.core.fuction.throwing.ThrowingConsumer;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.HttpHeaders;
import cn.zs.tool.http.core.converter.HttpMsgConverter;
import cn.zs.tool.http.core.execute.HttpExecuteProcessor;
import cn.zs.tool.httpclient5.response.HttpClient5Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.protocol.HttpContext;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * httpclient5 http请求处理
 *
 * @author sheng
 */
@Slf4j
public class HttpClient5Executor<R> extends HttpExecuteProcessor<R> {
    /**
     * 同步请求 #{@link HttpClient}
     */
    private final HttpClient httpClient;

    /**
     * 请求信息
     */
    private final HttpUriRequestBase request;

    /**
     * 请求上下文
     */
    private final HttpContext httpContext;

    /**
     * 响应
     */
    private HttpClient5Response httpClient5Response;

    /**
     * 是否已执行
     */
    private boolean isExecute = false;

    /**
     * 创建#{@link HttpClient5Executor}
     *
     * @param request          请求
     * @param msgConverter     响应结果处理器
     * @param errHandler       异常处理器
     * @param okPredicate      ok响应码判断
     * @param mustHandleResult 是否强制处理结果
     */
    public static <R> HttpClient5Executor<R> create(HttpClient httpClient, HttpUriRequestBase request, HttpContext httpContext,
                                                    HttpMsgConverter<R> msgConverter, Predicate<Integer> okPredicate,
                                                    ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult) {
        return new HttpClient5Executor<R>(httpClient, request, httpContext, msgConverter, okPredicate, errHandler, mustHandleResult);
    }

    public HttpClient5Executor(HttpClient httpClient, HttpUriRequestBase request, HttpContext httpContext, HttpMsgConverter<R> msgConverter,
                               Predicate<Integer> okPredicate, ThrowingConsumer<Throwable, Throwable> errHandler, boolean mustHandleResult) {
        super(msgConverter, okPredicate, errHandler, mustHandleResult);
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient can not be null");
        this.request = Objects.requireNonNull(request, "request can not be null");
        this.httpContext = httpContext;
        execute();
    }

    protected void execute() {
        if (Objects.isNull(httpClient5Response) && !isExecute) {
            isExecute = true;
            try (HttpClient5Response client5Response = HttpClient5Response.create(httpClient.executeOpen(null, request, httpContext))) {
                this.httpClient5Response = client5Response;
                if (isOk() || (mustHandleResult && Objects.nonNull(client5Response.getBody()))) {
                    result = msgConverter.apply(httpClient5Response.getBody());
                }
            } catch (Throwable e) {
                statusMsg = e.getMessage();
                errorHandler(e);
            }
        }
    }

    @Override
    public int getStatus() {
        return Objects.nonNull(httpClient5Response) ? httpClient5Response.getStatus() : -1;
    }

    /**
     * 获取状态码
     */
    @Override
    public String getMessage() {
        return StringUtil.isNotBlank(statusMsg) ? statusMsg : httpClient5Response.getMessage();
    }

    /**
     * 获取请求头
     */
    @Override
    public HttpHeaders getHeaders() {
        return Objects.nonNull(httpClient5Response) ? httpClient5Response.getHeaders() : HttpHeaders.EMPTY_HEADERS;
    }
}
