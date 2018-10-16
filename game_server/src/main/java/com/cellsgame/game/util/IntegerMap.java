/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cellsgame.game.util;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public final class IntegerMap<K> {
    private final Map<K, Integer> map;

    private IntegerMap(Map<K, Integer> map) {
        this.map = checkNotNull(map);
    }

    /**
     * Creates an {@code AtomicLongMap}.
     */
    public static <K> IntegerMap<K> create() {
        return new IntegerMap<>(new HashMap<>());
    }

    /**
     * Creates an {@code AtomicLongMap} with the same mappings as the specified {@code Map}.
     */
    public static <K> IntegerMap<K> create(Map<? extends K, Integer> m) {
        IntegerMap<K> result = create();
        result.putAll(m);
        return result;
    }

    /**
     * Returns the value associated with {@code key}, or zero if there is no value associated with
     * {@code key}.
     */
    public int get(K key) {
        Integer atomic = map.get(key);
        return atomic == null ? 0 : atomic;
    }

    /**
     * Increments by one the value currently associated with {@code key}, and returns the new value.
     */
    public int incrementAndGet(K key) {
        return addAndGet(key, 1);
    }

    /**
     * Decrements by one the value currently associated with {@code key}, and returns the new value.
     */
    public int decrementAndGet(K key) {
        return addAndGet(key, -1);
    }

    /**
     * Adds {@code delta} to the value currently associated with {@code key}, and returns the new
     * value.
     */
    public int addAndGet(K key, int delta) {
        Integer atomic = get(key);
        int newValue = atomic + delta;
        map.put(key, newValue);
        return newValue;
    }

    /**
     * Associates {@code newValue} with {@code key} in this map, and returns the value previously
     * associated with {@code key}, or zero if there was no such value.
     */
    public int put(K key, int newValue) {
        return map.put(key, newValue);
    }

    /**
     * Copies all of the mappings from the specified map to this map. The effect of this call is
     * equivalent to that of calling {@code put(k, v)} on this map once for each mapping from key
     * {@code k} to value {@code v} in the specified map. The behavior of this operation is undefined
     * if the specified map is modified while the operation is in progress.
     */
    public void putAll(Map<? extends K, Integer> m) {
        for (Map.Entry<? extends K, Integer> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    /**
     * Returns the number of key-value mappings in this map. If the map contains more than
     * {@code Integer.MAX_VALUE} elements, returns {@code Integer.MAX_VALUE}.
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns {@code true} if this map contains no key-value mappings.
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes all of the mappings from this map. The map will be empty after this call returns.
     * <p>
     * <p>This method is not atomic: the map may not be empty after returning if there were concurrent
     * writes.
     */
    public void clear() {
        map.clear();
    }

    public int sumValue() {
        int sum = 0;
        for (Integer integer : map.values()) {
            sum += integer;
        }
        return sum;
    }

    public Map<K,Integer> getMap(){
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
