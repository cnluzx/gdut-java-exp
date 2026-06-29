// 文件名: test.java
package Ex1_8;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class test {
    /**
     * 程序入口方法，演示Java反射的基本用法
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    public static void main(String[] args) throws Exception {

        System.out.println("=== 1. 获取Class对象 ===");
        // 通过类的全限定名获取Class对象
        Class<?> clazz = Class.forName("Ex1_8.Book");
        // 打印类的全限定名
        System.out.println("类名：" + clazz.getName());
        // 打印类的简单名称
        System.out.println("简单类名：" + clazz.getSimpleName());

        System.out.println("\n=== 2. 获取所有字段 ===");
        // 遍历类中声明的所有字段（包括私有字段）
        for (Field field : clazz.getDeclaredFields()) {
            // 打印字段的类型和名称
            System.out.println("字段：" + field.getType().getSimpleName() + " " + field.getName());
        }

        System.out.println("\n=== 3. 通过反射创建对象 ===");
        // 获取指定参数类型的构造方法
        Constructor<?> constructor = clazz.getConstructor(String.class, double.class);
        // 使用构造方法创建对象实例
        Object book = constructor.newInstance("Java编程思想", 59.8);
        System.out.println("创建的对象：" + book);

        System.out.println("\n=== 4. 通过反射调用方法 ===");
        Method showMethod = clazz.getMethod("show");
        System.out.print("调用show()方法：");
        showMethod.invoke(book);

        System.out.println("\n=== 5. 通过反射修改私有字段 ===");
        Field priceField = clazz.getDeclaredField("price");
        priceField.setAccessible(true);
        System.out.println("修改前的价格：" + priceField.get(book));
        priceField.set(book, 66.6);
        System.out.println("修改后的价格：" + priceField.get(book));

        System.out.println("\n=== 6. 再次调用show()方法验证 ===");
        showMethod.invoke(book);
    }
}