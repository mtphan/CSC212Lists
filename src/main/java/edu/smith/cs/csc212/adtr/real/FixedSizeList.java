package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.RanOutOfSpaceError;

public class FixedSizeList<T> extends ListADT<T> {
	/**
	 * This is the array of fixed size.
	 */
	private Object[] array;
	/**
	 * This keeps track of what we have used and what is left.
	 */
	private int fill;
	
	public FixedSizeList(int maximumSize) {
		this.array = new Object[maximumSize];
		this.fill = 0;
	}
	
	@Override
	protected boolean isEmpty() {
		return this.fill == 0;
	}

	@Override
	public int size() {
		return this.fill;
	}

	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		this.array[index] = value;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getIndex(int index) {
		checkNotEmpty();
		this.checkExclusiveIndex(index);
		return (T) this.array[index];
	}

	@Override
	public T getFront() {
		checkNotEmpty();
		return getIndex(0);
	}

	@Override
	public T getBack() {
		checkNotEmpty();
		return getIndex(this.fill-1);
	}

	@Override
	public void addIndex(int index, T value) {
		if (fill >= array.length) {
			throw new RanOutOfSpaceError();
		}
		// loop backwards, shifting items to the right.
		for (int j=fill; j>index; j--) {
			array[j] = array[j-1];
		}
		array[index] = value;
		fill++; 
	}

	@Override
	public void addFront(T value) {
		addIndex(0, value);
	}

	@Override
	public void addBack(T value) {
		if (fill < array.length) {
			array[fill++] = value;
		} else {
			throw new RanOutOfSpaceError();
}
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		T removed = this.getIndex(index);
		fill--;
		// shifting item to the left
		for (int i=index; i<fill; i++) {
			this.array[i] = this.array[i+1];
		}
		this.array[fill] = null;
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T value = this.getIndex(fill-1);
		fill--;
		this.array[fill] = null;
		return value;
	}

	@Override
	public T removeFront() {
		return removeIndex(0);
	}

	/**
	 * Is this data structure full? See {@linkplain ChunkyArrayList} for a user.
	 * @return if true this FixedSizeList is full.
	 */
	public boolean isFull() {
		return this.fill == this.array.length;
	}

}
