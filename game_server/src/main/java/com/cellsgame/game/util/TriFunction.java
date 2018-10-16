/**
 * 
 */
package com.cellsgame.game.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author peterveron
 *
 */
public interface TriFunction<T,U,K,R> {
	public R apply(T t,U u,K k);
	
	/**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <V> TriFunction<T,U,K,V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, K k) -> after.apply(apply(t, u, k));
    }
}
