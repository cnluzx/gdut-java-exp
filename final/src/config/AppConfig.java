package config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 应用配置 —— JSON 持久化 IDE 设置
 * 单例模式，存储在 config/ide.json
 */
public class AppConfig {

    private static final File CONFIG_DIR  = new File("config");
    private static final File CONFIG_FILE = new File(CONFIG_DIR, "ide.json");

    private static AppConfig instance;

    private String jdkPath;          // JDK 安装路径
    private String lastProjectPath;  // 最近打开的项目路径
    private String recentFiles;      // 最近文件列表（逗号分隔）
    private String themeName;        // 主题名称
    private int    windowWidth  = 900;
    private int    windowHeight = 650;

    // ==================== 单例 ====================

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
            instance.load();
        }
        return instance;
    }

    // ==================== JSON 读写 ====================

    /** 从 config/ide.json 加载配置 */
    private void load() {
        if (!CONFIG_FILE.exists()) {
            ensureDir();
            save();
            return;
        }
        try {
            String json = new String(Files.readAllBytes(CONFIG_FILE.toPath()),
                    StandardCharsets.UTF_8);
            parseJson(json);
        } catch (Exception e) {
            System.err.println("[配置] 加载失败: " + e.getMessage());
        }
    }

    /** 保存配置到 config/ide.json */
    public void save() {
        ensureDir();
        try (PrintWriter pw = new PrintWriter(
                new OutputStreamWriter(Files.newOutputStream(CONFIG_FILE.toPath()),
                        StandardCharsets.UTF_8))) {
            pw.print(toJson());
            pw.flush();
        } catch (Exception e) {
            System.err.println("[配置] 保存失败: " + e.getMessage());
        }
    }

    /** 重置为默认值 */
    public void reset() {
        jdkPath = null;
        lastProjectPath = null;
        recentFiles = null;
        themeName = null;
        windowWidth  = 900;
        windowHeight = 650;
        save();
    }

    // ==================== 简易 JSON 解析 ====================

    /** 解析 JSON 字符串并填充字段 */
    private void parseJson(String json) {
        jdkPath         = extract(json, "jdkPath");
        lastProjectPath = extract(json, "lastProjectPath");
        recentFiles     = extract(json, "recentFiles");
        themeName       = extract(json, "themeName");
        windowWidth     = extractInt(json, "windowWidth", 900);
        windowHeight    = extractInt(json, "windowHeight", 650);
    }

    /** 序列化为 JSON 字符串 */
    private String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"jdkPath\": \"").append(esc(jdkPath)).append("\",\n");
        sb.append("  \"lastProjectPath\": \"").append(esc(lastProjectPath)).append("\",\n");
        sb.append("  \"recentFiles\": \"").append(esc(recentFiles)).append("\",\n");
        sb.append("  \"themeName\": \"").append(esc(themeName)).append("\",\n");
        sb.append("  \"windowWidth\": ").append(windowWidth).append(",\n");
        sb.append("  \"windowHeight\": ").append(windowHeight).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /** 从 JSON 中提取字符串值 */
    private String extract(String json, String key) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return "";
        i = json.indexOf(':', i) + 1;
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == '\t')) i++;
        if (i >= json.length() || json.charAt(i) != '"') return "";
        int j = json.indexOf('"', i + 1);
        return (j > i) ? unesc(json.substring(i + 1, j)) : "";
    }

    /** 从 JSON 中提取整数值 */
    private int extractInt(String json, String key, int def) {
        int i = json.indexOf("\"" + key + "\"");
        if (i < 0) return def;
        i = json.indexOf(':', i) + 1;
        StringBuilder num = new StringBuilder();
        while (i < json.length()) {
            char ch = json.charAt(i);
            if (ch >= '0' && ch <= '9' || ch == '-') num.append(ch);
            else if (num.length() > 0) break;
            i++;
        }
        try { return Integer.parseInt(num.toString()); }
        catch (NumberFormatException e) { return def; }
    }

    /** JSON 字符串转义 */
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");
    }

    /** JSON 字符串反转义 */
    private String unesc(String s) {
        return s.replace("\\\"", "\"").replace("\\n", "\n").replace("\\t", "\t").replace("\\\\", "\\");
    }

    private void ensureDir() { CONFIG_DIR.mkdirs(); }

    // ==================== getter / setter ====================

    public String getJdkPath()             { return jdkPath; }
    public void   setJdkPath(String v)     { this.jdkPath = v; save(); }

    public String getLastProjectPath()     { return lastProjectPath; }
    public void   setLastProjectPath(String v) { this.lastProjectPath = v; save(); }

    public String getRecentFiles()         { return recentFiles; }
    public void   setRecentFiles(String v) { this.recentFiles = v; save(); }

    public String getThemeName()           { return themeName; }
    public void   setThemeName(String v)   { this.themeName = v; save(); }

    public int  getWindowWidth()           { return windowWidth; }
    public void setWindowWidth(int v)      { this.windowWidth = v; save(); }

    public int  getWindowHeight()          { return windowHeight; }
    public void setWindowHeight(int v)     { this.windowHeight = v; save(); }

    /** 获取 JDK 路径的 File 对象（可能为 null） */
    public File getJdkFile() {
        return (jdkPath != null && !jdkPath.isEmpty()) ? new File(jdkPath) : null;
    }

    public static File getConfigDir() { return CONFIG_DIR; }
}
