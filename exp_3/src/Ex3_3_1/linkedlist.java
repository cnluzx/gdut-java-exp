package Ex3_3_1;

import java.util.LinkedList;
import java.util.Iterator;

public class linkedlist {
    public static void main(String[] args) {


        LinkedList<String> list = new LinkedList<>();

        // 1. 添加元素add
        list.add("苹果");
        list.add("香蕉");
        list.add("橙子");
        System.out.println("添加后: " + list);

        // 2. 头部和尾部添加 add first last
        list.addFirst("葡萄");
        list.addLast("西瓜");
        System.out.println("头部加'葡萄'，尾部加'西瓜'后: " + list);

        // 3. 获取元素 get first last (2)
        System.out.println("getFirst: " + list.getFirst());
        System.out.println("getLast: " + list.getLast());
        System.out.println("get(2): " + list.get(2));

        // 4. 搜索元素 search
        System.out.println("indexOf('橙子'): " + list.indexOf("橙子"));
        System.out.println("contains('香蕉'): " + list.contains("香蕉"));

        // 5. 删除元素 remove
        list.remove("苹果");
        System.out.println("删除'苹果'后: " + list);
        list.removeFirst();
        System.out.println("删除头部后: " + list);
        list.removeLast();
        System.out.println("删除尾部后: " + list);

        // 6. 再次添加 add
        list.add("芒果");
        list.add("樱桃");
        System.out.println("添加'芒果''樱桃'后: " + list);

        // 7. 三种遍历方式  iterator for
        System.out.print("迭代器遍历: ");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) System.out.print(it.next() + " ");

        System.out.print("\n增强for遍历: ");
        for (String s : list) System.out.print(s + " ");

        System.out.print("\nfori遍历: ");
        for (int i = 0; i < list.size(); i++) System.out.print(list.get(i) + " ");
    }
}    // 第二种遍历方式：迭代器
