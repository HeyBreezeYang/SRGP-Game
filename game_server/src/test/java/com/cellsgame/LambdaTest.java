package com.cellsgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aly on  2016-10-10.
 */
public class LambdaTest {
    public static void main(String[] args) {
//        Arrays.asList("ss", "").forEach(System.out::println);
//        List<N> data = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            data.add(new N(i));
//        }
//        System.out.println(data.stream().sorted((a, b) -> b.value - a.value).limit(5).mapToInt(value -> value.value).sum());
//
//        String a = "aca";
//        String b = "caa";
//        System.out.println(a.hashCode() + " : " + b.hashCode());
//        System.out.println(hash(a) + " : " + hash(b));
//        20367810
//        19740847
//        130740990
//        127834124
//        125713037
        List<Integer> strings = new ArrayList<>();
        for (int i = 0; i < 1_000; i++) {
            strings.add(i);
        }
        forEach(Collections.singletonList(100));
        long nano = System.nanoTime();
        forEach(strings);
        System.out.println(System.nanoTime() - nano);
        nano = System.nanoTime();
        forEach(strings);
        System.out.println(System.nanoTime() - nano);
        nano = System.nanoTime();
        forEach(strings);
        System.out.println(System.nanoTime() - nano);
        nano = System.nanoTime();
        forEach(strings);
        System.out.println(System.nanoTime() - nano);
//        255793484
    }

    private static void forEach(List<Integer> strings) {
        final long[] sum = {0};
        strings.forEach(integer -> sum[0] += integer);
//        for (Integer string : strings) {
//            sum[0] += string;
//        }
//        System.out.println(sum[0]);
        Runnable run = getRun();
//        run.getClass().
        System.out.println(run);
    }

    private static Runnable getRun() {
        return () -> System.out.println("--sss");
    }

    private static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
