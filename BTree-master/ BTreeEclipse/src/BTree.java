import java.io.File;
import java.util.NoSuchElementException;

/**
 * @author Nealon Hager
 * @author Subarna Joshi
 * @author Kira Davis
 * @author Cayson Wilkins
 *
 * @param <T>
 */
public class BTree<T> {

	private BTreeNode<T> root;
	private File bTreeFile;
	private int degree, minDegree, size, cacheSize;
	private boolean useCache;
	private Cache cache;
	private boolean found;
	
	BTree(int minDegree) {
		this.minDegree = minDegree;
		degree = minDegree*2;
		root = new BTreeNode<T>(minDegree);
		useCache = false;
		size = 0;
	}
	
	BTree(int minDegree, int cacheSize) {
		this.minDegree = minDegree;
		root = new BTreeNode<T> (minDegree);
		degree = minDegree*2;
		size = 0;
		useCache = true;
		cache = new Cache(cacheSize);
	}
	
	/** This method returns the height of a node
	 * @param node
	 * @return int
	 */
	public int height(BTreeNode node) {
		if(node==null || node.isLeaf())
			return 0;
		return node.getHeight();
	}
	
	/** This method determines if the btree is empty or not
	 * @return true if empty
	 */
	public boolean isEmpty() {
		return (size==0);
	}
	
	/** This method returns the number of TreeObjects in the BTree
	 * @return int
	 */
	public int size() {
		return size;
	}
	
	/**
	 * This method insert the TreeObject in the leaf of the BTree.
	 * @param treeObj
	 */
	public void Insert( TreeObject treeObj) {
		
		// search cache before adding
		if(useCache) {
			TreeObject obj = (TreeObject) cache.get(treeObj);
			if(obj!=null) {
				obj.setFrequency();
				return;
			}
		}
		
		// without cache		
		BTreeNode<T> current = this.root;
		if(current.containsTreeObject(treeObj) >=0) {
			current.getObject(current.containsTreeObject(treeObj)).setFrequency();
		}
		else if (current.isFull()) { // check if the Root is full
			BTreeNode<T> newNode = new BTreeNode<T>(minDegree);
			this.root = newNode;
			newNode.addChild(current, 0);
			splitChild(newNode, 0, current);
			InsertNotFull(newNode, treeObj);
		}
			
		else {
			InsertNotFull(current,treeObj);
		}
	}
	
	/**
	 * This method split the BTreeNode if its full.
	 * @param parent
	 * @param i
	 * @param split
	 */
	private void splitChild(BTreeNode parent , int i, BTreeNode split) {
		// TODO Auto-generated method stub
		BTreeNode Node = new BTreeNode(minDegree);
		for(int j = 0; j <= minDegree - 2; j++) {
			Node.addObjectInIdx(split.removeObject(minDegree), j);
		}
		if(!split.isLeaf()) {  //check if the node have children or not
			for (int j = 0; j <= minDegree -1; j++) {
			Node.addChild(split.removeChild(j + minDegree), j);
		}
		}
		// need to set parent // 
		Node.setParent(parent);
		for(int j = parent.getSize(); j > i; j--) {
			parent.addChild(parent.removeChild(j), j + 1);
		}
		parent.setChild(Node,i + 1);  // set the child
        for(int j = parent.getSize(); j > i; j--) {
        	parent.addObjectInIdx(parent.getObject(j - 1), j);
        }
        parent.addObjectInIdx(split.removeObject(minDegree - 1), i);
		
		}
	
/**
 * This method insert the treeObject in the specified BTreeNode.
 * @param current
 * @param treeObj
 */
	public void InsertNotFull (BTreeNode<T> current,TreeObject treeObj)
	{
		int i = current.getSize();
		if(current.isLeaf()) {   
			current.addObject(treeObj);
			return;
		}
		else {  // when the node is not the leaf.
			if(i > 0 && treeObj.getKey() == current.getObject(i-1).getKey()) {
				current.getObject(i-1).setFrequency();
			}
			while(i>= 1 && treeObj.getKey() <= current.getObject(i-1).getKey()) {
				if(treeObj.getKey() == current.getObject(i-1).getKey()) {
					current.getObject(i-1).setFrequency();
				}
				i--;
			}
			BTreeNode child = current.getChild(i); 
			if(child.isFull()) {
				splitChild(current, (i), child);
				if(treeObj.getKey() == current.getObject(i).getKey()) {
					//current.getObject(i-1).setFrequency();
					current.getObject(i).setFrequency();
					return;
				}
				if(treeObj.getKey() > current.getObject(i).getKey()) {
				i++;
				child = current.getChild(i);}
			}
			InsertNotFull(child, treeObj);
		}	
			
		}
	
	/**
	 * This method search for the TreeObject in the BTree
	 * @param obj
	 * @return TreeObject.
	 */
	
