package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.core.codec.binary.Base64Util;
import io.github.rmy20.tool.core.lang.Assert;
import io.github.rmy20.tool.core.util.RandomUtil;
import org.bouncycastle.jcajce.spec.SM2ParameterSpec;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

/**
 * 签名算法测试
 *
 * @author sheng
 */
class SignedTest {

    @Test
    void signTest() {
        String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";

        final String privateKeyStr = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ4fG8vJ0tzu7tjXMSJhyNjlE5B7GkTKMKEQlR6LY3IhIhMFVjuA6W+DqH1VMxl9h3GIM4yCKG2VRZEYEPazgVxa5/ifO8W0pfmrzWCPrddUq4t0Slz5u2lLKymLpPjCzboHoDb8VlF+1HOxjKQckAXq9q7U7dV5VxOzJDuZXlz3AgMBAAECgYABo2LfVqT3owYYewpIR+kTzjPIsG3SPqIIWSqiWWFbYlp/BfQhw7EndZ6+Ra602ecYVwfpscOHdx90ZGJwm+WAMkKT4HiWYwyb0ZqQzRBGYDHFjPpfCBxrzSIJ3QL+B8c8YHq4HaLKRKmq7VUF1gtyWaek87rETWAmQoGjt8DyAQJBAOG4OxsT901zjfxrgKwCv6fV8wGXrNfDSViP1t9r3u6tRPsE6Gli0dfMyzxwENDTI75sOEAfyu6xBlemQGmNsfcCQQCzVWQkl9YUoVDWEitvI5MpkvVKYsFLRXKvLfyxLcY3LxpLKBcEeJ/n5wLxjH0GorhJMmM2Rw3hkjUTJCoqqe0BAkATt8FKC0N2O5ryqv1xiUfuxGzW/cX2jzOwDdiqacTuuqok93fKBPzpyhUS8YM2iss7jj6Xs29JzKMOMxK7ZcpfAkAf21lwzrAu9gEgJhYlJhKsXfjJAAYKUwnuaKLs7o65mtp242ZDWxI85eK1+hjzptBJ4HOTXsfufESFY/VBovIBAkAltO886qQRoNSc0OsVlCi4X1DGo6x2RqQ9EsWPrxWEZGYuyEdODrc54b8L+zaUJLfMJdsCIHEUbM7WXxvFVXNv";
        final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCeHxvLydLc7u7Y1zEiYcjY5ROQexpEyjChEJUei2NyISITBVY7gOlvg6h9VTMZfYdxiDOMgihtlUWRGBD2s4FcWuf4nzvFtKX5q81gj63XVKuLdEpc+btpSyspi6T4ws26B6A2/FZRftRzsYykHJAF6vau1O3VeVcTsyQ7mV5c9wIDAQAB";
        PrivateKey privateKey = SignedTool.SHA256WithRSA.toPrivateKey(Base64Util.decode(privateKeyStr));
        PublicKey publicKey = SignedTool.SHA256WithRSA.toPublicKey(Base64Util.decode(publicKeyStr));

        String sha256Signed = SignedTool.SHA256WithRSA.signBase64(plainText, privateKey);
        System.out.println("SHA256withRSA    base64签名：    " + sha256Signed);
        Assert.isTrue(SignedTool.SHA256WithRSA.verifyBase64(plainText, publicKey, sha256Signed), "SHA256withRSA 验签失败");

        Arrays.stream(SignedTool.values()).forEach(tool -> {
            AsymmetricCryptoKeyPairDTO asymmetricKeyPair = tool.generateKeyPairInfo();
            KeyPair keyPair = asymmetricKeyPair.getKeyPair();

            String signed = tool.signHex(plainText, keyPair.getPrivate());
            System.out.println(tool.getAlgorithm() + "    hex签名：    " + signed);
            Assert.isTrue(tool.verifyHex(plainText, keyPair.getPublic(), signed), tool.getAlgorithm() + " hex验签失败");

            signed = tool.signBase64(plainText, keyPair.getPrivate());
            System.out.println(tool.getAlgorithm() + "    base64签名：    " + signed);
            Assert.isTrue(tool.verifyBase64(plainText, keyPair.getPublic(), signed), tool.getAlgorithm() + " base64验签失败");
            System.out.println();

            if (SignedTool.SM3WithSM2 == tool) {
                AlgorithmParameterSpec parameterSpec = new SM2ParameterSpec(RandomUtil.generateRandomNumAndLetter(10).getBytes(StandardCharsets.UTF_8));

                signed = tool.signHex(plainText, keyPair.getPrivate(), parameterSpec);
                System.out.println(tool.getAlgorithm() + "    带参数 hex签名：    " + signed);
                Assert.isTrue(tool.verifyHex(plainText, keyPair.getPublic(), parameterSpec, signed), tool.getAlgorithm() + " 带参数 hex验签失败");

                signed = tool.signBase64(plainText, keyPair.getPrivate(), parameterSpec);
                System.out.println(tool.getAlgorithm() + "    带参数 base64签名：    " + signed);
                Assert.isTrue(tool.verifyBase64(plainText, keyPair.getPublic(), parameterSpec, signed), tool.getAlgorithm() + " 带参数 base64验签失败");
                System.out.println();
            }
        });
    }
}
