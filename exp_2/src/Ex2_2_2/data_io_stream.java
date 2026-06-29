package Ex2_2_2;

//掌握数据流（DataInputStream及DataOutputStream）的读写方法。
// 阅读课本【例7.4.2】，在IDEA中测试这段代码，验证测试结果。（本小题包名ex2_2_2）

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class data_io_stream {
    //这个类演示了数据流（DataOutputStream/DataInputStream）的读写操作，先用 writeUTF、writeInt、writeDouble、writeChar
    // 将四种不同类型的数据按顺序写入二进制文件，再用对应的 readUTF、readInt、readDouble、readChar 按相同顺序读取并输出到控制台。
    public static void main(String[] args) {


        // ========== 写入数据 ==========
        try {//建立程序到目标文件的字节输出通道
            FileOutputStream fos = new FileOutputStream("dos.txt");
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF("lzx");
            dos.writeInt(666);
            dos.writeDouble(0.514);
            dos.writeChar('A');
            dos.close();
            System.out.println("数据写入成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ========== 读取数据 ==========
        try {
            FileInputStream fis = new FileInputStream("dos.txt");
            DataInputStream dis = new DataInputStream(fis);

            // 按写入顺序读取数据
            String name = dis.readUTF();      // 读取字符串
            int number = dis.readInt();        // 读取整数
            double score = dis.readDouble();   // 读取浮点数
            char grade = dis.readChar();       // 读取字符

            // 输出读取结果
            System.out.println("读取结果如下：");
            System.out.println("姓名：" + name);
            System.out.println("学号：" + number);
            System.out.println("成绩：" + score);
            System.out.println("等级：" + grade);

            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}