package org.dromara.mybatis.jpa.query;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class ConditionValueTest {

	@Test
	public void valueOfList() {
		ArrayList<Integer> list =new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		System.out.println(ConditionValue.valueOfList(list));
	}
	
	@Test
	public void valueOfArray() {
		Integer []array =new Integer[] {1,2,3,4,5};
		System.out.println(ConditionValue.valueOfArray(array));
	}
	
	@Test
	public void valueOfIterator() {
		ArrayList<String> list =new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		System.out.println(ConditionValue.valueOfIterator(list));
	}
	
	@Test
	public void valueOfCollection() {
		ArrayList<String> list =new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		System.out.println(ConditionValue.valueOfCollection(list));
	}
}
