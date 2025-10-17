package com.ruoyi.common.utils.html;

import com.ruoyi.common.utils.StringUtils;

/**
 * 转义和反转义工具类
 * 
 * @author ruoyi
 */
public class EscapeUtil
{
    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    private static final char[][] TEXT = new char[64][];

    static
    {
        for (int i = 0; i < 64; i++)
        {
            TEXT[i] = new char[] { (char) i };
        }

        // special HTML characters
        TEXT['\''] = "&#039;".toCharArray(); // 单引号
        TEXT['"'] = "&#34;".toCharArray(); // 双引号
        TEXT['&'] = "&#38;".toCharArray(); // &符
        TEXT['<'] = "&#60;".toCharArray(); // 小于号
        TEXT['>'] = "&#62;".toCharArray(); // 大于号
    }

    /**
     * 转义文本中的HTML字符为安全的字符
     * 
     * @param text 被转义的文本
     * @return 转义后的文本
     */
    public static String escape(String text)
    {
        return encode(text);
    }

    /**
     * 还原被转义的HTML特殊字符
     * 
     * @param content 包含转义符的HTML内容
     * @return 转换后的字符串
     */
    public static String unescape(String content)
    {
        return decode(content);
    }

    /**
     * 清除所有HTML标签，但是不删除标签内的内容
     *
     * @param content 文本
     * @return 清除标签后的文本
     */
    public static String clean(String content)
    {
        return new HTMLFilter().filter(content);
    }

    /**
     * 提取HTML内容的纯文本概述
     * 清除所有HTML标签并格式化文本，适合用于列表展示的概述信息
     *
     * @param htmlContent HTML内容
     * @param maxLength 概述最大长度，超过则截断并添加"..."
     * @return 纯文本概述
     */
    public static String extractTextSummary(String htmlContent, int maxLength)
    {
        if (StringUtils.isEmpty(htmlContent))
        {
            return StringUtils.EMPTY;
        }

        // 优化：先尝试快速智能解析，避免HTMLFilter的性能开销
        String smartSummary = extractFastSmartSummary(htmlContent);
        if (StringUtils.isNotEmpty(smartSummary))
        {
            return limitLength(smartSummary, maxLength);
        }

        // 降级处理：快速移除HTML标签（避免HTMLFilter）
        String textOnly = htmlContent.replaceAll("<[^>]+>", "");

        // 解码HTML实体和清理空白
        textOnly = unescapeHtmlEntities(textOnly).replaceAll("\\s+", " ").trim();

        return limitLength(textOnly, maxLength);
    }

    /**
     * 提取HTML内容的纯文本概述（默认长度100）
     *
     * @param htmlContent HTML内容
     * @return 纯文本概述
     */
    public static String extractTextSummary(String htmlContent)
    {
        return extractTextSummary(htmlContent, 100);
    }

    /**
     * 快速智能解析 - 高性能版本
     *
     * @param htmlContent 原始HTML内容
     * @return 结构化的文本概述
     */
    private static String extractFastSmartSummary(String htmlContent)
    {
        if (StringUtils.isEmpty(htmlContent))
        {
            return "";
        }

        // 快速检测：如果包含产品通知标识，进行快速解析
        if (htmlContent.contains("product-title") || htmlContent.contains("status-badge"))
        {
            return extractProductInfoFast(htmlContent);
        }

        return "";
    }

