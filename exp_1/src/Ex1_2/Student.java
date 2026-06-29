// 文件名: Student.java
package Ex1_2;

//创建子类Student继承Person类
public class Student extends Person {
    public String major;
    public int score;

    //有参构造方法（5个参数：姓名、年龄、出生地、专业、成绩）
    public Student(String name, int age, String birthplace, String major, int score) {
        super(name, age, birthplace);  // 调用父类构造方法，更规范
        this.major = major;
        this.score = score;
    }

    //成员方法show()：输出学生所有信息
    public void show() {
        System.out.println("学生信息如下：");
        System.out.println("姓名：" + name);
        System.out.println("年龄：" + age);
        System.out.println("出生地：" + birthplace);
        System.out.println("专业：" + major);
        System.out.println("成绩：" + score);
        System.out.println("------------------------");
    }
}