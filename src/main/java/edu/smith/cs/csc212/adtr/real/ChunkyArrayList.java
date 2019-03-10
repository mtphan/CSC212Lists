package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.EmptyListError;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	private int chunkSize;
	private GrowableList<FixedSizeList<T>> chunks;

	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		checkNotEmpty();
		T removed = this.chunks.getFront().removeFront();
		if (this.chunks.getFront().isEmpty()) {
			this.chunks.removeFront();
		}
		return removed;
	}

	@Override
	public T removeBack() {
		checkNotEmpty();
		T removed = this.chunks.getBack().removeBack();
		if (this.chunks.getBack().isEmpty()) {
			this.chunks.removeBack();
		}
		return removed;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		int start = 0;
		int chunkIndex = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				T removed = chunk.removeIndex(index-start);
				// Remove empty chunk.
				if (chunk.isEmpty()) {
					chunks.removeIndex(chunkIndex);
				}
				return removed;
			}
			
			// update bounds of next chunk and its index.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}

	@Override
	public void addFront(T item) {
		if (this.chunks.isEmpty() || chunks.getFront().isFull()) {
			this.chunks.addFront(makeChunk());
		}
		this.chunks.getFront().addFront(item);
	}

	@Override
	public void addBack(T item) {
		if (this.chunks.isEmpty() || this.chunks.getBack().isFull()) {
			this.chunks.addBack(makeChunk());
		}
		this.chunks.getBack().addBack(item);
	}

	@Override
	public void addIndex(int index, T item) {
		
		int chunkIndex = 0;
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			// if index is added at the very end of a full chunk then check index against end-1 instead of end.
			// (i.e. use index < end instead of index <= end).
			if (start <= index && index <= (index-start == chunkSize ? end-1 : end)) {
				if (chunk.isFull()) {
					// check can roll to next
					// or need a new chunk
					FixedSizeList<T> newChunk = makeChunk();
					
					newChunk.addBack(chunk.removeBack());
					// chunk is now not full:
					chunk.addIndex(index-start, item);
					
					// add new chunk after current chunk
					this.chunks.addIndex(chunkIndex+1, newChunk);
				} else {
					// put right in this chunk, there's space.
					chunk.addIndex(index-start, item);
				}	
				// upon adding, return.
				return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		checkNotEmpty();
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
				
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				chunk.setIndex(index - start, value);
				return;
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
}