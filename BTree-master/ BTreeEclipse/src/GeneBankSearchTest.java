import org.junit.Test;
import static org.junit.Assert.*;

/** This class tests the GeneSearchTest class
 * @author Nealon Hager
 *
 */
public class GeneBankSearchTest {

	@Test
	public void test_keyToGenomeString_00() {
		GeneBankSearch gb = new GeneBankSearch();
		String i = "00";
		String b = gb.keyToGenomeString(i);
		assertTrue(b.equals("a"));
	}
	
	@Test
	public void test_keyToGenomeString_11() {
		GeneBankSearch gb = new GeneBankSearch();
		String i = "11";
		String b = gb.keyToGenomeString(i);
		assertTrue(b.equals("t"));
	}
	
	@Test
	public void test_keyToGenomeString_01() {
		GeneBankSearch gb = new GeneBankSearch();
		String i = "01";
		String b = gb.keyToGenomeString(i);
		assertTrue(b.equals("c"));
	}
	
	@Test
	public void test_keyToGenomeString_10() {
		GeneBankSearch gb = new GeneBankSearch();
		String i = "10";
		String b = gb.keyToGenomeString(i);
		assertTrue(b.equals("g"));
	}
	
	@Test
	public void test_keyToGenomeString_10000111() {
		GeneBankSearch gb = new GeneBankSearch();
		String i = "10000111";
		String b = gb.keyToGenomeString(i);
		assertTrue(b.equals("gact"));
	}
	
}
