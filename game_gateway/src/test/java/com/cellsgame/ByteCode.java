package com.cellsgame;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * File Description.
 *
 * @author Yang
 */
public class ByteCode {
    private static class A {
        private int a;

        public A(int a) {
            this.a = a;
        }

        public boolean isAlive() {
            return a % 2 == 0;
        }
    }

    public static void main(String[] args) {
        Map<String, A> m = Maps.newConcurrentMap();
        for (int i = 0; i < 10; i++) {
            m.put("i" + i, new A(i));
        }
//        m.values().stream().filter(A::isAlive).forEach(a -> System.out.println(a.a));

        long sys = System.currentTimeMillis();
//        Date cur = new Date();
//        System.out.println(sys);
//        System.out.println(cur.getTime());
//        System.out.println(Clock.systemDefaultZone().millis());
//        System.out.println(cur);
//        System.out.println(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
//        System.out.println(LocalTime.now());
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        System.out.println(LocalDateTime.ofInstant(new Date(sys).toInstant(), ZoneId.systemDefault()));
//        System.out.println(LocalDateTime.now(ZoneId.systemDefault()).getHour());
//
//        System.out.println(LocalTime.of(1, 2, 3, 4).isBefore(LocalTime.of(2, 0, 0, 0)));
    }
}
