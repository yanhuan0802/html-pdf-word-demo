package com.yanhuan.html.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * html转word工具类
 *
 * @author Yan
 */
@Slf4j
public class WordUtil {

    private WordUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 导出word文档
     *
     * @param response     httpResponse
     * @param wordFileName 文件名
     * @param templateName 模板名
     * @param variables    模板替换map
     */
    public static void exportDoc(HttpServletResponse response, String wordFileName, String templateName, Map<String, Object> variables) {
        //输出文件
        response.setCharacterEncoding("utf-8");
        //设置word格式
        response.setContentType("application/msword");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(wordFileName + ".doc", StandardCharsets.UTF_8));
        OutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            log.error("从response获取输出流异常：{}", e.toString());
        }
        //获取html串
        String content = ThymeleafUtil.generateHtml(variables, templateName);
        //转换并下载
        WordUtil.htmlToDoc(wordFileName, content, os);
    }


    /**
     * 批量压缩导出word文档
     *
     * @param response     httpResponse
     * @param zipName      压缩包名称
     * @param templateName 模板名称
     * @param map          文件名-变量map
     */
    public static void exportDocBatch(HttpServletResponse response, String zipName, String templateName, Map<String, Map<String, Object>> map) {
        Map<String, InputStream> inputMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            //html生成
            String content = ThymeleafUtil.generateHtml(entry.getValue(), templateName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //转换为word
            WordUtil.htmlToDoc(entry.getKey(), content, out);
            inputMap.put(entry.getKey() + ".doc", new ByteArrayInputStream(out.toByteArray()));
        }
        try {
            ZipUtil.downLoadZip(inputMap, zipName, response);
        } catch (IOException e) {
            log.error("压缩下载异常：{}", e.toString());
        }
    }

    /**
     * html转为word  写入输出流
     *
     * @param docName 文件名称
     * @param content html内容
     * @param os      输出流
     */
    private static void htmlToDoc(String docName, String content, OutputStream os) {
        //将字节数组包装到流中 这里是必须要设置编码的，不然导出中文就会乱码。
        ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        //生成word
        POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        try {
            directory.createDocument(docName, bais);
            poifs.writeFilesystem(os);
            bais.close();
            os.close();
        } catch (IOException e) {
            log.error("html转word文档异常：{}", e.toString());
        } finally {
            try {
                poifs.close();
            } catch (IOException e) {
                log.error("流关闭异常：{}", e.toString());
            }
        }
    }
}
