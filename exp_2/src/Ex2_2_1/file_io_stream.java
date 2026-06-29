package Ex2_2_1;

//掌握文件字节流（FileInputStream及FileOutputStream）的读写方法。
// 参考课本P150【例7.4.1】，在IDEA中测试这段代码，运行时从键盘输入文件名，实现文件的复制。（本小题包名ex2_2_1）

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class file_io_stream {
    //这个类实现了字节流文件复制功能，通过 FileInputStream 和 FileOutputStream 以1024字节缓冲区从键盘输入的源路径读取文件并写入目标路径
    public static void main(String[] args){
        try{
            Scanner scanner = new Scanner(System.in);//scanner输入命令行的文字

            System.out.print("请输入源文件路径: ");
            String sourcePath = scanner.nextLine(); // 读取源文件路径

            System.out.print("请输入目标文件路径: ");
            String targetPath = scanner.nextLine();  // 读取目标文件路径

            FileInputStream input_stream  = new FileInputStream(sourcePath); // 字节输入流
            FileOutputStream output_stream = new FileOutputStream(targetPath); //输出流

            byte[] buffer_stream = new byte[1024];  // 一次读取1024个字节
            int length ;
            while ((length = input_stream.read(buffer_stream)) != -1) { // read(buffer): 读取数据到buffer数组
                // 返回值：实际读取的字节数，-1表示文件结束
                output_stream.write(buffer_stream, 0, length); // write(buffer, 0, len): 将buffer中0到len位置的字节写入文件

            }
        }
        catch (Exception e){
            System.out.printf("存在错误：,%s",e);
        }
    }


}
