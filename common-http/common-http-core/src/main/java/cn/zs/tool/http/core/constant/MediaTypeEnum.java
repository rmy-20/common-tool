package cn.zs.tool.http.core.constant;

import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.http.core.MediaType;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * HTTP规范-媒体类型枚举
 *
 * @author sheng
 */
@Getter
public enum MediaTypeEnum {

    /**
     * 所有
     */
    ALL("*", "*"),

    /**
     * json
     */
    APPLICATION_JSON("application", "json"),

    /**
     * application/json;charset=UTF-8
     */
    APPLICATION_JSON_UTF8("application", "json", Collections.singletonMap("charset", StandardCharsets.UTF_8.name())),

    /**
     * application/xml，通用类型，用于指示资源是XML格式的，严格按照XML的规则来解析文档，不支持XML中的HTML兼容特性
     */
    APPLICATION_XML("application", "xml"),

    /**
     * application/xhtml+xml，特指XHTML文档，即遵循XML语法规范的HTML
     */
    APPLICATION_XHTML_XML("application", "xhtml+xml"),

    /**
     * application/octet-stream，指示数据是二进制流数据（如常见的文件下载），不特定于任何特定的格式或种类
     */
    APPLICATION_OCTET_STREAM("application", "octet-stream"),

    /**
     * application/pdf，PDF格式的文档
     */
    APPLICATION_PDF("application", "pdf"),

    /**
     * text/plain，无格式或基本格式的文本
     */
    TEXT_PLAIN("text", "plain"),

    /**
     * text/xml
     */
    TEXT_XML("text", "xml"),

    /**
     * text/html
     */
    TEXT_HTML("text", "html"),

    /**
     * image/gif，GIF格式的图片
     */
    IMAGE_GIF("image", "gif"),

    /**
     * image/png，PNG格式的图片
     */
    IMAGE_PNG("image", "png"),

    /**
     * image/jpeg，JPEG格式的图片
     */
    IMAGE_JPEG("image", "jpeg"),

    /**
     * form表单，适用于传输简单的文本表单（非文件）数据，不涉及文件或大量二进制信息
     */
    APPLICATION_FORM_URLENCODED("application", "x-www-form-urlencoded"),

    /**
     * multipart/form-data，用于HTTP请求中传输复杂表单数据，特别是包含文件上传的场景
     */
    MULTIPART_FORM_DATA("multipart", "form-data"),
    ;

    public static void main(String[] args) {
        Arrays.stream(values()).forEach(media -> System.out.println(media.getMediaType()));
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

    MediaTypeEnum(String type, String subType) {
        this(type, subType, Collections.emptyMap(), StandardCharsets.UTF_8);
    }

    MediaTypeEnum(String type, String subType, Map<String, String> parameters) {
        this(type, subType, parameters, StandardCharsets.UTF_8);
    }

    MediaTypeEnum(String type, String subType, Map<String, String> parameters, Charset charset) {
        this.type = type;
        this.subType = subType;
        this.parameters = Optional.ofNullable(parameters).orElse(Collections.emptyMap());
        this.charset = Optional.ofNullable(charset).orElse(StandardCharsets.UTF_8);
        this.mediaType = new MediaType(type, subType, parameters, charset);
        this.mediaTypeString = mediaType.getMediaType();
    }

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
    private final String mediaTypeString;

    /**
     * 编码
     */
    private final Charset charset;

    /**
     * 媒体类型对象
     */
    private final MediaType mediaType;
}
