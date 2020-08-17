package com.cy.http.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String getNameFromUrl(String url) {
        //file name太长无法创建文件
        if(url.length()>=100)url=url.substring(url.length()-100,url.length());
        if(url.charAt(0)=='.')url=url.substring(1,url.length());
        int index = url.lastIndexOf("/");
        if (index < 0) return url;
        return url.substring(index+1, url.length());
    }
    public static String getName(String path) {
        int index = path.lastIndexOf("/");
        if (index < 0) return path;
        return path.substring(index+1, path.length());
    }
    /**
     * 根据路径 创建文件
     *
     * @param pathFile
     * @return
     * @throws IOException
     */
    public static File createFile(String pathFile) throws IOException {
        File fileDir = new File(pathFile.substring(0, pathFile.lastIndexOf(File.separator)));
        File file = new File(pathFile);
        if (!fileDir.exists()) fileDir.mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @return
     */
    public static boolean deleteFolder(String filePath) {
        File dirFile = new File(filePath);
        if (!dirFile.exists()) return false;
        if (dirFile.isDirectory()) {
            // 如果下面还有文件
            for (File file : dirFile.listFiles()) {
                deleteFolder(file.getAbsolutePath());
            }
        }
        return dirFile.delete();
    }
    public static String getParentPath(String pathFile) {
        return pathFile.substring(0, pathFile.lastIndexOf("/"));
    }

    /**
     * 查找文件夹下所有指定文件(含部分字符串的),文件夹也可以
     *
     * @param path
     * @return 路径
     */
    public static List<String> listFileByNameFlag(String path, String flag) {
        List<String> list = new ArrayList<>();
        File fileRoot = new File(path);
        if (!fileRoot.exists()) return list;
        if (fileRoot.isDirectory()) {
            for (File file : fileRoot.listFiles()) {
                if (file.getName().contains(flag)) list.add(file.getAbsolutePath());
            }
        }
        return list;
    }


}