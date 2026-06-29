// 文件名: Person.java
package Ex1_1;

public class Person {
    //成员变量：姓名name,年龄age,出生地birthPlace
    public String name;
    public int age;
    public String birthplace;

    //无参构造方法
    public Person() {

    }

    //有参构造方法（3个参数）
    public Person(String name, int age, String birthplace) {
        this.name = name;
        this.age = age;
        this.birthplace = birthplace;
    }

    public void setName(String name) {
        this.name = name;
    }

    //改写toString()方法，当输出对象名时，能显示对象的所有信息；
    @Override
    public String toString() {
        return "姓名：" + name + ", 年龄：" + age + ", 出生地：" + birthplace;
    }
}