	public TreeObject search(TreeObject obj) {
		BTreeNode<T> current = root;
		
		if(useCache) {
			if(cache.get(obj)!=null)
				return (TreeObject) cache.get(obj);
		}
		
		boolean keepSearching = true;
		
		while(keepSearching) {			
			if(current==null || current.getSize()==0) // current is empty, item isn't in tree
				throw new NoSuchElementException();
			
			int idx = current.containsTreeObject(obj);
			if (idx >= 0) {	// object found in current node
				found = true;
				return current.getObject(current.containsTreeObject(obj));}
			else if (idx < 0) { // needs to search deeper
				int sz = current.getSize();
				for (int i = 0; i < sz; i++) {
					
					if(obj.getKey() < current.getObject(i).getKey()) { // go down to child
						current = current.getChild(i); // set current BTreeNode to child
						break;
					} else if (i == current.getSize() - 1 && (obj.getKey() > current.getObject(i).getKey())) { // edge case child
						current = current.getChild(i+1);
					}
				}
			}
		}
		
		return null;

	}
	/*
	 * This method merge the two given BTreeNode in a single Node.
	 */
	
	public void merge(BTreeNode<T> left, BTreeNode<T> right) {
		BTreeNode<T> merged = new BTreeNode<T>(minDegree);
		int idx = left.getSize();
		if(left.isLeaf() && right.isLeaf()) {
			while(right.getSize()>0) {
				left.addObject(right.removeObject(0));
			}
		}
		if(!left.isLeaf() || !right.isLeaf()) {
			while(right.getSize()>0) {
				left.addObject(right.removeObject(0));
			}
			int sizeOfchild = left.getSize();
			while(right.getChild(0).getSize()>0) {
				if(!left.getChild(idx).isFull()) {
					left.getChild(idx).addObject(right.getChild(0).removeObject(0));
				}
				else {
					splitChild(left,idx,left.getChild(idx));
					left.getChild(idx).addObject(right.getChild(0).removeObject(0));
				}
			}
			for (int k = 1; k < right.getSize(); k ++) {
				left.setChild(right.getChild(k), k + sizeOfchild);
			}			
		}
	}
	
	/**
	 * This method delete the specified Object from the BTreeNode.
	 * @param start
	 * @param treeObj
	 */
	public void delete(BTreeNode start,TreeObject treeObj) {
		boolean found = false;
		if(start.isLeaf()&& start.containsTreeObject(treeObj) < 0) {
			throw new NoSuchElementException();
		}
		if (start.containsTreeObject(treeObj) >= 0){
			int i = start.containsTreeObject(treeObj);
			if(start.getObject(i).getFrequency() >=1) {
				start.getObject(i).decreaseFrequency();
			}
			else {
			start.removeObject(start.containsTreeObject(treeObj));
			if(!start.isLeaf()) {
				if(start.getChild(i).getSize()> minDegree-1) {
					start.addObject(start.getChild(i).getObject(start.getChild(i).getSize()));
				}
				else if(start.getChild(i + 1).getSize()> minDegree-1) {
					start.addObject(start.getChild(i + 1).getObject(0));
				}
				else {
					merge(start.getChild(i),start.getChild(i + 1));
					start.removeChild(i+1);
					if(start.isRoot() && start.getSize() == 0) {
						this.root = start.getChild(i);
					}
				}
			}
			}
		}
		else {
			int i = start.getSize();
			while(i>= 1 && treeObj.getKey() <= start.getObject(i-1).getKey()) {
				i--;
			}
			BTreeNode child = start.getChild(i);
	      if(child.getSize() <= minDegree-1) {
	    	  
	    	  if(i > 0 && start.getChild(i -1).getSize() > minDegree-1) {
	    		  BTreeNode leftSib = start.getChild(i -1);
	    		  child.addObject(start.removeObject(i)); 
	    		  start.addObject(leftSib.removeObject(leftSib.getSize() - 1));
	    	  }
	    	  else if(i > start.getSize() && start.getChild(i + 1).getSize() > minDegree-1) {
	    		  BTreeNode rightSib = start.getChild(i+1);
	    		  child.addObject(start.removeObject(i)); 
	    		  start.addObject(rightSib.removeObject(0));
	    	  }
	    	  else {
	    		  merge(child,start.getChild(i+1));
	    		  start.removeChild(i+1);
					if(start.isRoot() && start.getSize() == 0) {
						this.root = child;
					}
	    	  }
	    	  delete(child,treeObj);
	    	  }
	      }
		}
		

	
	/**
	 * setter for the root of the BTree
	 * 
	 * @param newRoot
	 */
	public void setRoot(BTreeNode<T> newRoot) {
		root = newRoot;
	}
	
	/**
	 * getter for the root of the BTree
	 * 
	 * @return BTreeNode<T> root
	 */
	public BTreeNode<T> getRoot() {
		return root;
	}
}


	
	
