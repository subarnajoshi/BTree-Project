import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Nealon Hager
 *
 */
public class GeneBankCreateBTreeTest {

	@Test
	public void testIsInt() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String i = "101";
		assertTrue(gb.isInt(i));
	}
	
	@Test
	public void testIsInt_fail() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String i = "test";
		assertFalse(gb.isInt(i));
	}
	
	@Test
	public void generateKey_a() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("a");
		assertTrue(test.equals("00"));
	}
	
	@Test
	public void generateKey_t() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("t");
		assertTrue(test.equals("11"));
	}
	
	@Test
	public void generateKey_c() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("c");
		assertTrue(test.equals("01"));
	}
	
	@Test
	public void generateKey_g() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("g");
		assertTrue(test.equals("10"));
	}
	
	@Test
	public void generateKey_at() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("at");
		assertTrue(test.equals("0011"));
	}
	
	@Test
	public void generateKey_gatcctccat() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("gatcctccat");
		assertTrue(test.equals("10001101011101010011"));
	}
	
	@Test 
	public void generateKey_gatc() {
		GeneBankCreateBTree gb = new GeneBankCreateBTree();
		String test = gb.generateKey("gatc");
		assertTrue(test.equals("10001101"));
	}
}
