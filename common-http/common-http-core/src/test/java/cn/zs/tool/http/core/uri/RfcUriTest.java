package cn.zs.tool.http.core.uri;

import cn.zs.tool.core.date.DateConstants;
import cn.zs.tool.core.date.DateTool;
import cn.zs.tool.jackson.JsonTool;
import cn.zs.tool.jackson.SimpleModuleConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * RFC 3986 URI 解析测试
 *
 * @author sheng
 */
class RfcUriTest {
    static String uri = "https://user@pass:pass@pass@www.example.com:8080///path///path/to/" +
            "resource/?&query=value&query2=value2&a&b&name=哈哈$age=18$address=广州#fragment广州";
    static JsonTool jsonTool = JsonTool.create(JsonMapper.builder()
            // 忽略无法识别的字段
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // 枚举输出成字符串
            .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
            // 时间格式
            .defaultDateFormat(DateTool.yyyy_MM_dd_HH_mm_ss.getSimpleFormatter())
            .defaultTimeZone(DateConstants.GMT_8_TIME_ZONE)
            // java 8 时间模块
            .addModule(SimpleModuleConfig.JAVA_TIME_MODULE)
            .defaultLocale(Locale.CHINA)
            .enable(SerializationFeature.INDENT_OUTPUT)
            // 大数格式化
            .addModule(SimpleModuleConfig.BIG_NUM_MODULE)
            .findAndAddModules()
            .build());

    @Test
    void test() {
        System.out.println(uri);
        System.out.println();
        System.out.println();
        RfcUri rfcUri = RfcUri.parse(uri).newBuilder().pathSegmentsEncoded("/add1/add2/add3")
                .queryEncoded("friend", "朋友1")
                .queryEncoded("friend", "朋友2")
                .queryEncoded("friend", "朋友3")
                .build();
        System.out.println(jsonTool.toJson(rfcUri));
        System.out.println();
        System.out.println(RfcUriComponentEncoderEnum.PATH_SEGMENT.encode("/", StandardCharsets.UTF_8));
        System.out.println();

        List<String> uriList = Arrays.asList("news:comp.infosystems.www.servers.unix#fragment广州",
                "ftp://ftp.is.co.za/rfc/rfc1808.txt#fragment广州", "ldap://[2001:db8::7]/c=GB?objectClass?one#fragment广州",
                "mailto:<EMAIL>#fragment广州", "news:comp.infosystems.www.servers.unix#fragment广州",
                "tel:+1-816-555-1212#fragment广州", "mailto:user@example.com/haha/vv/aa/bb?query=value#fragment广州",
                "urn:isbn:096139210x#fragment广州", uri);
        uriList.forEach(uri -> {
            System.out.println(uri);
            System.out.println(jsonTool.toJson(RfcUri.parse(uri)));
            System.out.println();
            System.out.println();
        });
    }
}
