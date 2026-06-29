// 文件名: Animal.java
package Ex1_4;

public class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void cry() {
        System.out.println(name + "叫");
    }
}