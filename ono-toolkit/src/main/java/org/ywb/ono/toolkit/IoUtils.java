package org.ywb.ono.toolkit;

import java.io.*;

/**
 * @author Yuwenbo1
 * @version 1.0
 * <p>
 * desc: 流转换的工具
 * [注意]该工具类中没有提供流关闭的功能。
 * @since 2019/1/21 20:51
 */
public final class IoUtils {

    private static final int KB = 1024 * 1024;

    /**
     * 将inputStream流中的内容转换成String
     *
     * @return string
     */
    public static String streamToString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        byte[] buf = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = bis.read(buf)) != -1) {
            String temp = new String(buf, 0, len);
            sb.append(temp);
        }
        return sb.toString();
    }

    /**
     * String转换成OutputStream
     *
     * @param data String
     * @return OutputStream
     * @throws IOException IO
     */
    public static OutputStream stringToStream(String data) throws IOException {
        if (data == null) {
            return null;
        }
        byte[] byteData = data.getBytes();
        int size = byteData.length / KB + 2;
        BufferedOutputStream bos = new BufferedOutputStream(new ByteArrayOutputStream(), size);
        bos.write(byteData);
        return bos;
    }


    /**
     * 将字符串保存到本地磁盘
     * tips: 如果指定的文件夹不存在，程序会自动创建文件夹，
     *
     * @param data 要保存的数据
     * @param path 保存的路径
     * @return 文件写入成功返回true，文件写入失败返回false
     */
    public static boolean stringToFile(String data, String path) {
        if (data == null || path == null || "".equals(data) || "".equals(path)) {
            throw new RuntimeException("非法传参");
        }
        boolean flag = false;

        BufferedWriter bw = null;
        try {
            File dir = new File(path.substring(0, path.lastIndexOf("/")));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            FileWriter fw = new FileWriter(path);
            bw = new BufferedWriter(fw);
            bw.write(data);
            bw.flush();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 将文件中的内容读到内存中
     *
     * @param filePath 文件路径
     * @return data
     */
    public static String fileToString(String filePath) {
        if (filePath == null) {
            throw new RuntimeException("文件名不能为空");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("没有找到指定的文件路径");
        }
        BufferedReader br = null;
        String data = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String buf;
            StringBuilder sb = new StringBuilder();
            while ((buf = br.readLine()) != null) {
                sb.append(buf);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 将输入流转换成字节数组,这里，字节数组的最大容量是8KB
     *
     * @param input inputStream
     * @return byte[]
     */
    public static byte[] streamToByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8 * KB];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 将字节数组中的内容写入到指定的文件
     *
     * @param data byte[]
     * @param path filePath
     * @return 成功返回true，失败返回false
     * @throws IOException IO
     */
    public static boolean byteArrayToFile(byte[] data, String path) throws IOException {
        if (data == null) {
            return false;
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
        bos.write(data);
        bos.flush();
        return true;
    }

}

