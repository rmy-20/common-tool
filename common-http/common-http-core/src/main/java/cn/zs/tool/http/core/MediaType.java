package cn.zs.tool.http.core;

import cn.zs.tool.core.text.StringPool;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 媒体类型
 *
 * @author sheng
 */
@Getter
public class MediaType {
    /**
     * 媒体类型
     */
    private final String type;

    /**
     * 媒体子类型
     */
    private final String subType;

    /**
     * 参数
     */
    private final Map<String, String> parameters;

    /**
     * 完整媒体类型
     */
    private final String mediaType;

    /**
     * 编码
     */
    private final Charset charset;

    /**
     * 构造媒体类型，默认 UTF-8
     *
     * @param type       类型
     * @param subType    子类型
     * @param parameters 参数
     */
    public static MediaType create(String type, String subType, Map<String, String> parameters) {
        return new MediaType(type, subType, parameters);
    }

    /**
     * 构造媒体类型
     *
     * @param type       类型
     * @param subType    子类型
     * @param parameters 参数
     * @param charset    编码
     */
    public static MediaType create(String type, String subType, Map<String, String> parameters, Charset charset) {
        return new MediaType(type, subType, parameters, charset);
    }

    public MediaType(String type, String subType, Map<String, String> parameters) {
        this(type, subType, parameters, StandardCharsets.UTF_8);
    }

    public MediaType(String type, String subType, Map<String, String> parameters, Charset charset) {
        this.type = type;
        this.subType = subType;
        this.parameters = parameters;
        this.charset = Objects.isNull(charset) ? StandardCharsets.UTF_8 : charset;
        this.mediaType = generateMediaType();
    }

    /**
     * 构造媒体类型
     */
    private String generateMediaType() {
        StringBuilder builder = new StringBuilder();
        // 媒体类型
        builder.append(getType())
                .append(StringPool.SLASH_SIGN)
                .append(getSubType());
        if (Objects.nonNull(getParameters())) {
            // 参数
            getParameters().forEach((k, v) -> builder.append(StringPool.SEMICOLON_SIGN)
                    .append(k)
                    .append(StringPool.EQUAL_SIGN)
                    .append(v));
        }
        return builder.toString();
    }
}
