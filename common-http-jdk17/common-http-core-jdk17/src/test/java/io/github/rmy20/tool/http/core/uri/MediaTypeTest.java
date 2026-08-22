package io.github.rmy20.tool.http.core.uri;

import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.http.core.MediaType;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author sheng
 */
public class MediaTypeTest {
    @Test
    void test() {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        System.out.println(mediaType);
        Assert.isTrue("application/json;charset=utf-8".equals(mediaType.toString()), "application/json; charset=utf-8 解析失败");
        mediaType = MediaType.parse("application/json");
        System.out.println(mediaType);
        Assert.isTrue("application/json".equals(mediaType.toString()), "application/json 解析失败");
        mediaType = MediaType.parse("*; q=.2");
        System.out.println(mediaType);
        Assert.isTrue("*;q=.2".equals(mediaType.toString()), "*; q=.2 解析失败");
        mediaType = MediaType.parse("*/*; q=.2");
        System.out.println(mediaType);
        Assert.isTrue("*/*;q=.2".equals(mediaType.toString()), "*/*; q=.2 解析失败");
        mediaType = MediaType.parse("application/json; charset=utf-8; q=.2");
        System.out.println(mediaType);
        Assert.isTrue("application/json;charset=utf-8;q=.2".equals(mediaType.toString()), "application/json; charset=utf-8; q=.2 解析失败");
        mediaType = MediaType.create("application", "json", Collections.emptyList(), StandardCharsets.UTF_8);
        System.out.println(mediaType);
        Assert.isTrue("application/json".equals(mediaType.toString()), "application/json 解析失败");
    }
}
