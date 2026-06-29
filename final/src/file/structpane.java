package file;

import style.theme;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目结构预览面板 —— 解析 Java 源码，用树形展示类/重要方法/常量
 * 双击节点跳转到编辑器对应位置
 * 右键节点可查看对应的 Javadoc 注释
 *
 * @author 卢致旭
 * @version 2.0
 * @since 2026-06-24
 */
public class structpane extends JPanel {

    private JTree tree;
    private DefaultTreeModel treeModel;
    private StructureSelectListener selectListener;
    private String rawSource;
    private final javax.swing.Timer debounceTimer;

    private static final Pattern RE_CLASS = Pattern.compile(
            "(?:public\\s+|private\\s+|protected\\s+)?(?:class|interface|enum)\\s+(\\w+)");
    private static final Pattern RE_METHOD = Pattern.compile(
            "(?:public|protected|private)\\s+[\\w<>\\[\\],.\\s]+\\s+(\\w+)\\s*\\([^)]*\\)");
    private static final Pattern RE_CONSTANT = Pattern.compile(
            "(?:public|protected|private)\\s+static\\s+final\\s+[\\w<>\\[\\],.]+\\s+(\\w+)\\s*=");

    public structpane() {
        setLayout(new BorderLayout());
        setBackground(theme.BG_SIDEBAR);

        JLabel title = new JLabel("  项目结构");
        title.setFont(theme.TITLE_FONT);
        title.setForeground(theme.FG_DEFAULT);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
        title.setOpaque(true);
        title.setBackground(theme.BG_SIDEBAR);
        add(title, BorderLayout.NORTH);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("结构");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setFont(theme.UI_FONT);
        tree.setBackground(theme.BG_SIDEBAR);
        tree.setForeground(theme.FG_DEFAULT);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new StructureRenderer());

        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showCommentPopup(e);
                } else if (e.getClickCount() == 2 && selectListener != null) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path == null) return;
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Object obj = node.getUserObject();
                    if (obj instanceof StructItem) {
                        StructItem item = (StructItem) obj;
                        if (item.line > 0) selectListener.onItemSelected(item.line);
                    }
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showCommentPopup(e);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(null);
        scrollPane.setBackground(theme.BG_SIDEBAR);
        add(scrollPane, BorderLayout.CENTER);

        setPreferredSize(new Dimension(220, 180));

        // 防抖定时器：停止编辑 400ms 后才刷新结构树
        debounceTimer = new javax.swing.Timer(400, ev -> doParseAndShow());
        debounceTimer.setRepeats(false);
    }

    // ==================== 核心解析 ====================

    /**
     * 输入文本后调用此方法（会防抖，不会立即解析）
     */
    public void scheduleParse(String sourceText) {
        this.rawSource = sourceText;
        debounceTimer.restart();
    }

    private void doParseAndShow() {
        String sourceText = this.rawSource;
        if (sourceText == null || sourceText.trim().isEmpty()) {
            clear();
            return;
        }

        try {
            String clean = removeComments(sourceText);
            String[] lines = sourceText.split("\r\n|\n|\r", -1);
            DefaultMutableTreeNode root = buildStructureTree(clean, lines);
            treeModel.setRoot(root);
            for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
        } catch (Exception e) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("结构");
            root.add(new DefaultMutableTreeNode("解析失败: " + e.getMessage()));
            treeModel.setRoot(root);
        }
    }

    private DefaultMutableTreeNode buildStructureTree(String clean, String[] lines) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("结构");

        Matcher mc = RE_CLASS.matcher(clean);
        while (mc.find()) {
            String className = mc.group(1);
            int classLine = findLine(lines, mc.start());

            DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(
                    new StructItem("类", className, classLine));

            int bodyStart = mc.end();
            int bodyEnd = findMatchingBrace(clean, bodyStart);
            if (bodyEnd > bodyStart) {
                String body = clean.substring(bodyStart, bodyEnd);

                Matcher mm = RE_METHOD.matcher(body);
                while (mm.find()) {
                    int mLine = findLine(lines, bodyStart + mm.start());
                    classNode.add(new DefaultMutableTreeNode(
                            new StructItem("方法", mm.group(1) + "()", mLine)));
                }

                Matcher fm = RE_CONSTANT.matcher(body);
                while (fm.find()) {
                    int cLine = findLine(lines, bodyStart + fm.start());
                    classNode.add(new DefaultMutableTreeNode(
                            new StructItem("常量", fm.group(1), cLine)));
                }
            }

            root.add(classNode);
        }

        if (root.getChildCount() == 0) {
            root.add(new DefaultMutableTreeNode("未找到类/接口/枚举"));
        }

        return root;
    }

    // ==================== 注释提取与显示 ====================

    private void showCommentPopup(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null || rawSource == null) return;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = node.getUserObject();
        if (!(obj instanceof StructItem)) return;

        StructItem item = (StructItem) obj;
        String comment = extractDocComment(item.line);
        if (comment == null || comment.trim().isEmpty()) {
            comment = "（无注释）";
        }

        // 选中被右键的节点
        tree.setSelectionPath(path);

        JTextArea textArea = new JTextArea(comment);
        textArea.setEditable(false);
        textArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setBackground(new Color(255, 255, 240));
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 150));
        scrollPane.setBorder(null);

        JOptionPane.showMessageDialog(this, scrollPane,
                "注释 - " + item.toString(), JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 提取声明行上方的 Javadoc 或单行注释
     * 从 line-1 行向上找连续的注释行
     */
    private String extractDocComment(int line) {
        if (rawSource == null) return null;
        String[] lines = rawSource.split("\r\n|\n|\r", -1);
        if (line < 1 || line > lines.length) return null;

        int idx = line - 1; // 转为 0-based

        // 先检查上一行开始的连续注释
        java.util.List<String> commentLines = new java.util.ArrayList<>();
        boolean found = false;

        for (int i = idx - 1; i >= 0; i--) {
            String trimmed = lines[i].trim();

            // 空行可以出现在注释块之间
            if (trimmed.isEmpty()) {
                if (found) break; // 注释块结束后的空行 → 停止
                continue;         // 声明上方的空行 → 继续往上找
            }

            // 多行注释结束符 */
            if (trimmed.endsWith("*/")) {
                // 往回找到对应的 /*
                for (int j = i; j >= 0; j--) {
                    commentLines.add(0, lines[j].trim());
                    if (lines[j].trim().startsWith("/**") || lines[j].trim().startsWith("/*")) {
                        found = true;
                        i = j; // 继续从这段注释上方找
                        break;
                    }
                }
                continue;
            }

            // 单行注释
            if (trimmed.startsWith("//")) {
                commentLines.add(0, trimmed.substring(2).trim());
                found = true;
                continue;
            }

            // 既不是注释也不是空行 → 停止
            break;
        }

        if (commentLines.isEmpty()) return null;

        StringBuilder sb = new StringBuilder();
        for (String cl : commentLines) {
            // 去掉 /* */ 标记
            cl = cl.replaceAll("^/\\*\\*?\\s*", "")
                   .replaceAll("\\s*\\*/$", "")
                   .replaceAll("^\\s*\\*\\s?", "");
            if (!cl.trim().isEmpty()) {
                sb.append(cl).append("\n");
            }
        }
        return sb.toString().trim();
    }

    // ==================== 注释去除 ====================

    private String removeComments(String source) {
        StringBuilder sb = new StringBuilder(source);
        boolean inString = false, inChar = false;
        char prev = 0;

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '"' && prev != '\\' && !inChar) { inString = !inString; prev = c; continue; }
            if (c == '\'' && prev != '\\' && !inString) { inChar = !inChar; prev = c; continue; }
            if (inString || inChar) { prev = c; continue; }

            if (c == '/' && i + 1 < sb.length() && sb.charAt(i + 1) == '/') {
                while (i < sb.length() && sb.charAt(i) != '\n') { sb.setCharAt(i, ' '); i++; }
                continue;
            }

            if (c == '/' && i + 1 < sb.length() && sb.charAt(i + 1) == '*') {
                sb.setCharAt(i, ' '); sb.setCharAt(i + 1, ' '); i += 2;
                while (i < sb.length()) {
                    if (sb.charAt(i) == '*' && i + 1 < sb.length() && sb.charAt(i + 1) == '/') {
                        sb.setCharAt(i, ' '); sb.setCharAt(i + 1, ' '); i += 2; break;
                    }
                    sb.setCharAt(i, ' '); i++;
                }
                continue;
            }

            prev = c;
        }
        return sb.toString();
    }

    // ==================== 辅助 ====================

    private int findLine(String[] lines, int charPos) {
        int pos = 0;
        for (int i = 0; i < lines.length; i++) {
            pos += lines[i].length() + 1;
            if (pos > charPos) return i + 1;
        }
        return lines.length;
    }

    private int findMatchingBrace(String text, int pos) {
        if (pos >= text.length()) return text.length();
        int depth = 0;
        boolean inString = false, inChar = false, inComment = false;
        char prev = 0;
        for (int i = pos; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' && prev != '\\' && !inChar && !inComment) { inString = !inString; prev = c; continue; }
            if (c == '\'' && prev != '\\' && !inString && !inComment) { inChar = !inChar; prev = c; continue; }
            if (inString || inChar || inComment) { prev = c; continue; }
            if (c == '/' && i + 1 < text.length() && text.charAt(i + 1) == '*') { inComment = true; i++; prev = c; continue; }
            if (inComment && c == '*' && i + 1 < text.length() && text.charAt(i + 1) == '/') { inComment = false; i++; prev = c; continue; }
            if (c == '/' && i + 1 < text.length() && text.charAt(i + 1) == '/') { while (i < text.length() && text.charAt(i) != '\n') i++; prev = c; continue; }
            if (c == '{') depth++;
            else if (c == '}') { depth--; if (depth == 0) return i; }
            prev = c;
        }
        return text.length();
    }

    public void clear() {
        this.rawSource = null;
        treeModel.setRoot(new DefaultMutableTreeNode("结构"));
    }

    // ==================== 回调 ====================

    public interface StructureSelectListener {
        void onItemSelected(int line);
    }

    public void setStructureSelectListener(StructureSelectListener l) { this.selectListener = l; }

    // ==================== 内部类 ====================

    private static class StructItem {
        final String type, name;
        final int line;
        StructItem(String type, String name, int line) { this.type = type; this.name = name; this.line = line; }
        public String toString() { return "[" + type + "] " + name; }
    }

    private static class StructureRenderer extends DefaultTreeCellRenderer {
        private final Icon dirIcon = UIManager.getIcon("FileView.directoryIcon");
        private final Icon fileIcon = UIManager.getIcon("FileView.fileIcon");

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setBackgroundNonSelectionColor(theme.BG_SIDEBAR);
            setTextNonSelectionColor(theme.FG_DEFAULT);
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object obj = node.getUserObject();
            if (obj instanceof StructItem) {
                StructItem item = (StructItem) obj;
                switch (item.type) {
                    case "类": case "接口": case "枚举": setIcon(dirIcon); break;
                    default: setIcon(fileIcon); break;
                }
            }
            return this;
        }
    }
}
