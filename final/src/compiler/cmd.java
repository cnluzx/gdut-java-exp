package compiler;

import style.theme;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * 嵌入式控制台面板 —— 显示彩色输出，底部带命令行输入栏
 */
public class cmd extends JPanel
{
    private final JTextPane textPane;
    private final StyledDocument doc;

    // 三种输出样式：普通 / 错误 / 提示信息
    private final SimpleAttributeSet output_style;
    private final SimpleAttributeSet styleErr;
    private final SimpleAttributeSet styleInfo;

    private final JTextField inputField;

    public cmd() {
        setLayout(new BorderLayout());

        // ==== 输出区：不可编辑的文本面板 ==== final 字段
        textPane = create_textpane();

        doc = textPane.getStyledDocument();

        // 初始化三种样式（颜色来自 theme）
        output_style = makeStyle(theme.CMD_CONSOLE_OUT);   // 普通白色输出
        styleErr     = makeStyle(theme.CMD_CONSOLE_ERR);   // 红色报错
        styleInfo    = makeStyle(theme.CMD_CONSOLE_INFO);  // 灰色提示

        // 用滚动条包裹，输出过多时可滚动
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // ==== 输入区：底部命令输入栏 ====
        inputField = new JTextField();
        inputField.setFont(theme.CONSOLE_FONT);
        inputField.setForeground(theme.FG_DEFAULT);
        inputField.setBackground(theme.BG_CONSOLE.darker());
        inputField.setCaretColor(theme.FG_DEFAULT);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, theme.BG_SIDEBAR),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));

        // 回车时：先直接插入回显文本，再调用 onCommand
        inputField.addActionListener(e -> {
            String text = inputField.getText();
            if (text.isEmpty()) return;

            inputField.setText("");

            // 直接插入回显（当前已在 EDT，不用 invokeLater）
            try {
                doc.insertString(doc.getLength(), "> " + text + "\n", output_style);
                textPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException ignored) {}

            onCommand(text);
        });
        add(inputField, BorderLayout.SOUTH);
    }

    /** 创建一个指定前景色的样式 */
    private SimpleAttributeSet makeStyle(Color color) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontFamily(style, "Microsoft YaHei");
        StyleConstants.setItalic(style, false);
        return style;
    }

    /**
     *
     *
     */
    private JTextPane create_textpane(){
        JTextPane console_text = new JTextPane();
        console_text.setEditable(false);
        console_text.setFont(theme.CONSOLE_FONT);
        console_text.setBackground(theme.BG_CONSOLE);
        console_text.setCaretColor(theme.FG_DEFAULT);
        return console_text;
    }
    /**
     * 追加普通输出
     *
     * */
    public void appendOut(String text)  { append(text, output_style); }

    /** 追加错误输出（红色） */
    public void appendErr(String text)  { append(text, styleErr); }

    /** 追加提示信息（灰色） */
    public void appendInfo(String text) { append(text, styleInfo); }

    /** 在文档末尾插入文本并自动滚动到底部 */
    private void append(String text, AttributeSet style) {
        SwingUtilities.invokeLater(() -> {
            try {
                doc.insertString(doc.getLength(), text, style);
                textPane.setCaretPosition(doc.getLength());
            } catch (BadLocationException ignored) {}
        });
    }

    /** 清空控制台 */
    public void clear() {
        SwingUtilities.invokeLater(() -> {
            try {
                doc.remove(0, doc.getLength());
            } catch (BadLocationException ignored) {}
        });
    }

    /** 收到一条输入命令（子类重写以自定义行为） */
    public void onCommand(String cmd) {
        appendInfo("[系统] 未知命令: " + cmd + "\n");
    }

    public SimpleAttributeSet getOutput_style() { return output_style; }
    public SimpleAttributeSet getStyleErr()     { return styleErr; }
    public SimpleAttributeSet getStyleInfo()    { return styleInfo; }
}
