package io.github.rmy20.tool.http.core.uri;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * RFC 3986 URI 解析测试
 *
 * @author sheng
 */
class RfcUriTest {
    static String uri = "https://user@pass:pass@pass@www.example.com:8080///path///path/to/" +
            "resource/?&query=value&query2=value2&a&b&name=哈哈$age=18$address=广州#fragment广州";

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
        System.out.println(rfcUri.uriString());
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
            System.out.println(RfcUri.parse(uri).uriString());
            System.out.println();
            System.out.println();
        });
    }
}