    /**
     * 快速产品信息提取 - 单次遍历，高性能
     */
    private static String extractProductInfoFast(String htmlContent)
    {
        StringBuilder title = new StringBuilder();
        StringBuilder status = new StringBuilder();
        StringBuilder code = new StringBuilder();
        StringBuilder time = new StringBuilder();

        // 使用单次扫描和简单的字符串匹配，避免复杂正则
        String[] lines = htmlContent.split("(?=<)|(?<=>)");

        for (String line : lines)
        {
            line = line.trim();
            if (line.isEmpty()) continue;

            // 快速标题检测
            if (title.length() == 0 && line.contains("product-title"))
            {
                int start = line.indexOf('>') + 1;
                int end = line.indexOf('<', start);
                if (start > 0 && end > start)
                {
                    title.append(line.substring(start, end).trim());
                }
            }
            // 快速状态检测
            else if (status.length() == 0 && line.contains("status-badge"))
            {
                int start = line.indexOf('>') + 1;
                int end = line.indexOf('<', start);
                if (start > 0 && end > start)
                {
                    status.append(line.substring(start, end).trim());
                }
            }
            // 快速编码检测
            else if (code.length() == 0 && line.contains("产品编码"))
            {
                int colonIndex = line.indexOf('：');
                if (colonIndex == -1) colonIndex = line.indexOf(':');
                if (colonIndex != -1)
                {
                    String afterColon = line.substring(colonIndex + 1).trim();
                    int tagIndex = afterColon.indexOf('<');
                    if (tagIndex != -1)
                    {
                        code.append(afterColon.substring(0, tagIndex).trim());
                    }
                    else
                    {
                        code.append(afterColon);
                    }
                }
            }
            // 快速时间检测
            else if (time.length() == 0 && line.contains("操作时间"))
            {
                int colonIndex = line.indexOf('：');
                if (colonIndex == -1) colonIndex = line.indexOf(':');
                if (colonIndex != -1)
                {
                    String afterColon = line.substring(colonIndex + 1).trim();
                    int tagIndex = afterColon.indexOf('<');
                    if (tagIndex != -1)
                    {
                        time.append(afterColon.substring(0, tagIndex).trim());
                    }
                    else
                    {
                        time.append(afterColon);
                    }
                }
            }

            // 性能优化：如果已经提取到所有信息，提前退出
            if (title.length() > 0 && status.length() > 0 &&
                code.length() > 0 && time.length() > 0)
            {
                break;
            }
        }

        // 如果什么都没提取到，返回空
        if (title.length() == 0 && status.length() == 0 &&
            code.length() == 0 && time.length() == 0)
        {
            return "";
        }

        // 快速构建结果
        StringBuilder result = new StringBuilder();

        if (title.length() > 0)
        {
            result.append(title);
        }

        if (status.length() > 0)
        {
            if (result.length() > 0) result.append("：");
            result.append(status);
        }

        if (code.length() > 0)
        {
            if (result.length() > 0) result.append(" ");
            result.append("(编码：").append(code).append(")");
        }

        if (time.length() > 0)
        {
            if (result.length() > 0) result.append(" - ");
            result.append(time);
        }

        return result.toString();
    }

    
    
    /**
     * 限制文本长度
     *
     * @param text 原始文本
     * @param maxLength 最大长度
     * @return 限制后的文本
     */
    private static String limitLength(String text, int maxLength)
    {
        if (maxLength <= 0 || StringUtils.isEmpty(text))
        {
            return text;
        }

        if (text.length() <= maxLength)
        {
            return text;
        }

        return text.substring(0, maxLength) + "...";
    }

    /**
     * 解码HTML实体字符
     *
     * @param content 包含HTML实体的内容
     * @return 解码后的内容
     */
    private static String unescapeHtmlEntities(String content)
    {
        if (StringUtils.isEmpty(content))
        {
            return content;
        }

        // 解码常见的HTML实体
        content = content.replace("&nbsp;", " ");
        content = content.replace("&lt;", "<");
        content = content.replace("&gt;", ">");
        content = content.replace("&amp;", "&");
        content = content.replace("&quot;", "\"");
        content = content.replace("&#39;", "'");
        content = content.replace("&#x27;", "'");
        content = content.replace("&#x2F;", "/");
        content = content.replace("&#x3D;", "=");
        content = content.replace("&#x60;", "`");

        return content;
    }

    /**
     * Escape编码
     * 
     * @param text 被编码的文本
     * @return 编码后的字符
     */
    private static String encode(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return StringUtils.EMPTY;
        }

        final StringBuilder tmp = new StringBuilder(text.length() * 6);
        char c;
        for (int i = 0; i < text.length(); i++)
        {
            c = text.charAt(i);
            if (c < 256)
            {
                tmp.append("%");
                if (c < 16)
                {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            }
            else
            {
                tmp.append("%u");
                if (c <= 0xfff)
                {
                    // issue#I49JU8@Gitee
                    tmp.append("0");
                }
                tmp.append(Integer.toString(c, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码
     * 
     * @param content 被转义的内容
     * @return 解码后的字符串
     */
    public static String decode(String content)
    {
        if (StringUtils.isEmpty(content))
        {
            return content;
        }

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < content.length())
        {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos)
            {
                if (content.charAt(pos + 1) == 'u')
                {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                }
                else
                {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            }
            else
            {
                if (pos == -1)
                {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                }
                else
                {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    public static void main(String[] args)
    {
        String html = "<script>alert(1);</script>";
        String escape = EscapeUtil.escape(html);
        // String html = "<scr<script>ipt>alert(\"XSS\")</scr<script>ipt>";
        // String html = "<123";
        // String html = "123>";
        System.out.println("clean: " + EscapeUtil.clean(html));
        System.out.println("escape: " + escape);
        System.out.println("unescape: " + EscapeUtil.unescape(escape));
    }
}
