package com.iflytek.fw.testng;

import static android.os.SystemClock.sleep;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestNg {
    private List<String> result = new ArrayList<>();
    Class<?> mClass;
    List<String> name;
    String group;

    /**
     * 执行指定测试类, 使用构造函数传入测试类可一步到位，直接运行测试
     */
    public TestNg() {
    }


    public void setClass(Class<?> c) {
        mClass = c;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void run() {
        Thread thread = new Thread(() -> result = executeTest(mClass));
        thread.start();
        while (thread.isAlive()) sleep(100);
    }

    public List<String> getResult() {
        return result;
    }

    /**
     * @param c 执行指定测试类
     * @return 测试结果
     */
    public List<String> executeTest(Class<?> c) {
        // 指定包名, 不然扫描全部项目包, 包括引用的 jar
        Method[] methods = c.getDeclaredMethods();

        methodBeforeClass(methods, c);  // 测试前
        Method[] test = methodPng(methods, group, name);
        methodTestClass(test, c);  // 测试中
        methodAfterClass(methods, c);   // 测试后
        return result;
    }

    /**
     * @param methods 先将数据copy到TestMethod, 再进行分组和单例规避，最后做优先级排序
     * @return 排序后的结果
     */
    Method[] methodPng(Method[] methods, String group, List<String> name) {
        ArrayList<MethodPriority> testMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                Test test = method.getAnnotation(Test.class);
                if (test != null && test.enabled()) {
                    if (group != null || name != null) {
                        if (group == null) {// 没有设置分组，仅规避单例执行
                            if (name.contains(test.testName())) {
                                testMethods.add(new MethodPriority(method));
                            }
                        } else if (name == null) {// 没有设置单例名称，仅规避分组
                            Log.i("zeng", "没有设置单例名称，仅规避分组");
                            if (Arrays.toString(test.groups()).contains(group)) {
                                testMethods.add(new MethodPriority(method));
                            }
                        } else if (Arrays.toString(test.groups()).contains(group)) {// 名字和分组都要规避
                            if (name.contains(test.testName())) {
                                testMethods.add(new MethodPriority(method));
                            }
                        }
                    } else testMethods.add(new MethodPriority(method)); // 没有分组和单例，执行所有测试用例
                }
            }
        }

        Collections.sort(testMethods);  // 数据排序
        Log.i("zeng", "me.size=" + testMethods.size());
        Method[] out = new Method[testMethods.size()];
        for (
                int i = 0; i < testMethods.size(); i++) {
            out[i] = testMethods.get(i).method;
        }
        return out;
    }

    /**
     * @param methods 测试中执行的注解
     * @param c       执行指定测试类
     */
    void methodTestClass(Method[] methods, Class<?> c) {
        for (Method method : methods) {
            if (method == null) return;
            if (method.isAnnotationPresent(Test.class)) {
                Test test = method.getAnnotation(Test.class);
                if (test != null) {
                    Thread thread = new Thread(() -> {
                        try {
                            String s = (String) method.invoke(c.newInstance());
                            result.add(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    while (thread.isAlive()) sleep(100);
                }
            }
        }
    }

    /**
     * @param methods 测试完成后执行的注解
     * @param c       执行指定测试类
     */
    void methodAfterClass(Method[] methods, Class<?> c) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(AfterClass.class)) {
                AfterClass beforeClass = method.getAnnotation(AfterClass.class);
                if (beforeClass != null) {
                    Thread thread = new Thread(() -> {
                        try {
                            method.invoke(c.newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    while (thread.isAlive()) sleep(100);
                }
            }
        }
    }

    /**
     * @param methods 测试之前执行的注解
     * @param c       执行指定测试类
     */
    void methodBeforeClass(Method[] methods, Class<?> c) {
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeClass.class)) {
                BeforeClass beforeClass = method.getAnnotation(BeforeClass.class);
                if (beforeClass != null) {
                    Thread thread = new Thread(() -> {
                        try {
                            method.invoke(c.newInstance());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    while (thread.isAlive()) sleep(100);
                }
            }
        }
    }

    /**
     * 重写优先级排序
     */
    static class MethodPriority implements Comparable<MethodPriority> {
        Method method;

        public MethodPriority(Method method) {
            this.method = method;
        }

        @Override
        public int compareTo(MethodPriority o) {
            return Objects.requireNonNull(this.method.getAnnotation(Test.class)).priority() - Objects.requireNonNull(o.method.getAnnotation(Test.class)).priority();
        }
    }
}