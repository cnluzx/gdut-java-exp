// 文件名: Triangle.java
package Ex1_3;

public class Triangle extends obj {
    private double s1, s2, s3;

    public Triangle(double s1, double s2, double s3) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
    }

    // 判断三条边是否构成三角形
    public boolean isValidTriangle() {
        return (s1 + s2 > s3) && (s1 + s3 > s2) && (s2 + s3 > s1) &&
                s1 > 0 && s2 > 0 && s3 > 0;
    }

    @Override
    public double get_perimeters() {
        return s1 + s2 + s3;
    }

    @Override
    public double get_area() {
        double p = (s1 + s2 + s3) / 2;
        return Math.sqrt(p * (p - s1) * (p - s2) * (p - s3));
    }
}