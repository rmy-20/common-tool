package cn.zs.tool.crypto.constant;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.ECNamedCurveTable;
import org.bouncycastle.asn1.x9.X962Parameters;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;

import java.math.BigInteger;
import java.security.spec.ECFieldFp;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;

/**
 * 常量
 *
 * @author sheng
 */
public class CryptoConstant {
    /**
     * 算法模板
     */
    public static final String ALGORITHM_TEMPLATE = "%s/%s/%s";

    /**
     * sm2-曲线名称
     */
    public static final String SM2_CURVE_NAME = "sm2p256v1";

    /**
     * sm2-曲线参数
     */
    public static final ECGenParameterSpec SM2_P256_V1 = new ECGenParameterSpec(CryptoConstant.SM2_CURVE_NAME);

    /**
     * 椭圆曲线参数对象，包含了SM2椭圆曲线算法所需的参数，如曲线方程、基点、领域参数等
     */
    public static final X9ECParameters X9EC_PARAMETERS = GMNamedCurves.getByName(CryptoConstant.SM2_CURVE_NAME);

    /**
     * 椭圆曲线参数
     */
    public static final ECCurve EC_CURVE = X9EC_PARAMETERS.getCurve();

    /**
     * sm2-曲线
     */
    public static final SM2P256V1Curve SM2_CURVE = new SM2P256V1Curve();

    /**
     * SM2椭圆曲线参数a，用于定义椭圆曲线方程的一部分
     */
    public final static BigInteger SM2_ECC_A = SM2_CURVE.getA().toBigInteger();

    /**
     * SM2椭圆曲线参数b，用于定义椭圆曲线方程的一部分
     */
    public final static BigInteger SM2_ECC_B = SM2_CURVE.getB().toBigInteger();

    /**
     * SM2椭圆曲线的参数Q，它是椭圆曲线上的一个大整数
     */
    public final static BigInteger SM2_ECC_P = SM2_CURVE.getQ();

    /**
     * 椭圆曲线上的一个特定点G的X坐标值
     */
    public final static BigInteger SM2_ECC_GX = new BigInteger("32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);

    /**
     * 椭圆曲线上的一个特定点G的Y坐标值
     */
    public final static BigInteger SM2_ECC_GY = new BigInteger("BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);

    /**
     * 椭圆曲线上的一个固定点G
     * <p>
     * public final static BigInteger SM2_ECC_GX = new BigInteger(
     * "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
     * <p>
     * public final static BigInteger SM2_ECC_GY = new BigInteger(
     * "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
     * <p>
     * public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
     */
    public static final ECPoint G_POINT = X9EC_PARAMETERS.getG();

    /**
     * SM2椭圆曲线模数常量N
     * <p>
     * public final static BigInteger SM2_ECC_N = SM2_CURVE.getOrder();
     */
    public static final BigInteger SM2_ECC_N = X9EC_PARAMETERS.getN();

    /**
     * SM2椭圆曲线的基点高度常量H
     * <p>
     * public final static BigInteger SM2_ECC_H = SM2_CURVE.getCofactor();
     */
    public static final BigInteger SM2_ECC_H = X9EC_PARAMETERS.getH();

    /**
     * SM2推荐曲线参数
     */
    public static final ECDomainParameters SM2_DOMAIN_PARAMS = new ECDomainParameters(EC_CURVE, G_POINT, SM2_ECC_N, SM2_ECC_H);

    public static final EllipticCurve JDK_CURVE = new EllipticCurve(new ECFieldFp(SM2_ECC_P), SM2_ECC_A, SM2_ECC_B);

    public static final java.security.spec.ECPoint JDK_G_POINT = new java.security.spec.ECPoint(
            G_POINT.getAffineXCoord().toBigInteger(), G_POINT.getAffineYCoord().toBigInteger());

    public static final ECParameterSpec JDK_EC_SPEC = new ECParameterSpec(
            JDK_CURVE, JDK_G_POINT, SM2_ECC_N, SM2_ECC_H.intValue());

    /**
     * ASN.1 编码对象，用于 SM2 算法的 EC 参数编码。
     * <p>
     * 该对象初始化为 SM2 曲线的 X962Parameters，X962Parameters 是一个 ASN.1 结构，
     * 用于表示椭圆曲线参数。此处使用 SM2_CURVE_NAME 获取对应的椭圆曲线OID，
     * 以便在ASN.1编码过程中使用正确的曲线参数。
     */
    public static final ASN1Encodable ASN_1_ENCODE = new X962Parameters(ECNamedCurveTable.getOID(SM2_CURVE_NAME));

    /**
     * SM2椭圆曲线参数规范
     */
    public static final ECNamedCurveParameterSpec EC_NAMED_CURVE_PARAMETER_SPEC =
            org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(SM2_CURVE_NAME);

    /**
     * 算法EC
     */
    public static final String ALGORITHM_EC = "EC";

    /**
     * RSA算法标识符
     */
    public static final AlgorithmIdentifier RSA_ALGORITHM_IDENTIFIER = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
}
