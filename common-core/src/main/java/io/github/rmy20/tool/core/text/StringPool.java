package io.github.rmy20.tool.core.text;

/**
 * 字符串常量
 *
 * @author sheng
 */
public interface StringPool {
    /**
     * 空字符串
     */
    String EMPTY = "";

    /**
     * 冒号 :
     */
    String COLON_STR = ":";

    /**
     * 空白字符串
     */
    String WHITE_SPACE = " ";

    /**
     * 空白字符串正则
     */
    String WHITE_SPACE_REGEX = "\\s+";

    /**
     * 逗号字符串
     */
    String COMMA = ",";

    /**
     * -字符串
     */
    String MINUS_SIGN = "-";

    /**
     * $字符串
     */
    String DOLLAR_SIGN = "$";

    /**
     * &字符串
     */
    String GIVE_SIGN = "&";

    /**
     * ?字符串
     */
    String QUERY_SIGN = "?";

    /**
     * =字符串
     */
    String EQUAL_SIGN = "=";

    /**
     * /字符串
     */
    String SLASH_SIGN = "/";

    /**
     * / 字符串编码
     */
    String SLASH_SIGN_ENCODE = "%2F";

    /**
     * //
     */
    String DOUBLE_SLASH_SIGN = "//";

    /**
     * 正则：/+$，匹配结尾为 / 的字符串
     */
    String REGEX_END_SLASH = "/+$";

    /**
     * ;字符串
     */
    String SEMICOLON_SIGN = ";";

    /**
     * {
     */
    String START_CURLY_BRACKET = "{";

    /**
     * [
     */
    String START_SQUARE_BRACKET = "[";

    /**
     * @
     */
    String AT_SIGN = "@";

    /**
     * \n
     */
    String LINE_FEE = "\n";

    /**
     * "0"
     */
    String ZERO = "0";

    /**
     * -1
     */
    String MINUS_ONE = "-1";

    /**
     * Y
     */
    String Y = "Y";

    /**
     * N
     */
    String N = "N";
}
