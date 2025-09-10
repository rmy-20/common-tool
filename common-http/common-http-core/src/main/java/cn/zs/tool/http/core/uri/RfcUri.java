package cn.zs.tool.http.core.uri;

import cn.zs.tool.core.collection.CollectionUtil;
import cn.zs.tool.core.lang.Assert;
import cn.zs.tool.core.text.CharacterUtil;
import cn.zs.tool.core.text.StringPool;
import cn.zs.tool.core.text.StringUtil;
import cn.zs.tool.http.core.exception.UriException;
import cn.zs.tool.http.core.exception.UriParseException;
import cn.zs.tool.http.core.util.UriUtil;
import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/**
 * RFC 3986 uri
 *
 * @author sheng
 * @see <a href="https://www.rfc-editor.org/info/rfc3986">RFC 3986</a>
 */
public class RfcUri {
    /**
     * 兼容的协议
     */
    private static final Set<String> HIERARCHICAL_SCHEMES = new HashSet<>(Arrays.asList("ftp", "file", "http", "https", "ws", "wss"));

    /**
     * 协议，如 "ftp", "file", "http", "https", "ws", "wss"，后接":"
     */
    private final String scheme;

    /**
     * 用户名
     */
    private final String userinfo;

    /**
     * 主机名
     */
    private final String host;

    /**
     * 端口
     */
    private final String port;

    /**
     * 资源路径
     */
    private final List<String> pathSegments;

    /**
     * 查询参数，如 index=i 为 name，则index = i + 1 为 value
     */
    private final List<String> queryParameters;

    /**
     * 片段标识符，前缀"#"
     */
    private final String fragment;

    /**
     * 不透明路径
     */
    private final String schemeSpecificPart;

    /**
     * 编码时所用字符集
     */
    private final Charset encodedCharset;

    /**
     * url
     */
    private final URI uri;

    /**
     * 创建 #{@link Builder}，编码时使用 UTF-8 字符集
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 创建 #{@link Builder}
     *
     * @param encodedCharset 编码时所用字符集
     */
    public static Builder builder(Charset encodedCharset) {
        return new Builder(encodedCharset);
    }

    /**
     * 解析uri，使用 UTF-8 进行编码
     *
     * @param uri 待解析的uri
     */
    public static RfcUri parse(String uri) {
        return parse(uri, true, StandardCharsets.UTF_8);
    }

    /**
     * 解析uri
     *
     * @param uri            待解析的uri
     * @param encoded        对 uri 解析出来的组件是否进行编码
     * @param encodedCharset 对 uri 解析出来的组件进行编码的字符集
     */
    public static RfcUri parse(String uri, boolean encoded, Charset encodedCharset) {
        return new InternalParse(uri).parse(encoded, encodedCharset).build();
    }

    private RfcUri(String scheme, String userinfo, String host, String port, List<String> pathSegments,
                   List<String> queryParameters, String fragment, String schemeSpecificPart, Charset encodedCharset, URI uri) {
        this.scheme = scheme;
        this.userinfo = userinfo;
        this.host = host;
        this.port = port;
        this.pathSegments = pathSegments;
        this.queryParameters = queryParameters;
        this.fragment = fragment;
        this.schemeSpecificPart = schemeSpecificPart;
        this.encodedCharset = encodedCharset;
        this.uri = uri;
    }

    /**
     * 获取 URI
     */
    public URI uri() {
        return uri;
    }

    /**
     * 获取uri字符串
     */
    public String uriString() {
        return uri.toString();
    }

    /**
     * 获取#{@link URL}
     */
    public URL url() {
        try {
            return uri.toURL();
        } catch (Exception e) {
            throw new UriException("RFC uri转换URL异常", e);
        }
    }

    /**
     * 创建新的Builder，并设置当前Builder的组件
     */
    public Builder newBuilder() {
        Builder builder = new Builder(encodedCharset).scheme(scheme);
        if (StringUtil.isNotBlank(schemeSpecificPart)) {
            builder.schemeSpecificPart(schemeSpecificPart);
        } else {
            builder.userinfo(userinfo).host(host).port(port).pathSegments(pathSegments).query(queryParameters);
        }
        return builder.fragment(fragment);
    }

