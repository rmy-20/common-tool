package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.codec.binary.HexUtil;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

/**
 * RSA 测试
 *
 * @author sheng
 */
class RSATest {

    @Test
    void rsa() throws Exception {
        String publicKeyStr = "30818902818100CB01DADCF9DDB89315298D45D56E996E9446FB5AF277CCD9E3370C51E911AADA69E04C6016C323D8E719AEB619F49FEAF8E13AE635781343EFAE52532A23A34B7877AA9E3152913ACADF825BF3E4F6EDED01EA9FB7825B48E46012A663311EDC613CEE74C265A17F5126D5832E75224544CCE518AB083CB1752614400E23FD1B0203010001";
        String privateKeyStr = "3082025C02010002818100CB01DADCF9DDB89315298D45D56E996E9446FB5AF277CCD9E3370C51E911AADA69E04C6016C323D8E719AEB619F49FEAF8E13AE635781343EFAE52532A23A34B7877AA9E3152913ACADF825BF3E4F6EDED01EA9FB7825B48E46012A663311EDC613CEE74C265A17F5126D5832E75224544CCE518AB083CB1752614400E23FD1B020301000102818021E9996A38C37545FCCF082E964CF78CA5708624D74474102208DC2202D154BD4FDA8C67B0E5C0634797DEBBC4F23C94C026BFF41D1286055F50BBA7F0EE2B24F03520B84EB98CD7BD3100510FF0AAA64B34E22DF8DA1EAAF390FD4CEABAEBCD0A5428076CE01E9B0707824D53F49D60405EB4A1E9363FBBA9DF4D43B04BCBE1024100EA4E245A1963212FA5EB6EED7FDC15421B5A8B634E1DDE8C1132BAD5E3403AE02800F9094A8F43853DF374B765D69DB3C8B54E8D2383FF3A3719C1E99AF1D19F024100DDCDD72376A40F64CB84BC7DB1720BB11B6088FA7E577F54DB80C1168E296B3C267FE6EE94900AD88D4BAA63113FBCDADE0BAF98F47A95877AC678FD8E9CFB0502401C5D856D1AF1B34CCF6B672C7D742966AD6512C6DCE824CF142DEE0C82CC7DD431439CE2911232FC687996C1B5247C470D9F557924B990267E11323693B97EC502403B8ABB5B28C927DCBE0996F403038381FAC07265142F76DE17C2BB92CB7CF0ED87C44400FDDEA11ABAEB7A04F8276D1230136103257616D7F4AC7CE44C3975E50241008BF3699C060976AD61D23010D7EA8294A0506535BB8052C853F90930BD4E9510B51C5D60B08133506F97D88DFB1DA6176870D2D6F949A2F0D9010F059FF48B0A";
        String plainText = "Test1234567890中文哈哈---///,,,。。。~~~$$$%%%@@@&*()&*()&*()###===";

        PublicKey rsaPublicKey = RSATool.toPublicKeyAsn1(HexUtil.decodeHex(publicKeyStr));
        PrivateKey rsaPrivateKey = RSATool.toPrivateKey(HexUtil.decodeHex(privateKeyStr));

        Arrays.stream(RSATool.values()).forEach(tool -> {
            String algorithmName = tool.getAlgorithm().getAlgorithmName();
            String encrypted = tool.encryptHex(plainText, rsaPublicKey);
            System.out.println(algorithmName + "    公钥加密Hex密文：    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptHex(encrypted, rsaPrivateKey)), algorithmName + "私钥 hex 解密失败");
            System.out.println();

            encrypted = tool.encryptBase64(plainText, rsaPrivateKey);
            System.out.println(algorithmName + "    私钥加密base64密文：    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptBase64(encrypted, rsaPublicKey)), algorithmName + "公钥 base64 解密失败");
            System.out.println();
        });

        Arrays.stream(RSATool.values()).forEach(tool -> {
            // 构建密钥对
            KeyPair keyPair = RSATool.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            String algorithmName = tool.getAlgorithm().getAlgorithmName();

            String encrypted = tool.encryptHex(plainText, publicKey);
            System.out.println(algorithmName + "    公钥加密Hex密文：    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptHex(encrypted, privateKey)), algorithmName + "私钥 hex 解密失败");
            System.out.println();

            encrypted = tool.encryptBase64(plainText, privateKey);
            System.out.println(algorithmName + "    私钥加密base64密文：    " + encrypted);
            Assert.isTrue(plainText.equals(tool.decryptBase64(encrypted, publicKey)), algorithmName + "公钥 base64 解密失败");
            System.out.println();
        });
    }
}
