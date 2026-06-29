// 文件名: test.java
package Ex1_2;

public class test {
    public static void main(String[] args) {

        //使用有参构造方法创建第1个对象；
        Student obj_student_1 = new Student("cdl", 20, "广东", "AI", 0);

        //给第1个对象的成员变量赋值；
        obj_student_1.name = "lzx";
        obj_student_1.score = 66;

        //使用有参构造方法创建第2个对象；
        Student obj_student_2 = new Student("张三", 21, "北京", "计算机", 90);

        //输出2个对象的信息。
        System.out.println("第一个学生：");
        obj_student_1.show();
        System.out.println("第二个学生：");
        obj_student_2.show();
    }
}