package Ex3_1_2;

import java.util.Arrays;

public class gclass<T> {

    // 倒排数组（返回新数组） reverse
    public T[] reverse(T[] array) {
        if (array == null) return array;
        T[] result = array.clone();// 复制一个新的数组
        for (int i = 0; i < result.length / 2; i++) {  //交换算法
            T temp = result[i];
            result[i] = result[result.length - 1 - i];
            result[result.length - 1 - i] = temp;
        }
        return result;
    }

    public void swap(T[] array, int i, int j) {// 交换两个元素
        if (array == null || i < 0 || j < 0 || i >= array.length || j >= array.length) { //检查输入
            System.out.println("索引无效");
            return;
        }
        if (i != j) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static void main(String[] args) {


        // 整型数组倒排  new gclass <int>
        gclass<Integer> intG = new gclass<>();
        Integer[] intArr = {1, 2, 3, 4, 5};
        System.out.println("\n=== 整型数组倒排 ===");
        System.out.println("倒排前: " + Arrays.toString(intArr));
        System.out.println("倒排后: " + Arrays.toString(intG.reverse(intArr)));

        // Student数组倒排
        gclass<Student> stuG = new gclass<>();
        Student[] stuArr = {
                new Student("张三", 20),
                new Student("李四", 21),
                new Student("王五", 22),
                new Student("赵六", 23)
        };
        System.out.println("\n=== Student数组倒排 ===");
        System.out.println("倒排前: " + Arrays.toString(stuArr));
        System.out.println("倒排后: " + Arrays.toString(stuG.reverse(stuArr)));

        // 字符串数组交换元素
        gclass<String> strG = new gclass<>();
        String[] strArr = {"苹果", "香蕉", "橙子", "葡萄", "西瓜"};
        System.out.println("\n=== 字符串数组交换元素 ===");
        System.out.println("交换前: " + Arrays.toString(strArr));
        strG.swap(strArr, 0, 4);
        System.out.println("交换后(索引0和4): " + Arrays.toString(strArr));
    }
}

