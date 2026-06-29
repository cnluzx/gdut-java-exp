package Ex3_2_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class array {
/**
 * 演示ArrayList的基本操作方法，包括添加、插入、替换、获取、搜索、删除等，
 * 以及集合与数组的转换和三种遍历方式：for循环、迭代器和增强for循环。
 */
    public static void main(String[] args) {


    // 创建一个ArrayList集合对象
        List<String> list = new ArrayList<>();

    // 1. 添加元素 add
        list.add("苹果");        // 1. 添加元素 add
        list.add("香蕉");
        list.add("橙子");
        System.out.println("add后: " + list);

    // 2. 插入元素 add
        list.add(1, "葡萄");        // 2. 插入元素 add
        System.out.println("插入后: " + list);

    // 3. 替换元素 set
        list.set(2, "西瓜");        // 3. 替换元素 set
        System.out.println("替换后: " + list);

    // 4. 获取元素 get
        System.out.println("get(1): " + list.get(1));        // 4. 获取元素 get

    // 5. 搜索元素 indexof contains
        System.out.println("indexOf('西瓜'): " + list.indexOf("西瓜"));        // 5. 搜索元素     indexof contains
        System.out.println("contains('苹果'): " + list.contains("苹果"));

    // 6. 删除元素 remove
        list.remove("苹果");        // 6. 删除元素 remove
        list.remove(0);
        System.out.println("删除后: " + list);

    // 7. 集合转数组 list to array
        String[] arr = list.toArray(new String[0]);        // 7. 集合转数组 list to array
        System.out.println("转数组: " + Arrays.toString(arr));

    // 8. 添加更多元素用于遍历
        list.add("芒果");        // 8. 添加更多元素用于遍历
        list.add("樱桃");
        System.out.println("当前集合: " + list);

        System.out.println("\n--- for i循环 ---");

        for (int i = 0; i < list.size(); i++) {
            System.out.print("  [" + i + "] " + list.get(i));
        }

        System.out.println("\n--- 迭代器 ---");

        Iterator<String> it = list.iterator();
        while (it.hasNext())
            System.out.print("  " + it.next());

        System.out.println("\n--- 增强for ---");

        for (String s : list)
            System.out.print("  " + s);
    }

}