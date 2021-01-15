package com.yanhuan.html.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;

/**
 * Thymeleaf模板处理工具类
 *
 * @author Yan
 */
public class ThymeleafUtil {

    private ThymeleafUtil() {
        throw new IllegalStateException("Utility class");
    }


    /**
     * html生成
     *
     * @param variables    变量map
     * @param templateName 模板名称
     * @return 渲染后的html串
     */
    public static String generateHtml(Map<String, Object> variables, String templateName) {
        //构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        //模板所在目录，相对于当前classloader的classpath。
        resolver.setPrefix("/pdf/templates/");
        //模板文件后缀
        resolver.setSuffix(".html");
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        //构造上下文(Model)
        Context context = new Context();
        context.setVariables(variables);
        //渲染模板
        return templateEngine.process(templateName, context);
    }

    /**
     * 将两边带双引号及文中带转义符号的富文本框内容转为html串
     *
     * @param input 富文本框内容
     * @return html串
     */
    public static String richTextToHtmlStr(String input) {
        if (input == null || input.length() < 3) {
            return StringUtils.EMPTY;
        }
        return StringEscapeUtils.unescapeJava(input.substring(1, input.length() - 1));
    }


}
