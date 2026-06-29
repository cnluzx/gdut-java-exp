// 文件名: RabbitPet.java
package Ex1_5;

public class RabbitPet extends Pet {
    public RabbitPet() {
        super("兔子", 260.0);
    }

    @Override
    public String getDescription() {
        return "小兔";
    }
}