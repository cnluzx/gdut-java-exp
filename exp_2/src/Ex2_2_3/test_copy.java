package Ex2_2_3;

import java.io.File;
import java.io.IOException;

/**
 * 测试缓冲流 vs 非缓冲流文件复制效率
 */
public class test_copy {
    public static void main(String[] args) {
      
        System.out.println("缓冲流 vs 非缓冲流 文件复制效率对比");
        System.out.println("==============================================");

        // 1. 创建测试文件（10MB）
        String sourceFile = "D:\\test_source.dat";
        try {
            System.out.println("正在创建10MB测试文件...");
            test_file.createTestFile(sourceFile, 10);//创建代码
        } catch (IOException e) {
            System.out.println("创建测试文件失败：" + e.getMessage());
            return;
        }

        // 2. 检查文件是否存在
        File src = new File(sourceFile);
        if (!src.exists()) {
            System.out.println("源文件不存在！");
            return;
        }
        System.out.println("源文件大小：" + String.format("%.2f", src.length() / (1024.0 * 1024.0)) + " MB\n");

        // 3. 测试无缓冲流复制
        String dest1 = "D:\\test_dest1.dat";
        System.out.println("【测试1】不使用缓冲流复制");
        long time1 = file_copy_compair.fileCopy1(sourceFile, dest1);
        System.out.println("  耗时：" + time1 + " ms");
        File destFile1 = new File(dest1);
        if (destFile1.exists()) {
            System.out.println("  目标文件大小：" + String.format("%.2f", destFile1.length() / (1024.0 * 1024.0)) + " MB");
        }
        System.out.println();

        // 4. 测试有缓冲流复制
        String dest2 = "D:\\test_dest2.dat";
        System.out.println("【测试2】使用缓冲流复制");
        long time2 = file_copy_compair.fileCopy2(sourceFile, dest2);
        System.out.println("  耗时：" + time2 + " ms");
        File destFile2 = new File(dest2);
        if (destFile2.exists()) {
            System.out.println("  目标文件大小：" + String.format("%.2f", destFile2.length() / (1024.0 * 1024.0)) + " MB");
        }
        System.out.println();

        // 5. 效率对比
        System.out.println("==============================================");
        System.out.println("📊 效率对比结果：");
        System.out.println("  不使用缓冲流：" + time1 + " ms");
        System.out.println("  使用缓冲流：" + time2 + " ms");

        if (time1 > time2) {
            double ratio = (double) time1 / time2;
            System.out.println("  ✅ 缓冲流快了 " + String.format("%.2f", ratio) + " 倍！");
            System.out.println("  💡 原因：缓冲流减少了磁盘IO次数，提高了效率");
        } else if (time1 < time2) {
            double ratio = (double) time2 / time1;
            System.out.println("  ⚠️ 非缓冲流反而快了 " + String.format("%.2f", ratio) + " 倍");
            System.out.println("  💡 可能原因：文件太小或系统缓存影响");
        } else {
            System.out.println("  两种方法耗时相同");
        }
        System.out.println("==============================================");

        // 6. 清理文件（可选）
        System.out.println("\n是否删除测试文件？(y/n)");
        // 如果需要自动清理，取消注释下面的代码
        // new File(sourceFile).delete();
        // new File(dest1).delete();
        // new File(dest2).delete();
        // System.out.println("测试文件已清理");
    }
}