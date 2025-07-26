package cn.zs.tool.crypto.asymmetric;

import cn.zs.tool.crypto.common.BaseCrypto;
import cn.zs.tool.crypto.constant.AlgorithmEnum;
import cn.zs.tool.crypto.exception.CryptoException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.DSAEncoding;
import org.bouncycastle.crypto.signers.PlainDSAEncoding;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.bouncycastle.crypto.signers.StandardDSAEncoding;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * 国密SM2非对称算法实现
 * <p>
 * SM2算法只支持公钥加密，私钥解密
 * <p>
 * 国密算法包括：
 * <ol>
 *     <li>非对称加密和签名：SM2，asymmetric</li>
 *     <li>摘要签名算法：SM3，digest</li>
 *     <li>对称加密：SM4，symmetric</li>
 * </ol>
 *
 * @author sheng
 */
@Slf4j
@Getter
public class SM2 implements BaseCrypto {

    /**
     * 默认的加密模式，[《SM2密码算法使用规范》 GM/T 0009-2012 标准为 C1C3C2]
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成，其中：
     * <ol>
     *     <li>C1 生成随机数的计算出的椭圆曲线点</li>
     *     <li>C2 密文数据</li>
     *     <li>C3 SM3的摘要值</li>
     * </ol>
     */
    public static final SM2Engine.Mode SM2_DEFAULT_MODE = SM2Engine.Mode.C1C3C2;

    /**
     * 算法
     */
    private final AsymmetricCryptoAlgorithmEnum algorithm;

    /**
     * 获取SM2算法实例
     */
    public static SM2 create() {
        return new SM2();
    }

    public SM2() {
        this.algorithm = AsymmetricCryptoAlgorithmEnum.SM2;
    }

    @Override
    public AlgorithmEnum algorithm() {
        return algorithm.getAlgorithm();
    }

    /**
     * 加密
     *
     * @param data      被加密的bytes
     * @param publicKey 公钥
     * @param mode      加密模式
     * @return 加密后的bytes
     */
    public byte[] encrypt(byte[] data, ECPublicKeyParameters publicKey, SM2Engine.Mode mode) {
        try {
            final SM2Engine engine = new SM2Engine(Objects.nonNull(mode) ? mode : SM2_DEFAULT_MODE);
            engine.init(true, new ParametersWithRandom(publicKey, new SecureRandom()));
            return engine.processBlock(data, 0, data.length);
        } catch (Exception e) {
            throw new CryptoException("SM2加密异常", e);
        }
    }

    /**
     * 解密
     *
     * @param encrypted  密文
     * @param privateKey 私钥
     * @param mode       加密模式
     */
    public byte[] decrypt(final byte[] encrypted, ECPrivateKeyParameters privateKey, final SM2Engine.Mode mode) {
        try {
            final SM2Engine engine = new SM2Engine(Objects.nonNull(mode) ? mode : SM2_DEFAULT_MODE);
            engine.init(false, privateKey);
            return engine.processBlock(encrypted, 0, encrypted.length);
        } catch (Exception e) {
            throw new CryptoException("SM2解密异常", e);
        }
    }

    /**
     * 私钥签名
     *
     * @param encoding   编码
     *                   <ol>
     *                         <li>明文编码 {@link PlainDSAEncoding#INSTANCE}</li>
     *                         <li>ASN1编码 {@link StandardDSAEncoding#INSTANCE}</li>
     *                    </ol>
     * @param data       被签名的数据数据
     * @param privateKey 私钥
     * @param withId     标识
     * @return 签名
     */
    public byte[] sign(DSAEncoding encoding, byte[] data, ECPrivateKeyParameters privateKey, byte[] withId) {
        try {
            final SM2Signer signer = new SM2Signer(encoding);
            CipherParameters param = new ParametersWithRandom(privateKey);
            if (Objects.nonNull(withId)) {
                param = new ParametersWithID(param, withId);
            }
            signer.init(true, param);
            signer.update(data, 0, data.length);
            return signer.generateSignature();
        } catch (Exception e) {
            throw new CryptoException("SM2私钥签名异常", e);
        }
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param encoding 编码，
     *                 <ol>
     *                      <li>明文编码 {@link PlainDSAEncoding#INSTANCE}</li>
     *                      <li>ASN1编码 {@link StandardDSAEncoding#INSTANCE}</li>
     *                 </ol>
     * @param data     数据
     * @param sign     签名
     * @param withId   可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @return true 为验证通过
     */
    public boolean verify(DSAEncoding encoding, byte[] data, ECPublicKeyParameters publicKey, byte[] withId, byte[] sign) {
        try {
            final SM2Signer signer = new SM2Signer(encoding);
            CipherParameters param = publicKey;
            if (Objects.nonNull(withId)) {
                param = new ParametersWithID(param, withId);
            }
            signer.init(false, param);
            signer.update(data, 0, data.length);
            return signer.verifySignature(sign);
        } catch (Exception e) {
            log.error("SM2验签异常", e);
        }
        return false;
    }
}
