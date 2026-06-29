// 文件名: CatPet.java
package Ex1_5;

public class CatPet extends Pet {
    public CatPet() {
        super("猫咪", 520.0);
    }

    @Override
    public String getDescription() {
        return "小猫";
    }
}