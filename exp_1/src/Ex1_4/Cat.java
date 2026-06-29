// 文件名: Cat.java
package Ex1_4;

public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public void cry() {
        System.out.println(name + "：喵");
    }
}