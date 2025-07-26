package cn.zs.tool.crypto.util;

import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.crypto.asymmetric.RSATool;
import cn.zs.tool.crypto.constant.CryptoConstant;
import cn.zs.tool.crypto.constant.ProviderEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * pem 证书
 *
 * @author sheng
 */
public class PemUtil {
    /**
     * PEM 格式匹配
     */
    public static final Pattern PEM_PATTERN = Pattern.compile("-----BEGIN(.*)-----([^-]*)-----END(.*)-----");

    /**
     * 加载 PEM 证书
     */
    public static String loadPem(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder builder = new StringBuilder();
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                builder.append(line);
            }
            return loadPem(builder.toString());
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 加载 PEM 证书内容
     */
    public static String loadPem(String pemContent) {
        Matcher matcher = PEM_PATTERN.matcher(pemContent);
        Assert.isTrue(matcher.find(), "PEM格式不匹配");
        String content = matcher.group(2);
        return StringUtil.replaceAll(content, StringPool.WHITE_SPACE_REGEX, StringPool.EMPTY);
    }

    /**
     * RSA PUBLIC KEY
     *
     * @param publicKeyArr 公钥
     */
    public static PublicKey loadRsaPublicKey(byte[] publicKeyArr) {
        try {
            RSAPublicKey rsaPublicKey = RSAPublicKey.getInstance(publicKeyArr);
            SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(CryptoConstant.RSA_ALGORITHM_IDENTIFIER, rsaPublicKey);
            return RSATool.toPublicKey(subjectPublicKeyInfo.getEncoded());
        } catch (IOException e) {
            throw new CryptoException("加载 RSA PUBLIC KEY 异常", e);
        }
    }

    /**
     * RSA PRIVATE KEY
     *
     * @param privateKeyArr 私钥
     */
    public static PrivateKey loadRsaPrivateKey(byte[] privateKeyArr) {
        try {
            ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(privateKeyArr);
            RSAPrivateKey rsaPrivateKey = RSAPrivateKey.getInstance(asn1Sequence);
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(CryptoConstant.RSA_ALGORITHM_IDENTIFIER, rsaPrivateKey);
            return RSATool.toPrivateKey(privateKeyInfo.getEncoded());
        } catch (IOException e) {
            throw new CryptoException("加载 RSA PRIVATE KEY 异常", e);
        }
    }

    /**
     * EC PRIVATE KEY
     *
     * @param privateKeyArr 私钥
     */
    public static ECPrivateKey loadEcPrivateKey(byte[] privateKeyArr) {
        try {
            ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(privateKeyArr);
            org.bouncycastle.asn1.sec.ECPrivateKey ecPrivateKey = org.bouncycastle.asn1.sec.ECPrivateKey.getInstance(asn1Sequence);
            AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, ecPrivateKey.getParametersObject());
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, ecPrivateKey);
            return KeyUtil.loadPkcs8EcPrivateKey(privateKeyInfo.getEncoded(), ProviderEnum.BC);
        } catch (IOException e) {
            throw new CryptoException("加载 EC PRIVATE KEY 异常", e);
        }
    }
}
