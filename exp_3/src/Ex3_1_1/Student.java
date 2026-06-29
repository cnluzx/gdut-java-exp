// 包名: Ex3_1_1
// Student.java
package Ex3_1_1;

public class Student {
    String name;
    int age;
    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    @Override
    public String toString() {
        return name + "(" + age + ")";
    }
}