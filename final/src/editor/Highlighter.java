package editor;

import style.theme;

import javax.swing.text.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java 语法高亮器 —— 对 StyledDocument 中的 Java 源码进行着色
 * 创建了6种样式对象，分别对应：关键字、字符串、注释、数字、注解、默认文本
 */
public class Highlighter {

    private final SimpleAttributeSet styleKeyword; //关键字
    private final SimpleAttributeSet styleString;  //字符串
    private final SimpleAttributeSet styleComment; //注释
    private final SimpleAttributeSet styleNumber; //数字
    private final SimpleAttributeSet styleAnnotation; //注解
    private final SimpleAttributeSet styleDefault; //默认文本

    private static final String[] KEYWORDS = {//关键字
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
        "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native", "new",
        "package", "private", "protected", "public", "return", "short", "static",
        "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while", "true", "false", "null"
    };

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
        "(//[^\n]*)"                          // 1: 单行注释
        + "|(\"/\\*[\\s\\S]*?\\*/\")"         // 2: 多行注释
        + "|(\"(?:[^\"\\\\]|\\\\.)*\")"       // 3: 字符串
        + "|(\\b\\d+\\.?\\d*\\b)"             // 4: 数字
        + "|(@[a-zA-Z]+)"                     // 5: 注解
        + "|(\\b[a-zA-Z_]\\w*\\b)"            // 6: 标识符（含关键字）
    );

    public Highlighter() {
        styleKeyword    = makeStyle(theme.FG_KEYWORD, true);
        styleString     = makeStyle(theme.FG_STRING, false);
        styleComment    = makeStyle(theme.FG_COMMENT, false);
        styleNumber     = makeStyle(theme.FG_NUMBER, false);
        styleAnnotation = makeStyle(theme.FG_ANNOTATION, false);
        styleDefault    = makeStyle(theme.FG_DEFAULT, false);
    }

    /** 对全文重新着色 */
    public void highlight(StyledDocument doc) {


        String text;//step 1: 获取代码文本
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) { return; }

        // 重置为默认样式 yes
        doc.setCharacterAttributes(0, text.length(), styleDefault, true);

        Matcher m = TOKEN_PATTERN.matcher(text);
        while (m.find()) {
            AttributeSet style = null;
            if (m.group(1) != null || m.group(2) != null) {
                style = styleComment;
            } else if (m.group(3) != null) {
                style = styleString;
            } else if (m.group(4) != null) {
                style = styleNumber;
            } else if (m.group(5) != null) {
                style = styleAnnotation;
            } else if (m.group(6) != null && isKeyword(m.group(6))) {
                style = styleKeyword;
            }
            if (style != null) {
                doc.setCharacterAttributes(m.start(), m.end() - m.start(), style, false);
            }
        }
    }

    private boolean isKeyword(String word) {

        for (String kw : KEYWORDS) {
            if (kw.equals(word)) return true;
        }
        return false;
    }
    public void hybrid_highlight(StyledDocument doc){
        String text;//step 1: 获取代码文本
        try {
            text = doc.getText(0, doc.getLength());
        } catch (BadLocationException e) { return; }

        // 重置为默认样式 yes
        doc.setCharacterAttributes(0, text.length(), styleDefault, true);
    }

    private SimpleAttributeSet makeStyle(Color color, boolean bold) {
        SimpleAttributeSet style = new SimpleAttributeSet();
        StyleConstants.setForeground(style, color);
        StyleConstants.setBold(style, bold);
        return style;
    }
}
