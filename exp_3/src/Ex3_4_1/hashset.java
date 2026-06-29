package Ex3_4_1;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class hashset {
    private static final String[] stuNames = {"A1", "B2", "C3", "D4", "E5", "B2"};

    public static void main(String[] args) {


        // HashSet特点：无序、不可重复
        HashSet<String> names = new HashSet<>(10);

        // 1. 添加元素（重复元素"B2"只保留一个） addall
        names.addAll(Arrays.asList(stuNames));

        // 2. 迭代器遍历 iter
        System.out.print("容量10时: ");
        Iterator<String> iter = names.iterator();
        while (iter.hasNext()) {
            System.out.print(iter.next() + " ");
        }
        System.out.println();

        // 3. 不同容量影响存储顺序
        names = new HashSet<>(100);

        Collections.addAll(names, stuNames);

        // 4. 增强for遍历
        System.out.print("容量100时: ");
        for (String ele : names) {
            System.out.print(ele + " ");
        }
        System.out.println();

        // 5. 常用方法演示
        System.out.println("\n--- 常用方法 ---");
        System.out.println("集合大小: " + names.size());
        System.out.println("是否包含'A1': " + names.contains("A1"));
        System.out.println("是否包含'B2': " + names.contains("B2"));
        names.remove("D4");
        System.out.println("删除'D4'后: " + names);
        System.out.println("是否为空: " + names.isEmpty());

        // 6. 清空集合
        names.clear();
        System.out.println("清空后大小: " + names.size());
    }
}