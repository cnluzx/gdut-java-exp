// 文件名: Book.java
package Ex1_8;

public class Book {
    private String name;
    private double price;

    public Book() {
    }

    public Book(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void show() {
        System.out.println("图书信息：书名='" + name + "', 价格=" + price);
    }
}