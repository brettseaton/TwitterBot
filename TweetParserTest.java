package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
public class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton(String word) {
        List<String> l = new LinkedList<String>();
        l.add(word);
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<String>();
        for (String s : words) {
            l.add(s);
        }
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    public void removeURLsTest() {
        assertEquals("abc . def.", TweetParser.removeURLs("abc http://www.cis.upenn.edu. def."));
        assertEquals("abc", TweetParser.removeURLs("abc"));
        assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
        assertEquals("abc .", TweetParser.removeURLs("abc http://www.cis.upenn.edu."));
        assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
        assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
        assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
        assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
    }

    @Test
    public void testCleanWord() {
        assertEquals("abc", TweetParser.cleanWord("abc"));
        assertEquals("abc", TweetParser.cleanWord("ABC"));
        assertNull(TweetParser.cleanWord("@abc"));
        assertEquals("ab'c", TweetParser.cleanWord("ab'c"));
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    /* **** ****** ***** **** EXTRACT COLUMN TESTS **** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testExtractColumnGetsCorrectColumn() {
        assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                )
        );
    }

    @Test
    public void testGetColumnCorrectImpl() {
        assertEquals(TweetParser.extractColumn("This, is a, correct extraction", 1), " is a");
    }

    @Test
    public void testGetColumnNotInBoundsSimpleSmallErrorIndexCheck() {
        assertNull(TweetParser.extractColumn("this, this, is wrong", 3));
    }

    @Test
    public void testGetColumnIsNull() {
        assertNull(TweetParser.extractColumn(null, 0));
    }

    @Test
    public void testGetColumnNotInBounds() {
        assertNull(TweetParser.extractColumn("This is an out of bounds column", 9));
    }

    @Test
    public void testExtractColumnNullArgument() {
        assertNull(TweetParser.extractColumn(null, 27));
    }

    /* **** ****** ***** ***** CSV DATA TO TWEETS ***** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        assertEquals(expected, tweets);
    }

    @Test
    public void testDataIntoTweetsEmptyCase() {
        StringReader sReader = new StringReader("");
        BufferedReader br = new BufferedReader(sReader);
        List<String> expectation = new LinkedList<String>();
        List<String> result = TweetParser.csvDataToTweets(br, 0);
        assertEquals(expectation, result);
    }

    /* **** ****** ***** ** PARSE AND CLEAN SENTENCE ** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void pAndCDoesntCountWordsThatDontCount() {
        List<String> result = TweetParser.parseAndCleanSentence("brett $$ arthur $$ seaton");
        List<String> expectation = new LinkedList<String>();
        expectation.add("brett");
        expectation.add("arthur");
        expectation.add("seaton");
        assertEquals(expectation, result);
    }

    /* **** ****** ***** **** PARSE AND CLEAN TWEET *** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testPAndCLotsOfTweets() {
        List<List<String>> actual = TweetParser.parseAndCleanTweet(
                "@Brett loves to code." +
                        "Are we right or what?"
        );
        List<List<String>> expected = new LinkedList<List<String>>();
        LinkedList<String> subEx1 = new LinkedList<String>();
        LinkedList<String> subEx2 = new LinkedList<String>();
        subEx1.add("loves");
        subEx1.add("to");
        subEx1.add("code");
        subEx2.add("are");
        subEx2.add("we");
        subEx2.add("right");
        subEx2.add("or");
        subEx2.add("what");
        expected.add(subEx1);
        expected.add(subEx2);
        assertEquals(expected, actual);
    }

    /* **** ****** ***** ** CSV DATA TO TRAINING DATA ** ***** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTrainingDataSimpleCSV() {
        StringReader stringRead = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader bufferedRead = new BufferedReader(stringRead);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(bufferedRead, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testDataFromCSVToTrainingFullDataAndURL() {
        StringReader stringRead = new StringReader(
                "Hello! Want to watch the world cup on fox? \n " +
                        "https://chat.openai.com/chat \n" +
                        "lets watch \n"
        );
        BufferedReader bufferedRead = new BufferedReader(stringRead);
        List<List<String>> result = TweetParser.csvDataToTrainingData(bufferedRead, 0);
        List<List<String>> expectation = new LinkedList<List<String>>();
        List<String> subEx1 = new LinkedList<String>();
        List<String> subEx2 = new LinkedList<String>();
        List<String> subEx3 = new LinkedList<String>();
        subEx1.add("hello");
        subEx2.add("want");
        subEx2.add("to");
        subEx2.add("watch");
        subEx2.add("the");
        subEx2.add("world");
        subEx2.add("cup");
        subEx2.add("on");
        subEx2.add("fox");
        subEx3.add("lets");
        subEx3.add("watch");
        expectation.add(subEx1);
        expectation.add(subEx2);
        expectation.add(subEx3);
        assertEquals(expectation, result);
    }

    @Test
    public void testDataFromCSVToTrainingNegativeData() {
        StringReader stringRead = new StringReader("Brett plays violin");
        BufferedReader bufferedRead = new BufferedReader(stringRead);
        List<List<String>> result = TweetParser.csvDataToTrainingData(bufferedRead, -1);
        List<List<String>> expectation = new LinkedList<List<String>>();
        assertEquals(expectation, result);
    }

    @Test
    public void testDataFromCSVToTrainingEmptyData() {
        StringReader stringRead = new StringReader("");
        BufferedReader br = new BufferedReader(stringRead);
        List<List<String>> result = TweetParser.csvDataToTrainingData(br, 0);
        List<List<String>> expectation = new LinkedList<List<String>>();
        assertEquals(expectation, result);
    }

}
