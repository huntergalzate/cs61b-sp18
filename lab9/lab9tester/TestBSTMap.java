package lab9tester;

import static org.junit.Assert.*;

import org.junit.Test;
import lab9.BSTMap;

/**
 * Tests by Brendan Hu, Spring 2015, revised for 2018 by Josh Hug
 */
public class TestBSTMap {

    @Test
    public void sanityGenericsTest() {
        try {
            BSTMap<String, String> a = new BSTMap<String, String>();
            BSTMap<String, Integer> b = new BSTMap<String, Integer>();
            BSTMap<Integer, String> c = new BSTMap<Integer, String>();
            BSTMap<Boolean, Integer> e = new BSTMap<Boolean, Integer>();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void simplePutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("b", 2);
        b.put("a", 1);
        b.put("c" ,3);
        assertEquals(3, b.size());
        assertTrue(b.containsKey("a"));
        assertTrue(b.containsKey("b"));
        assertTrue(b.containsKey("c"));
    }

    //assumes put/size/containsKey/get work
    @Test
    public void sanityClearTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1 + i);
            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i));
            assertTrue(b.get("hi" + i).equals(1 + i));
            assertTrue(b.containsKey("hi" + i));
        }
        assertEquals(455, b.size());
        b.clear();
        assertEquals(0, b.size());
        for (int i = 0; i < 455; i++) {
            assertTrue(null == b.get("hi" + i) && !b.containsKey("hi" + i));
        }
    }

    // assumes put works
    @Test
    public void sanityContainsKeyTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertFalse(b.containsKey("waterYouDoingHere"));
        b.put("waterYouDoingHere", 0);
        assertTrue(b.containsKey("waterYouDoingHere"));
    }

    // assumes put works
    @Test
    public void sanityGetTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(null, b.get("starChild"));
        assertEquals(0, b.size());
        b.put("starChild", 5);
        assertTrue(((Integer) b.get("starChild")).equals(5));
        b.put("KISS", 5);
        assertTrue(((Integer) b.get("KISS")).equals(5));
        assertNotEquals(null, b.get("starChild"));
        assertEquals(2, b.size());
    }

    // assumes put works
    @Test
    public void sanitySizeTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        assertEquals(0, b.size());
        b.put("hi", 1);
        assertEquals(1, b.size());
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        assertEquals(456, b.size());
    }

    //assumes get/containskey work
    @Test
    public void sanityPutTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("hi", 1);
        assertTrue(b.containsKey("hi"));
        assertTrue(b.get("hi") != null);
    }


    @Test
    public void removeRootNoChildrenTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("b", 2);
        assertEquals(Integer.valueOf(2), b.remove("b"));
        assertEquals(0, b.size());

        assertNull(b.remove("b"));
        assertEquals(0, b.size());
    }

    @Test
    public void removeNonExistentChildTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("b", 2);
        assertNull(b.remove("c"));
        assertEquals(1, b.size());
    }
    //adds 3 items. removes left item. then remove right item. then removes root. (no test on nodes with 1 or 2 children)
    @Test
    public void removeNoChildrenTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("b", 2);
        b.put("a", 1);
        b.put("c", 3);
        assertEquals(3, b.size());

        assertEquals(Integer.valueOf(1), b.remove("a"));
        assertEquals(2, b.size());

        assertEquals(Integer.valueOf(3), b.remove("c"));
        assertEquals(1, b.size());

        assertEquals(Integer.valueOf(2), b.remove("b"));
        assertEquals(0, b.size());
    }

    //adds 4 items. removes left item. then remove right item. then removes root. (no test on nodes with 1 or 2 children)
    @Test
    public void removeOneChildrenTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("c", 3);
        b.put("a", 1);
        b.put("b", 2);
        b.put("d", 4);

        assertEquals(4, b.size());

        assertEquals(Integer.valueOf(2), b.remove("b"));
        assertEquals(3, b.size());
    }

    @Test
    public void removeTwoChildTest() {
        BSTMap<String, Integer> b = new BSTMap<String, Integer>();
        b.put("c", 3);
        b.put("a", 1);
        b.put("b", 2);
        b.put("d", 4);

        assertEquals(4, b.size());
        assertEquals(Integer.valueOf(3), b.remove("c"));
        assertFalse(b.containsKey("c"));
        assertEquals(3, b.size());
    }

    @Test
    public void testRemoveRoot() {
        BSTMap<String,String> q = new BSTMap<String,String>();
        q.put("c","a");
        q.put("b","a");
        q.put("a","a");
        q.put("d","a");
        q.put("e","a"); // a b c d e
        assertNotNull(q.remove("c"));
        assertFalse(q.containsKey("c"));
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        assertTrue(q.containsKey("e"));
        assertEquals("checking the size() updated from 5 to 4",4, q.size());
    }

    @Test
    public void testRemoveThreeCases() {
        BSTMap<String,String> q = new BSTMap<String,String>();
        q.put("c","a");
        q.put("b","a");
        q.put("a","a");
        q.put("d","a");
        q.put("e","a");
        assertEquals(5, q.size());// a b c d e
        assertTrue(null != q.remove("e"));      // a b c d
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("c"));
        assertTrue(q.containsKey("d"));
        assertTrue(null != q.remove("c"));      // a b d
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("d"));
        q.put("f","a");                         // a b d f
        assertTrue(null != q.remove("d"));      // a b f
        assertTrue(q.containsKey("a"));
        assertTrue(q.containsKey("b"));
        assertTrue(q.containsKey("f"));
        assertEquals(3, q.size());
    }

    /* Remove Test 3
     *  Checks that remove works correctly on root nodes
     *  when the node has only 1 or 0 children on either side. */
    @Test
    public void testRemoveRootEdge() {
        BSTMap<Character, Integer> rightChild = new BSTMap<>();
        rightChild.put('A', 1);
        rightChild.put('B', 2);
        Integer result = (Integer) rightChild.remove('A');
        assertTrue(result.equals(new Integer(1)));
        for (int i = 0; i < 10; i++) {
            rightChild.put((char) ('C'+i), 3+i);
        }
        rightChild.put('A', 100);
        assertTrue(((Integer) rightChild.remove('D')).equals(new Integer(4)));
        assertTrue(((Integer) rightChild.remove('G')).equals(new Integer(7)));
        assertTrue(((Integer) rightChild.remove('A')).equals(new Integer(100)));
        assertTrue(rightChild.size()==9);

        BSTMap<Character, Integer> leftChild = new BSTMap<>();
        leftChild.put('B', 1);
        leftChild.put('A', 2);
        assertTrue(((Integer) leftChild.remove('B')).equals(1));
        assertEquals(1, leftChild.size());
        assertEquals(null, leftChild.get('B'));

        BSTMap noChild = new BSTMap();
        noChild.put('Z', 15);
        assertTrue(((Integer) noChild.remove('Z')).equals(15));
        assertEquals(0, noChild.size());
        assertEquals(null, noChild.get('Z'));
    }

    public static void main(String[] args) {
        jh61b.junit.TestRunner.runTests(TestBSTMap.class);
    }
}
