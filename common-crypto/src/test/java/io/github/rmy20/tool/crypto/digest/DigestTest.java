package io.github.rmy20.tool.crypto.digest;

import io.github.rmy20.tool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 摘要算法测试
 *
 * @author sheng
 */
class DigestTest {

    @Test
    void test() {
        String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";
        String digested = DigestTool.MD5.digestHex(plainText, "加盐", Integer.MAX_VALUE, 3);
        System.out.println("MD5    " + digested);
        Assert.isTrue("07bb4d2c47dc6fb75a1ba33b9722a059".equals(digested), "MD5 hex 验签异常");

        digested = DigestTool.SHA_256.digestHex(plainText, "加盐", 0, 6);
        System.out.println("SHA-256    " + digested);
        Assert.isTrue("cb9f1f25f1522c0745e2b637608e6810b72bf6a35d6408855255dac7cdc3f349".equals(digested), "SHA256 hex 验签异常");

        digested = DigestTool.SM3.digestHex(plainText, "加盐", 2, 6);
        System.out.println("SM3    " + digested);
        Assert.isTrue("c84dd2217d4faa5e153dd133584b7abac8210395b5938dd861d3922fe361dfb3".equals(digested), "SM3 hex 验签异常");

        String salt = digested;
        Arrays.stream(DigestTool.values()).forEach(tool -> {
            String digestHex = tool.digestHex(plainText, salt, 6, 1);
            String digestBase64 = tool.digestBase64(plainText, salt);
            System.out.println(tool + " hex:     " + digestHex);
            System.out.println(tool + " base64:     " + digestBase64);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8));
            String inputStreamHex = tool.digestHex(byteArrayInputStream, salt, 6, 1);
            Assert.isTrue(digestHex.equals(inputStreamHex), tool + "流散列计算hex异常");

            String inputStreamBase64 = tool.digestBase64(new ByteArrayInputStream(plainText.getBytes(StandardCharsets.UTF_8)), salt);
            Assert.isTrue(digestBase64.equals(inputStreamBase64), tool + "流散列计算base64异常");
            System.out.println();
        });
    }
}
