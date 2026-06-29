package editor;

import style.theme;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 简易代码编辑器面板 —— JTextPane 行号 + 语法高亮
 */
public class EditorPane extends JPanel {

    private final JTextPane       textPane;
    private final LineNumberHeader lineNumberArea;
    private final Highlighter     highlighter;
    private final ErrorHighlighter errorHL;
    private File                  currentFile;
    private int                   editorFontSize = 13;   // 当前字号
    private boolean               ctrlWheelZoom  = true;  // Ctrl+滚轮缩放开关
    private ContentChangeListener contentListener;

    public EditorPane() {
        setLayout(new BorderLayout());

        highlighter = new Highlighter();
        errorHL = new ErrorHighlighter();

        // ---- 行号区（纯绘制） ----
        lineNumberArea = new LineNumberHeader();
        lineNumberArea.setLNFont(theme.LINE_NUMBER_FONT);

        // ---- 编辑区 ----
        textPane = new JTextPane();
        applyEditorFont();

        textPane.setBackground(theme.BG_EDITOR);
        textPane.setForeground(theme.FG_DEFAULT);
        textPane.setCaretColor(theme.FG_DEFAULT);

        // 文档变化时更新行号 + 语法高亮
        textPane.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { onDocChanged(); }
            public void removeUpdate(DocumentEvent e)  { onDocChanged(); }
            public void changedUpdate(DocumentEvent e) {}
        });

        // ---- 滚动面板（行号 + 编辑区） ----
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setRowHeaderView(lineNumberArea);
        scrollPane.setBorder(null);

        // Ctrl+滚轮缩放字体（加在 scrollPane 上，不干扰普通滚轮滚动）
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!ctrlWheelZoom) return;
                if (e.isControlDown()) {
                    e.consume();
                    int delta = e.getWheelRotation();
                    editorFontSize -= delta;
                    if (editorFontSize < 8)  editorFontSize = 8;
                    if (editorFontSize > 40) editorFontSize = 40;
                    applyEditorFont();
                }
                // 没按 Ctrl 时不 consume，让 JScrollPane 正常处理滚动
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    /** 文档变化：更新行号 + 重新高亮 + 通知结构面板 */
    private void onDocChanged() {
        updateLineNumbers();
        SwingUtilities.invokeLater(this::highlight);
        if (contentListener != null) {
            contentListener.onContentChanged(textPane.getText());
        }
    }

    /** 更新行号 */
    private void updateLineNumbers() {
        lineNumberArea.setTextPane(textPane);
        lineNumberArea.repaint();
    }

    /** 全文重新高亮（语法 + 覆盖错误标记） */
    private void highlight() {
        // 保存光标位置，setCharacterAttributes 会移动光标
        int caret = textPane.getCaretPosition();
        errorHL.clearAll(textPane.getStyledDocument());
        highlighter.highlight(textPane.getStyledDocument());
        textPane.setCaretPosition(caret);
    }

    /** 根据编译错误信息高亮对应行 */
    public void showCompileErrors(String javacOutput, String sourceFileName) {
        SwingUtilities.invokeLater(() -> {
            int caret = textPane.getCaretPosition();
            errorHL.clearAll(textPane.getStyledDocument());
            for (ErrorHighlighter.ErrorInfo e : errorHL.parseErrors(javacOutput, sourceFileName)) {
                errorHL.highlightLine(textPane.getStyledDocument(), e.line - 1);
            }
            textPane.setCaretPosition(caret);
        });
    }

    /** 清除所有错误高亮 */
    public void clearErrors() {
        SwingUtilities.invokeLater(() -> {
            int caret = textPane.getCaretPosition();
            errorHL.clearAll(textPane.getStyledDocument());
            highlight();
            textPane.setCaretPosition(caret);
        });
    }

    // ==================== 文件操作 ====================

    /** 打开文件，自动检测编码读取内容到编辑器 */
    public void openFile(File file) {
        try {
            byte[] raw = Files.readAllBytes(file.toPath());
            String content = new String(raw, java.nio.charset.StandardCharsets.UTF_8);

            // 检查是否可能是 GBK 编码（UTF-8 解码后出现乱码特征）！！！！！
            if (hasGarbledText(content)) {
                content = new String(raw, java.nio.charset.Charset.forName("GBK"));
            }

            textPane.setText(content);
            currentFile = file;
            // setText 不触发 DocumentListener，手动通知结构面板
            if (contentListener != null) {
                contentListener.onContentChanged(content);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "无法打开文件: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** 简单检测文本中是否有乱码（含无效字符或常见乱码字符） */
    private boolean hasGarbledText(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Unicode 替换字符（�）或者出现无法显示的字符
            if (c == '�') return true;
            // 检测常见的 GBK 被误读特征：连续的扩展 ASCII 区字符
            if (c >= 0x80 && c <= 0xFF) return true;
        }
        return false;
    }

    /** 保存当前文件（UTF-8 编码） */
    public void saveFile() {
        if (currentFile == null) {
            saveFileAs();
            return;
        }
        try {
            Files.write(currentFile.toPath(), textPane.getText().getBytes(
                    java.nio.charset.StandardCharsets.UTF_8));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "无法保存: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** 另存为 */
    public void saveFileAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter("Java 文件 (*.java)", "java"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();
            saveFile();
        }
    }

    // ==================== getter ====================

    public JTextPane  getTextPane()   { return textPane; }
    public File       getCurrentFile() { return currentFile; }
    public String     getContent()    { return textPane.getText(); }
    public void       setContent(String s) { textPane.setText(s); }

    // ==================== 字体缩放 ====================

    /** 应用编辑器字体（字号可调） */
    private void applyEditorFont() {
        Font f = new Font("Consolas", Font.PLAIN, editorFontSize);
        textPane.setFont(f);
        // 同步更新行号字体
        lineNumberArea.setLNFont(new Font("Consolas", Font.PLAIN, editorFontSize - 1));
    }

    /** 增大字号 */
    public void zoomIn() {
        if (editorFontSize < 40) {
            editorFontSize++;
            applyEditorFont();
        }
    }

    /** 减小字号 */
    public void zoomOut() {
        if (editorFontSize > 8) {
            editorFontSize--;
            applyEditorFont();
        }
    }

    /** 重置字号 */
    public void zoomReset() {
        editorFontSize = 13;
        applyEditorFont();
    }

    /** 开启/关闭 Ctrl+滚轮缩放 */
    public void setCtrlWheelZoom(boolean on) { this.ctrlWheelZoom = on; }
    public boolean isCtrlWheelZoom()         { return ctrlWheelZoom; }

    /** 获取/设置当前字号 */
    public int  getEditorFontSize()      { return editorFontSize; }
    public void setEditorFontSize(int s) { editorFontSize = s; applyEditorFont(); }

    // ==================== 内容变化回调 ====================

    public interface ContentChangeListener {
        void onContentChanged(String text);
    }

    public void setContentChangeListener(ContentChangeListener l) { this.contentListener = l; }

    // ==================== 跳转到指定行 ====================

    /** 跳转到指定行（1-based） */
    public void gotoLine(int line) {
        try {
            int target = line - 1;
            if (target < 0) target = 0;
            int total = textPane.getDocument().getDefaultRootElement().getElementCount();
            if (target >= total) target = total - 1;
            int start = textPane.getDocument().getDefaultRootElement().getElement(target).getStartOffset();
            textPane.setCaretPosition(start);
            textPane.requestFocus();
        } catch (Exception ignored) {}
    }

    // ==================== 行号绘制组件 ====================

    /**
     * 行号栏：根据 JTextPane 的行高和行数纯绘制，不使用 JTextArea
     * 避免 setText() 导致的滚动偏移问题
     */
    private static class LineNumberHeader extends JComponent {
        private JTextPane textPane;
        private Font lnFont;
        private int lineCount;
        private int fontHeight;

        void setTextPane(JTextPane pane) {
            this.textPane = pane;
            this.lineCount = pane.getDocument().getDefaultRootElement().getElementCount();
            this.fontHeight = pane.getFontMetrics(pane.getFont()).getHeight();
        }

        void setLNFont(Font f) {
            this.lnFont = f;
        }

        @Override
        public Dimension getPreferredSize() {
            if (textPane == null) return new Dimension(40, 0);
            int width = 45;
            if (lineCount >= 100)  width = 55;
            if (lineCount >= 1000) width = 65;
            return new Dimension(width, textPane.getPreferredSize().height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (textPane == null) return;

            g.setColor(new Color(240, 240, 240));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(new Color(160, 160, 160));
            if (lnFont != null) g.setFont(lnFont);

            FontMetrics fm = g.getFontMetrics();
            Rectangle clip = g.getClipBounds();
            int startLine = Math.max(0, clip.y / fontHeight);
            int endLine = Math.min(lineCount, (clip.y + clip.height) / fontHeight + 1);

            int yOff = 0;
            try {
                java.awt.Rectangle r = textPane.modelToView(0);
                if (r != null) yOff = r.y;
            } catch (BadLocationException ignored) {}

            for (int i = startLine; i < endLine; i++) {
                int y = yOff + (i + 1) * fontHeight - fm.getDescent() - 2;
                String num = String.valueOf(i + 1);
                int x = getWidth() - fm.stringWidth(num) - 8;
                g.drawString(num, x, y);
            }
        }
    }
}
