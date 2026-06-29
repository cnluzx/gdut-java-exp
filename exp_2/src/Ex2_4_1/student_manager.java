package Ex2_4_1;

import java.io.*;
import java.util.*;

/**
 * 学生管理类
 * 包含序列化保存和反序列化读取功能
 */
public class student_manager {
    private static final String FILE_NAME = "students.dat";

    /**
     * 方法1：通过键盘输入学生对象信息，并保存到文件中（对象序列化）
     * 支持批量输入格式：学号,姓名,性别,成绩
     */
    public void inputAndSerialize() {
        Scanner scanner = new Scanner(System.in);
        List<student> students = new ArrayList<>();

        System.out.print("请输入要录入的学生人数（至少3人）: ");
        int count = scanner.nextInt();
        scanner.nextLine();

        if (count < 3) {
            System.out.println("❌ 至少需要输入3名学生信息！");
            return;
        }

        System.out.println("\n📝 请按格式输入学生信息：学号,姓名,性别,成绩");
        System.out.println("   示例：2024001,张三,男,89.5\n");

        for (int i = 0; i < count; i++) {
            System.out.print("第 " + (i + 1) + " 名学生: ");
            String line = scanner.nextLine().trim();
            String[] parts = line.split(",");

            // 检查格式是否正确
            while (parts.length != 4) {
                System.out.print("❌ 格式错误！请重新输入（学号,姓名,性别,成绩）: ");
                line = scanner.nextLine().trim();
                parts = line.split(",");
            }

            try {
                String id = parts[0].trim();
                String name = parts[1].trim();
                String gender = parts[2].trim();
                double score = Double.parseDouble(parts[3].trim());

                students.add(new student(id, name, gender, score));
                System.out.println("  ✅ 已录入: " + name);
            } catch (NumberFormatException e) {
                System.out.println("❌ 成绩格式错误，请输入数字！");
                i--; // 重新输入这一条
            }
        }

        // 序列化保存到文件
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
            System.out.println("\n✅ 成功将 " + students.size() + " 名学生信息保存到文件 " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("❌ 保存文件失败: " + e.getMessage());
        }
    }

    /**
     * 方法2：读入所保存的文件（反序列化），并以格式化的形式在控制台显示
     */
    public void deserializeAndDisplay() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("❌ 文件 " + FILE_NAME + " 不存在，请先录入学生信息！");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            @SuppressWarnings("unchecked")
            List<student> students = (List<student>) ois.readObject();

            System.out.println("\n========== 学生信息列表 ==========");
            System.out.println("共 " + students.size() + " 名学生：");
            System.out.println("-----------------------------------");
            for (student s : students) {
                System.out.println(s);
            }
            System.out.println("===================================");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ 读取文件失败: " + e.getMessage());
        }
    }
}