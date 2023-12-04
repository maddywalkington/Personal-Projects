package finalproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MyHashTable<K,V> implements Iterable<MyPair<K,V>>{
	// num of entries to the table
	private int size;
	// num of buckets 
	private int capacity = 16;
	// load factor needed to check for rehashing 
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<MyPair<K,V>>> buckets; 


	// constructors
	public MyHashTable() {
		// ADD YOUR CODE BELOW THIS
		size = 0;
		capacity = 16;
		buckets = new ArrayList<>(capacity);
		for (int i = 0; i < capacity; i++) {
			buckets.add(new LinkedList<MyPair<K, V>>());
		}
		//ADD YOUR CODE ABOVE THIS
	}

	public MyHashTable(int initialCapacity) {
		// ADD YOUR CODE BELOW THIS
		size = 0;
		capacity = initialCapacity;
		buckets = new ArrayList<>(capacity);
		for (int i = 0; i < capacity; i++) {
			buckets.add(new LinkedList<MyPair<K, V>>());
		}
		//ADD YOUR CODE ABOVE THIS
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public int numBuckets() {
		return this.capacity;
	}

	/**
	 * Returns the buckets variable. Useful for testing  purposes.
	 */
	public ArrayList<LinkedList< MyPair<K,V> > > getBuckets(){
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key. 
	 */
	public int hashFunction(K key) {
		int hashValue = Math.abs(key.hashCode())%this.capacity;
		return hashValue;
	}

	/**
	 * Takes a key and a value as input and adds the corresponding HashPair
	 * to this HashTable. Expected average run time  O(1)
	 */
	public V put(K key, V value) {
		//  ADD YOUR CODE BELOW HERE
			int bucketIndex = hashFunction(key);
			LinkedList<MyPair<K, V>> bucket = buckets.get(bucketIndex);

			for (MyPair<K, V> pair : bucket) {
				if (pair.getKey().equals(key)) {
					V oldValue = pair.getValue();
					pair.setValue(value);
					return oldValue;
				}
			}

			bucket.add(new MyPair<>(key, value));
			size++;

			if ((double) size / capacity > MAX_LOAD_FACTOR) {
				rehash();
			}

			return null;

		//  ADD YOUR CODE ABOVE HERE
	}


	/**
	 * Get the value corresponding to key. Expected average runtime O(1)
	 */

	public V get(K key) {
		//ADD YOUR CODE BELOW HERE
			int bucketIndex = hashFunction(key);
			LinkedList<MyPair<K, V>> bucket = buckets.get(bucketIndex);

			for (MyPair<K, V> pair : bucket) {
				if (pair.getKey().equals(key)) {
					return pair.getValue();
				}
			}

			return null;

		//ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1) 
	 */
	public V remove(K key) {
		//ADD YOUR CODE BELOW HERE

			int bucketIndex = hashFunction(key);
			LinkedList<MyPair<K, V>> bucket = buckets.get(bucketIndex);

			Iterator<MyPair<K, V>> iterator = bucket.iterator();
			while (iterator.hasNext()) {
				MyPair<K, V> pair = iterator.next();
				if (pair.getKey().equals(key)) {
					iterator.remove();
					size--;
					return pair.getValue();
				}
			}

			return null;
		//ADD YOUR CODE ABOVE HERE
	}


	/** 
	 * Method to double the size of the hashtable if load factor increases
	 * beyond MAX_LOAD_FACTOR.
	 * Made public for ease of testing.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */
	public void rehash() {
		//ADD YOUR CODE BELOW HERE
			int newCapacity = capacity * 2;
			ArrayList<LinkedList<MyPair<K, V>>> newBuckets = new ArrayList<>(newCapacity);

			// Initialize new buckets
			for (int i = 0; i < newCapacity; i++) {
				newBuckets.add(new LinkedList<MyPair<K, V>>());
			}
			capacity = newCapacity;
			// Rehash existing entries into new buckets
			for (LinkedList<MyPair<K, V>> bucket : buckets) {
				for (MyPair<K, V> pair : bucket) {
					int newBucketIndex = hashFunction(pair.getKey());
					LinkedList<MyPair<K, V>> newBucket = newBuckets.get(newBucketIndex);
					newBucket.add(pair);
				}
			}
		// Update fields
		buckets = newBuckets;
		//ADD YOUR CODE ABOVE HERE
	}


	/**
	 * Return a list of all the keys present in this hashtable.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public ArrayList<K> getKeySet() {
			ArrayList<K> keys = new ArrayList<>();
			for (LinkedList<MyPair<K, V>> bucket : buckets) {
				for (MyPair<K, V> pair : bucket) {
					keys.add(pair.getKey());
				}
			}
			return keys;

	}

	/**
	 * Returns an ArrayList of unique values present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<V> getValueSet() {
			ArrayList<V> values = new ArrayList<>();
			ArrayList<MyPair<K, V>> entries = getEntries();
			for (MyPair<K, V> entry : entries) {
				V value = entry.getValue();
				if (!values.contains(value)) {
					values.add(value);
				}
			}
			return values;

	}


	/**
	 * Returns an ArrayList of all the key-value pairs present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<MyPair<K, V>> getEntries() {
			ArrayList<MyPair<K, V>> entries = new ArrayList<>();
			for (LinkedList<MyPair<K, V>> bucket : buckets) {
				for (MyPair<K, V> pair : bucket) {
					entries.add(pair);
				}
			}
			return entries;
		}


	
	
	@Override
	public MyHashIterator iterator() {
		return new MyHashIterator();
	}


	private class MyHashIterator implements Iterator<MyPair<K, V>> {
		private int currentBucket;
		private Iterator<MyPair<K, V>> currentElement;

		public MyHashIterator() {
			currentBucket = 0;
			currentElement = null;
			// Find the first non-empty bucket, if any
			while (currentBucket < capacity && buckets.get(currentBucket).isEmpty()) {
				currentBucket++;
			}
			if (currentBucket < capacity) {
				currentElement = buckets.get(currentBucket).listIterator();
			}
		}

		public boolean hasNext() {
			if (currentElement == null) {
				return false;
			}
			// If there is another element in the current bucket, return true
			if (currentElement.hasNext()) {
				return true;
			}
			// Otherwise, find the next non-empty bucket, if any
			int nextBucket = currentBucket + 1;
			while (nextBucket < capacity && buckets.get(nextBucket).isEmpty()) {
				nextBucket++;
			}
			if (nextBucket < capacity) {
				return true;
			}
			return false;
		}

		public MyPair<K, V> next() {
			if (!hasNext()) {
				return null;
			}
			// If there is another element in the current bucket, return it
			if (currentElement.hasNext()) {
				return currentElement.next();
			}
			// Otherwise, move to the next non-empty bucket and return the first element
			currentBucket++;
			while (currentBucket < capacity && buckets.get(currentBucket).isEmpty()) {
				currentBucket++;
			}
			currentElement = buckets.get(currentBucket).listIterator();
			return currentElement.next();
		}
	}

}

