package Ex2_4_1;
import java.io.Serializable;

/**
 * 可序列化的学生类
 * 包含学号、姓名、性别、成绩
 */
public class student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;       // 学号
    private String name;     // 姓名
    private String gender;   // 性别
    private double score;    // 成绩

    public student(String id, String name, String gender, double score) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.score = score;
    }

    // Getter 方法
    public String getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public double getScore() { return score; }

    // Setter 方法
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setScore(double score) { this.score = score; }

    @Override
    public String toString() {
        return String.format("学号: %-8s | 姓名: %-6s | 性别: %-2s | 成绩: %.2f",
                id, name, gender, score);
    }
}