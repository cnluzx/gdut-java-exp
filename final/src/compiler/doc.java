package compiler;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Javadoc 文档生成器
 * 调用 javadoc 命令为项目源码生成 API 文档
 */
public class doc {

    private File jdkPath;

    public doc() {}

    /**
     * 设置 JDK 路径
     * @param path JDK 安装目录，传 null 则使用系统 PATH 中的 javadoc
     */
    public void setJDKpath(File path) { this.jdkPath = path; }

    /**
     * 为指定源码目录生成 Javadoc
     * @param sourceDir   源码根目录
     * @param outputDir   文档输出目录
     * @param subpackages 要生成的子包名，冒号分隔（如 "compiler:editor:file"），传 null 则生成全部
     * @return 生成结果
     */
    public DocResult generate(File sourceDir, File outputDir, String subpackages) {
        if (!sourceDir.exists()) {
            return new DocResult(false, "源码目录不存在: " + sourceDir.getAbsolutePath());
        }

        String javadoc = buildJavadocCommand();

        // 未指定子包则默认生成全部项目包
        String packages = (subpackages != null && !subpackages.isEmpty())
                ? subpackages
                : "compiler:config:editor:file:style:ui";

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    javadoc,
                    "-encoding", "UTF-8",
                    "-charset", "UTF-8",
                    "-d", outputDir.getAbsolutePath(),
                    "-sourcepath", sourceDir.getAbsolutePath(),
                    "-subpackages", packages
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Windows 下 javadoc 输出是 GBK，用 GBK 读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.forName("GBK"))
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            String msg = output.toString().trim();

            if (exitCode == 0) {
                return new DocResult(true, "Javadoc 生成成功\n输出目录: " + outputDir.getAbsolutePath());
            } else {
                return new DocResult(false, "Javadoc 生成失败 (退出码: " + exitCode + ")\n" + msg);
            }

        } catch (Exception e) {
            return new DocResult(false, "Javadoc 异常: " + e.getMessage());
        }
    }

    /**
     * 为单个 Java 文件生成 Javadoc
     * @param sourceFile 源文件
     * @param outputDir  输出目录
     * @return 生成结果
     */
    public DocResult generateForFile(File sourceFile, File outputDir) {
        if (!sourceFile.exists()) {
            return new DocResult(false, "文件不存在: " + sourceFile.getAbsolutePath());
        }

        String javadoc = buildJavadocCommand();

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    javadoc,
                    "-encoding", "UTF-8",
                    "-charset", "UTF-8",
                    "-d", outputDir.getAbsolutePath(),
                    sourceFile.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.forName("GBK"))
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            String msg = output.toString().trim();

            if (exitCode == 0) {
                return new DocResult(true, "Javadoc 生成成功: " + sourceFile.getName());
            } else {
                return new DocResult(false, "Javadoc 生成失败 (退出码: " + exitCode + ")\n" + msg);
            }

        } catch (Exception e) {
            return new DocResult(false, "Javadoc 异常: " + e.getMessage());
        }
    }

    private String buildJavadocCommand() {
        if (jdkPath != null) {
            return new File(jdkPath, "bin/javadoc.exe").getAbsolutePath();
        } else {
            return "javadoc";
        }
    }

    // ==================== 内部类：生成结果 ====================

    /**
     * Javadoc 生成结果
     */
    public static class DocResult {
        private final boolean success;
        private final String  message;

        public DocResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        /** 生成是否成功 */
        public boolean isSuccess()  { return success; }

        /** 获取结果消息 */
        public String  getMessage() { return message; }

        @Override
        public String  toString()  { return message; }
    }
}
