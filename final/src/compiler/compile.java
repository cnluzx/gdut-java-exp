package compiler;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Java 编译器
 * 职责：调用 javac 编译 Java 源文件，返回编译结果
 * 独立类，不依赖任何 GUI 组件
 */
public class compile {

    private File jdkPath;  // JDK 安装目录（null = 使用系统 PATH）

    /**
     * 构造方法
     */
    public compile() {}

    /**
     * 设置 JDK 路径
     * @param path JDK 安装目录，传 null 则使用系统 PATH 中的 javac
     */
    public void setJdkPath(File path) { this.jdkPath = path; }

    /**
     * 编译指定的 .java 文件
     * @param sourceFile 要编译的源文件
     * @return 编译结果对象
     */
    public CompileResult compile(File sourceFile) {
        if (!sourceFile.exists()) {
            return new CompileResult(false, null,
                    "文件不存在: " + sourceFile.getAbsolutePath());
        }

        String javac = buildJavacCommand();

        try {
            // 构建 classpath：源文件目录 + 项目 out 目录
            String cp = sourceFile.getParent() + File.pathSeparator + "out";
            ProcessBuilder pb = new ProcessBuilder(
                    javac,
                    "-encoding", "UTF-8",
                    "-cp", cp,
                    "-d", sourceFile.getParent(),
                    sourceFile.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Windows 下 javac 输出是 GBK，用 GBK 读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), Charset.forName("GBK"))
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            String outputStr = output.toString().trim();
            boolean success = (exitCode == 0);

            if (success) {
                if (outputStr.isEmpty()) {
                    outputStr = "编译成功: " + sourceFile.getName();
                }
                return new CompileResult(true, outputStr, null);
            } else {
                return new CompileResult(false, null,
                        "编译失败 (退出码: " + exitCode + ")\n" + outputStr);
            }

        } catch (Exception e) {
            return new CompileResult(false, null,
                    "编译异常: " + e.getMessage());
        }
    }

    /**
     * 构建 javac 命令路径
     */
    private String buildJavacCommand() {
        if (jdkPath != null) {
            return new File(jdkPath, "bin/javac.exe").getAbsolutePath();
        } else {
            return "javac";
        }
    }

    // ==================== 内部类：编译结果 ====================

    /**
     * 编译结果
     */
    public static class CompileResult {
        private final boolean success;
        private final String output;   // 标准输出（成功时）
        private final String error;    // 错误信息（失败时）

        public CompileResult(boolean success, String output, String error) {
            this.success = success;
            this.output = output;
            this.error = error;
        }

        /** 编译是否成功 */
        public boolean isSuccess()  { return success; }

        /** 获取标准输出（编译成功时） */
        public String  getOutput()  { return output; }

        /** 获取错误信息（编译失败时） */
        public String  getError()   { return error; }

        /** 判断是否有错误信息 */
        public boolean hasError()   { return error != null && !error.isEmpty(); }

        /** 判断是否有正常输出 */
        public boolean hasOutput()  { return output != null && !output.isEmpty(); }

        /** 获取完整输出 */
        public String getFullMessage() {
            if (success) return output != null ? output : "编译成功";
            else         return error  != null ? error  : "编译失败";
        }

        @Override
        public String toString() { return getFullMessage(); }
    }
}
