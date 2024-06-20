package top.meethigher.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 对文件进行分片与合成
 *
 * @author chenchuancheng
 * @since 2024/06/18 21:13
 */
@Slf4j
public class FileFragment {

    private final int bufferSize = 1024 * 1024;// 在切片时加载内存里的数据缓冲区大小

    private final String delimiter = "."; // 分割符

    private final int DEFAULT_CHUNK_SIZE = 1024 * 1024; // 默认分片大小为1MB

    private final String EXTENSION = delimiter + "part";// 文件扩展名

    private final String SPLIT_FILENAME_TEMPLATE = "{order}" + delimiter + "{originFileName}{extension}";// 文件分片后的小文件名称

    /**
     * 将文件分割成若干小文件
     *
     * @param filePath  文件路径
     * @param chunkSize 小文件的大小
     * @return 小文件的名称
     */
    public List<String> splitFile(String filePath, int chunkSize) {
        // TODO: 2024/6/20 使用缓冲区进行分片
        // TODO: 2024/6/20 使用多线程进行分片
        List<String> list = new ArrayList<>();
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[chunkSize];
                int bytesRead;
                int chunkNumber = 0;
                while ((bytesRead = fis.read(buffer)) > 0) {
                    //分片的文件名
                    String chunkFile = SPLIT_FILENAME_TEMPLATE.replace("{originFileName}", file.getName())
                            .replace("{order}", String.format("%03d", ++chunkNumber))
                            .replace("{extension}", EXTENSION);
                    String targetChunkFilePath = file.getAbsolutePath().replace(file.getName(), chunkFile);
                    try (FileOutputStream fos = new FileOutputStream(targetChunkFilePath)) {
                        fos.write(buffer, 0, bytesRead);
                        fos.flush();
                    }
                    log.info("{} is created", chunkFile);
                    list.add(chunkFile);
                }
                log.info("file split success, the total number of part files is {}", chunkNumber);
            } catch (Exception e) {
                log.error("split file error, " + e.toString());
            }
        } else {
            log.error("file not exist");
        }
        return list;
    }


    /**
     * 将文件分割成若干小文件，小文件大小默认为1MB
     *
     * @param filePath 文件路径
     * @return 小文件的名称
     */
    public List<String> splitFile(String filePath) {
        return splitFile(filePath, DEFAULT_CHUNK_SIZE);
    }

    /**
     * 将多个小文件合并成大文件
     *
     * @param directoryPath 小文件目录路径
     * @param fileName      合成后的文件名称, 含文件扩展名
     * @return 文件名称
     */
    public String mergeFiles(String directoryPath, String fileName) {
        // TODO: 2024/6/20 使用缓冲区进行合并，否则容易把内存干崩
        File dir = new File(directoryPath);
        String mergeFileName = fileName;
        if (!(dir.exists() && dir.isDirectory())) {
            log.error("directory not exist");
            return null;
        }
        File[] files = dir.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(EXTENSION));
        if (!(files != null && files.length > 0)) {
            log.error("part file not exist");
            return null;
        }
        if (mergeFileName == null) {
            //按照规范截取出原文件名
            String tempName = files[0].getName();
            mergeFileName = tempName.substring(tempName.indexOf(delimiter) + 1, tempName.lastIndexOf(delimiter));
        }
        // TODO: 2024/6/20 按顺序合并
        //保存到原目录下
        try (FileOutputStream fos = new FileOutputStream(mergePath(directoryPath, mergeFileName))) {
            for (File file : files) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[(int) file.length()];
                    fis.read(buffer);
                    fos.write(buffer);
                    fos.flush();
                }
            }
        } catch (Exception e) {
            log.error("failed to merge files, " +e.toString());
            return null;
        }
        log.info("merge files success");
        //合并后的善后操作
        for (File file : files) {
            file.delete();
        }
        log.info("part files delete success");
        log.info("target merge file name is {}", mergeFileName);
        return mergeFileName;
    }

    /**
     * 将多个小文件合并成大文件
     *
     * @param directoryPath 小文件目录路径
     * @return 文件名称
     */
    public String mergeFiles(String directoryPath) {
        return mergeFiles(directoryPath, null);
    }

    /**
     * 获取合并后的路径
     *
     * @param directoryPath 目录路径
     * @param fileName      文件名
     * @return 文件的全路径
     */
    private String mergePath(String directoryPath, String fileName) {
        File dir = new File(directoryPath, fileName);
        return dir.getAbsolutePath();
    }

}
