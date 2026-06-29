// 文件名: test.java
package Ex1_3;

import java.util.Scanner;

public class test {
    public void start_menu() {
        System.out.println("菜单");
        System.out.println("1、矩形");
        System.out.println("2、圆形");
        System.out.println("3、三角形");
        System.out.println("0、退出");
    }

    public static void main(String[] args) {
        test obj_t = new test();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            obj_t.start_menu();
            System.out.print("请选择需要求解的几何图形：");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("程序结束！");
                break;
            }

            switch (choice) {
                case 1: // 矩形
                    System.out.print("请输入矩形的长和宽：");
                    double length = scanner.nextDouble();
                    double width = scanner.nextDouble();
                    Rectangle rect = new Rectangle(length, width);
                    System.out.printf("矩形的周长是：%.2f%n", rect.get_perimeters());
                    System.out.printf("矩形的面积是：%.2f%n", rect.get_area());
                    break;

                case 2: // 圆形
                    System.out.print("请输入圆的半径：");
                    double radius = scanner.nextDouble();
                    Circle circle = new Circle(radius);
                    System.out.printf("圆的周长是：%.2f%n", circle.get_perimeters());
                    System.out.printf("圆的面积是：%.2f%n", circle.get_area());
                    break;

                case 3: // 三角形
                    System.out.print("请输入三角形的三个边长：");
                    double s1 = scanner.nextDouble();
                    double s2 = scanner.nextDouble();
                    double s3 = scanner.nextDouble();
                    Triangle triangle = new Triangle(s1, s2, s3);
                    if (triangle.isValidTriangle()) {
                        System.out.printf("三角形的周长是：%.2f%n", triangle.get_perimeters());
                        System.out.printf("三角形的面积是：%.2f%n", triangle.get_area());
                    } else {
                        System.out.println("输入的三个边长不构成三角形！");
                    }
                    break;

                default:
                    System.out.println("无效选择，请重新输入！");
            }
            System.out.println();
        }
        scanner.close();
    }
}