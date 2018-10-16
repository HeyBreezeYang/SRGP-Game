package com.cellsgame;

import java.util.Date;

import com.cellsgame.common.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * File Description.
 *
 * @author Yang
 */
public class GsonDate {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        TestObject object = new TestObject();
        object.setC(new Date());
        String string = gson.toJson(object);
//
//        TestObject from = gson.fromJson(string, new TypeToken<TestObject>() {
//        }.getType());
//
//        System.out.println(string);

        print(1, 2, 3);
    }

    private static void print(Object... pa) {
        print2(pa);
    }

    private static void print2(Object... pa) {
        print3(pa);
    }

    private static void print3(Object... pa) {
        System.out.println(JSONUtils.toJSONString(pa));
    }

    public enum T {
        a,;
    }

    public static class TestObject {
        private int a;
        private String b;
        private Date c;
        private T d = T.a;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Date getC() {
            return c;
        }

        public void setC(Date c) {
            this.c = c;
        }

        public T getD() {
            return d;
        }

        public void setD(T d) {
            this.d = d;
        }
    }
}
