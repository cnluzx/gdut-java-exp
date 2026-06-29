package style;

import java.awt.Color;
import java.awt.Font;

/**
 * 全局主题配置 —— 字体、颜色常量集中管理
 */
public class theme {

    // ======== 字体 ========
    public static final Font EDITOR_FONT       = new Font("Consolas", Font.PLAIN, 14);
    public static final Font LINE_NUMBER_FONT  = new Font("Consolas", Font.PLAIN, 12);
    public static final Font CONSOLE_FONT      = new Font("Microsoft YaHei", Font.PLAIN, 13);
    public static final Font UI_FONT           = new Font("Microsoft YaHei", Font.PLAIN, 12);
    public static final Font TITLE_FONT        = new Font("Microsoft YaHei", Font.BOLD, 12);

    // ======== 背景色 ========
    public static final Color BG_DARK         = new Color(43, 43, 43);
    public static final Color BG_EDITOR       = new Color(255, 255, 255);
    public static final Color BG_CONSOLE      = new Color(245, 245, 245);
    public static final Color BG_LINE_NUMBER  = new Color(240, 240, 240);
    public static final Color BG_SIDEBAR      = new Color(250, 250, 250);

    // ======== 前景 / 文字色 ========
    public static final Color FG_DEFAULT     = new Color(30, 30, 30);
    public static final Color FG_LINE_NUMBER = new Color(160, 160, 160);
    public static final Color FG_KEYWORD     = new Color(0, 51, 153);
    public static final Color FG_STRING      = new Color(0, 128, 0);
    public static final Color FG_COMMENT     = new Color(128, 128, 128);
    public static final Color FG_NUMBER      = new Color(175, 100, 0);
    public static final Color FG_ANNOTATION  = new Color(150, 150, 0);

    // ======== 控制台输出色 ========
    public static final Color CMD_CONSOLE_OUT  = new Color(30, 30, 30);    // 普通输出
    public static final Color CMD_CONSOLE_ERR  = new Color(200, 30, 30);   // 错误红色
    public static final Color CMD_CONSOLE_INFO = new Color(140, 140, 140);  // 提示灰色

    // ======== 状态色 ========
    public static final Color STATUS_OK   = new Color(100, 200, 100);
    public static final Color STATUS_WARN = new Color(220, 220, 80);
}
