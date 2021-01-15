package com.yanhuan.html.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 *
 * @author Yan
 */
@Slf4j
public class ZipUtil {

    private ZipUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 多个输入流压缩下载
     *
     * @param map      文件名和输入流对应的map集合
     * @param zipName  压缩包名称
     * @param response http响应对象
     */
    public static void downLoadZip(Map<String, InputStream> map, String zipName, HttpServletResponse response) throws IOException {
        //设置content-disposition响应头控制浏览器弹出保存框，若没有此句则浏览器会直接打开并显示文件。
        //中文名要经过URLEncoder.encode编码，否则虽然客户端能下载但显示的名字是乱码
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(zipName + ".zip", StandardCharsets.UTF_8));

        //response输出流转化为zip流
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

        //循环取出输入流
        for (Map.Entry<String, InputStream> entry : map.entrySet()) {
            byte[] buf = new byte[1024 * 8];
            int len = 0;
            try (InputStream bis = new BufferedInputStream(entry.getValue(), buf.length)) {
                //创建ZIP实体，并添加进压缩包
                String fileId = entry.getKey();
                ZipEntry zipEntry = new ZipEntry(fileId);
                zos.putNextEntry(zipEntry);
                while ((len = bis.read(buf)) != -1) {
                    //使用OutputStream将缓冲区的数据输出到客户端浏览器
                    zos.write(buf, 0, len);
                }
                zos.flush();
                zos.closeEntry();
            } catch (IOException e) {
                log.error("zip包压缩失败");
            }
        }
        response.flushBuffer();
        zos.close();
    }
}
