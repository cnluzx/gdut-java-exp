// 文件名: DogPet.java
package Ex1_5;

public class DogPet extends Pet {
    public DogPet() {
        super("狗狗", 680.0);
    }

    @Override
    public String getDescription() {
        return "小狗";
    }
}