/**
 * 
 */
package com.cellsgame.game.module.quest.cons;

import java.util.Comparator;

/**
 * @author peterveron
 *
 */
public class Comparators {
	public static final Comparator<Long> DESC_LONG = new Comparator<Long>(){

		@Override
		public int compare(Long o1, Long o2) {
			if(o1>o2)
				return -1;
			else if(o1<o2)
				return 1;
			else
				return 0;
		}};
	public static final Comparator<Integer> DESC_INT = new Comparator<Integer>(){

		@Override
		public int compare(Integer o1, Integer o2) {
			if(o1>o2)
					return -1;
				else if(o1<o2)
					return 1;
				else
					return 0;
		}};


}
