import org.junit.Test;
import static org.junit.Assert.*;

import java.util.NoSuchElementException;

/**
 * @author Nealon Hager
 *
 */
public class BTreeNodeTest {

	@Test
	public void empty_node_addObject() {
		BTreeNode node = new BTreeNode(4);
		TreeObject obj = new TreeObject(5, 5);
		TreeObject obj1 = new TreeObject(7, 7);
		TreeObject obj2 = new TreeObject(3, 3);
		TreeObject obj3 = new TreeObject(6, 6);
		node.addObject(obj);
		node.addObject(obj1);
		node.addObject(obj2);
		node.addObject(obj3);
		assertTrue(!node.isEmpty());
	}
	@Test
	public void empty_node_addchild() {
		BTreeNode node = new BTreeNode(4);
		BTreeNode child1 = new BTreeNode(4);
		BTreeNode child2 = new BTreeNode(4);
		BTreeNode child3 = new BTreeNode(4);
		BTreeNode child4 = new BTreeNode(4); 
		node.addChild(child1, 0);
		node.addChild(child2, 1);
		node.addChild(child3, 2);
		node.addChild(child4, 3);
		assertTrue(node.childSize() == 4);
	}
	
	@Test
	public void empty_node_replacechild() {
		BTreeNode node = new BTreeNode(4);
		BTreeNode child1 = new BTreeNode(4);
		BTreeNode child2 = new BTreeNode(4);
		BTreeNode child3 = new BTreeNode(4);
		BTreeNode child4 = new BTreeNode(4); 
		node.addChild(child1, 0);
		node.addChild(child2, 1);
		node.addChild(child3, 2);
		node.addChild(child4, 2);
		assertTrue(node.childSize() == 3);
	}
	@Test
	public void empty_node_removechild() {
		BTreeNode node = new BTreeNode(4);
		BTreeNode child1 = new BTreeNode(4);
		BTreeNode child2 = new BTreeNode(4);
		BTreeNode child3 = new BTreeNode(4);
		BTreeNode child4 = new BTreeNode(4); 
		node.addChild(child1, 0);
		node.addChild(child2, 1);
		node.addChild(child3, 2);
		node.addChild(child4, 2);
		node.removeChild(2);
		assertTrue(node.childSize() == 2);
	}
	@Test
	public void emptyNode_isEmpty() {
		BTreeNode node = new BTreeNode(2);
		assertTrue(node.isEmpty());
	}
	
	@Test
	public void full_node_addObject() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(2, 2);
		TreeObject obj1 = new TreeObject(3, 3);
		TreeObject obj2 = new TreeObject(5, 5);
		TreeObject obj3 = new TreeObject(1, 1);
		node.addObject(obj);
		node.addObject(obj1);
		node.addObject(obj2);
		assertTrue(node.addObject(obj3) == -1);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void empty_node_removeObject() {
		BTreeNode node = new BTreeNode(1);
		node.removeObject(0);
	}
	
	@Test
	public void oneObject_removeObject() {
		BTreeNode node = new BTreeNode(3);
		TreeObject obj = new TreeObject(5, 5);
		TreeObject obj1 = new TreeObject(3, 3);
		TreeObject obj2 = new TreeObject(6, 6);
		TreeObject obj3 = new TreeObject(1, 1);
		TreeObject obj4 = new TreeObject(4, 4);
		node.addObject(obj);
		node.addObject(obj1);
		node.addObject(obj2);
		node.addObject(obj3);
		node.addObject(obj4);
		node.removeObject(0);
		node.removeObject(1);
		assertTrue(node.getSize()==3);
	}
	
	@Test
	public void oneObject_removeObject2() {
		BTreeNode node = new BTreeNode(1);
		TreeObject obj = new TreeObject(2, 2);
		node.addObject(obj);
		assertTrue(node.removeObject(0)==obj);
	}
	
	@Test
	public void noParent_getParent() {
		BTreeNode node = new BTreeNode(1);
		assertNull(node.getParent());
	}
	
	@Test
	public void noChildren_getChild0() {
		BTreeNode node = new BTreeNode(1);
		assertNull(node.getChild(0));
	}
	
	@Test 
	public void parent_getParent() {
		BTreeNode parent = new BTreeNode(2);
		BTreeNode node = new BTreeNode(2);
		node.setParent(parent);
		assertTrue(node.getParent()==parent);
	}
	
	@Test
	public void singleChild_getChild0() {
		BTreeNode node = new BTreeNode(2);
		BTreeNode child = new BTreeNode(2);
		node.setChild(child, 0);
		assertTrue(node.getChild(0)==child);
	}
	
