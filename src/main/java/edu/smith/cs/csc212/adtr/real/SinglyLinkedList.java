package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;

public class SinglyLinkedList<T> extends ListADT<T> {
	/**
	 * The start of this list.
	 * Node is defined at the bottom of this file.
	 */
	Node<T> start;
	
	@Override
	public T removeFront() {
		checkNotEmpty();
		T removed = this.start.value;
		this.start = this.start.next;
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		Node<T> secondToLastNode = null;
		T removed;
		
		for (Node<T> n = this.start; n.next != null; n = n.next) {
			secondToLastNode = n;
		}
		if (secondToLastNode == null) {
			removed = this.start.value;
			this.start = null;
		} else {
			removed = secondToLastNode.next.value;
			secondToLastNode.next = null;
		}
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		
		if (index == 0) {
			return removeFront();
		}

		Node<T> beforeIndex = getNode(index-1);
		T removed = beforeIndex.next.value;
		beforeIndex.next = beforeIndex.next.next;
		return removed;
	}

	@Override
	public void addFront(T item) {
		this.start = new Node<T>(item, start);
	}

	@Override
	public void addBack(T item) {
		Node<T> lastNode = null;
		for (Node<T> n = this.start; n != null; n = n.next) {
			lastNode = n;
		}
		if (lastNode != null) {
			lastNode.next = new Node<T>(item, null);
		} else {
			this.start = new Node<T>(item, null);
		}
	}

	@Override
	public void addIndex(int index, T item) {
		if (index == 0) {
			addFront(item);
			return;
		}
		
		Node<T> beforeIndex = getNode(index-1);
		beforeIndex.next = new Node<T>(item, beforeIndex.next);
		return;
	}
	
	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		T lastNodeValue = null;
		for (Node<T> n = this.start; n != null; n = n.next) {
			lastNodeValue = n.value;
		}
		return lastNodeValue;
	}

	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		return getNode(index).value;
	}
	

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		getNode(index).value = value;
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = this.start; n != null; n = n.next) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return this.start == null;
	}

	/**
	 * Return the Node at the specified index.
	 * @param index - the index of the Node.
	 * @return the Node at index. Throw a BadIndexError if no such node is found.
	 */
	private Node<T> getNode(int index) {
		int at = 0;
		for (Node<T> n = start; n != null; n = n.next) {
			if (at++ == index) {
				return n;
			}
		}
		throw new BadIndexError(index);
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of SinglyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes after me?
		 */
		public Node<T> next;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}
	}

}
