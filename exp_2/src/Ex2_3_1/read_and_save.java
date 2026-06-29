package Ex2_3_1;
//文本文件的输入及保存。程序运行时要求：
// 从键盘输入一段文字，按回车后输入完成，将文字输出到文本文件Text1.txt中；
// 在控制台上显示Text1.txt内容。（本小题包名ex2_3_1）

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;


public class read_and_save
{

    //这个类实现了文本文件的输入与保存功能：先通过 Scanner 从键盘读取用户输入的一段文字，
    // 用 BufferedWriter 将文字写入 Text1.txt 文件，再用 BufferedReader 读取同一文件并在控制台显示其内容，
    // 展示了字符流（FileWriter/FileReader）和缓冲流（BufferedWriter/BufferedReader）的基本用法
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("=== 文本输入及保存 ===");
        System.out.println("请输入一段文字（输入完成后按回车）：");
        String text = scanner.nextLine();

        String file_path = "src/Ex2_3_1/Text1.txt";

        try (FileWriter fw = new FileWriter(file_path);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(text);
            System.out.println("文字已保存到 Text1.txt 文件中！");

        } catch (Exception e) {
            System.out.println("写入文件失败：" + e.getMessage());
        }
        System.out.println("\n--- Text1.txt 文件内容 ---");

        try (FileReader fr = new FileReader(file_path);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Exception e) {
            System.out.println("读取文件失败：" + e.getMessage());
        }

        scanner.close();
    }

}
