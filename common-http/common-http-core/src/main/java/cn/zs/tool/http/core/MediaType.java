package cn.zs.tool.http.core;

import cn.zs.tool.core.collection.CollectionUtil;
import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 媒体类型
 *
 * @author sheng
 */
@Getter
public class MediaType {
    /**
     * 所有
     */
    public static final MediaType ALL = new MediaType("*", "*", Collections.emptyList());

    /**
     * json
     */
    public static final MediaType APPLICATION_JSON = new MediaType("application", "json", Collections.emptyList());

    /**
     * application/json;charset=UTF-8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json",
            Arrays.asList("charset", StandardCharsets.UTF_8.name()));

    /**
     * application/xml，通用类型，用于指示资源是XML格式的，严格按照XML的规则来解析文档，不支持XML中的HTML兼容特性
     */
    public static final MediaType APPLICATION_XML = new MediaType("application", "xml", Collections.emptyList());

    /**
     * application/xhtml+xml，特指XHTML文档，即遵循XML语法规范的HTML
     */
    public static final MediaType APPLICATION_XHTML_XML = new MediaType("application", "xhtml+xml", Collections.emptyList());

    /**
     * application/octet-stream，指示数据是二进制流数据（如常见的文件下载），不特定于任何特定的格式或种类
     */
    public static final MediaType APPLICATION_OCTET_STREAM = new MediaType("application", "octet-stream", Collections.emptyList());

    /**
     * application/pdf，PDF格式的文档
     */
    public static final MediaType APPLICATION_PDF = new MediaType("application", "pdf", Collections.emptyList());

    /**
     * text/plain，无格式或基本格式的文本
     */
    public static final MediaType TEXT_PLAIN = new MediaType("text", "plain", Collections.emptyList());

    /**
     * text/xml
     */
    public static final MediaType TEXT_XML = new MediaType("text", "xml", Collections.emptyList());

    /**
     * text/html
     */
    public static final MediaType TEXT_HTML = new MediaType("text", "html", Collections.emptyList());

    /**
     * image/gif，GIF格式的图片
     */
    public static final MediaType IMAGE_GIF = new MediaType("image", "gif", Collections.emptyList());

    /**
     * image/png，PNG格式的图片
     */
    public static final MediaType IMAGE_PNG = new MediaType("image", "png", Collections.emptyList());

    /**
     * image/jpeg，JPEG格式的图片
     */
    public static final MediaType IMAGE_JPEG = new MediaType("image", "jpeg", Collections.emptyList());

    /**
     * form表单，适用于传输简单的文本表单（非文件）数据，不涉及文件或大量二进制信息
     */
    public static final MediaType APPLICATION_FORM_URLENCODED = new MediaType("application", "x-www-form-urlencoded", Collections.emptyList());

    /**
     * multipart/form-data，用于HTTP请求中传输复杂表单数据，特别是包含文件上传的场景
     */
    public static final MediaType MULTIPART_FORM_DATA = new MediaType("multipart", "form-data", Collections.emptyList());

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
    private final List<String> parameters;

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
    public static MediaType create(String type, String subType, List<String> parameters) {
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
    public static MediaType create(String type, String subType, List<String> parameters, Charset charset) {
        return new MediaType(type, subType, parameters, charset);
    }

    public MediaType(String type, String subType, List<String> parameters) {
        this(type, subType, parameters, StandardCharsets.UTF_8);
    }

    public MediaType(String type, String subType, List<String> parameters, Charset charset) {
        this.type = type;
        this.subType = subType;
        Assert.isTrue(Objects.isNull(parameters) || parameters.size() % 2 == 0, "parameters size must be even");
        this.parameters = parameters;
        this.charset = Objects.isNull(charset) ? StandardCharsets.UTF_8 : charset;
        this.mediaType = generateMediaType();
    }

    /**
     * 解析并创建#{@link MediaType}
     */
    public static MediaType parse(String mediaType) {
        int index = 0;
        int length = mediaType.length();
        StringBuilder builder = new StringBuilder();
        index = findAndAppendUntil(builder, index, mediaType, '/');
        String type = builder.toString();
        builder.setLength(0);

        index = findAndAppendUntil(builder, index, mediaType, ';');
        String subType = builder.toString();
        builder.setLength(0);
        Charset charset = null;
        List<String> parameters = null;
        if (index < length) {
            parameters = new ArrayList<>();
            for (; index < length; index++) {
                char ch = mediaType.charAt(index);
                if (ch == '=' || ch == ';') {
                    parameters.add(builder.toString());
                    builder.setLength(0);
                    continue;
                } else if (Character.isWhitespace(ch)) {
                    continue;
                }
                builder.append(ch);
            }
            if (builder.length() > 0) {
                parameters.add(builder.toString());
            }
            if (parameters.size() % 2 != 0) {
                parameters.add(StringPool.EMPTY);
            }
            for (int i = 0; i < parameters.size(); i += 2) {
                String key = parameters.get(i);
                if ("charset".equalsIgnoreCase(key)) {
                    charset = Charset.forName(parameters.get(i + 1));
                    break;
                }
            }
        }
        return new MediaType(type, subType, parameters, charset);
    }

    private static int findAndAppendUntil(StringBuilder builder, int index, String mediaType, char target) {
        for (; index < mediaType.length(); index++) {
            char ch = mediaType.charAt(index);
            if (ch == target) {
                index++;
                break;
            } else if (Character.isWhitespace(ch)) {
                continue;
            }
            builder.append(ch);
        }
        return index;
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
        if (CollectionUtil.isNotEmpty(parameters)) {
            // 参数
            for (int i = 0; i < parameters.size(); i += 2) {
                builder.append(StringPool.SEMICOLON_SIGN)
                        .append(parameters.get(i));
                String value = parameters.get(i + 1);
                if (StringUtil.isNotBlank(value)) {
                    builder.append(StringPool.EQUAL_SIGN)
                            .append(value);
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取参数
     */
    public String getParameter(String key) {
        if (CollectionUtil.isNotEmpty(parameters)) {
            for (int i = 0; i < parameters.size(); i += 2) {
                if (parameters.get(i).equals(key)) {
                    return parameters.get(i + 1);
                }
            }
        }
        return null;
    }

    /**
     * 根据参数构建新的#{@link MediaType}
     */
    public MediaType withParameters(List<String> parameters) {
        return MediaType.create(type, subType, parameters, charset);
    }

    /**
     * 根据参数构建新的#{@link MediaType}
     */
    public MediaType withParameters(String... parameters) {
        return MediaType.create(type, subType, Arrays.asList(parameters), charset);
    }

    @Override
    public String toString() {
        return mediaType;
    }
}
