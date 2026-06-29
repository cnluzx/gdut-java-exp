package Ex2_2_3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 测试文件生成器
 * 用于创建用于测试的文件
 */
public class test_file {

    /**
     * 创建测试文件
     * @param filePath 文件路径
     * @param sizeMB 文件大小（MB）
     * @throws IOException 如果文件创建失败
     */
    public static void createTestFile(String filePath, int sizeMB) throws IOException {
        File file = new File(filePath);

        // 如果文件已存在，询问是否覆盖
        if (file.exists()) {
            System.out.println("文件 " + filePath + " 已存在，将覆盖它。");
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            // 计算需要写入的数据量
            long totalBytes = sizeMB * 1024L * 1024L;
            byte[] data = new byte[8192]; // 使用8KB缓冲区加快写入速度

            // 填充数据
            for (int i = 0; i < data.length; i++) {
                data[i] = (byte)(i % 256);
            }

            long written = 0;
            while (written < totalBytes) {
                int writeSize = (int)Math.min(data.length, totalBytes - written);
                fos.write(data, 0, writeSize);
                written += writeSize;

                // 显示进度（每10%显示一次）
                int progress = (int)(written * 100 / totalBytes);
                if (progress % 10 == 0 && progress > 0) {
                    System.out.print(".");
                }
            }

            System.out.println(); // 换行
            System.out.println("✓ 成功创建测试文件：" + filePath);
            System.out.println("  文件大小：" + sizeMB + " MB");

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // 忽略
                }
            }
        }
    }

    /**
     * 获取文件大小（MB）
     * @param filePath 文件路径
     * @return 文件大小（MB）
     */
    public static double getFileSizeMB(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.length() / (1024.0 * 1024.0);
        }
        return 0;
    }

    public static void main(String[] args) {
     
        System.out.println("字节流 vs 缓冲流 文件复制效率对比");
        System.out.println("==============================================");

        // 创建测试文件（10MB）
        String testFile = "D:\\test_copy.dat";
        try {
            System.out.println("正在创建10MB测试文件...");
            test_file.createTestFile(testFile, 10);
        } catch (IOException e) {
            System.out.println("创建测试文件失败：" + e.getMessage());
            return;
        }

        System.out.println("\n开始复制测试...\n");

        // 测试1：无缓冲流（调用 file_copy_compair 类的方法）
        String dest1 = "D:\\test_copy1.dat";
        System.out.println("【方法1】不使用缓冲流 (缓冲区1024字节)");
        long time1 = file_copy_compair.fileCopy1(testFile, dest1);  // ← 修改这里
        System.out.println("  ✅ 耗时：" + time1 + " ms");
        System.out.println("  文件大小：" + new File(dest1).length() / (1024.0 * 1024.0) + " MB\n");

        // 测试2：使用缓冲流（调用 file_copy_compair 类的方法）
        String dest2 = "D:\\test_copy2.dat";
        System.out.println("【方法2】使用缓冲流 (缓冲区1024字节)");
        long time2 = file_copy_compair.fileCopy2(testFile, dest2);  // ← 修改这里
        System.out.println("  ✅ 耗时：" + time2 + " ms");
        System.out.println("  文件大小：" + new File(dest2).length() / (1024.0 * 1024.0) + " MB\n");

        // 对比结果
        System.out.println("==============================================");
        System.out.println("📊 效率对比结果：");
        System.out.println("  不使用缓冲流：" + time1 + " ms");
        System.out.println("  使用缓冲流：" + time2 + " ms");

        if (time1 > time2) {
            double faster = (double) time1 / time2;
            System.out.println("  🎉 缓冲流快了 " + String.format("%.2f", faster) + " 倍！");
        } else if (time1 < time2) {
            double faster = (double) time2 / time1;
            System.out.println("  ⚠️ 普通流快了 " + String.format("%.2f", faster) + " 倍（系统波动可能影响结果）");
        } else {
            System.out.println("  两种方法耗时相同");
        }
        System.out.println("==============================================");
        System.out.println("💡 结论：缓冲流通过减少磁盘IO次数，通常比非缓冲流更快");
    }
}