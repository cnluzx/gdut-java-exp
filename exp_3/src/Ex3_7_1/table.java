package Ex3_7_1;

import java.util.*;

public class table {
    public static void main(String[] args) {

        // 方式1：List嵌套List（表格形式）  array.aslist
        System.out.println("\n=== 方式1: List<List<String>> ===");
        List<List<String>> table1 = Arrays.asList(
                Arrays.asList("A", "B", "C"),
                Arrays.asList("A1", "B1", "C1"),
                Arrays.asList("A2", "B2", "C2"),
                Arrays.asList("A3", "B3", "C3")
        );
        for (List<String> row : table1) {
            System.out.println(String.join("\t", row));
        }

        // 方式2：Map的List（键值对形式）
        System.out.println("\n=== 方式2: List<Map<String, String>> ===");
        List<Map<String, String>> table2 = new ArrayList<>();
        table2.add(new HashMap<String, String>() {{ put("A","A1"); put("B","B1"); put("C","C1"); }});
        table2.add(new HashMap<String, String>() {{ put("A","A2"); put("B","B2"); put("C","C2"); }});
        table2.add(new HashMap<String, String>() {{ put("A","A3"); put("B","B3"); put("C","C3"); }});

        System.out.println("A\tB\tC");
        for (Map<String, String> row : table2) {
            System.out.println(row.get("A") + "\t" + row.get("B") + "\t" + row.get("C"));
        }
    }
}