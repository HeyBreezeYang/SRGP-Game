package com.cellsgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * File Description.
 *
 * @author Yang
 */
public class TableTest {

    public static void main(String[] args) {
        List<Integer> logs = new ArrayList<>();
        logs.add(1);
        logs.add(2);
        logs.add(3);
        logs.add(4);
        logs.add(5);
        logs.add(6);
        int size = 2;
        for (int i = logs.size() - size; i > 0; i--) {
            logs.remove(0);
        }
        logs.add(6);
        System.out.println(logs);
        Table<Integer, Integer, Integer> data = HashBasedTable.create();
        data.put(1, 1, 2);
        data.put(1, 2, 2);
        data.put(1, 3, 2);
        Set<Integer> row = new HashSet<>(data.row(1).keySet());
        for (Integer integer : row) {
            data.remove(1, integer);
        }
    }
}
