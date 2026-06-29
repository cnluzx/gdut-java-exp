// 文件名: Dog.java
package Ex1_4;

public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public void cry() {
        System.out.println(name + ":汪");
    }
}