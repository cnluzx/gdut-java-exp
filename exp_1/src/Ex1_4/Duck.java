// 文件名: Duck.java
package Ex1_4;

public class Duck extends Animal {
    public Duck(String name) {
        super(name);
    }

    @Override
    public void cry() {
        System.out.println(name + "：嘎");
    }
}