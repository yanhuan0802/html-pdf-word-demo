package com.yanhuan.html.util;


import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yan
 */
@Slf4j
public class PdfUtil {

    private PdfUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final String FONT_PATH = "/home/fonts/simsun.ttc";

    private static final String FONT_FAMILY = "SimSun";


    /**
     * 导出pdf
     *
     * @param response     httpResponse
     * @param pdfFileName  文件名
     * @param templateName 模板名
     * @param variables    模板替换map
     */
    public static void exportPdf(HttpServletResponse response, String pdfFileName, String templateName, Map<String, Object> variables) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(pdfFileName + ".pdf", StandardCharsets.UTF_8));
        OutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            log.error("从response获取输出流异常：{}", e.toString());
        }
        //获取html串
        String content = ThymeleafUtil.generateHtml(variables, templateName);
        //转换并下载
        PdfUtil.htmlToPdf(content, os);
    }


    /**
     * 批量导出pdf
     *
     * @param response     httpResponse
     * @param zipName      压缩包名称
     * @param templateName 模板名称
     * @param map          文件名-变量map
     */
    public static void exportPdfBatch(HttpServletResponse response, String zipName, String templateName, Map<String, Map<String, Object>> map) {
        Map<String, InputStream> inputMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            //内容生成
            String content = ThymeleafUtil.generateHtml(entry.getValue(), templateName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //转换为pdf
            PdfUtil.htmlToPdf(content, out);
            inputMap.put(entry.getKey() + ".pdf", new ByteArrayInputStream(out.toByteArray()));
        }
        try {
            //压缩下载
            ZipUtil.downLoadZip(inputMap, zipName, response);
        } catch (IOException e) {
            log.error("压缩下载异常：{}", e.toString());
        }
    }

    /**
     * html转为pdf  写入输出流
     *
     * @param content html内容
     * @param os      输出流
     */
    private static void htmlToPdf(String content, OutputStream os) {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFont(FileUtil.readFile("pdf/fonts/simsun.ttc", FONT_PATH), FONT_FAMILY);
        builder.withHtmlContent(content, null);
        builder.toStream(os);
        try {
            builder.run();
        } catch (IOException e) {
            log.error("html转pdf异常：{}", e.toString());
        }
    }
}
