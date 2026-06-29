package Ex2_3_4;

import java.io.Serializable;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String name;
    private String major;
    private String className;
    private double score;
    private String gender;
    private int grade;

    public Student() {}

    public Student(String studentId, String name, String major,
                   String className, double score, String gender, int grade) {
        this.studentId = studentId;
        this.name = name;
        this.major = major;
        this.className = className;
        this.score = score;
        this.gender = gender;
        this.grade = grade;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getGrade() { return grade; }
    public void setGrade(int grade) { this.grade = grade; }

    @Override
    public String toString() {
        return String.format("学号：%-10s | 姓名：%-6s | 性别：%-2s | 年级：%d | 专业：%-12s | 班级：%-8s | 成绩：%.2f",
                studentId, name, gender, grade, major, className, score);
    }
}