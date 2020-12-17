import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * @author Subarna Joshi
 * @author Nealon Hager
 * @author Kira Davis
 *
 * @param <T>
 */
public class BTreeNode<T> {

	private BTreeNode<T> parent;
	private BTreeNode<T>[] Children;
	private TreeObject[] objects;
	private int minDegree;
	private int size;
	private int childSize;

	/**
	 * Constructor
	 * 
	 * @param minimumDegree
	 */
	public BTreeNode(int minimumDegree) {
		objects = new TreeObject[(2 * minimumDegree)-1];
		parent = null;
		Children = new BTreeNode[2*minimumDegree];
		minDegree = minimumDegree;
		size = 0;
	}

	/**
	 * This methods gets the parent of the BTreeNode
	 * 
	 * @return parent
	 */
	public BTreeNode<T> getParent() {
		return this.parent;
	}

	/**
	 * This methods sets the parent of the BTreeNode
	 * 
	 * @param parent
	 */
	public void setParent(BTreeNode<T> parent) {
		this.parent = parent;
	}

	/**
	 * This method gives the child BTreeNode of specified index
	 * 
	 * @param index
	 * @return Children[index]
	 */

	public BTreeNode<T> getChild(int index) {
		if (index < 0 || index >= Children.length)
			throw new IndexOutOfBoundsException();
		return Children[index];
	}

	/**
	 * This method sets the child BtreeNode in the specified index
	 * 
	 * @param child
	 * @param index
	 */
	public void setChild(BTreeNode<T> child, int index) {
		if (index < 0 || index >= Children.length)
			throw new IndexOutOfBoundsException();
		this.Children[index] = child;
	}
	
	/**
	 * This methods add child to the specified index
	 * @param index
	 */
	public BTreeNode<T> removeChild (int index) {
		if (index < 0 || index >= Children.length)
			throw new IndexOutOfBoundsException();
		if(this.Children[index] == null) {
			 throw new NoSuchElementException();
			}
			else {
				BTreeNode<T> result = Children[index];
				Children[index] = null;
				childSize--;
				return result;
			}
	}
	
	/**
	 * This methods add child to the specified index
	 * @param child
	 * @param index
	 */
	public void addChild (BTreeNode<T> child, int index) {
		if (index < 0 || index >= Children.length)
			throw new IndexOutOfBoundsException();
		if(this.Children[index] == null) {
			Children[index] = child;
			childSize++;}
			else {
				Children[index] = child;
			}
	}

	/**
	 * This method sets the TreeObject in the BTreeNode
	 * 
	 * @param obj
	 * @param index
	 */
	public void setObject(TreeObject obj, int index) {
		if (index < 0 || index >= objects.length)
			throw new IndexOutOfBoundsException();
		this.objects[index] = obj;
	}

	/**
	 * @param index index in BTreeNode to get
	 * @return BTreeNode
	 * @throws IndexOutOfBoundsException if index is out of bounds
	 * @throws NoSuchElementException if no element at index
	 */
	public TreeObject getObject(int index) {
		int idx = 0;
		boolean found = false;
		if(index < 0 || index >= objects.length)
			throw new IndexOutOfBoundsException();
		if(objects[index]==null)
			throw new NoSuchElementException();
		return objects[index];
	}

	/** This method adds object if the node is full or not, returns index if node needs a split after add
	 * @param obj
	 * @return -1 if a regular add, index if a split is needed, -2 if error
	 */
	public int addObject(TreeObject obj) {
		if(size == 0) {
			setObject(obj,0);
			size++;
			return -1;
		}
		int idx = size - 1 ;
		if(obj.getKey() > objects[idx].getKey()) {
			setObject(obj,idx+1);
			size++;
		}
		else {
		while(idx >= 0 && obj.getKey() <= objects[idx].getKey()) {
			if(obj.getKey() == objects[idx].getKey()) {
				objects[idx].setFrequency();
				return -1;
			}
			if(isFull()) {
				return -1;
			}
			setObject(objects[idx],idx+1);
			idx--;
		}
	     setObject(obj,idx + 1);
		size++;
		}
		if(size == objects.length) {
			return objects.length/2;
		}
		return -1;
	}
	
	/**
	 * add TreeObject to specified index
	 * @param obj
	 * @param index
	 */
	public void addObjectInIdx(TreeObject obj,int index) {
		if(index < 0 || index >= objects.length)
			throw new IndexOutOfBoundsException();
		if(objects[index] == null) {
		objects[index] = obj;
		size++;}
		else if(obj.getKey() == objects[index].getKey()) {
			objects[index].setFrequency();
		}
		else {
			objects[index] = obj;
		}
	}

	/**
	 * This method removes the TreeObject specified by index
	 * 
	 * @param index
	 * @return result
	 */
	public TreeObject removeObject(int index) {
		
		if(index < 0 || index >= objects.length)
			throw new IndexOutOfBoundsException();
		if(objects[index]==null)
			throw new NoSuchElementException();
		TreeObject result = objects[index];
		objects[index] = null;
		if(size != 1) {
		while(index < size - 1) {
			setObject(objects[index + 1],index);
			setObject(null, index +1);
			index++;
		}
		}
		size--;
		return result;
	}

	/**
	 * This method check if the BTreeNode is a leaf or not
	 * 
	 * @return true if it's a leaf else false
	 */
	public boolean isLeaf() {
		for(BTreeNode child: Children) {
			if(child != null)
				return false;
		}
		return true;
	}

	/**
	 * This method check if the BTreeNode is empty or not
	 * 
	 * @return true if empty else return false
	 */
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;

	}

	/**
	 * This method check if the BTreeNode is full or not
	 * 
	 * @return true if full else false
	 */
	public boolean isFull() {
		if (size == 2*minDegree-1) {
			return true;
		}
		return false;
	}

	/**
	 * This method gets the height of this node
	 * 
	 * @return height as int
	 */
	public int getHeight() {
		for(BTreeNode<T> child:Children) {
			if(child == null)
				return 0;
			return 1 + child.getHeight();
		}
		return 0;
	}
	
	/** This method returns the number of TreeObjects in the node
	 * @return int
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * This method returns the size of the Children
	 * @return
	 */
	public int childSize() {
		return childSize;
	}
	   
    /** This method returns the children array
     * @return BTreeNode[]
     */
    public BTreeNode[] getChildrenArray() {
    	return Children;
    }
    
    /**
     * This method check if the TreeObject contains in the BTreeNode.
     * @param obj
     * @return index of TreeObject if exists else -1
     */
    public int containsTreeObject(TreeObject obj) {
		for (int i = 0; i < objects.length; i++){
			if (objects[i] != null && objects[i].getKey() == obj.getKey() && objects[i].getValue() == obj.getValue())
    			return i;
    	}
		return -1;
    }
    
    /**
     * This method determine whether the BTreeNode is Root or not.
     * @return
     */
    public boolean isRoot() {
		if(this.getParent() == null) {
			return true;
		}
		return false;
}
}
    
    
