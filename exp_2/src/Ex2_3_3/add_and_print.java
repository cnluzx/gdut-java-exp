package Ex2_3_3;

import java.io.*;
import java.util.Scanner;

public class add_and_print
{
    //这个类实现了文本文件的追加修改与显示功能：先显示 Text2.txt 的原内容，
    // 再通过 Scanner 读取键盘输入的新文字，使用 FileWriter 的追加模式（第二个参数为 true）
    // 结合 PrintWriter 将新内容追加到文件末尾，
    // 最后再次读取文件显示修改后的全部内容，展示了字符流追加写入和文件内容更新的操作。
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
     
        System.out.println("=== 文本文件修改及保存 ===");

        // 显示原内容
        System.out.println("\n原文件内容：");
        readFile("src/Ex2_3_3/Text2.txt");

        // 输入新内容
        System.out.println("\n请输入要追加的文字：");
        String newText = scanner.nextLine();

        // 追加写入
        try (FileWriter fw = new FileWriter("src/Ex2_3_3/Text2.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            pw.println();      // 换行
            pw.println(newText);
            System.out.println("✓ 追加成功！");

        } catch (IOException e) {
            System.out.println("写入失败：" + e.getMessage());
        }

        // 显示新内容
        System.out.println("\n修改后的文件内容：");
        readFile("Text2.txt");

        scanner.close();
    }

    public static void readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("读取失败：" + e.getMessage());
        }
    }
}