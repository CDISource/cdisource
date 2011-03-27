package org.cdisource.beancontainer.namespace;

import java.util.Map;
import java.util.TreeMap;

public class AbstractNamespaceGraph<T> {

	private final Map<String, T> children = new TreeMap<String, T>();

	protected String[] splitName(final String name) {
		if (name == null) {
			throw new IllegalArgumentException("Object name cannot be null");
		}

		if (name.length() == 0) {
			throw new IllegalArgumentException(
					"Cannot add bean with a name of zero length");
		}
		String[] result = null;
		if (name.contains(".")) {
			result = name.split("\\.");
		} else {
			result = new String[] { name };
		}
		return result;
	}

	/**
	 * Add named object to the tree. Assumes the last element in the name, or
	 * the only element in the name if it is a single name, is the actual name
	 * with anything preceeding it is the namespace.
	 * 
	 * @param name
	 * @param object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addObject(String name, T object) {
		if (name == null) {
			throw new IllegalArgumentException("Object name cannot be null");
		}
		String[] path = splitName(name);
		AbstractNamespaceGraph node = this;
		// locate the next to last namespace node
		for (int i = 0; i < path.length - 1; i++) {

			String key = path[i];
			Object next = node.children.get(key);
			// no node exists so create one
			if (next == null) {
				AbstractNamespaceGraph newNode = new AbstractNamespaceGraph();
				node.children.put(key, newNode);
				node = newNode;
			} else {
				// if this is not a graph node, we have an error because some
				// object lives on our path.
				if (!(next instanceof AbstractNamespaceGraph)) {
					String error = "Invalid namespace clash when inserting "
							+ name + " - found instance of " + next.getClass();
					throw new IllegalArgumentException(error);
				} else {
					// move down to the next graph node
					node = (AbstractNamespaceGraph) next;
				}
			}
		}
		if (node != null) {
			node.children.put(path[path.length - 1], object);
		}
	}

	/**
	 * Walks the graph and returns the first non-BeanGraphNode object it finds
	 * on the expression path. If no non-Node objects are found then we return
	 * null
	 * 
	 * @param expression
	 *            Expression used to locate the node.
	 * 
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public T findObject(String expression) {
		String[] path = splitName(expression);
		AbstractNamespaceGraph node = this;
		for (String s : path) {
			Object o = node.children.get(s);
			if (!(o instanceof AbstractNamespaceGraph)) {
				return (T) o;
			} else {
				node = (AbstractNamespaceGraph) o;
			}
		}
		return null;
	}

	public void dump() {
		dump("  ");
	}

	public void dump(String indent) {
		for (String k : children.keySet()) {
			Object v = children.get(k);
			System.out.println(indent + k + " - " + v);
			if (v instanceof AbstractNamespaceGraph) {
				@SuppressWarnings("rawtypes")
				AbstractNamespaceGraph bn = (AbstractNamespaceGraph) v;
				bn.dump(indent + "  ");
			}

		}
	}
	
	public boolean contains(final String name) {
		return children.containsKey(name);
	}

}
