package io.github.rmy20.tool.httpclient5;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.rmy20.tool.core.date.DateTool;
import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.util.RandomUtil;
import io.github.rmy20.tool.http.core.MediaType;
import io.github.rmy20.tool.http.core.converter.JsonHttpMsgConverter;
import io.github.rmy20.tool.http.core.execute.BaseExecutor;
import io.github.rmy20.tool.http.core.request.BaseMultipartRequest;
import io.github.rmy20.tool.http.core.request.BaseRequestTool;
import io.github.rmy20.tool.httpclient5.request.HttpClient5RequestTool;
import io.github.rmy20.tool.jackson.JsonTool;
import org.apache.hc.client5.http.config.RequestConfig;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * httpclient5测试类
 *
 * @author sheng
 */
class HttpClient5RequestTest {
    private static final boolean skip = true;
    private static final String uri = "http://127.0.0.1:6021/";
    private static final Logger log = LoggerFactory.getLogger(HttpClient5RequestTest.class);
    private static final BaseRequestTool tool = HttpClient5RequestTool.create()
            .requestConfig(RequestConfig.custom().setResponseTimeout(30, TimeUnit.SECONDS).build());

    @Test
    void get() throws Exception {
        if (skip) {
            return;
        }
        BaseExecutor<String> executor = tool.get(uri).paths("/test/get")
                .setContentType(MediaType.APPLICATION_JSON)
                .queryEncoded("name", "小明")
                .queryEncoded("age", 18)
                .queryEncoded("sex", "男")
                .queryEncoded("address", "中国")
                .stringExecutor().execute();
        Assert.isTrue(executor.isOk(), "http client get请求失败");
        System.out.println("http client get结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void getAsync() throws Exception {
        if (skip) {
            return;
        }
        CompletableFuture<? extends BaseExecutor<String>> future = tool.get(uri).paths("/test/get")
                .setContentType(MediaType.APPLICATION_JSON)
                .queryEncoded("name", "小明")
                .queryEncoded("age", 18)
                .queryEncoded("sex", "男")
                .queryEncoded("address", "中国")
                .stringExecutor().executeAsync();

        for (int i = 0; i < 20; i++) {
            log.info("{}", i);
            TimeUnit.MILLISECONDS.sleep(200L);
        }

        BaseExecutor<String> executor = future.join();
        Assert.isTrue(executor.isOk(), "http client get 异步请求失败");
        System.out.println("http client get 异步结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void post() {
        if (skip) {
            return;
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("appId", "appId.h5");
        map.put("method", "apps.digit.line");
        map.put("signType", "NA");
        map.put("timestamp", Instant.now().toEpochMilli());
        map.put("sessionId", "");
        map.put("version", "1.0");
        BaseExecutor<Map<String, Object>> executor = tool.post(uri).pathsEncoded("/test/post")
                .setContentType(MediaType.APPLICATION_JSON_UTF8)
                .body(JsonTool.JSON_TOOL.toJson(map))
                .executor(JsonHttpMsgConverter.create(JsonTool.JSON_TOOL, new TypeReference<Map<String, Object>>() {
                })).mustHandleResult(true).execute();
        Assert.isTrue(executor.isOk(), "http client post请求失败");
        System.out.println("http client post结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void postAsync() throws Exception {
        if (skip) {
            return;
        }
        Map<String, Object> map = new HashMap<>(16);
        map.put("appId", "appId.h5");
        map.put("method", "apps.digit.line");
        map.put("signType", "NA");
        map.put("timestamp", Instant.now().toEpochMilli());
        map.put("sessionId", "");
        map.put("version", "1.0");
        CompletableFuture<? extends BaseExecutor<Map<String, Object>>> future = tool.post(uri).pathsEncoded("/test/post")
                .setContentType(MediaType.APPLICATION_JSON_UTF8)
                .body(JsonTool.JSON_TOOL.toJson(map))
                .jsonExecutor(JsonHttpMsgConverter.create(JsonTool.JSON_TOOL, new TypeReference<Map<String, Object>>() {
                })).executeAsync();
        for (int i = 0; i < 20; i++) {
            log.info("{}", i);
            TimeUnit.MILLISECONDS.sleep(200L);
        }
        BaseExecutor<Map<String, Object>> executor = future.join();
        Assert.isTrue(executor.isOk(), "http client post 异步请求失败");
        System.out.println("http client post 异步结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void download() {
        if (skip) {
            return;
        }
        BaseExecutor<Boolean> executor = tool.get(uri).paths("download")
                .setContentType(MediaType.APPLICATION_OCTET_STREAM)
                .downloadExecutor(new File("/opt/httpclient/" + "SM2公私钥对.txt")).execute();
        Assert.isTrue(executor.isOk() && executor.get(), "http client 下载文件失败");
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void downloadAsync() throws Exception {
        if (skip) {
            return;
        }
        CompletableFuture<? extends BaseExecutor<Boolean>> future = tool.get(uri).paths("download")
                .setContentType(MediaType.APPLICATION_OCTET_STREAM)
                .downloadExecutor(new File("/opt/httpclient/async/" + "SM2公私钥对.txt")).executeAsync();
        for (int i = 0; i < 20; i++) {
            log.info("{}", i);
            TimeUnit.MILLISECONDS.sleep(200L);
        }
        BaseExecutor<Boolean> executor = future.join();
        Assert.isTrue(executor.isOk() && executor.get(), "http client 异步下载文件失败");
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void form() {
        if (skip) {
            return;
        }
        BaseExecutor<String> executor = tool.form(uri).paths("/test/form")
                .addTextEncoded("access_key", RandomUtil.generateUuidSimple())
                .addTextEncoded("biz_content", RandomUtil.generateUuidSimple())
                .addTextEncoded("format", "json")
                .addTextEncoded("request_id", RandomUtil.generateByTimeAndLetter(16, DateTool.yyyyMMddHHmmss))
                .addTextEncoded("timestamp", Instant.now().toEpochMilli())
                .addTextEncoded("version", "版本号")
                .addTextEncoded("sign", "MEUCIQC5er362TvTWrTpoZzvYeHldXTJtEIZpZJOea6nDseHngIgV61eTm/R7XLOd4/9lWV9lbRQJFEhTxASWfWkqE65F5c=")
                .stringExecutor().execute();
        Assert.isTrue(executor.isOk(), "http client form 请求失败");
        System.out.println("http client form结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void formAsync() throws Exception {
        if (skip) {
            return;
        }
        CompletableFuture<? extends BaseExecutor<String>> future = tool.form(uri).paths("/test/form")
                .addTextEncoded("access_key", RandomUtil.generateUuidSimple())
                .addTextEncoded("biz_content", RandomUtil.generateUuidSimple())
                .addTextEncoded("format", "json")
                .addTextEncoded("request_id", RandomUtil.generateByTimeAndLetter(16, DateTool.yyyyMMddHHmmss))
                .addTextEncoded("timestamp", Instant.now().toEpochMilli())
                .addTextEncoded("version", "版本号")
                .addTextEncoded("sign", "MEUCIQC5er362TvTWrTpoZzvYeHldXTJtEIZpZJOea6nDseHngIgV61eTm/R7XLOd4/9lWV9lbRQJFEhTxASWfWkqE65F5c=")
                .stringExecutor().executeAsync();
        for (int i = 0; i < 20; i++) {
            log.info("{}", i);
            TimeUnit.MILLISECONDS.sleep(200L);
        }
        BaseExecutor<String> executor = future.join();
        Assert.isTrue(executor.isOk(), "http client form 异步请求失败");
        System.out.println("http client form 异步结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void upload() throws Exception {
        if (skip) {
            return;
        }
        Map<String, String> base64FileMap = new HashMap<>(8);
        base64FileMap.put("t2.txt", "5aSp");
        base64FileMap.put("SM2公私钥对.txt",
                "U00y56eB6ZKlDQoJNTJjYjI4MjE3OTI2Yjk1N2UzNThhMTk5MmJiYTY3NzE3N2JhNzAyZTEzNGM1YzhkODJjNmNjMWY5MDcwZmQ3Nw0KU00y5YWs6ZKlDQoJMDQ4ODk5MTRmMjQ2N2E0ZjdiZjgyMTA1ZTMzOWVhYzc5NGM2YTIxZjZjNWEwOGMzYTVlMTgwYzdhY2JmYjc0NTdmYWY3OTQyNjc1MDcxNWY1ZjdlZTg2ZDgwZDBmYjQwMjJlZGY2OWQ1MmYxYmMyZTIyOWI0N2ZiOTQyYzRkNzdjMg0KDQpTTTLnp4HpkqUNCgkwMDk0NTUxMjYzYmEwMmM0YWUyOGZiYzQzYzA4YWQ3NTA3ZjhjY2Y1MjI5ZTcwZmNiNDk5OTc1N2NjODA2ZDBkZmYNClNNMuWFrOmSpQ0KCTA0MzY2YjZhZDNlODMwYmM0Yjk2YmIzMThiMTJiM2RkNmMwZjFjNzk4NmY0MzNjMTVlN2IwNjUzYTk4ZjM3YTA0YWZmNDA2NWUwNGE4MmZmZTQ2YzJmNzAwYzk0MTcwM2I5ODY0ZTI1YzNlNTBkZDY0ZGFhNjk5ZjVkZmVhOWVmYzQNCg==");
        base64FileMap.put("test.html", "PCFET0NUWVBFIGh0bWw+DQo8aHRtbCBsYW5nPSJlbiI+DQo8aGVhZD4NCiAgICA8bWV0YSBjaGFyc2V0PSJVVEYtOCI" +
                "+DQogICAgPHRpdGxlPlRpdGxlPC90aXRsZT4NCjwvaGVhZD4NCjxib2R5Pg0KICAxDQo8L2JvZHk+DQo8L2h0bWw+");
        base64FileMap.put("t.docx", "UEsDBAoAAAAAAIdO4kAAAAAAAAAAAAAAAAAJAAAAZG9jUHJvcHMvUEsDBBQAAAAIAIdO4kC1" +
                "/ZeoWwEAAHECAAAQAAAAZG9jUHJvcHMvYXBwLnhtbJ1Ry26DMBC8V+o/IO5gQ0JeMkQpaU5VGwnaHCPLbIJVsC3biZK/rwlVQq" +
                "+97czujmZ3yPLSNt4ZtOFSpH4UYt8DwWTFxTH1P8tNMPM9Y6moaCMFpP4VjL/Mnp" +
                "/IVksF2nIwnpMQJvVra9UCIcNqaKkJXVu4zkHqlloH9RHJw4EzWEt2akFYFGM8QXCxICqoAnUX9HvFxdn+V7SSrPNnvsqrcoYzUkKrGmohe" +
                "+/sNGElbUvQnSVbegSTRQT1BdlJXd1wX5C8ppoy6/7UDQ0QeePCbWKC+sIpaXrUVNU3coBIKS1tSt5CN/0ApGC0gdzZzQ60MUDQg+jUv82nKuW6M//b/0sOvO24rQtFWX" +
                "/Kw+WAJyulGs6odXlnu23hfdwy2Udx6MIPo/l0iveb6HUUT1/yIJ7M82A8SqpgFSVxgJM8GeMZxnG" +
                "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");
        BaseMultipartRequest<?> multipartRequest = tool.multipart(uri).path("upload").path("multi")
                .addText("bucketName", "local")
                .addText("oosType", "qn")
                .addText("md5", RandomUtil.generateUuidSimple())
                .addText("format", "哈哈faga---===");
        base64FileMap.forEach((k, v) -> multipartRequest.addBinary("file", k, Base64.getDecoder().decode(v.getBytes(StandardCharsets.UTF_8))));
        BaseExecutor<String> executor = multipartRequest.stringExecutor().execute();
        Assert.isTrue(executor.isOk(), "http client上传失败");
        System.out.println("http client上传结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }

    @Test
    void uploadAsync() throws Exception {
        if (skip) {
            return;
        }
        Map<String, String> base64FileMap = new HashMap<>(8);
        base64FileMap.put("t2.txt", "5aSp");
        base64FileMap.put("SM2公私钥对.txt",
                "U00y56eB6ZKlDQoJNTJjYjI4MjE3OTI2Yjk1N2UzNThhMTk5MmJiYTY3NzE3N2JhNzAyZTEzNGM1YzhkODJjNmNjMWY5MDcwZmQ3Nw0KU00y5YWs6ZKlDQoJMDQ4ODk5MTRmMjQ2N2E0ZjdiZjgyMTA1ZTMzOWVhYzc5NGM2YTIxZjZjNWEwOGMzYTVlMTgwYzdhY2JmYjc0NTdmYWY3OTQyNjc1MDcxNWY1ZjdlZTg2ZDgwZDBmYjQwMjJlZGY2OWQ1MmYxYmMyZTIyOWI0N2ZiOTQyYzRkNzdjMg0KDQpTTTLnp4HpkqUNCgkwMDk0NTUxMjYzYmEwMmM0YWUyOGZiYzQzYzA4YWQ3NTA3ZjhjY2Y1MjI5ZTcwZmNiNDk5OTc1N2NjODA2ZDBkZmYNClNNMuWFrOmSpQ0KCTA0MzY2YjZhZDNlODMwYmM0Yjk2YmIzMThiMTJiM2RkNmMwZjFjNzk4NmY0MzNjMTVlN2IwNjUzYTk4ZjM3YTA0YWZmNDA2NWUwNGE4MmZmZTQ2YzJmNzAwYzk0MTcwM2I5ODY0ZTI1YzNlNTBkZDY0ZGFhNjk5ZjVkZmVhOWVmYzQNCg==");
        base64FileMap.put("test.html", "PCFET0NUWVBFIGh0bWw+DQo8aHRtbCBsYW5nPSJlbiI+DQo8aGVhZD4NCiAgICA8bWV0YSBjaGFyc2V0PSJVVEYtOCI" +
                "+DQogICAgPHRpdGxlPlRpdGxlPC90aXRsZT4NCjwvaGVhZD4NCjxib2R5Pg0KICAxDQo8L2JvZHk+DQo8L2h0bWw+");
        base64FileMap.put("t.docx", "UEsDBAoAAAAAAIdO4kAAAAAAAAAAAAAAAAAJAAAAZG9jUHJvcHMvUEsDBBQAAAAIAIdO4kC1" +
                "/ZeoWwEAAHECAAAQAAAAZG9jUHJvcHMvYXBwLnhtbJ1Ry26DMBC8V+o/IO5gQ0JeMkQpaU5VGwnaHCPLbIJVsC3biZK/rwlVQq" +
                "+97czujmZ3yPLSNt4ZtOFSpH4UYt8DwWTFxTH1P8tNMPM9Y6moaCMFpP4VjL/Mnp" +
                "/IVksF2nIwnpMQJvVra9UCIcNqaKkJXVu4zkHqlloH9RHJw4EzWEt2akFYFGM8QXCxICqoAnUX9HvFxdn+V7SSrPNnvsqrcoYzUkKrGmohe" +
                "+/sNGElbUvQnSVbegSTRQT1BdlJXd1wX5C8ppoy6/7UDQ0QeePCbWKC+sIpaXrUVNU3coBIKS1tSt5CN/0ApGC0gdzZzQ60MUDQg+jUv82nKuW6M//b/0sOvO24rQtFWX" +
                "/Kw+WAJyulGs6odXlnu23hfdwy2Udx6MIPo/l0iveb6HUUT1/yIJ7M82A8SqpgFSVxgJM8GeMZxnG" +
                "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");
        BaseMultipartRequest<?> multipartRequest = tool.multipart(uri).path("upload").path("multi")
                .addText("bucketName", "local")
                .addText("oosType", "qn")
                .addText("md5", RandomUtil.generateUuidSimple())
                .addText("format", "哈哈faga---===");
        base64FileMap.forEach((k, v) -> multipartRequest.addBinary("file", k, Base64.getDecoder().decode(v.getBytes(StandardCharsets.UTF_8))));
        CompletableFuture<? extends BaseExecutor<String>> future = multipartRequest.stringExecutor().executeAsync();
        for (int i = 0; i < 20; i++) {
            log.info("{}", i);
            TimeUnit.MILLISECONDS.sleep(200L);
        }
        BaseExecutor<String> executor = future.join();
        Assert.isTrue(executor.isOk(), "http client 异步上传失败");
        System.out.println("http client 异步上传结果：" + executor.get());
        executor.getHeaders().forEach((name, value) -> System.out.println("header --> " + name + "：" + value));
    }
}
