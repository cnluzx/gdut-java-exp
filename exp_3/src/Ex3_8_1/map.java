package Ex3_8_1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class map {
    /**
     * 主方法，演示HashMap的基本操作
     * @param args 命令行参数
     */
    public static void main(String[] args) {


        Map<String, Integer> map = new HashMap<>();
        map.put("苹果", 5);
        map.put("香蕉", 3);
        map.put("橙子", 8);
        map.put("葡萄", 2);
        System.out.println("初始: " + map);

        // 1. entrySet遍历
        System.out.println("\n--- entrySet遍历 ---");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print("  " + entry.getKey() + "=" + entry.getValue());
        }

        // 2. 查询操作
        System.out.println("\n--- 查询 ---");
        System.out.println("橙子=" + map.getOrDefault("橙子", -1));
        System.out.println("西瓜=" + map.getOrDefault("西瓜", -1));

        // 3. 修改操作
        map.put("苹果", 15);
        System.out.println("修改'苹果'为15后: " + map);

        // 4. 通过Entry修改值
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getKey().equals("香蕉")) {
                entry.setValue(10);
            }
        }
        System.out.println("修改'香蕉'为10后: " + map);

        // 5. 删除操作（迭代器遍历删除value<5）
        System.out.println("\n--- 删除value<5 ---");
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            if (entry.getValue() < 5) {
                System.out.println("  删除: " + entry.getKey() + "=" + entry.getValue());
                it.remove();
            }
        }
        System.out.println("删除后: " + map);

        // 6. 最终结果
        System.out.println("\n--- 最终结果 ---");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print("  " + entry.getKey() + "=" + entry.getValue());
        }
    }
}