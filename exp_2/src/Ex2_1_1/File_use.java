package Ex2_1_1;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class File_use {
    /*
    File_use 类是一个简单的文件和目录操作管理工具，它提供了以下功能：
    创建目录：通过 newFolder 方法，可以创建一个新的目录。如果目录已存在，则不会重复创建。
    创建文件：newFile 方法用于创建一个新的文件，并向其中写入指定的内容。如果文件已存在，则不会重新创建，而是直接写入内容。
    删除文件：delFile 方法用来删除一个指定的文件。
    列出目录内容：listFiles 方法能够列出指定目录下的所有文件和子目录。
    重命名文件：renameFile 方法允许用户对现有文件进行重命名。如果目标名称已经存在于同一目录下，则会提示错误信息。
    显示菜单：show_menu 方法提供了一个简单的命令行界面，让用户可以从几个预定义的操作中选择执行
     */
    public static void newFolder(String folderPath) {
        File myFilePath = new File(folderPath); //File 类 （输入文件夹路径）
        if (!myFilePath.exists()) {
            boolean flag = myFilePath.mkdir();   // mkdir方法检测这个路径是否存在
            if (flag)
                System.out.println("新建目录成功");
            else
                System.out.println("新建目录失败");
        }
    }

    public static void newFile(String filePathAndName, String fileContent) {
        try {
            File myFilePath = new File(filePathAndName);
            if (!myFilePath.exists()) {
                myFilePath.createNewFile();//
            }
            FileWriter resultFile = new FileWriter(myFilePath);// 负责建立程序到目标文件之间的字符输出通道
            PrintWriter myFile = new PrintWriter(resultFile);   //字符输出流
            myFile.println(fileContent);
            myFile.close();
            resultFile.close();
            System.out.println("新建文件成功！");
        } catch (IOException e) {
            System.out.println("新建文件出错!");
            e.printStackTrace();
        }
    }

    public static void delFile(String filePathAndName) {
        File myDelFile = new File(filePathAndName);
        boolean flag = myDelFile.delete();
        if (flag)
            System.out.println("删除文件成功！");
        else
            System.out.println("删除文件出错！");
    }

    public static void listFiles(String filePath) {
        File path = new File(filePath);
        String[] list = path.list();
        System.out.println("文件和文件夹总数:" + list.length);
        for (String s : list) {
            System.out.println(s);
        }
    }

    public static void renameFile(String filePathAndName, String newname) {
        File sourceFile = new File(filePathAndName);//导入当前文件路径和名字，格式.../test.txt
        if (!sourceFile.exists()) {
            System.out.println("错误：源文件或文件夹不存在！");
            return;
        }
        String parentPath = sourceFile.getParent();//找到当前文件的目录
        String newpath = parentPath + newname;
        File newfile = new File(newpath);

        if (newfile.exists()) {
            System.out.println("这个文件重复了,执行出错");
            return;
        }
        if (sourceFile.renameTo(newfile)) {
            System.out.println("重命名成功！");
            System.out.println(filePathAndName + " → " + newpath);
        } else {
            System.out.println("重命名失败！");
        }


    }

    public static void show_menu() {// 简单的菜单显示

        System.out.println("          文件操作管理系统       ");
        System.out.println("========================================");
        System.out.println("1. 创建目录");
        System.out.println("2. 创建文件");
        System.out.println("3. 删除文件");
        System.out.println("4. 查看目录内容");
        System.out.println("5. 重命名文件");
        System.out.println("6. 退出系统");
        System.out.println("========================================");
        System.out.print("请选择操作 (1-6): ");

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用文件操作管理系统！");
        while (true) {
            show_menu();
            String choice = scanner.nextLine().trim();//选择case
            switch (choice) {
                case "1":
                    System.out.print("请输入要创建的目录路径: ");
                    String dirPath = scanner.nextLine().trim();
                    newFolder(dirPath);
                    break;

                case "2":
                    System.out.print("请输入要创建的文件路径: ");
                    String filePath = scanner.nextLine().trim();
                    System.out.print("请输入文件内容: ");
                    String content = scanner.nextLine().trim();
                    newFile(filePath, content);
                    break;

                case "3":
                    System.out.print("请输入要删除的文件路径: ");
                    String delPath = scanner.nextLine().trim();
                    delFile(delPath);
                    break;

                case "4":
                    System.out.print("请输入要查看的目录路径: ");
                    String listPath = scanner.nextLine().trim();
                    listFiles(listPath);
                    break;

                case "5":
                    System.out.println("\n--- 文件重命名操作 ---");
                    System.out.print("请输入要重命名的文件路径: ");
                    String oldPath = scanner.nextLine().trim();
                    System.out.print("请输入新的文件名: ");
                    String newName = scanner.nextLine().trim();
                    renameFile(oldPath, newName);
                    break;

                case "6":
                    System.out.println("感谢使用，再见！");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("无效选择，请重新输入！");
            }
        }
    }
}