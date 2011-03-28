package org.cdisource.beancontainer.namespace;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BeanNamespaceTest {

	private BeanNamespace root;

	@Before
	public void beforeTest() {
		root = new BeanNamespace();
	}

	@Test
	public void testEmptyNamespaces() {
		Object result = root.findObject("Some.item");
		assertNull(result);
		result = root.findObject("item");
		assertNull(result);
	}

	@Test
	public void testSplitFunctionWithSingleValue() {
		String[] value = root.splitName("Single");
		assertNotNull(value);
		assertEquals(1, value.length);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSplitFunctionWithNoValue() {
		root.splitName("");
	} 

	@Test(expected = IllegalArgumentException.class)
	public void testSplitFunctionWithNullValue() {
		root.splitName(null);
	}

	@Test
	public void testSplitFunctionWithMultiLevelName() {
		String[] value = root.splitName("Level1.Level2");
		assertNotNull(value);
		assertEquals(2, value.length);
		assertEquals("Level1", value[0]);
		assertEquals("Level2", value[1]);

	}

	@Test
	public void testTopLevelInsert() {
		Integer value = new Integer(23);
		root.addObject("number", value);
		Object result = root.findObject("number");
		Object nonResult = root.findObject("missing");
		assertNotNull(result);
		assertEquals(value, result);
		assertNull(nonResult);
	}

	public void testMultiLevelInsert() {
		Integer value = new Integer(23);
		root.addObject("number.23", value);
		Object graphNode = root.findObject("number");
		// test we got a graph node back
		assertNotNull(graphNode);
		assertTrue(graphNode.getClass().equals(BeanNamespace.class));

		// now look for our value
		Object number = root.findObject("number.23");
		assertEquals(value, number);

		// check we can't find 23
		Object nonResult = root.findObject("23");
		assertNull(nonResult);
	}

	@Test
	public void testAddingToExistingPath() {
		root.addObject("one.two", new Integer(12));
		root.addObject("one.three", new Integer(13));
		
	}

	@Test
	public void testMultipleObjects() {
		for (int i = 900; i < 1000; i++) {

			String text = numberToNamespace(i);
			root.addObject(text, new Integer(i));
		}
		Object result = root.findObject("9.5.6");
		assertNotNull(result);
		assertEquals(new Integer(956), result);
	}

	@Test
	public void testSimilarPathsNamespace() {
		// special case where the insert code doesn't move on and 995 -> 9.5 so
		// looking for 9.5.6 ends up with the integer at 9.5
		root.addObject("9.5.6", new Integer(956));
		root.addObject("9.9.5", new Integer(995));
		Object v1 = root.findObject("9.5.6");
		Object v2 = root.findObject("9.9.5");
		assertEquals(956, v1);
		assertEquals(995, v2);

	}

	@Test
	public void testNumberToNamespace() {
		String v1 = numberToNamespace(3382);
		assertEquals("3.3.8.2", v1);

		String v2 = numberToNamespace(4);
		assertEquals("4", v2);

	}

	private String numberToNamespace(int number) {
		String result = "";

		while (number > 0) {
			int digit = number % 10;
			String text = String.valueOf(digit);
			if (result.length() != 0) {
				result = "." + result;
			}
			result = text + result;
			number = number / 10;
		}
		return result;
	}

	@Test
	public void testCaseInsensitivity() {
		root.addObject("A", new Integer(1));
		root.addObject("a", new Integer(2));
		Object va = root.findObject("a");
		Object vA = root.findObject("A");
		assertNotSame(va,vA);
	}
	
	@Test
	public void testCaseInsensitivityInPath() {
		String name1 = "org.some.Object";
		String name2 = "org.SOME.Object";
		root.addObject(name1, new Integer(1));
		root.addObject(name2, new Integer(2));
		Object o1 = root.findObject(name1);
		Object o2 = root.findObject(name2);
		assertNotSame(o1,o2);
		assertEquals(o1, 1);
		assertEquals(o2, 2);
	}
	
}

