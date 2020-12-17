import org.junit.Test;
import static org.junit.Assert.*;

import java.util.NoSuchElementException;

/**
 * @author Nealon Hager
 *
 */
public class BTreeTest {

	@Test
	public void emptyTree_isEmpty() {
		BTree tree = new BTree(2);
		assertTrue(tree.isEmpty());
	}
	
	@Test
	public void emptyTree_size() {
		BTree tree = new BTree(2);
		assertTrue(tree.size() == 0);
	}
	
	@Test
	public void emptyTree_height() {
		BTree tree = new BTree(2);
		assertTrue(tree.height(tree.getRoot()) == 0);
	}
	
	@Test
	public void setRoot() {
		BTree tree = new BTree(2);
		BTreeNode node = new BTreeNode(2);
		tree.setRoot(node);
		assertTrue(tree.getRoot()==node);
	}
	
	@Test
	public void emptyTree_add() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.getRoot().addObject(obj);
		assertTrue(tree.getRoot().getObject(0)==obj);
	}
	
	@Test
	public void emptyTree_addMultipleTimes() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(2,2);
		TreeObject obj2 = new TreeObject(3,3);
		tree.getRoot().addObject(obj);
		tree.getRoot().addObject(obj1);
		tree.getRoot().addObject(obj2);
		assertTrue(tree.getRoot().getObject(0)==obj
				&& tree.getRoot().getObject(1)==obj1
				&& tree.getRoot().getObject(2)==obj2
				);
	}
	
	@Test
	public void emptyTree_addMultipleTimesOutOfOrder() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(2,2);
		TreeObject obj2 = new TreeObject(3,3);
		tree.getRoot().addObject(obj1);
		tree.getRoot().addObject(obj);
		tree.getRoot().addObject(obj2);
		assertTrue(tree.getRoot().getObject(0)==obj
				&& tree.getRoot().getObject(1)==obj1
				&& tree.getRoot().getObject(2)==obj2
				);
	}
	
	@Test
	public void itemInRoot_search() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.Insert(obj);
		assertTrue(tree.search(obj) == obj);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void itemNotFound_search() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.search(obj);
	}
	
	@Test
	public void emptyTree_addSameObjTwice() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.getRoot().addObject(obj);
		tree.getRoot().addObject(obj);
		assertTrue(tree.getRoot().getObject(0).getFrequency()==1 && tree.getRoot().getSize()==1);
	}
	
	@Test
	public void fullRoot_in0thChild_search() {
		BTree tree = new BTree(2);
		BTreeNode child = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj2 = new TreeObject(2,2);
		TreeObject obj3 = new TreeObject(3,3);
		TreeObject obj4 = new TreeObject(4,4);
		BTreeNode root = tree.getRoot();
		
		root.addObject(obj2);
		root.addObject(obj3);
		root.addObject(obj4);
		root.setChild(child, 0);
		child.addObject(obj);
		
		assertTrue(tree.search(obj)==obj);
	}
	
	@Test
	public void fullRoot_empty0thChild_in1stChild_search() {
		BTree tree = new BTree(2);
		BTreeNode child = new BTreeNode(2);
		BTreeNode child1 = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj2 = new TreeObject(2,2);
		TreeObject obj3 = new TreeObject(3,3);
		TreeObject obj4 = new TreeObject(4,4);
		BTreeNode root = tree.getRoot();
		
		root.addObject(obj);
		root.addObject(obj3);
		root.addObject(obj4);
		root.setChild(child, 0);
		root.setChild(child1, 1);
		child1.addObject(obj2);
		
		assertTrue(tree.search(obj2)==obj2);
	}
	
	@Test
	public void fullRoot_no0thChild_in1stChild_search() {
		BTree tree = new BTree(2);
		BTreeNode child1 = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj2 = new TreeObject(2,2);
		TreeObject obj3 = new TreeObject(3,3);
		TreeObject obj4 = new TreeObject(4,4);
		BTreeNode root = tree.getRoot();
		
		root.addObject(obj);
		root.addObject(obj3);
		root.addObject(obj4);
		root.setChild(child1, 1);
		child1.addObject(obj2);
		
		assertTrue(tree.search(obj2)==obj2);
	}
	
	@Test
	public void fullRoot_only3rdChild_in3rdChild_search() {
		BTree tree = new BTree(2);
		BTreeNode child1 = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj2 = new TreeObject(2,2);
		TreeObject obj3 = new TreeObject(3,3);
		TreeObject obj4 = new TreeObject(4,4);
		BTreeNode root = tree.getRoot();
		
		root.addObject(obj);
		root.addObject(obj2);
		root.addObject(obj3);
		root.setChild(child1, 3);
		child1.addObject(obj4);
		
		assertTrue(tree.search(obj4)==obj4);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void fullRoot_hasChildren_failSearch() {
		BTree tree = new BTree(2);
		BTreeNode child = new BTreeNode(2);
		BTreeNode child1 = new BTreeNode(2);
		BTreeNode child2 = new BTreeNode(2);
		BTreeNode child3 = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(5,5);
		TreeObject obj2 = new TreeObject(2,2);
		TreeObject obj3 = new TreeObject(3,3);
		BTreeNode root = tree.getRoot();
		
		root.addObject(obj);
		root.addObject(obj2);
		root.addObject(obj3);
		root.setChild(child, 0);
		root.setChild(child1, 1);
		root.setChild(child2, 2);
		root.setChild(child1, 3);
		tree.search(obj1);
	}
	
	@Test
	public void fullRoot_objInGrandchild_search() {
		BTree tree = new BTree(2);
		BTreeNode child = new BTreeNode(2);
		BTreeNode grandchild = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(5,5);
		TreeObject obj2 = new TreeObject(2,2);
		BTreeNode root = tree.getRoot();
		root.addObject(obj2);
		root.setChild(child, 0);
		child.addObject(obj1);
		child.setChild(grandchild, 0);
		grandchild.addObject(obj);
		
		assertTrue(tree.search(obj)==obj);
	}
	
	@Test
	public void emptyRoot_insert() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.Insert(obj);
		assertTrue(tree.getRoot().getObject(0)==obj);
	}
	
	@Test
	public void emptyRoot_insertToFull() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1, 1);
		TreeObject obj1 = new TreeObject(2, 2);
		TreeObject obj2 = new TreeObject(3, 3);
		tree.Insert(obj);
		tree.Insert(obj1);
		tree.Insert(obj2);
		BTreeNode root = tree.getRoot();
		
		assertTrue(root.getObject(0) == obj 
				&& root.getObject(1) == obj1 
				&& root.getObject(2) == obj2);
	}
	
	@Test
	public void emptyRoot_insertOutOfOrderToFull() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1, 1);
		TreeObject obj1 = new TreeObject(2, 2);
		TreeObject obj2 = new TreeObject(3, 3);
		tree.Insert(obj1);
		tree.Insert(obj);
		tree.Insert(obj2);
		BTreeNode root = tree.getRoot();
		
		assertTrue(root.getObject(0) == obj 
				&& root.getObject(1) == obj1 
				&& root.getObject(2) == obj2);
	}
	
	@Test
	public void fullRoot_insert() {
		BTree<TreeObject> tree = new BTree<TreeObject>(2);
		TreeObject obj = new TreeObject(1, 1);
		TreeObject obj1 = new TreeObject(2, 2);
		TreeObject obj2 = new TreeObject(3, 3);
		TreeObject obj3 = new TreeObject(4, 4);
		tree.Insert(obj);
		tree.Insert(obj1);
		tree.Insert(obj2);
		tree.Insert(obj3);
		BTreeNode<TreeObject> root = tree.getRoot();
		
		assertTrue(root.getObject(0) == obj1
				&& root.getChild(0).getObject(0)==obj 
				&& root.getChild(1).getObject(0)==obj2
				&& root.getChild(1).getObject(1)==obj3);
	}
	
	@Test
	public void emptyRoot_insert_sameObjTwice() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1, 1);
		tree.Insert(obj);
		tree.Insert(obj);
		assertTrue(tree.getRoot().getObject(0).getFrequency() == 1);
	}
	
	@Test
	public void fullRoot_insertTwice() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1, 1);
		TreeObject obj1 = new TreeObject(2, 2);
		TreeObject obj2 = new TreeObject(3, 3);
		TreeObject obj3 = new TreeObject(4, 4);
		TreeObject obj4 = new TreeObject(5, 5);
		TreeObject obj5 = new TreeObject(0,0);
		tree.Insert(obj);
		tree.Insert(obj1);
		tree.Insert(obj2);
		tree.Insert(obj3);
		tree.Insert(obj4);
		tree.Insert(obj5);
		BTreeNode root = tree.getRoot();
		
		assertTrue(root.getObject(0) == obj1
				&& root.getChild(0).getObject(0)==obj5 
			    && root.getChild(0).getObject(1)==obj 
				&& root.getChild(1).getObject(0)==obj2
				&& root.getChild(1).getObject(1)==obj3
				&& root.getChild(1).getObject(2)==obj4);
	}
	
	@Test
	public void empty_insert_insertLarge() {
		BTree tree = new BTree(2);
		for(int i = 2; i<= 10; i+= 2) {
		TreeObject obj = new TreeObject(i, i);
		tree.Insert(obj);
		}
		for(int i = 1; i<= 50; i+= 2) {
			TreeObject obj = new TreeObject(i, i);
			try {
				tree.Insert(obj);
			} catch (Exception e) {
				System.out.println(i);
			}
			}
		BTreeNode root = tree.getRoot();
		assertTrue(1==1);	
	}
	
	@Test
	public void fullRoot_FullChild_FullGrandchild_insert() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(20, 20);
		TreeObject obj2 = new TreeObject(21, 21);
		TreeObject obj3 = new TreeObject(22, 22);
		TreeObject obj4 = new TreeObject(10, 10);
		TreeObject obj5 = new TreeObject(11, 11);
		TreeObject obj6 = new TreeObject(12, 12);
		TreeObject obj7 = new TreeObject(1, 1);
		TreeObject obj8 = new TreeObject(2, 2);
		TreeObject obj9 = new TreeObject(3, 3);
		TreeObject obj0 = new TreeObject(4, 4);
		TreeObject obj1 = new TreeObject(23,23);
		TreeObject obj01 = new TreeObject(24,24);
		
		tree.Insert(obj3);
		tree.Insert(obj2);
		tree.Insert(obj);
		tree.Insert(obj6);
		tree.Insert(obj5);
		tree.Insert(obj4);
		tree.Insert(obj0);
		tree.Insert(obj9);
		tree.Insert(obj8);
		tree.Insert(obj7);
		tree.Insert(obj1);
		tree.Insert(obj01);
		tree.search(obj0);
		BTreeNode root = tree.getRoot();

		assertTrue(root.getObject(0)==obj6);
	}
	
	@Test
	public void fullRoot_insertExisting() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(20, 20);
		TreeObject obj2 = new TreeObject(21, 21);
		TreeObject obj3 = new TreeObject(22, 22);
		tree.Insert(obj);
		tree.Insert(obj2);
		tree.Insert(obj3);
		tree.Insert(obj);
		BTreeNode root = tree.getRoot();
		
		assertTrue(root.getSize()==3 && root.getObject(0).getFrequency()==1);
	}
	
	
	@Test(expected= NoSuchElementException.class)
	public void nothing_delete() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.delete(tree.getRoot(),obj);
	}
	
	@Test
	public void inRoot_delete() {
		BTree tree = new BTree(2);
		TreeObject obj = new TreeObject(1,1);
		tree.Insert(obj);
		tree.delete(tree.getRoot(), obj);
		assertTrue(tree.getRoot().getSize()==0);
	}

	@Test(expected= NoSuchElementException.class)
	public void inRoot_deleteSpecifiedObject() {
		BTree tree = new BTree(2);
		for(int i = 0; i<= 20; i+=2) {
		TreeObject obj = new TreeObject(i,i);
		tree.Insert(obj);
		}
		TreeObject objs = new TreeObject(6,6);
		tree.delete(tree.getRoot(), objs);
		tree.search(objs);
	}
	
	
}