    /**
     * 构建者
     */
    public static class Builder {
        /**
         * 协议，如 "ftp", "file", "http", "https", "ws", "wss"，后接":"
         */
        private String scheme;

        /**
         * 用户名:密码
         */
        private String userinfo;

        /**
         * 主机名
         */
        private String host;

        /**
         * 端口
         */
        private String port;

        /**
         * 资源路径
         */
        private final List<String> pathSegments = new ArrayList<>();

        /**
         * 查询参数，如 index=i 为 name，则index = i + 1 为 value
         */
        private List<String> queryParameters;

        /**
         * 片段标识符，前缀"#"
         */
        private String fragment;

        /**
         * 不透明路径
         */
        private String schemeSpecificPart;

        /**
         * 默认编码
         */
        private final Charset charset;

        private Builder() {
            this(StandardCharsets.UTF_8);
        }

        private Builder(Charset charset) {
            this.charset = Objects.requireNonNull(charset, "charset must not be null");
        }

        /**
         * 设置协议
         */
        public Builder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        /**
         * 将传入协议编码，并设置
         */
        public Builder schemeEncoded(String scheme) {
            this.scheme = RfcUriComponentEncoderEnum.SCHEME.encode(scheme, charset);
            return this;
        }

        /**
         * 设置用户名:密码
         */
        public Builder userinfo(String userinfo) {
            this.userinfo = userinfo;
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 将传入用户名:密码编码，并设置
         */
        public Builder userinfoEncoded(String userinfo) {
            this.userinfo = RfcUriComponentEncoderEnum.USER_INFO.encode(userinfo, charset);
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 设置主机名
         */
        public Builder host(String host) {
            this.host = host;
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 将传入主机名编码，并设置
         */
        public Builder hostEncoded(String host) {
            RfcUriComponentEncoderEnum hostComponent = StringUtil.isNotBlank(host) && host.startsWith(StringPool.START_SQUARE_BRACKET)
                    ? RfcUriComponentEncoderEnum.HOST_IPV6 : RfcUriComponentEncoderEnum.HOST_IPV4;
            this.host = hostComponent.encode(host, charset);
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 设置端口
         */
        public Builder port(String port) {
            this.port = port;
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 添加资源路径
         */
        public Builder pathSegment(String pathSegment) {
            this.pathSegments.add(UriUtil.encodePathSlashSign(pathSegment));
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 添加资源路径，会根据 / 切割
         */
        public Builder pathSegments(String pathSegments) {
            return pathSegments(UriUtil.splitPathSegments(pathSegments));
        }

        /**
         * 添加资源路径，会将 / 进行编码
         */
        public Builder pathSegments(List<String> pathSegments) {
            for (String pathSegment : pathSegments) {
                this.pathSegments.add(UriUtil.encodePathSlashSign(pathSegment));
            }
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 添加资源路径
         */
        public Builder pathSegments(String... pathSegments) {
            for (String pathSegment : pathSegments) {
                this.pathSegments.add(UriUtil.encodePathSlashSign(pathSegment));
            }
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 资源路径编码后添加
         */
        public Builder pathSegmentEncoded(String pathSegment) {
            this.pathSegments.add(RfcUriComponentEncoderEnum.PATH_SEGMENT.encode(pathSegment, charset));
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 添加资源路径编码后添加
         *
         * @param pathSegments 会根据 / 切割
         */
        public Builder pathSegmentsEncoded(String pathSegments) {
            return pathSegmentsEncoded(UriUtil.splitPathSegments(pathSegments));
        }

        /**
         * 添加资源路径编码后添加
         *
         * @param pathSegments 对每一个元素进行编码
         */
        public Builder pathSegmentsEncoded(List<String> pathSegments) {
            for (String pathSegment : pathSegments) {
                this.pathSegments.add(RfcUriComponentEncoderEnum.PATH_SEGMENT.encode(pathSegment, charset));
            }
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 添加资源路径编码后添加
         *
         * @param pathSegments 对每一个元素进行编码
         */
        public Builder pathSegmentsEncoded(String... pathSegments) {
            for (String pathSegment : pathSegments) {
                this.pathSegments.add(RfcUriComponentEncoderEnum.PATH_SEGMENT.encode(pathSegment, charset));
            }
            resetSchemeSpecificPart();
            return this;
        }

        private void ensureQueryParametersExist() {
            if (this.queryParameters == null) {
                this.queryParameters = new ArrayList<>();
            }
        }

        /**
         * 添加查询参数
         *
         * @param name  key
         * @param value value
         */
        public Builder query(String name, String value) {
            ensureQueryParametersExist();
            this.queryParameters.add(name);
            this.queryParameters.add(Objects.toString(value, StringPool.EMPTY));
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 设置查询参数，如 index=i 为 name，则index = i + 1 为 value
         *
         * @param queryParameters index=i 为 name，则index = i + 1 为 value
         */
        public Builder query(List<String> queryParameters) {
            if (CollectionUtil.isEmpty(queryParameters)) {
                return this;
            }
            if (queryParameters.size() % 2 != 0) {
                throw new UriException("queryParameters size must be even");
            }
            ensureQueryParametersExist();
            this.queryParameters.addAll(queryParameters);
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 查询参数编码后设置
         *
         * @param name  key
         * @param value value
         */
        public Builder queryEncoded(String name, String value) {
            ensureQueryParametersExist();
            this.queryParameters.add(RfcUriComponentEncoderEnum.QUERY_PARAM.encode(name, charset));
            this.queryParameters.add(RfcUriComponentEncoderEnum.QUERY_PARAM.encode(Objects.toString(value, StringPool.EMPTY), charset));
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 查询参数编码后设置，如 index=i 为 name，则index = i + 1 为 value
         *
         * @param queryParameters index=i 为 name，则index = i + 1 为 value
         */
        public Builder queryEncoded(List<String> queryParameters) {
            if (CollectionUtil.isEmpty(queryParameters)) {
                return this;
            }
            Assert.isTrue(queryParameters.size() % 2 == 0, () -> new UriException("queryParameters size must be even"));
            ensureQueryParametersExist();
            for (String queryParameter : queryParameters) {
                this.queryParameters.add(RfcUriComponentEncoderEnum.QUERY_PARAM.encode(queryParameter, charset));
            }
            resetSchemeSpecificPart();
            return this;
        }

        /**
         * 设置片段标识符
         */
        public Builder fragment(String fragment) {
            this.fragment = fragment;
            return this;
        }

        /**
         * 设置片段标识符
         */
        public Builder fragmentEncoded(String fragment) {
            this.fragment = RfcUriComponentEncoderEnum.FRAGMENT.encode(fragment, charset);
            return this;
        }

        /**
         * 重置分层uri组件
         */
        private void resetHierarchicalComponents() {
            this.userinfo = null;
            this.host = null;
            this.port = null;
            this.pathSegments.clear();
            this.queryParameters = null;
        }

        /**
         * 重置不透明路径
         */
        private void resetSchemeSpecificPart() {
            this.schemeSpecificPart = null;
        }

        /**
         * 设置不透明路径
         */
        public Builder schemeSpecificPart(String schemeSpecificPart) {
            this.schemeSpecificPart = schemeSpecificPart;
            resetHierarchicalComponents();
            return this;
        }

        /**
         * 获取资源路径
         */
        public String getPath() {
            if (CollectionUtil.isEmpty(pathSegments)) {
                return StringPool.EMPTY;
            }
            StringJoiner joiner = new StringJoiner(StringPool.SLASH_SIGN, StringPool.SLASH_SIGN, StringPool.EMPTY);
            pathSegments.forEach(joiner::add);
            return joiner.toString();
        }

        /**
         * 获取完整查询参数
         */
        public String getQuery() {
            return UriUtil.stitchQueryParameters(queryParameters);
        }

        /**
         * 获取端口
         */
        public int getPort() {
            if (StringUtil.isBlank(port)) {
                return -1;
            }
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                throw new UriException("The port must be an integer: " + port);
            }
        }

        /**
         * 转为 #{@link URI}
         */
        public URI toUri() {
            try {
                if (StringUtil.isNotBlank(schemeSpecificPart)) {
                    return new URI(scheme, schemeSpecificPart, fragment);
                }
                StringBuilder builder = new StringBuilder();
                if (StringUtil.isNotBlank(scheme)) {
                    builder.append(scheme).append(':');
                }
                boolean hasUserInfo = Objects.nonNull(userinfo);
                boolean hasHost = Objects.nonNull(host);
                if (hasUserInfo || hasHost) {
                    builder.append(StringPool.DOUBLE_SLASH_SIGN);
                    if (hasUserInfo) {
                        builder.append(userinfo).append('@');
                    }
                    if (hasHost) {
                        builder.append(host);
                    }
                    if (StringUtil.isNotBlank(port) && !StringPool.MINUS_ONE.equals(port)) {
                        builder.append(':').append(port);
                    }
                }
                String path = getPath();
                if (StringUtil.isNotBlank(path)) {
                    builder.append(path);
                }
                String query = getQuery();
                if (StringUtil.isNotBlank(query)) {
                    builder.append('?').append(query);
                }
                if (StringUtil.isNotBlank(fragment)) {
                    builder.append('#').append(fragment);
                }
                return new URI(builder.toString());
            } catch (URISyntaxException e) {
                throw new IllegalStateException("Can't not create URI Object :" + e.getMessage(), e);
            }
        }

        public RfcUri build() {
            return new RfcUri(scheme, userinfo, host, port, pathSegments,
                    queryParameters, fragment, schemeSpecificPart, charset, toUri());
        }
    }

    /**
     * RFC 3986 uri 解析
     */
    static class InternalParse {
        /**
         * 协议，如 "ftp", "file", "http", "https", "ws", "wss"，后接":"
         */
        private String scheme;

        /**
         * 用户名:密码
         */
        private String userinfo;

        /**
         * 主机名
         */
        private String host;

        /**
         * 端口
         */
        private String port;

        /**
         * 资源路径
         */
        private String path;

        /**
         * 查询参数，前缀"?"
         */
        private String query;

        /**
         * 片段标识符，前缀"#"
         */
        private String fragment;

        /**
         * 待解析 uri
         */
        private final String uri;

        /**
         * 当前处理组件
         */
        private ComponentEnum component = ComponentEnum.START;

        /**
         * 是否为不透明uri
         */
        @Getter
        private boolean opaque;

        /**
         * 当前uri解析索引
         */
        private int index = 0;

        /**
         * 当前uri解析的组件开始索引
         */
        private int componentIndex = 0;

        /**
         * 剩余的待处理百分比编码字符数
         */
        private int remainingPercentEncodedChars = 0;

        /**
         * 当前字符是否在 utf16 字符中
         */
        private boolean inUtf16Sequence = false;

        /**
         * 当前字符是否在 {} 中
         */
        private int openCurlyBracketCount;

        /**
         * 当前字符是否在密码中
         */
        private boolean inPassword;

        private InternalParse(String uri) {
            this.uri = uri;
        }

        /**
         * 解析 uri
         *
         * @param encode        解析结果为分层uri时是否编码
         * @param encodeCharset 解析结果为分层uri时编码的字符集
         */
        public Builder parse(boolean encode, Charset encodeCharset) {
            Assert.isTrue(this.component == ComponentEnum.START && this.index == 0, () -> new UriParseException("Internal uri parse Error"));
            while (hasNext()) {
                this.component.handleNext(this);
                this.index++;
            }
            this.component.handleEnd(this);

            if (opaque) {
                String schemeSpecificPart = Objects.toString(path, StringPool.EMPTY) + Objects.toString(query, StringPool.EMPTY);
                return new Builder().scheme(scheme).schemeSpecificPart(schemeSpecificPart).fragment(fragment);
            } else {
                if (encode) {
                    return new Builder(encodeCharset).schemeEncoded(scheme).userinfoEncoded(userinfo).hostEncoded(host)
                            .port(port).pathSegmentsEncoded(path).queryEncoded(UriUtil.splitQueryParameters(query))
                            .fragmentEncoded(fragment);
                } else {
                    return new Builder().scheme(scheme).userinfo(userinfo).host(host).port(port).pathSegments(path)
                            .query(UriUtil.splitQueryParameters(query)).fragment(fragment);
                }
            }
        }

        /**
         * 是否有下一个字符
         */
        public boolean hasNext() {
            return index < uri.length();
        }

        /**
         * 获取当前索引的字符
         */
        public char currentChar() {
            return uri.charAt(index);
        }

        /**
         * 获取当前索引
         */
        public int currentIndex() {
            return index;
        }

        /**
         * 下一个索引
         */
        public void nextIndex() {
            index++;
        }

        /**
         * 回退索引
         */
        public void countDownIndex() {
            index--;
        }

        /**
         * 设置当前组件开始索引
         */
        public void componentIndex(int componentIndex) {
            this.componentIndex = componentIndex;
        }

        /**
         * 标记当前处理字符为 % 编码
         */
        public void markPercentEncoding() {
            Assert.isTrue(remainingPercentEncodedChars < 1, () -> new UriParseException("bad encoding"));
            // 假设为普通编码 %XX
            remainingPercentEncodedChars = 2;
            inUtf16Sequence = false;
        }

        /**
         * 若当前字符处于路径中的 % 编码中，则返回 true，并减少剩余编码字符数
         */
        public boolean countDownPercentEncodingInPath() {
            if (remainingPercentEncodedChars < 1) {
                return false;
            }
            // 是否为 Unicode 编码 %uXXXX 中的 u
            if (remainingPercentEncodedChars == 2 && currentChar() == 'u' && !inUtf16Sequence) {
                remainingPercentEncodedChars = 4;
                inUtf16Sequence = true;
                return true;
            }
            remainingPercentEncodedChars--;
            Assert.isTrue(CharacterUtil.isHexDigit(currentChar()), () -> new UriParseException("Bad path"));
            // 为下一次的 %uXXXX 中的 u 赋值
            inUtf16Sequence &= remainingPercentEncodedChars > 0;
            return true;
        }

        /**
         * 若当前字符处于路径中的 % 编码中，则返回 true，并减少剩余编码字符数
         */
        public boolean countDownPercentEncodingInHost() {
            if (remainingPercentEncodedChars < 1) {
                return false;
            }
            remainingPercentEncodedChars--;
            Assert.isTrue(CharacterUtil.isHexDigit(currentChar()), () -> new UriParseException("Bad authority"));
            return true;
        }

        /**
         * 若有 user 则返回 true
         */
        public boolean hasUser() {
            return Objects.nonNull(userinfo);
        }

        /**
         * 若有协议则返回 true
         */
        public boolean hasScheme() {
            return Objects.nonNull(scheme);
        }

        /**
         * 若有 host 则返回 true
         */
        public boolean hasHost() {
            return Objects.nonNull(host);
        }

        /**
         * 判断当前字符是否在{}中
         */
        public boolean processCurlyBrackets() {
            char currentChar = currentChar();
            if (currentChar == '{') {
                openCurlyBracketCount++;
                return true;
            } else if (currentChar == '}') {
                if (openCurlyBracketCount > 0) {
                    openCurlyBracketCount--;
                    return true;
                }
                return false;
            }
            return openCurlyBracketCount > 0;
        }

        /**
         * 当前字符是否在组件的开始位置
         */
        public boolean isAtStartOfComponent() {
            return index == componentIndex;
        }

        /**
         * 推进流程
         *
         * @param nextComponent 下一个流程
         */
        public void advanceTo(ComponentEnum nextComponent) {
            component = nextComponent;
            openCurlyBracketCount = 0;
        }

        /**
         * 推进流程
         *
         * @param nextProcessor  下一个流程
         * @param componentIndex 下一个组件开始索引
         */
        public void advanceTo(ComponentEnum nextProcessor, int componentIndex) {
            componentIndex(componentIndex);
            advanceTo(nextProcessor);
        }

        /**
         * 判断当前 uri 是否为不透明 uri
         */
        public void resolveIfOpaque() {
            this.opaque = currentChar() != '/' && !HIERARCHICAL_SCHEMES.contains(scheme);
        }

        /**
         * 截取组件
         */
        public String captureComponent() {
            return uri.substring(componentIndex, index);
        }

        /**
         * 端口为密码时，将用户名切换为完整的用户名
         */
        public void switchPortForFullPassword() {
            userinfo = host + StringPool.COLON_STR + captureComponent();
        }

        /**
         * 端口为密码时，切换为用户/密码解析
         */
        public void switchPortForPassword() {
            inPassword = true;
            if (Objects.nonNull(host)) {
                componentIndex = componentIndex - host.length() - 1;
                host = null;
            }
        }

        /**
         * 截取端口
         */
        public void capturePort() {
            Assert.isTrue(openCurlyBracketCount == 0, () -> new UriParseException("Bad authority"));
            port = captureComponent();
        }

        /**
         * 截取 host
         */
        public void captureHost() {
            Assert.isTrue(remainingPercentEncodedChars == 0 && !inPassword, "Bad authority");
            host = captureComponent();
        }

        /**
         * 存在host则截取 host
         */
        public void captureHostIfNotEmpty() {
            if (index > componentIndex) {
                captureHost();
            }
        }

        /**
         * 截取 user
         */
        public void captureUser() {
            inPassword = false;
            userinfo = StringUtil.isEmpty(userinfo) ? captureComponent()
                    : userinfo + StringPool.AT_SIGN + captureComponent();
        }

        /**
         * 截取 scheme
         */
        public void captureScheme() {
            String scheme = captureComponent();
            this.scheme = !scheme.contains(StringPool.START_CURLY_BRACKET) ? scheme.toLowerCase(Locale.ROOT) : scheme;
        }

        /**
         * 截取 path
         */
        public void capturePath() {
            this.path = captureComponent();
        }

        /**
         * 存在 fragment 则截取 fragment
         */
        public void captureFragmentIfNotEmpty() {
            if (index > componentIndex + 1) {
                fragment = captureComponent();
            }
        }

        /**
         * 截取 query
         */
        public void captureQuery() {
            query = captureComponent();
        }
    }

    /**
     * 组件枚举
     */
    enum ComponentEnum {
        /**
         * 开始解析
         */
        START() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                // 跳过空白字符
                if (Character.isWhitespace(currentChar)) {
                    parser.componentIndex(parser.currentIndex() + 1);
                    return;
                }
                switch (currentChar) {
                    case '/':
                        parser.advanceTo(HOST_OR_PATH, parser.currentIndex());
                        break;
                    case ';':
                    case '.':
                        parser.advanceTo(PATH, parser.currentIndex());
                        break;
                    case '%':
                        // 普通编码 %XX 或 Unicode 编码 %uXXXX
                        parser.markPercentEncoding();
                        parser.advanceTo(PATH, parser.currentIndex());
                        break;
                    case '?':
                        parser.advanceTo(QUERY, parser.currentIndex() + 1);
                        break;
                    case '#':
                        parser.advanceTo(FRAGMENT, parser.currentIndex() + 1);
                        break;
                    default:
                        if (parser.hasScheme()) {
                            parser.resolveIfOpaque();
                            parser.advanceTo(PATH, parser.currentIndex());
                        } else {
                            parser.advanceTo(SCHEME_OR_PATH, parser.currentIndex());
                        }
                        break;
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.capturePath();
            }
        },

        /**
         * 域名或路径
         */
        HOST_OR_PATH() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                switch (currentChar) {
                    case '/':
                        parser.componentIndex(parser.currentIndex());
                        parser.captureHost();
                        parser.advanceTo(HOST, parser.currentIndex() + 1);
                        break;
                    case '%':
                    case '@':
                    case ';':
                    case '?':
                    case '#':
                    case '.':
                        parser.countDownIndex();
                        parser.advanceTo(PATH);
                        break;
                    default:
                        parser.advanceTo(PATH);
                        break;
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.capturePath();
            }
        },

        /**
         * 协议或路径
         */
        SCHEME_OR_PATH() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                switch (currentChar) {
                    case ':':
                        parser.captureScheme();
                        parser.advanceTo(START);
                        break;
                    case '/':
                    case ';':
                        parser.advanceTo(PATH);
                        break;
                    case '%':
                        parser.markPercentEncoding();
                        parser.advanceTo(PATH);
                        break;
                    case '?':
                        parser.capturePath();
                        parser.advanceTo(QUERY, parser.currentIndex() + 1);
                        break;
                    case '#':
                        parser.capturePath();
                        parser.advanceTo(FRAGMENT);
                        break;
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.capturePath();
            }
        },

        /**
         * 主机
         */
        HOST() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                switch (currentChar) {
                    case '/':
                        parser.captureHost();
                        parser.advanceTo(PATH, parser.currentIndex());
                        break;
                    case ':':
                        parser.captureHostIfNotEmpty();
                        parser.advanceTo(PORT, parser.currentIndex() + 1);
                        break;
                    case '?':
                        parser.captureHostIfNotEmpty();
                        parser.advanceTo(QUERY, parser.currentIndex() + 1);
                        break;
                    case '#':
                        parser.captureHostIfNotEmpty();
                        parser.advanceTo(FRAGMENT, parser.currentIndex() + 1);
                        break;
                    case '@':
                        parser.captureUser();
                        parser.componentIndex(parser.currentIndex() + 1);
                        break;
                    case '%':
                        parser.markPercentEncoding();
                        break;
                    case '[':
                        Assert.isTrue(parser.isAtStartOfComponent(), "Bad authority");
                        parser.advanceTo(IPV6);
                        break;
                    default:
                        boolean isAllowed = parser.processCurlyBrackets() || parser.countDownPercentEncodingInHost()
                                || UriUtil.isUnreservedOrSubDelimiter(currentChar);
                        Assert.isTrue(isAllowed, "Bad authority");
                        break;
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.captureHostIfNotEmpty();
            }
        },

        /**
         * IPV6
         */
        IPV6() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                switch (currentChar) {
                    case ']':
                        parser.nextIndex();
                        parser.captureHost();
                        if (parser.hasNext()) {
                            if (parser.currentChar() == ':') {
                                parser.advanceTo(PORT, parser.currentIndex() + 1);
                            } else {
                                parser.advanceTo(PATH, parser.currentIndex());
                            }
                        }
                        break;
                    case ':':
                        break;
                    default:
                        Assert.isTrue(CharacterUtil.isHexDigit(currentChar), "Bad authority");
                        break;
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                Assert.isTrue(parser.hasHost(), "Bad authority");
            }
        },

        /**
         * 端口
         */
        PORT() {
            @Override
            public void handleNext(InternalParse parser) {
                char currentChar = parser.currentChar();
                if (currentChar == '@') {
                    Assert.isTrue(parser.hasUser(), "Bad authority");
                    parser.switchPortForFullPassword();
                    parser.advanceTo(HOST, parser.currentIndex() + 1);
                } else if (currentChar == '/') {
                    parser.capturePort();
                    parser.advanceTo(PATH, parser.currentIndex());
                } else if (currentChar == '?') {
                    parser.capturePort();
                    parser.advanceTo(QUERY, parser.currentIndex() + 1);
                } else if (currentChar == '#') {
                    parser.capturePort();
                    parser.advanceTo(FRAGMENT, parser.currentIndex() + 1);
                } else if (!Character.isDigit(currentChar)) {
                    if (parser.processCurlyBrackets()) {
                        return;
                    } else if (currentChar == '%' || UriUtil.isUnreservedOrSubDelimiter(currentChar)) {
                        parser.switchPortForPassword();
                        parser.advanceTo(HOST);
                        return;
                    }
                    throw new IllegalArgumentException("Bad authority");
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.capturePort();
            }
        },

        /**
         * 路径
         */
        PATH() {
            @Override
            public void handleNext(InternalParse parser) {
                if (!parser.countDownPercentEncodingInPath()) {
                    switch (parser.currentChar()) {
                        case '?':
                            if (!parser.isOpaque()) {
                                parser.capturePath();
                                parser.advanceTo(QUERY, parser.currentIndex() + 1);
                            }
                            break;
                        case '#':
                            parser.capturePath();
                            parser.advanceTo(FRAGMENT, parser.currentIndex() + 1);
                            break;
                        case '%':
                            parser.markPercentEncoding();
                            break;
                    }
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.capturePath();
            }
        },

        /**
         * 参数
         */
        QUERY() {
            @Override
            public void handleNext(InternalParse parser) {
                if (parser.currentChar() == '#') {
                    parser.captureQuery();
                    parser.advanceTo(FRAGMENT, parser.currentIndex() + 1);
                }
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.captureQuery();
            }
        },

        /**
         * 片段
         */
        FRAGMENT() {
            @Override
            public void handleNext(InternalParse parser) {
            }

            @Override
            public void handleEnd(InternalParse parser) {
                parser.captureFragmentIfNotEmpty();
            }
        },
        ;

        /**
         * 处理当前流程
         *
         * @param parser 解析器
         */
        public abstract void handleNext(InternalParse parser);

        /**
         * 处理结束
         */
        public abstract void handleEnd(InternalParse parser);
    }
}
