package io.github.rmy20.tool.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * json、xml测试
 *
 * @author sheng
 */
class JacksonToolTest {
    private static final String json = "{\n" +
            "    \"transaction_id\": \"4200002500202411298300896913\",\n" +
            "    \"nonce_str\": \"GPNKSYXdhhImOfrsSDyzktDZjZAEaw5Y\",\n" +
            "    \"bank_type\": \"CMB_DEBIT\",\n" +
            "    \"openid\": \"oYK8m66l_4te7dGiQ0-f5O-IR8VY\",\n" +
            "    \"sign\": \"AE7D69CF492DEC9CC9DDABF1ACCE7674\",\n" +
            "    \"fee_type\": \"CNY\",\n" +
            "    \"mch_id\": \"1235276502\",\n" +
            "    \"cash_fee\": \"10000\",\n" +
            "    \"out_trade_no\": \"20241129264039087\",\n" +
            "    \"appid\": \"wx84022efb2bbad799\",\n" +
            "    \"total_fee\": \"10000\",\n" +
            "    \"trade_type\": \"APP\",\n" +
            "    \"result_code\": \"SUCCESS\",\n" +
            "    \"time_end\": \"20241129070609\",\n" +
            "    \"is_subscribe\": \"N\",\n" +
            "    \"return_code\": \"SUCCESS\"\n" +
            "}";

    @Test
    void readValue() {
        Map<String, Object> jsonToMap = JsonUtil.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(jsonToMap, "read json error");
        String xml = XmlUtil.toXml(jsonToMap);
        Assertions.assertNotNull(xml, "to xml error");
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
        Map<String, Object> xmlToMap = XmlUtil.readValue(bai, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertEquals(xmlToMap, XmlUtil.readValue(bytes, new TypeReference<Map<String, Object>>() {
        }), "read byte error");
        Assertions.assertEquals(jsonToMap.size(), xmlToMap.size(), "read xml error");
        JsonNode jsonNode = JsonUtil.readTree(json);
        JsonNode xmlNode = XmlUtil.toJsonNode(xmlToMap);
        Assertions.assertEquals(jsonNode, xmlNode, "readTree error");
        Map<String, Object> jsonNodeToMap = XmlUtil.readValue(xmlNode, new TypeReference<Map<String, Object>>() {
        });
        System.out.println(jsonNode);
        Assertions.assertEquals(jsonToMap, jsonNodeToMap, "readValue error");
        Map<String, Object> convertValue = JsonUtil.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(convertValue, "convertValue error");
        System.out.println(JsonUtil.toJson(convertValue));
    }
}
