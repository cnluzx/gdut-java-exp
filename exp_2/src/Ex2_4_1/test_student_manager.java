package Ex2_4_1;

import java.util.Scanner;

/**
 * 学生管理系统测试类
 * 提供菜单界面，测试序列化和反序列化功能
 */
public class test_student_manager {
    public static void main(String[] args) {
    
        System.out.println("      学生信息管理系统（序列化/反序列化）");
        System.out.println("==============================================");

        student_manager manager = new student_manager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n请选择操作：");
            System.out.println("1. 录入学生信息并保存（序列化）");
            System.out.println("2. 读取并显示学生信息（反序列化）");
            System.out.println("3. 退出系统");
            System.out.print("请输入选择 (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manager.inputAndSerialize();
                    break;
                case "2":
                    manager.deserializeAndDisplay();
                    break;
                case "3":
                    System.out.println("感谢使用，再见！");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("❌ 无效选择，请重新输入！");
            }
        }
    }
}