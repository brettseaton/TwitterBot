package org.cis1200;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for FileLineIterator */
public class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void testNextEmptyFile() {
        FileLineIterator iterator = new FileLineIterator("files/empty.csv");
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void testNullPathForFileToReader() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileLineIterator.fileToReader(null);
        });
    }

    @Test
    public void testExceptionForNullNextElement() {
        StringReader sReader = new StringReader("");
        BufferedReader br = new BufferedReader(sReader);
        FileLineIterator iterator = new FileLineIterator(br);
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void testFileReaderDNE() {
        assertThrows(IllegalArgumentException.class, () -> {
            FileLineIterator.fileToReader("stelaisacutie.csv");
        });
    }

    @Test
    public void testTheNextPostIsFinalLine() {
        FileLineIterator iterator = new FileLineIterator("files/simple_test_data.csv");
        assertTrue(iterator.hasNext());
        assertEquals("0, The end should come here.", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void testContainsFollowingEmptyString() {
        StringReader sReader = new StringReader("");
        BufferedReader br = new BufferedReader(sReader);
        FileLineIterator iterator = new FileLineIterator(br);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testTheReaderIsNullPointer() {
        BufferedReader br = null;
        assertThrows(IllegalArgumentException.class, () -> {
            FileLineIterator iterator = new FileLineIterator(br);
        });
    }

}
