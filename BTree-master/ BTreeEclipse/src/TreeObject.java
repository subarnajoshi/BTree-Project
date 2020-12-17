
/**
 * @author Subarna Joshi
 *
 */
public class TreeObject {

	private long value;
	private long key;
	private int frequency;
	private String str;
	
	/**
	 * constructor for TreeObject
	 * @param value
	 * @param key
	 */
	public TreeObject(long value, long key) {
		setValue(value);
		setKey(key);
		frequency = 0;
	}
	
	public TreeObject(long value, long key, String valAsString) {
		setValue(value);
		setKey(key);
		str = valAsString;
		frequency = 0;
	}
	
	public String getStr() {
		return str;
	}
	
	/**
	 * This method gives the value of TreeObject
	 * @return
	 */
	public long getValue() {
		return value;
	}
	
	/**
	 * This method sets the value of the TreeObject
	 * @param value
	 */
	public void setValue(long value) {
		this.value = value;
	}
	
	/**
	 * This method gives the key of the TreeObject
	 * @return key
	 */
	public long getKey() {
		return key;
	}
	
	/**
	 * This method sets the key to the TreeObject
	 * @param key
	 */
	public void setKey(long key) {
		this.key = key;
	}
	
	/**
	 * This method gives the frequency of the TreeObject
	 * @return frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	
	/**
	 * This method sets the frequency of the TreeObject
	 */
	public void setFrequency() {
		frequency++;
	}
	
	/**
	 * This method decrease the frequency of the TreeObject.
	 */
	public void decreaseFrequency() {
		frequency--;
	}
}
