// 文件名: test.java
package Ex1_1;

public class test {
    public static void main(String[] args) {

        //使用无参构造方法创建第1个对象；
        Person obj_person_1 = new Person();

        //给第1个对象的成员变量赋值；
        obj_person_1.name = "cdl";
        obj_person_1.age = 20;
        obj_person_1.birthplace = "广东";

        //使用有参构造方法创建第2个对象；
        Person obj_person_2 = new Person("lzx", 20, "江西");

        //输出2个对象的信息。
        System.out.println("第一个对象信息：" + obj_person_1);
        System.out.println("第二个对象信息：" + obj_person_2);
    }
}