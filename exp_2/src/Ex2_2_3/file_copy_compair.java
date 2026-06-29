package Ex2_2_3;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件复制工具类
 * 提供使用缓冲流和非缓冲流的文件复制功能
 */
public class file_copy_compair {

    /**
     * 不使用字节缓冲流的文件复制
     * @param srcPath 源文件路径
     * @param destPath 目标文件路径
     * @return 执行消耗的时间（毫秒）
     */

    //流程是创建输入输出流
    public static long fileCopy1(String srcPath, String destPath) {
        long startTime = System.currentTimeMillis();//记录启动时间

        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(srcPath);
            fos = new FileOutputStream(destPath);//和上面的一起

            byte[] buffer = new byte[1024];
            int len;

            // 循环读取并写入
            while ((len = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }

        } catch (IOException e) {
            System.err.println("文件复制失败（无缓冲流）：" + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeQuietly(fis);
            closeQuietly(fos);
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * 使用字节缓冲流的文件复制
     * @param srcPath 源文件路径
     * @param destPath 目标文件路径
     * @return 执行消耗的时间（毫秒）
     */
    public static long fileCopy2(String srcPath, String destPath) {
        long startTime = System.currentTimeMillis();

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(srcPath));
            bos = new BufferedOutputStream(new FileOutputStream(destPath));

            byte[] buffer = new byte[1024];
            int len;

            // 循环读取并写入
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            // 刷新缓冲区，确保所有数据都写入
            bos.flush();

        } catch (IOException e) {
            System.err.println("文件复制失败（使用缓冲流）：" + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeQuietly(bis);
            closeQuietly(bos);
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    /**
     * 安静关闭输入流（不抛出异常）
     * @param is 输入流
     */
    private static void closeQuietly(FileInputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }

    /**
     * 安静关闭输出流（不抛出异常）
     * @param os 输出流
     */
    private static void closeQuietly(FileOutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }

    /**
     * 安静关闭缓冲输入流（不抛出异常）
     * @param bis 缓冲输入流
     */
    private static void closeQuietly(BufferedInputStream bis) {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }

    /**
     * 安静关闭缓冲输出流（不抛出异常）
     * @param bos 缓冲输出流
     */
    private static void closeQuietly(BufferedOutputStream bos) {
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                // 忽略关闭异常
            }
        }
    }
}