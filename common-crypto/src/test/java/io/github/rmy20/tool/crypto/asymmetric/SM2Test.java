package io.github.rmy20.tool.crypto.asymmetric;

import io.github.rmy20.tool.core.lang.Assert;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

/**
 * 国密 SM2 算法测试
 *
 * @author sheng
 */
class SM2Test {
    @Test
    public void test() {
        // 获取密钥对
        AsymmetricCryptoKeyPairDTO keyPairInfo = SM2Tool.generateKeyPairInfo();
        KeyPair keyPair = keyPairInfo.getKeyPair();
        ECPrivateKeyParameters privateKey = SM2Tool.toEcPrivateKeyParameters(keyPair.getPrivate());
        ECPublicKeyParameters publicKey = SM2Tool.toEcPublicKeyParameters(keyPair.getPublic());

        String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";
        // base64
        String encrypted = SM2Tool.encryptBase64(plainText, publicKey);
        System.out.println("SM2 C1C3C2 base64：" + encrypted);
        Assert.isTrue(plainText.equals(SM2Tool.decryptBase64(encrypted, privateKey)), "SM2 C1C3C2 base64 解密失败");

        encrypted = SM2Tool.encryptBase64(plainText, publicKey, SM2Engine.Mode.C1C2C3);
        System.out.println("SM2 C1C2C3 base64：" + encrypted);
        Assert.isTrue(plainText.equals(SM2Tool.decryptBase64(encrypted, privateKey, SM2Engine.Mode.C1C2C3)), "SM2 C1C2C3 base64 解密失败");

        // hex
        encrypted = SM2Tool.encryptHex(plainText, publicKey);
        System.out.println("SM2 C1C3C2 hex：" + encrypted);
        Assert.isTrue(plainText.equals(SM2Tool.decryptHex(encrypted, privateKey)), "SM2 C1C3C2 hex 解密失败");

        encrypted = SM2Tool.encryptHex(plainText, publicKey, SM2Engine.Mode.C1C2C3);
        System.out.println("SM2 C1C2C3 hex：" + encrypted);
        Assert.isTrue(plainText.equals(SM2Tool.decryptHex(encrypted, privateKey, SM2Engine.Mode.C1C2C3)), "SM2 C1C2C3 hex 解密失败");

        // 签名
        String withId = "qazwsxedcrfvtgb中文哈哈+++===";
        String signed = SM2Tool.signHexWithPlainEncoding(plainText, privateKey, withId);
        System.out.println("SM2 明文编码 hex 签名：" + signed);
        Assert.isTrue(SM2Tool.verifyHexWithPlainEncoding(plainText, publicKey, withId, signed), "SM2 明文编码 hex 验签异常");

        signed = SM2Tool.signHex(plainText, privateKey, withId);
        System.out.println("SM2 hex 签名：" + signed);
        Assert.isTrue(SM2Tool.verifyHex(plainText, publicKey, withId, signed), "SM2 hex 验签异常");

        signed = SM2Tool.signBase64WithPlainEncoding(plainText, privateKey, withId);
        System.out.println("SM2 明文编码 base64 签名：" + signed);
        Assert.isTrue(SM2Tool.verifyBase64WithPlainEncoding(plainText, publicKey, withId, signed), "SM2 明文编码 base64 验签异常");

        signed = SM2Tool.signBase64(plainText, privateKey, withId);
        System.out.println("SM2 base64 签名：" + signed);
        Assert.isTrue(SM2Tool.verifyBase64(plainText, publicKey, withId, signed), "SM2 base64 验签异常");
    }
}
