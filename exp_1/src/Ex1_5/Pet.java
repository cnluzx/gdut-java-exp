// 文件名: Pet.java
package Ex1_5;

public abstract class Pet {
    private final String type;
    private final double price;

    public Pet(String type, double price) {
        this.type = type;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getDescription();
}