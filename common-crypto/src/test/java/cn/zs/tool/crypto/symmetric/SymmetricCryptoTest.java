package cn.zs.tool.crypto.symmetric;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.crypto.constant.ModeEnum;
import cn.zs.tool.crypto.constant.PaddingEnum;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 对称加密测试
 *
 * @author sheng
 */
public class SymmetricCryptoTest {
    @Test
    void test() {
        Arrays.stream(SymmetricCryptoTool.values()).forEach(tool -> {
            // 构造密钥
            SymmetricCryptoSecretKeyDTO secretKeyInfo = tool.generateSecretKeyInfo(SymmetricCryptoKeySizeEnum.KEY_SIZE_256);
            SecretKey secretKey = secretKeyInfo.getSecretKey();
            AlgorithmParameterSpec iv = tool.toAlgorithmParameterSpec(secretKeyInfo.getIvByte());

            String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";
            // 无模无填充
            String encrypted = tool.encryptHex(plainText, secretKey, iv);
            System.out.println(tool.getAlgorithm() + "    无模无填充hex    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptHex(encrypted, secretKey, iv)), tool.getAlgorithm() + " hex解密失败");

            encrypted = tool.encryptBase64(plainText, secretKey, iv);
            System.out.println(tool.getAlgorithm() + "    无模无填充base64    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptBase64(encrypted, secretKey, iv)), tool.getAlgorithm() + " base64解密失败");
            System.out.println();

            if (SymmetricCryptoTool.ChaCha20 == tool) {
                return;
            }

            // 有模有填充
            Stream.of(ModeEnum.CBC, ModeEnum.CFB, ModeEnum.CTR, ModeEnum.CTS, ModeEnum.ECB, ModeEnum.OFB).forEach(mode -> {
                Stream.of(PaddingEnum.PKCS5Padding, PaddingEnum.PKCS7Padding).forEach(padding -> {
                    String encrypt = tool.encryptHex(plainText, secretKey, mode.isNeedIv() ? iv : null, mode, padding);
                    String algorithmName = tool.getAlgorithm().joinAlgorithmName(mode, padding);
                    System.out.println(algorithmName + "    hex   " + encrypt);
                    Assert.isTrue(plainText.equals(tool.decryptHex(encrypt, secretKey, mode.isNeedIv() ? iv : null, mode, padding)), algorithmName + " hex解密失败");

                    encrypt = tool.encryptBase64(plainText, secretKey, mode.isNeedIv() ? iv : null, mode, padding);
                    System.out.println(algorithmName + "    base64   " + encrypt);
                    Assert.isTrue(plainText.equals(tool.decryptBase64(encrypt, secretKey, mode.isNeedIv() ? iv : null, mode, padding)), algorithmName + " base64解密失败");
                    System.out.println();
                });
            });

            // GCM 模式
            if (SymmetricCryptoTool.SM4 == tool || SymmetricCryptoTool.AES == tool) {
                Consumer<Cipher> cipherAdditional = cipher -> cipher.updateAAD("添加额外的加密认证数据".getBytes(StandardCharsets.UTF_8));
                String encrypt = tool.encryptHex(plainText, secretKey, iv, cipherAdditional, ModeEnum.GCM, PaddingEnum.NoPadding);
                String algorithmName = tool.getAlgorithm().joinAlgorithmName(ModeEnum.GCM, PaddingEnum.NoPadding);
                System.out.println(algorithmName + "    hex   " + encrypt);
                Assert.isTrue(plainText.equals(tool.decryptHex(encrypt, secretKey, iv, cipherAdditional, ModeEnum.GCM, PaddingEnum.NoPadding)), algorithmName + " hex解密失败");

                encrypt = tool.encryptBase64(plainText, secretKey, iv, cipherAdditional, ModeEnum.GCM, PaddingEnum.NoPadding);
                System.out.println(algorithmName + "    base64   " + encrypt);
                Assert.isTrue(plainText.equals(tool.decryptBase64(encrypt, secretKey, iv, cipherAdditional, ModeEnum.GCM, PaddingEnum.NoPadding)), algorithmName + " base64解密失败");
                System.out.println();
            }
        });
    }
}
