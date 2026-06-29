package Ex2_3_4;

import java.io.*;
import java.util.*;

public class Studentdemo {
    public static void main(String[] args) {
        List<Student> students = createStudents();
        displayToConsole(students);
        writeToFile(students, "Text3.txt");
        System.out.println("\n✅ 学生信息已输出到控制台和 Text3.txt");
    }

    private static List<Student> createStudents() {
        return Arrays.asList(
                new Student("2024001", "lx", "计", "计", 89.5, "男", 2024),
                new Student("2024002", "l", "计", "计", 95.0, "男", 2024),
                new Student("2024003", "h", "人", "人", 78.3, "男", 2024)
        );
    }

    private static void displayToConsole(List<Student> students) {
        
        System.out.println("========== 学生信息列表（共 " + students.size() + " 人）==========");
        System.out.printf("%-10s %-8s %-4s %-4s %-12s %-8s %-6s%n",
                "学号", "姓名", "性别", "年级", "专业", "班级", "成绩");
        System.out.println("------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-10s %-8s %-4s %-4d %-12s %-8s %-6.2f%n",
                    s.getStudentId(), s.getName(), s.getGender(),
                    s.getGrade(), s.getMajor(), s.getClassName(), s.getScore());
        }
        System.out.println("==============================================");
    }

    private static void writeToFile(List<Student> students, String fileName) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("src/Ex2_3_4/" + fileName))) {
            pw.println("========== 学生信息列表（共 " + students.size() + " 人）==========");
            pw.printf("%-10s %-8s %-4s %-4s %-12s %-8s %-6s%n",
                    "学号", "姓名", "性别", "年级", "专业", "班级", "成绩");
            pw.println("------------------------------------------------------------");
            for (Student s : students) {
                pw.printf("%-10s %-8s %-4s %-4d %-12s %-8s %-6.2f%n",
                        s.getStudentId(), s.getName(), s.getGender(),
                        s.getGrade(), s.getMajor(), s.getClassName(), s.getScore());
            }
            pw.println("==============================================");
            System.out.println("✓ 数据已写入: src/Ex2_3_4/" + fileName);
        } catch (IOException e) {
            System.err.println("✗ 写入失败: " + e.getMessage());
        }
    }
}