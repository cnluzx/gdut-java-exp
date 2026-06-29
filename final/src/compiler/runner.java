package compiler;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * IDE 运行控制台
 * 整合 cmd(显示) + compile(编译) + run(运行)
 * 通过底部输入栏接受命令: compile / run / all / clear / help
 */
public class runner extends cmd {

    private final compile compiler;
    private File javaPath;
    private String lastCompileDir;

    // 编译完成回调（null = 无回调）
    private CompileCallback onCompileDone;

    /** 编译完成回调接口 */
    public interface CompileCallback {
        void onCompiled(String filePath, boolean success, String output);
    }

    public void setCompileCallback(CompileCallback cb) { this.onCompileDone = cb; }

    public runner() {
        super();
        this.compiler = new compile();
        appendInfo("[系统] 就绪, 输入 help 查看命令\n");
    }

    public void setJdkPath(File path) {
        compiler.setJdkPath(path);
        this.javaPath = path;
    }

    @Override
    public void onCommand(String input) {
        String s = input.trim();
        if (s.isEmpty()) return;

        switch (s.toLowerCase()) {
            case "help":
                appendInfo("[命令] compile <文件> / run / all <文件> / clear / help\n");
                appendInfo("  示例: compile Hello.java\n");
                appendInfo("  示例: all Hello.java\n");
                break;
            case "run":
                runLastClass();
                break;
            case "clear":
                clear();
                appendInfo("[系统] 已清屏\n");
                break;
            default:
                if (s.startsWith("compile ")) {
                    compileFile(s.substring(8).trim());
                } else if (s.startsWith("all ")) {
                    compileFile(s.substring(4).trim());
                    runLastClass();
                } else {
                    appendErr("[错误] 未知命令: " + s + "\n");
                }
        }
    }

    /** 编译指定文件路径 */
    private void compileFile(String filePath) {
        File src = new File(filePath);
        // 如果是相对路径, 补全为绝对路径
        if (!src.isAbsolute()) {
            src = new File(System.getProperty("user.dir"), filePath);
        }

        appendInfo("[编译] " + src.getName() + "\n");
        compile.CompileResult r = compiler.compile(src);

        if (r.isSuccess()) {
            appendOut(r.getOutput() + "\n");
            lastCompileDir = src.getParent();
        } else {
            appendErr(r.getError() + "\n");
        }
        if (onCompileDone != null) {
            onCompileDone.onCompiled(src.getAbsolutePath(), r.isSuccess(), r.getFullMessage());
        }
    }

    /** 运行最近一次编译产出的 class */
    private void runLastClass() {
        if (lastCompileDir == null) {
            appendErr("[错误] 请先执行编译 (compile <文件>)\n");
            return;
        }
        // 递归查找目录下最新的 .class 文件
        File dir = new File(lastCompileDir);
        File newest = findNewestClass(dir);
        if (newest == null) {
            appendErr("[错误] 目录下没有 .class 文件\n");
            return;
        }
        runClass(newest);
    }

    /** 递归查找目录下最新的 .class 文件 */
    private File findNewestClass(File dir) {
        File newest = null;
        File[] files = dir.listFiles();
        if (files == null) return null;
        for (File f : files) {
            if (f.isDirectory()) {
                File sub = findNewestClass(f);
                if (sub != null && (newest == null || sub.lastModified() > newest.lastModified())) {
                    newest = sub;
                }
            } else if (f.getName().endsWith(".class")) {
                if (newest == null || f.lastModified() > newest.lastModified()) {
                    newest = f;
                }
            }
        }
        return newest;
    }

    /** 运行指定 class 文件，自动推导包名 */
    public void runClass(File classFile) {
        // 从 classFile 路径推导全限定类名
        // 相对 lastCompileDir 的路径去掉 .class 后缀
        String classPath = classFile.getAbsolutePath();
        String basePath = new File(lastCompileDir).getAbsolutePath();
        String relative;
        if (classPath.startsWith(basePath)) {
            relative = classPath.substring(basePath.length());
            // 去掉开头可能的分隔符
            if (relative.startsWith(File.separator)) {
                relative = relative.substring(1);
            }
        } else {
            relative = classFile.getName();
        }
        relative = relative.replace(File.separator, ".");
        if (relative.endsWith(".class")) {
            relative = relative.substring(0, relative.length() - 6);
        }
        String fullClassName = relative;

        String java = (javaPath != null)
                ? new File(javaPath, "bin/java.exe").getAbsolutePath()
                : "java";

        appendInfo("[运行] " + fullClassName + "\n");

        // 构建完整 classpath：项目根 out 目录 + 源码输出目录
        String projectOut = new File("out").getAbsolutePath();
        String cp = projectOut + File.pathSeparator + lastCompileDir;
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    java, "-cp", cp, fullClassName);
            pb.directory(new File(lastCompileDir));
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // Windows 下 java 输出是 GBK，用 GBK 读取
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                appendOut(line + "\n");
            }

            int exitCode = p.waitFor();
            if (exitCode != 0) {
                appendErr("[运行结束] 退出码: " + exitCode + "\n");
            }
        } catch (Exception e) {
            appendErr("[异常] " + e.getMessage() + "\n");
        }
    }
}
