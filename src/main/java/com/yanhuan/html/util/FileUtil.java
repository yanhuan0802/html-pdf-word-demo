package com.yanhuan.html.util;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件拷贝
 *
 * @author dpj
 */
@Slf4j
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 读取文件并将其写入到自定义文件中
     *
     * @param sourcePath 源文件路径：classpath下的相对路径
     * @param targetPath 目标文件路径：项目部署地方的绝对路径(docker部署则是容器中的绝对路径)
     * @return 目标文件
     */
    public static File readFile(String sourcePath, String targetPath) {
        ClassPathResource resource = new ClassPathResource(sourcePath);
        InputStream inputStream = resource.getStream();
        // 目标文件
        File target = new File(targetPath);
        if (target.exists()) {
            log.info("目标文件已存在========路径=======>:{}", target.getAbsolutePath());
        }

        // 校验文件夹是否存在,不存在则先创建文件夹再创建文件
        File fileParent = target.getParentFile();
        if (!fileParent.exists()) {
            boolean mkdir = fileParent.mkdir();
            if (!mkdir) {
                log.info("目标文件夹创建失败mkdir():{}", Boolean.FALSE);
            }
        }
        try {
            boolean newFile = target.createNewFile();
            if (newFile) {
                log.info("目标文件创建成功========路径=======>:{}", target.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将读取文件中的内容写入到目标文件中
        try (FileOutputStream fileOutputStream = new FileOutputStream(target)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }

    /**
     * 读取文件并将其写入到自定义文件中
     *
     * @param sourcePath 源文件路径：classpath下的相对路径
     * @param targetPath 目标文件路径：项目部署地方的绝对路径(docker部署则是容器中的绝对路径)
     * @return 目标文件的绝对路径
     */
    public static String readFilePath(String sourcePath, String targetPath) {
        File target = readFile(sourcePath, targetPath);
        // 获取目标文件所在的文件夹的绝对路径
        String path = target.getParentFile().getAbsolutePath();
        log.info("目标文件所在的文件夹的绝对路径=========>>>>>:{}", path);
        return path;
    }

}
