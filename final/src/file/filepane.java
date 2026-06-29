package file;

import style.theme;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 文件树 Swing 面板 —— 左侧项目文件浏览器
 * 双击文件通知外部监听器
 */
public class filepane extends JPanel {

    private JTree              tree;   //左侧树状图的组件
    private DefaultTreeModel   treeModel;
    private File               rootDir;
    private FileSelectListener listener;   // 选中文件回调

    public filepane() {
        setLayout(new BorderLayout());
        setBackground(theme.BG_SIDEBAR);
        setPreferredSize(new Dimension(220, 0));

        // ---- 标题 ----
        JLabel title = new JLabel("  项目文件");
        title.setFont(theme.TITLE_FONT);
        title.setForeground(theme.FG_DEFAULT);
        title.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        title.setOpaque(true);
        title.setBackground(theme.BG_SIDEBAR);
        add(title, BorderLayout.NORTH);

        // ---- 空树 ----
        DefaultMutableTreeNode placeholder = new DefaultMutableTreeNode("未打开项目");
        treeModel = new DefaultTreeModel(placeholder);
        tree = new JTree(treeModel);
        tree.setFont(theme.UI_FONT);
        tree.setBackground(theme.BG_SIDEBAR);
        tree.setForeground(theme.FG_DEFAULT);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new FileTreeRenderer());

        // 双击打开文件 + 右键菜单
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleDoubleClick(e);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopup(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showPopup(e);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(null);
        scrollPane.setBackground(theme.BG_SIDEBAR);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ==================== 构建树 ====================

    /** 从项目根目录构建文件树 */
    public void setRootDirectory(File dir) {
        this.rootDir = dir;
        if (dir == null || !dir.exists()) return;

        DefaultMutableTreeNode rootNode = buildTreeNode(dir);
        treeModel = new DefaultTreeModel(rootNode);
        tree.setModel(treeModel);
        expandFirstLevel();
    }

    /** 刷新当前目录 */
    public void refresh() {
        if (rootDir != null) setRootDirectory(rootDir);
    }

    /** 把 File 递归转为 DefaultMutableTreeNode，目录优先、过滤无关文件 */
    private DefaultMutableTreeNode buildTreeNode(File file) {
        FileTreeNodeWrapper wrapper = new FileTreeNodeWrapper(file);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(wrapper);

        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                // 排序：目录在前，文件在后，各按名称排序
                Arrays.sort(children, Comparator
                        .comparingInt((File f) -> f.isDirectory() ? 0 : 1)
                        .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));

                for (File child : children) {
                    if (child.isHidden()) continue;
                    // 跳过 out 和 config 目录（编译输出和配置文件，不关心中间产物）
                    if (child.isDirectory() && skipDir(child.getName())) continue;
                    node.add(buildTreeNode(child));
                }
            }
        }
        return node;
    }

    /** 文件树中跳过这些目录 */
    private boolean skipDir(String name) {
        return name.equals("out") || name.equals("config") || name.equals(".git") || name.equals(".claude");
    }

    /** 展开第一层子目录 */
    private void expandFirstLevel() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            tree.expandRow(i + 1); // row 0 is root
        }
    }

    // ==================== 右键菜单 ====================

    private File rightClickedFile;  // 右键命中的文件/目录

    private void showPopup(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) return;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object userObj = node.getUserObject();
        if (!(userObj instanceof FileTreeNodeWrapper)) return;

        File file = ((FileTreeNodeWrapper) userObj).file;
        rightClickedFile = file;
        // 选中该节点
        tree.setSelectionPath(path);

        JPopupMenu popup = new JPopupMenu();

        if (file.isDirectory()) {
            JMenuItem newFile = new JMenuItem("新建文件");
            JMenuItem newDir  = new JMenuItem("新建文件夹");
            newFile.addActionListener(ev -> createNewFile(file));
            newDir.addActionListener(ev -> createNewDir(file));
            popup.add(newFile);
            popup.add(newDir);
            popup.addSeparator();
        }

        JMenuItem delete = new JMenuItem("删除");
        delete.addActionListener(ev -> deleteFile(file));
        popup.add(delete);

        popup.show(tree, e.getX(), e.getY());
    }

    /** 在当前目录下新建 Java 文件 */
    private void createNewFile(File parentDir) {
        String name = JOptionPane.showInputDialog(this, "文件名:", "新建文件", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) return;
        if (!name.endsWith(".java")) name += ".java";
        File newFile = new File(parentDir, name.trim());
        if (newFile.exists()) {
            JOptionPane.showMessageDialog(this, "文件已存在", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            newFile.createNewFile();
            refresh();
            // 通知外部打开该文件
            if (listener != null) listener.onFileSelected(newFile);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "创建失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** 在当前目录下新建子文件夹 */
    private void createNewDir(File parentDir) {
        String name = JOptionPane.showInputDialog(this, "文件夹名:", "新建文件夹", JOptionPane.PLAIN_MESSAGE);
        if (name == null || name.trim().isEmpty()) return;
        File newDir = new File(parentDir, name.trim());
        if (newDir.exists()) {
            JOptionPane.showMessageDialog(this, "文件夹已存在", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        newDir.mkdirs();
        refresh();
    }

    /** 删除文件或目录 */
    private void deleteFile(File file) {
        int r = JOptionPane.showConfirmDialog(this,
                "确认删除 " + file.getName() + " ?", "删除确认", JOptionPane.YES_NO_OPTION);
        if (r != JOptionPane.YES_OPTION) return;
        deleteRecursive(file);
        refresh();
    }

    private void deleteRecursive(File f) {
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            if (children != null) {
                for (File c : children) deleteRecursive(c);
            }
        }
        f.delete();
    }

    // ==================== 点击事件 ====================

    private void handleDoubleClick(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path == null) return;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object userObj = node.getUserObject();
        if (userObj instanceof FileTreeNodeWrapper) {
            File file = ((FileTreeNodeWrapper) userObj).file;
            if (file.isFile() && listener != null) {
                listener.onFileSelected(file);
            }
        }
    }

    // ==================== 回调接口 ====================

    public interface FileSelectListener {
        void onFileSelected(File file);
    }

    public void setFileSelectListener(FileSelectListener l) { this.listener = l; }

    // ==================== 内部包装类 ====================

    /** 包装 File 对象，toString 显示文件名 */
    private static class FileTreeNodeWrapper {
        final File file;
        FileTreeNodeWrapper(File f) { this.file = f; }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.isEmpty()) name = file.getPath();
            return name;
        }
    }

    /** 自定义渲染器 */
    private static class FileTreeRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setBackgroundNonSelectionColor(theme.BG_SIDEBAR);
            setTextNonSelectionColor(theme.FG_DEFAULT);

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            Object obj = node.getUserObject();
            if (obj instanceof FileTreeNodeWrapper) {
                File f = ((FileTreeNodeWrapper) obj).file;
                if (f.isDirectory()) {
                    setIcon(UIManager.getIcon("FileView.directoryIcon"));
                } else {
                    setIcon(UIManager.getIcon("FileView.fileIcon"));
                }
            }
            return this;
        }
    }
}
