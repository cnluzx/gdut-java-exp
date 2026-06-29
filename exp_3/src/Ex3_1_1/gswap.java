package Ex3_1_1;

import java.util.Arrays;

public class gswap {
    // 泛型方法：交换数组两个元素<T>
    public static <T> void swapElements(T[] array, int i, int j) {
        if (array == null || i < 0 || j < 0 || i >= array.length || j >= array.length) {
            System.out.println("索引无效");
            return;
        }
        if (i != j) {
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static <T> void printArray(T[] array) {
        System.out.println(Arrays.toString(array));
    }

    public static void main(String[] args) {


        // 整型数组测试 print array
        Integer[] intArr = {1, 2, 3, 4, 5};
        System.out.print("交换前: "); printArray(intArr);
        swapElements(intArr, 0, 4);
        System.out.print("交换后: "); printArray(intArr);

        // 字符串数组测试 print array
        String[] strArr = {"苹果", "香蕉", "橙子", "葡萄", "西瓜"};
        System.out.print("\n交换前: "); printArray(strArr);
        swapElements(strArr, 1, 3);
        System.out.print("交换后: "); printArray(strArr);

        // Student数组测试
        Student[] stuArr = {
                new Student("张三", 20),
                new Student("李四", 21),
                new Student("王五", 22),
                new Student("赵六", 23)
        };
        System.out.print("\n交换前: "); printArray(stuArr);
        swapElements(stuArr, 0, 2);
        System.out.print("交换后: "); printArray(stuArr);
    }
}
