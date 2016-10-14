package com.ervine.description.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SortTest {

	@Test
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(1);
		
		System.out.println("Unsorted: " + list);
		
		Collections.sort(list);
		
		System.out.println("Sorted: " + list);
	}

}
