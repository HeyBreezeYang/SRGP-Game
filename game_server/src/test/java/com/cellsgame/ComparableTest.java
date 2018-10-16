package com.cellsgame;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * File Description.
 *
 * @author Yang
 */
public class ComparableTest {
    private int i;

    public ComparableTest() {
    }

    public static ComparableTest newI() {
        return new ComparableTest();
    }

    public static void main(String[] args) throws Throwable {
//        List<Boolean> a = Arrays.asList(true, false, true, false, true);
//        List<Boolean> b = Arrays.asList(true, true, true);
//        List<Boolean> c = Arrays.asList(false, false, false);
//        System.out.println(a.stream().allMatch(Boolean::booleanValue));
//        System.out.println(b.stream().allMatch(Boolean::booleanValue));
//        System.out.println(c.stream().allMatch(Boolean::booleanValue));
//
//        List<Integer> d = Arrays.asList(4, 3, 2, 1);
//        System.out.println(JSONUtils.toJSONString(d.stream().sorted((a1, a2) -> a1 - a2).collect(Collectors.toList())));

        getReflectCallMethodCostTime(100000);
        getReflectCallFieldCostTime(100000);
        callNewIn(100000);
        callStatic(100000);
        callNew(100000);

        int count = 1_0000_0000;
        System.out.println(getReflectCallMethodCostTime(count));
        System.out.println(getReflectCallFieldCostTime(count));

        System.out.println(callNew(count));
        System.out.println(callNewIn(count));
        System.out.println(callStatic(count));
        Object[] parm = new String[]{"adsaa", "adsfad", "SADASD", "DASFASSSS"};
        System.out.println(Tcast(parm, count));
        System.out.println(Tcast2(parm, count));
        System.out.println("----------------------------------");
        System.out.println(Tcast(parm, count));
        System.out.println(Tcast2(parm, count));

    }

    private static long Tcast2(Object[] parm, int num) {
        long start = System.currentTimeMillis();
        for (int j = 0; j < num; j++) {
            String[] p = Arrays.copyOf(parm, parm.length, String[].class);
            xxxx(p);
        }
        return System.currentTimeMillis() - start;
    }

    private static long Tcast(Object[] parm, int num) {
        long start = System.currentTimeMillis();
        for (int j = 0; j < num; j++) {
            String[] p = new String[parm.length];
            for (int i = 0; i < parm.length; i++) {
                p[i] = (String) parm[i];
            }
            xxxx(p);
        }
        return System.currentTimeMillis() - start;
    }

    private static void xxxx(String[] p) {
    }

    private static long callNewIn(int count) throws Exception {
        Constructor<ComparableTest> constructor = ComparableTest.class.getConstructor((Class<?>[]) null);
        long start = System.currentTimeMillis();
        long ix = 0;
        for (int i = 0; i < count; i++) {
            ix += constructor.newInstance((Object[]) null).invokeVoid();
        }
        System.out.println(ix);
        return System.currentTimeMillis() - start;
    }

    private static long callNew(int count) throws Exception {
        long start = System.currentTimeMillis();
        long ix = 0;
        for (int i = 0; i < count; i++) {
            ix += new ComparableTest().invokeVoid();
        }
        System.out.println(ix);
        return System.currentTimeMillis() - start;
    }

    private static long callStatic(int count) throws Throwable {
        MethodHandle handle = MethodHandles.lookup().findStatic(ComparableTest.class, "newI", MethodType.methodType(ComparableTest.class));
        long start = System.currentTimeMillis();
        int ix = 0;
        for (int i = 0; i < count; i++) {
            ix += ((ComparableTest) handle.invoke()).invokeVoid();
        }
        System.out.println(ix);
        return System.currentTimeMillis() - start;
    }

    private static long getReflectCallMethodCostTime(int count) throws Exception {
//        ProgramMonkey programMonkey = new ProgramMonkey("小明", "男", 12);
//        Method setmLanguageMethod;
//
//        setmLanguageMethod = programMonkey.getClass().getMethod("setmLanguage", String.class);
//        setmLanguageMethod.setAccessible(true);
//
//
        long startTime = System.currentTimeMillis();
//        for (int index = 0; index < count; index++) {
//            setmLanguageMethod.invoke(programMonkey, "Java");
//        }


        return System.currentTimeMillis() - startTime;
    }

    private static long getReflectCallFieldCostTime(int count) throws Exception {
//        ProgramMonkey programMonkey = new ProgramMonkey("小明", "男", 12);
//        Field ageField;
//        ageField = programMonkey.getClass().getDeclaredField("mLanguage");
//        ageField.setAccessible(true);
//
        long startTime = System.currentTimeMillis();
//        for (int index = 0; index < count; index++) {
//            ageField.set(programMonkey, "Java");
//        }

        return System.currentTimeMillis() - startTime;
    }

    public int invokeVoid() {
        return ++i;
    }
}
