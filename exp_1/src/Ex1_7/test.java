// 文件名: test.java
package Ex1_7;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {

        // 使用Lambda表达式实现Calculator接口
        Calculator add = (a, b) -> a + b;

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入两个数字：");
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int result = add.calc(a, b);
        System.out.println(a + " + " + b + " = " + result);
        scanner.close();
    }
}