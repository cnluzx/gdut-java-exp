// 文件名: Rectangle.java
package Ex1_3;

public class Rectangle extends obj {
    private double width;  // 宽
    private double height; // 高

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double get_perimeters() {
        return 2 * (width + height);
    }

    @Override
    public double get_area() {
        return width * height;
    }
}