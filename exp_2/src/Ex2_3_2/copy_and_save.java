package Ex2_3_2;

import java.io.*;

public class copy_and_save
{
    //这个类实现了文本文件的复制与显示功能：先用 BufferedReader 读取 text1.txt，
    // 再用 BufferedWriter 逐行写入 text2.txt 完成复制，
    // 最后分别读取两个文件并在控制台显示内容，展示了字符缓冲流在文件复制场景中的应用。
    public static void main(String[] args) {
        // 复制文件
        try {
       
            // 读取源文件
            FileReader fr = new FileReader("src/Ex2_3_2/text1.txt");
            BufferedReader br = new BufferedReader(fr);

            // 写入目标文件
            FileWriter fw = new FileWriter("src/Ex2_3_2/text2.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }

            // 关闭资源
            br.close();
            bw.close();
            fr.close();
            fw.close();

            System.out.println("文件复制成功！");

        } catch (Exception e) {
            System.out.println("复制失败：" + e.getMessage());
        }

        // 显示tText1.txt内容
        System.out.println("\ntext1.txt内容：");
        try {
            FileReader fr = new FileReader("text1.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("读取失败：" + e.getMessage());
        }

        // 显示text2.txt内容
        System.out.println("\ntext2.txt内容：");
        try {
            FileReader fr = new FileReader("text2.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            System.out.println("读取失败：" + e.getMessage());
        }
    }
}