// 文件名: test.java
package Ex1_4;

public class test {
    public static void main(String[] args) {

        Animal[] animals = {
                new Dog("小狗"),
                new Cat("小猫"),
                new Duck("小鸭")
        };

        System.out.println("动物：");
        for (Animal animal : animals) {
            animal.cry();
        }
    }
}