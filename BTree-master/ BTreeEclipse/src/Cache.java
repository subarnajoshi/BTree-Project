
import java.util.NoSuchElementException;

/**
 * @author Subarna Joshi
 *
 * @param <T>
 */
public class Cache<T> implements ICache<T> {
	private DLLNode<T> front, rear;
	private double Hits = 0;
	private int size = 0;
	private int max_capacity;
	private double totalAccess = 0;
  
	/** constructor.
	 * This constructor setup a cache with default capacity of cache.
	 */
	public Cache() {
		front = rear = null;
		max_capacity = 100;
	}
   /** 
    *@override constructor
    * This constructor setup a cache and the capacity of the cache can be 
    * passed as parameter.
    * @param capacity
    */
	public Cache(int capacity) {
		front = rear = null;
		max_capacity = capacity;
	}
    /**
     * launch the class.
     * @param args
     */
	public static <T> void main(String[] args) {
	}

	/**
	 * This method gets the data from cache and moves it to the front, if it's
	 * there. If not, returns null reference and add the data at the top of the
	 * cache.
	 * 
	 * @param target
	 * @return target, if found
	 * @return null, if target not found.
	 */
	public T get(T target) {
		boolean found = false;
		DLLNode<T> current = front;
		while (current != null && !found) {
			if (target.equals(current.getElement())) { // checking if the current element is equal to the target.
				found = true;
				Hits++;

			} else {
				current = current.getNext();
			}
		}
		totalAccess++;
		if (!found) {
			add(target);  // add the target element if the target is not found
			return null;
		}
		if (size() == 1) { // if cache have only one element
			front = rear = current;
		} else if (current == rear) { // if the target is at the rear or last position.
			rear = current.getPrevious();
			current.setPrevious(null);
			rear.setNext(null);
			current.setNext(front);
			front.setPrevious(current);
			front = current;
		} else if (current == front) { // if the target is at the first position, no need to move to first
			current = front;
		} else { // if the target is somewhere in the middle.
			current.getPrevious().setNext(current.getNext());
			current.getNext().setPrevious(current.getPrevious());
			current.setNext(front);
			front.setPrevious(current);
			front = current;
		}
		
		return current.getElement();
	}
    /**
     * This method gives the size of the Cache.
     * @return size.
     */
	public int size() { 
		return size;
	}

	/***
	 * Clears contents of the cache, but doesn't change its capacity.
	 */
	public void clear() {

		front = null;  
		rear = null;
		size = 0;
	}

	/***
	 * This method adds given data to front of cache and Removes data in last position, if full.
	 * 
	 * @param data
	 */
	public void add(T data) {
		DLLNode<T> member = new DLLNode<T>(data);
		if (isEmpty()) { // adding if the cache is empty.
			front = rear = member;
		} else if (size >= max_capacity) { // check if the size of the cache have reached its limit
			removeLast(); // remove the last element if the size of the cache is full.
			DLLNode<T> current = front;
			front = member;
			member.setNext(current);
			current.setPrevious(member);

		} else {
			DLLNode<T> current = front;  // adding the data in the first position
			front = member;
			member.setNext(current);
			current.setPrevious(member);
		}
		size++;
	}

	/***
	 * This method removes the data in last position from the cache.
	 * 
	 * @throws IllegalStateException - if cache is empty.
	 */
	public void removeLast() {
		if (isEmpty()) { // when the list is empty.
			throw new IllegalStateException();
		}
		if (size > 1) { // if the cache have more than one element
			rear = rear.getPrevious();
			rear.setNext(null);
		} else {  // when the cache have only one element.
			front = rear = null;
		}
		size--;
	}

	/**
	 * This method removes the given target data from the cache.
	 * 
	 * @throws NoSuchElementException, if the target is not found
	 * @param target
	 */
	public void remove(T target) {
		if (isEmpty()) { // if the cache is empty.
			throw new NoSuchElementException();
		}
		boolean found = false;
		DLLNode<T> current = front;
		while (current != null && !found) {
			if (target.equals(current.getElement())) { // checking if the current element is equal to the target.
				found = true;
				Hits++;
			} else {
				current = current.getNext(); // change the current element to the next element
			}
		}
		if (!found) {
			throw new NoSuchElementException();
		}

		if (size() == 1) { // if cache have only one element
			front = rear = null;
		} else if (current == front) { // if the target is at the first position
			front = current.getNext();
		} else if (current == rear) { // if the target is at the rear or last position.
			rear = current.getPrevious();
			current.setPrevious(null);
			rear.setNext(null);
		} else {    // if the target at the middle somewhere.
			current.getPrevious().setNext(current.getNext());
			current.getNext().setPrevious(current.getPrevious());
		}
		size--;
	}

	/**
	 * This method moves data already in cache to the front and add the target data in front if not found.
	 * 
	 * @throws NoSuchElementException - if data is not found in cache.
	 * @param data
	 */
	public void write(T data) {
		if (isEmpty()) { // if the list is empty.
			throw new NoSuchElementException();
		}
		boolean found = false;
		DLLNode<T> current = front;

		while (current != null && !found) {
			if (data.equals(current.getElement())) { // checking if the current element is equal to the target.
				found = true;
			} else {
				current = current.getNext();   // change the current element to the next element
			}
		}
		if (!found) {
			add(data);   // add the target element if the target is not found
			throw new NoSuchElementException();
		}

		if (size() == 1) {  // if cache have only one element
			front = rear = current;
		} else if (current == front) {  // if the target is at the first position
			current = front;
		} else if (current == rear) { // if the target is at the rear or last position.
			rear = current.getPrevious();
			rear.setNext(null);
			current.setPrevious(null);
			current.setNext(front);
			front.setPrevious(current);
			front = current;
		} else {   // if the target at the middle somewhere.
			current.getPrevious().setNext(current.getNext());
			current.getNext().setPrevious(current.getPrevious());
			current.setNext(front);
			front.setPrevious(current);
			front = current;
		}

	}
	/**
	 * This method gives the hit rate of the cache.
	 * @return getHitRate. 
	 */
	public double getHitRate() {
		double hitRate = 0;
		if (totalAccess != 0) {  // make sure the memory access is not 0.
			hitRate = Hits / totalAccess;
		} else {  // when the memory access is 0
			return 0.00;  
		}
		return hitRate;
	}
	/**
	 * This method gives the miss rate of the cache.  
	 * @return missRate
	 */
	public double getMissRate() {
		double missRate = 0;
		missRate = 1 - getHitRate(); // gives the missRate based on hitRate.

		return missRate;
	}
	/**
	 * This method check Whether there's any data in cache or not. 
	 * @return true, if the cache is empty
	 * @return false, if the cache is not empty 
	 */
	public boolean isEmpty() {
		if (size == 0) { 
			return true;
		} else {
			return false;
		}
	}
	/**
	 * This method gives the number of Hits.
	 * @return Hits.
	 */
	public double getHit() {
		return Hits;
	}
	/** 
	 * This method gives the value of total Cache accessed.
	 * @return totalAccess.
	 */
    public double getAccess() {
    	return totalAccess;
    } 
  
	
}
