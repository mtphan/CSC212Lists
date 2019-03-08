package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;

public class DoublyLinkedList<T> extends ListADT<T> {
	private Node<T> start;
	private Node<T> end;
	
	/**
	 * A doubly-linked list starts empty.
	 */
	public DoublyLinkedList() {
		this.start = null;
		this.end = null;
	}
	

	@Override
	public T removeFront() {
		checkNotEmpty();
		
		T removed = this.start.value;
		Node<T> secondFront = this.start.after;
		if (secondFront == null) {
			this.start = this.end = null;
		} else {
			secondFront.before = null;
			this.start = secondFront;
		}
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		
		T removed = this.end.value;
		Node<T> secondLast = this.end.before;
		if (secondLast == null) {
			this.start = this.end = null;
		} else {
			secondLast.after = null;
			this.end = secondLast;
		}
		
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		if (index == 0) {
			return removeFront();
		}
		
		int at = 0;
		for (Node<T> n = this.start; n != null; n = n.after) {
			if (at++ == index) {
				T removed = n.value;
				n.before.after = n.after;
				// Remove back
				if (n.after == null) {
					this.end = n.before;
				} else {
					n.after.before = n.before;
				}
				return removed;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public void addFront(T item) {
		if (start == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondFront = start;
			start = new Node<T>(item);
			start.after = secondFront;
			secondFront.before = start;
		}
	}

	@Override
	public void addBack(T item) {
		if (end == null) {
			start = end = new Node<T>(item);
		} else {
			Node<T> secondLast = end;
			end = new Node<T>(item);
			end.before = secondLast;
			secondLast.after = end;
		}
	}

	@Override
	public void addIndex(int index, T item) {
		Node<T> toAdd = new Node<T>(item);
		if (index == 0) {
			addFront(item);
			return;
		}
		
		int at = 0;		
		for (Node<T> n = this.start; n != null; n = n.after) {
			// Support adding at the very back.
			if (++at == index) {
				Node<T> atIndex = n.after;
				toAdd.before = n;
				toAdd.after = atIndex;
				n.after = toAdd;
				// If at the very end;
				if (atIndex == null) {
					this.end = toAdd;
				} else {
					atIndex.before = toAdd;
				}
				return;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return this.start.value;
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return this.end.value;
	}
	
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		int at = 0;
		for (Node<T> n = start; n != null; n = n.after) {
			if (at++ == index) {
				return n.value;
			}
		}
		throw new BadIndexError(index);
	}
	
	public void setIndex(int index, T value) {
		checkNotEmpty();
		
		int at = 0;
		for (Node<T> n = start; n != null; n = n.after) {
			if (at++ == index) {
				n.value =value;
				return;
			}
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		int count = 0;
		for (Node<T> n = start; n != null; n = n.after) {
			count++;
		}
		return count;
	}

	@Override
	public boolean isEmpty() {
		return (this.start == null && this.end == null);
	}
	
	/**
	 * The node on any linked list should not be exposed.
	 * Static means we don't need a "this" of DoublyLinkedList to make a node.
	 * @param <T> the type of the values stored.
	 */
	private static class Node<T> {
		/**
		 * What node comes before me?
		 */
		public Node<T> before;
		/**
		 * What node comes after me?
		 */
		public Node<T> after;
		/**
		 * What value is stored in this node?
		 */
		public T value;
		/**
		 * Create a node with no friends.
		 * @param value - the value to put in it.
		 */
		public Node(T value) {
			this.value = value;
			this.before = null;
			this.after = null;
		}
	}
}
