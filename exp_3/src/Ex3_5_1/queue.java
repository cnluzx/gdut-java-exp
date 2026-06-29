package Ex3_5_1;

import java.util.LinkedList;
import java.util.Queue;

public class queue {
    public static void main(String[] args) {


        Queue<String> queue = new LinkedList<>();

        // 1. 入队 offer
        queue.offer("苹果");
        queue.offer("香蕉");
        queue.offer("橙子");
        System.out.println("入队后: " + queue);

        // 2. 查看队头 peek()
        System.out.println("peek=" + queue.peek() + ", element=" + queue.element());

        // 3. 出队 poll()
        System.out.println("poll=" + queue.poll() + "，剩余: " + queue);
        System.out.println("poll=" + queue.poll() + "，剩余: " + queue);

        // 4. 再次入队  offer
        queue.offer("葡萄");
        queue.offer("西瓜");
        System.out.println("再次入队后: " + queue);

        // 5. 遍历（增强for）
        System.out.print("遍历: ");
        for (String s : queue) System.out.print(s + " ");
        System.out.println();

        // 6. 逐个出队 poll
        System.out.print("全部出队: ");
        while (!queue.isEmpty()) {
            System.out.print(queue.poll() + " ");
        }
        System.out.println("\n队列为空: " + queue.isEmpty());//总是true
    }
}