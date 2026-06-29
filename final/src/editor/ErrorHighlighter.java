package editor;

import style.theme;

import javax.swing.text.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编译错误高亮器 —— 解析 javac 错误信息，在对应行标红加下划线
 */
public class ErrorHighlighter {

    // 红色背景 + 波浪下划线
    private final SimpleAttributeSet styleError;

    /** 一条解析出的错误信息 */
    public static class ErrorInfo {
        public final int    line;      // 行号 (1-based)
        public final String message;   // 错误描述
        public final String file;      // 源文件名

        public ErrorInfo(String file, int line, String message) {
            this.file = file;
            this.line = line;
            this.message = message;
        }
    }

    // javac 输出格式: 文件名:行号: 错误: 描述
    // 或: 路径\文件名.java:行号: 错误:...
    private static final Pattern JAVAC_ERROR = Pattern.compile(
        "([^:]+?\\.java):(\\d+):\\s*(错误|警告|error|warning):\\s*(.+)");

    public ErrorHighlighter() {
        styleError = new SimpleAttributeSet();
        StyleConstants.setBackground(styleError, new Color(80, 30, 30));  // 暗红背景
        StyleConstants.setUnderline(styleError, true);
        StyleConstants.setForeground(styleError, theme.CMD_CONSOLE_ERR);
    }

    /** 解析 javac 输出文本，返回所有错误行号 */
    public List<ErrorInfo> parseErrors(String javacOutput, String sourceFileName) {
        List<ErrorInfo> errors = new ArrayList<>();
        Matcher m = JAVAC_ERROR.matcher(javacOutput);
        while (m.find()) {
            try {
                int line = Integer.parseInt(m.group(2));
                String errFile = m.group(1);
                // 错误的文件名（可能是绝对路径，只取文件名）
                String errFileName = errFile;
                int lastSep = Math.max(errFile.lastIndexOf('/'), errFile.lastIndexOf('\\'));
                if (lastSep >= 0) {
                    errFileName = errFile.substring(lastSep + 1);
                }
                errors.add(new ErrorInfo(errFileName, line, m.group(4)));
            } catch (NumberFormatException ignored) {}
        }
        return errors;
    }

    /** 高亮指定行 */
    public void highlightLine(StyledDocument doc, int line) {
        Element root = doc.getDefaultRootElement();
        if (line < 0 || line >= root.getElementCount()) return;
        Element elem = root.getElement(line);
        int start = elem.getStartOffset();
        int len = elem.getEndOffset() - start;
        // 跳过末尾换行符
        if (len > 0) len--;
        doc.setCharacterAttributes(start, Math.max(len, 0), styleError, false);
    }

    /** 清除所有行的高亮（恢复到默认前景色） */
    public void clearAll(StyledDocument doc) {
        try {
            doc.setCharacterAttributes(0, doc.getLength(),
                    new SimpleAttributeSet(), true);
        } catch (Exception ignored) {}
    }
}
