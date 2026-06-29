// 文件名: PetShop.java
package Ex1_5;

public class PetShop {
    private int totalCount;
    private double totalIncome;

    public void sell(Pet pet, int count) {
        if (count <= 0) {
            System.out.println("购买数量必须大于0！");
            return;
        }

        double money = pet.getPrice() * count;
        totalCount += count;
        totalIncome += money;
        System.out.printf("成功售出 %d 只%s，单价：¥%.2f，总金额：¥%.2f%n",
                count, pet.getType(), pet.getPrice(), money);
    }

    public void printSummary() {
        System.out.println("\n=== 销售统计 ===");
        System.out.println("累计售卖数量：" + totalCount + "只");
        System.out.printf("累计营业额：¥%.2f%n", totalIncome);
    }
}