	@Test
	public void singleObject_getObject0() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(2, 2);
		node.addObject(obj);
		assertTrue(node.getObject(0)==obj);
	}
	
	@Test (expected = NoSuchElementException.class)
	public void noObject_getObject() {
		BTreeNode node = new BTreeNode(2);
		node.getObject(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void incorrectBounds_getObject() {
		BTreeNode node = new BTreeNode(2);
		node.getObject(-1);
	}
	
	@Test
	public void noChildren_isLeaf() {
		BTreeNode node = new BTreeNode(2);
		assertTrue(node.isLeaf());
	}
	
	@Test 
	public void hasChildren_isLeaf() {
		BTreeNode node = new BTreeNode(2);
		BTreeNode node2 = new BTreeNode(2);
		node.setChild(node2, 0);
		assertFalse(node.isLeaf());
	}
	
	@Test
	public void noChildren_getHeight() {
		BTreeNode node = new BTreeNode(2);
		int height = node.getHeight();
		assertTrue(height == 0);
	}
	
	@Test
	public void hasChildren_getHeight() {
		BTreeNode node = new BTreeNode(2);
		BTreeNode node2 = new BTreeNode(2);
		node.setChild(node2, 0);
		int height = node.getHeight();
		assertTrue(height == 1);
	}
	
	@Test
	public void hasGrandchildren_getHeight() {
		BTreeNode node = new BTreeNode(2);
		BTreeNode node2 = new BTreeNode(2);
		BTreeNode node3 = new BTreeNode(2);
		node.setChild(node2, 0);
		node2.setChild(node3, 0);
		int height = node.getHeight();
		assertTrue(height == 2);
	}
	
	@Test
	public void hasMultipleChildren_getHeight() {
		BTreeNode node = new BTreeNode(2);
		BTreeNode node2 = new BTreeNode(2);
		BTreeNode node3 = new BTreeNode(2);
		node.setChild(node2, 0);
		node.setChild(node3, 1);
		int height = node.getHeight();
		assertTrue(height == 1);
	}
	
	@Test
	public void noObjects_getSize() {
		BTreeNode node = new BTreeNode(2);
		assertTrue(node.getSize()==0);
	}
	
	@Test
	public void addObject_getSize() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		node.addObject(obj);
		assertTrue(node.getSize()==1);
	}
	
	@Test
	public void addObject_removeObj_getSize() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		node.addObject(obj);
		node.removeObject(0);
		assertTrue(node.getSize()==0);
	}
	
	@Test
	public void emptyNode_addObject() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		assertTrue(node.addObject(obj)==-1);
	}
	
	@Test
	public void fullNode_addObject_getSize() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(2,2);
		TreeObject obj2 = new TreeObject(3,3);
		TreeObject obj3 = new TreeObject(4,4);
		TreeObject obj4 = new TreeObject(5,5);
		node.addObject(obj);
		node.addObject(obj1);
		node.addObject(obj2);
		assertTrue(node.getSize()==3);
	}
	
	@Test 
	public void sloppyNode_mergeSort() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(2,2);
		TreeObject obj2 = new TreeObject(3,3);
		node.addObject(obj2);
		node.addObject(obj);
		node.addObject(obj1);
		TreeObject[] expected = {obj,obj1,obj2};
		TreeObject[] objects = {node.getObject(0),node.getObject(1),node.getObject(2)};
		assertArrayEquals(expected,objects);
	}
	
	@Test
	public void notFullNode_isFull() {
		BTreeNode node = new BTreeNode(2);
		assertFalse(node.isFull());
	}
	
	@Test
	public void fullNode_isFull() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		TreeObject obj1 = new TreeObject(2,2);
		TreeObject obj2 = new TreeObject(3,3);
		node.addObject(obj2);
		node.addObject(obj);
		node.addObject(obj1);
		assertTrue(node.isFull());
	}
	
	@Test
	public void addSameObjTwice() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		node.addObject(obj);
		node.addObject(obj);
		assertTrue(node.getObject(0).getFrequency()==1 && node.getSize()==1);
	}
	
	@Test
	public void objAt0_contains() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		node.addObject(obj);
		assertTrue(node.containsTreeObject(obj)==0);
	}
	
	@Test
	public void objAt2_contains() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		node.setObject(obj, 2);
		assertTrue(node.containsTreeObject(obj)==2);
	}
	
	@Test
	public void noObj_contains() {
		BTreeNode node = new BTreeNode(2);
		TreeObject obj = new TreeObject(1,1);
		assertTrue(node.containsTreeObject(obj)==-1);
	}
}