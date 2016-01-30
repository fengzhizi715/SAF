package cn.salesuite.saf.utils;

import java.io.File;

/**
 * Created by Tony Shen on 16/1/30.
 */
public class FileUtils {

    /**
     * 判断是否文件
     * @param file
     * @return
     */
    public static boolean isFile(File file) {
        return file!=null && file.exists() && file.isFile();
    }

    /**
     * 判断是否目录
     * @param file
     * @return
     */
    public static boolean isDirectory(File file) {
        return file!=null && file.exists() && file.isDirectory();
    }
}
