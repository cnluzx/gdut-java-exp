// 文件名: test.java
package Ex1_5;

import java.util.Scanner;

public class test {
    public static void main(String[] args) {
        System.out.println("欢迎光临宠物店！");

        Scanner scanner = new Scanner(System.in);
        PetShop shop = new PetShop();

        while (true) {
            printMenu();
            System.out.print("请选择：");
            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("感谢光临，欢迎下次再来！");
                break;
            }

            Pet pet = createPet(choice);
            if (pet == null) {
                System.out.println("无效选择，请重新输入！");
                continue;
            }

            System.out.print("请输入购买数量：");
            int count = scanner.nextInt();
            shop.sell(pet, count);
        }

        shop.printSummary();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== 宠物店菜单 ===");
        System.out.println("1、小狗（¥680.00）");
        System.out.println("2、小猫（¥520.00）");
        System.out.println("3、小兔（¥260.00）");
        System.out.println("0、退出");
    }

    private static Pet createPet(int choice) {
        switch (choice) {
            case 1:
                return new DogPet();
            case 2:
                return new CatPet();
            case 3:
                return new RabbitPet();
            default:
                return null;
        }
    }
}