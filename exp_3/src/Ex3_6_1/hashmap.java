package Ex3_6_1;

import java.util.HashMap;
import java.util.Map;

public class hashmap {
    public static void main(String[] args) {


        Map<String, Integer> map = new HashMap<>();

        // 1. 添加键值对 map.put
        map.put("苹果", 5);
        map.put("香蕉", 3);
        map.put("橙子", 8);
        map.put("葡萄", 2);
        System.out.println("添加后: " + map);

        // 2. 获取值  map.get
        System.out.println("get('苹果')=" + map.get("苹果"));
        System.out.println("getOrDefault('西瓜',0)=" + map.getOrDefault("西瓜", 0));

        // 3. 判断包含 map.containskey /constainsvalue
        System.out.println("containsKey('香蕉')=" + map.containsKey("香蕉"));
        System.out.println("containsValue(8)=" + map.containsValue(8));

        // 4. 修改值（key相同覆盖）
        map.put("苹果", 10);
        System.out.println("修改'苹果'为10后: " + map);

        // 5. 删除  remove
        map.remove("葡萄");
        System.out.println("删除'葡萄'后: " + map);

        // 6. 集合大小 .size
        System.out.println("size=" + map.size());

        // 7. 三种遍历方式 .get
        System.out.println("\n--- keySet遍历 ---");
        for (String key : map.keySet()) {
            System.out.print("  " + key + "=" + map.get(key));
        }

        System.out.println("--- entrySet遍历 ---");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.print("  " + entry.getKey() + "=" + entry.getValue());
        }

        System.out.println("--- values遍历（只取值） ---");
        for (Integer val : map.values()) {
            System.out.print(val + " ");
        }
        System.out.println();
    }
}