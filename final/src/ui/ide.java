package ui;

import compiler.runner;
import config.AppConfig;
import editor.EditorPane;
import file.filepane;
import file.structpane;
import style.theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * IDE 主窗口
 * BorderLayout: 上=工具栏, 左=文件树, 中=编辑器, 下=控制台
 */
public class ide {

    // 全局组件
    private JFrame          frame;
    private filepane        filepane;
    private structpane      structpane;
    private EditorPane      editor;
    private runner          console;
    private JLabel          statusLabel;

    // 当前项目根目录
    private File projectDir;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ide().launch());
    }
    /**
     * <code>launch</code> 是一个简单的
     *
     *
     * */
    private void launch() {

        AppConfig cfg = AppConfig.getInstance();

        // 设置浅色 UI 外观
        setLightUI();

        // ======== 主窗口 ========
        frame = new JFrame("Simplify IDE");  //主窗口名字
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(cfg.getWindowWidth(), cfg.getWindowHeight());
        frame.setLocationRelativeTo(null);

        // ======== 菜单栏 ========
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(theme.BG_SIDEBAR);
        menuBar.setBorder(null);

        // 文件菜单
        JMenu fileMenu = new JMenu("文件(F)");
        fileMenu.setForeground(theme.FG_DEFAULT);
        JMenuItem openItem  = new JMenuItem("打开项目");
        JMenuItem saveItem  = new JMenuItem("保存");
        JMenuItem exitItem  = new JMenuItem("退出");
        for (JMenuItem item : new JMenuItem[]{openItem, saveItem, exitItem}) {
            item.setForeground(theme.FG_DEFAULT);
            item.setBackground(theme.BG_SIDEBAR);
        }
        openItem.addActionListener(e -> openProject());
        saveItem.addActionListener(e -> editor.saveFile());
        exitItem.addActionListener(e -> frame.dispose());
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // 设置菜单
        JMenu configMenu = new JMenu("设置(S)");
        configMenu.setForeground(theme.FG_DEFAULT);
        JMenuItem jdkItem = new JMenuItem("JDK 路径");
        jdkItem.setForeground(theme.FG_DEFAULT);
        jdkItem.setBackground(theme.BG_SIDEBAR);
        jdkItem.addActionListener(e -> setJdkPath());
        configMenu.add(jdkItem);
        configMenu.addSeparator();

        // 字体大小子菜单
        JMenu fontSizeMenu = new JMenu("字体大小");
        fontSizeMenu.setForeground(theme.FG_DEFAULT);
        JMenuItem zoomInItem  = new JMenuItem("放大 Ctrl+=");
        JMenuItem zoomOutItem = new JMenuItem("缩小 Ctrl+-");
        JMenuItem zoomResetItem = new JMenuItem("重置 Ctrl+0");
        JMenuItem fontSizeItem = new JMenuItem("自定义大小...");
        for (JMenuItem item : new JMenuItem[]{zoomInItem, zoomOutItem, zoomResetItem, fontSizeItem}) {
            item.setForeground(theme.FG_DEFAULT);
            item.setBackground(theme.BG_SIDEBAR);
        }
        zoomInItem.addActionListener(e -> editor.zoomIn());
        zoomOutItem.addActionListener(e -> editor.zoomOut());
        zoomResetItem.addActionListener(e -> editor.zoomReset());
        fontSizeItem.addActionListener(e -> {
            String s = JOptionPane.showInputDialog(frame, "字号 (8-40):", String.valueOf(editor.getEditorFontSize()));
            if (s != null) {
                try {
                    int size = Integer.parseInt(s.trim());
                    if (size >= 8 && size <= 40) editor.setEditorFontSize(size);
                } catch (NumberFormatException ignored) {}
            }
        });
        fontSizeMenu.add(zoomInItem);
        fontSizeMenu.add(zoomOutItem);
        fontSizeMenu.add(zoomResetItem);
        fontSizeMenu.addSeparator();
        fontSizeMenu.add(fontSizeItem);
        configMenu.add(fontSizeMenu);

        // Ctrl+滚轮缩放开关
        JCheckBoxMenuItem wheelZoomItem = new JCheckBoxMenuItem("Ctrl+滚轮缩放", true);
        wheelZoomItem.setForeground(theme.FG_DEFAULT);
        wheelZoomItem.setBackground(theme.BG_SIDEBAR);
        wheelZoomItem.addActionListener(e -> editor.setCtrlWheelZoom(wheelZoomItem.isSelected()));
        configMenu.add(wheelZoomItem);

        menuBar.add(configMenu);

        frame.setJMenuBar(menuBar);

        // ======== BorderLayout 主区域 ========
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(theme.BG_DARK);

        // ---- 工具栏 ----
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(235, 235, 235));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        JButton btnCompile = new JButton("编译");
        JButton btnRun     = new JButton("运行");
        JButton btnAll     = new JButton("编译+运行");
        JButton btnClear   = new JButton("清屏");
        styleToolbarButton(btnCompile);
        styleToolbarButton(btnRun);
        styleToolbarButton(btnAll);
        styleToolbarButton(btnClear);

        btnCompile.addActionListener(e -> compileCurrentFile());
        btnRun    .addActionListener(e -> doRun());
        btnAll    .addActionListener(e -> { compileCurrentFile(); doRun(); });
        btnClear  .addActionListener(e -> console.clear());

        toolbar.add(btnCompile);
        toolbar.add(btnRun);
        toolbar.add(btnAll);
        toolbar.addSeparator();
        toolbar.add(btnClear);

        // ---- 文件树 (左) ----
        filepane = new filepane();
        filepane.setFileSelectListener(this::onFileSelected);

        // ---- 结构预览 (文件树下方) ----
        structpane = new structpane();
        structpane.setStructureSelectListener(line -> editor.gotoLine(line));

        // 文件树 + 结构面板 上下排列
        JSplitPane leftPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        leftPanel.setTopComponent(filepane);
        leftPanel.setBottomComponent(structpane);
        leftPanel.setDividerLocation(320);
        leftPanel.setResizeWeight(0.65);
        leftPanel.setBorder(null);

        // ---- 编辑器 (中) ----
        editor = new EditorPane();
        editor.setContentChangeListener(text -> structpane.scheduleParse(text));

        // ---- 控制台 (下) ----
        console = new runner();
        console.setCompileCallback((filePath, success, output) -> {
            if (!success) {
                editor.showCompileErrors(output, new File(filePath).getName());
            } else {
                editor.clearErrors();
            }
        });
        AppConfig appCfg = AppConfig.getInstance();
        if (appCfg.getJdkFile() != null) {
            console.setJdkPath(appCfg.getJdkFile());
        }

        // ---- 状态栏 ----
        statusLabel = new JLabel("  就绪");
        statusLabel.setFont(theme.UI_FONT);
        statusLabel.setForeground(theme.FG_DEFAULT);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(2, 0, 2, 0)));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(240, 240, 240));

        // ---- 中右区域 (编辑器 + 控制台下) ----
        JSplitPane centerBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        centerBottom.setTopComponent(editor);
        centerBottom.setBottomComponent(console);
        centerBottom.setDividerLocation(400);
        centerBottom.setResizeWeight(0.65);
        centerBottom.setBorder(null);

        // ---- 左右分割 (文件树+结构 | 中右区域) ----
        JSplitPane leftRight = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        leftRight.setLeftComponent(leftPanel);
        leftRight.setRightComponent(centerBottom);
        leftRight.setDividerLocation(220);
        leftRight.setResizeWeight(0.0);
        leftRight.setBorder(null);

        // ---- 组装 ----
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(toolbar, BorderLayout.NORTH);
        topPanel.add(leftRight, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);

        // ======== 窗口关闭保存配置 ========
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                AppConfig cfg = AppConfig.getInstance();
                cfg.setWindowWidth(frame.getWidth());
                cfg.setWindowHeight(frame.getHeight());
            }
        });

        frame.setVisible(true);

        // 默认打开当前项目目录
        setProjectDir(new File(System.getProperty("user.dir")));
    }

    // ==================== 操作 ====================

    /**
     * 打开项目目录
     * @
     * */
    private void openProject() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            setProjectDir(chooser.getSelectedFile());
        }
    }

    /** 设置项目目录并刷新文件树 */
    private void setProjectDir(File dir) {
        this.projectDir = dir;
        filepane.setRootDirectory(dir);
        frame.setTitle("SimpleIDE - " + dir.getAbsolutePath());
    }

    /** 点击文件树中的文件，在编辑器中打开 */
    private void onFileSelected(File file) {
        editor.openFile(file);
        statusLabel.setText("  " + file.getAbsolutePath());
    }

    /** 编译当前编辑器中的文件 */
    private void compileCurrentFile() {
        File f = editor.getCurrentFile();
        if (f == null) {
            console.appendErr("[错误] 请先在文件树中选择一个文件\n");
            return;
        }
        editor.saveFile();
        console.onCommand("compile " + f.getAbsolutePath());
    }

    /** 运行最近编译的 class */
    private void doRun() {//dorun
        console.onCommand("run");
    }

    /** 设置 JDK 路径 */
    private void setJdkPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String currentJdk = AppConfig.getInstance().getJdkPath();
        if (currentJdk != null && !currentJdk.isEmpty()) {
            chooser.setCurrentDirectory(new File(currentJdk));
        }
        if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File jdkDir = chooser.getSelectedFile();
            AppConfig.getInstance().setJdkPath(jdkDir.getAbsolutePath());
            console.setJdkPath(jdkDir);
            JOptionPane.showMessageDialog(frame,
                    "JDK 路径已设置为: " + jdkDir.getAbsolutePath(),
                    "设置", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** 统一工具栏按钮样式 */
    private void styleToolbarButton(JButton btn) {
        btn.setFont(theme.UI_FONT);
        btn.setForeground(theme.FG_DEFAULT);
        btn.setBackground(new Color(250, 250, 250));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        btn.setFocusPainted(false);
    }

    /** 设置浅色 UI 外观 */
    private void setLightUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

}
