package io.github.rmy20.tool.crypto.digest;

import io.github.rmy20.tool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * hmac 测试
 *
 * @author sheng
 */
class HMacTest {
    @Test
    void test() {
        String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";
        String key = RandomUtil.generateRandomNumAndLetter(128);
        Arrays.stream(HMacTool.values()).forEach(tool -> {
            SecretKey secretKey = tool.toSecretKey(key.getBytes(StandardCharsets.UTF_8));
            String macHex = tool.macHex(plainText, secretKey);
            System.out.println(tool.getAlgorithm() + "    hex    " + macHex);
            assertEquals(macHex, tool.macHex(new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8)), secretKey),
                    tool.getAlgorithm() + "流mac失败");
            System.out.println();
        });
    }
}
