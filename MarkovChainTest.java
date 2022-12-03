package org.cis1200;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Tests for MarkovChain */
public class MarkovChainTest {

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    /* ***** ****** ***** ***** TRAIN TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testTrain() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testWalk() {
        /*
         * Using the sentences "CIS 120 rocks" and "CIS 120 beats CIS 160",
         * we're going to put some bigrams into the Markov Chain.
         *
         * While in the real world, we want the sentence we output to be random,
         * we don't want this in testing. For testing, we want to modify our
         * ProbabilityDistribution such that it will output a predictable chain
         * of words.
         *
         * Luckily, we've provided a `fixDistribution` method that will do this
         * for you! By calling `fixDistribution` with a list of words that you
         * expect to be output, the ProbabilityDistributions will be modified to
         * output your words in that order.
         *
         * See our below test for an example of how to use this.
         */

        String[] expectedWords = { "CIS", "120", "beats", "CIS", "120", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 120 rocks";
        String sentence2 = "CIS 120 beats CIS 160";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        mc.reset("CIS"); // we start with "CIS" since that's the word our desired walk starts with
        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));

        for (int i = 0; i < expectedWords.length; i++) {
            assertTrue(mc.hasNext());
            assertEquals(expectedWords[i], mc.next());
        }

    }

    @Test
    public void testOfStartingWordsAddition() {
        MarkovChain markovChainTest = new MarkovChain();

        List grouping1 = new LinkedList<String>();
        List grouping2 = new LinkedList<String>();
        List grouping3 = new LinkedList<String>();

        grouping1.add("hey");
        grouping2.add("sandwich");
        grouping3.add("whatsup");
        grouping1.add("dockers");
        grouping2.add("soccer");
        grouping3.add("blockers");

        markovChainTest.train(grouping1.iterator());
        markovChainTest.train(grouping2.iterator());
        markovChainTest.train(grouping3.iterator());

        assertTrue(markovChainTest.startWords.getRecords().containsKey("hey"));
        assertTrue(markovChainTest.startWords.getRecords().containsKey("sandwich"));
        assertTrue(markovChainTest.startWords.getRecords().containsKey("whatsup"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("dockers"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("blockers"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("soccer"));

        assertEquals(1, markovChainTest.startWords.count("hey"));
        assertEquals(1, markovChainTest.startWords.count("sandwich"));
        assertEquals(1, markovChainTest.startWords.count("whatsup"));
    }

    @Test
    public void testOfStartingWordsAdditionButWithMultipleInGrouping() {
        MarkovChain markovChainTest = new MarkovChain();

        List grouping1 = new LinkedList<String>();
        List grouping2 = new LinkedList<String>();
        List grouping3 = new LinkedList<String>();

        grouping1.add("hey");
        grouping2.add("sandwich");
        grouping3.add("sandwich");
        grouping1.add("dockers");
        grouping2.add("soccer");
        grouping3.add("blockers");

        markovChainTest.train(grouping1.iterator());
        markovChainTest.train(grouping2.iterator());
        markovChainTest.train(grouping3.iterator());

        assertTrue(markovChainTest.startWords.getRecords().containsKey("hey"));
        assertTrue(markovChainTest.startWords.getRecords().containsKey("sandwich"));
        assertTrue(markovChainTest.startWords.getRecords().containsKey("sandwich"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("dockers"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("blockers"));
        assertFalse(markovChainTest.startWords.getRecords().containsKey("soccer"));

        assertEquals(1, markovChainTest.startWords.count("hey"));
        assertEquals(2, markovChainTest.startWords.count("sandwich"));
    }

    @Test
    public void testTheTrainingIsNull() {
        MarkovChain markovChainTest = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> {
            markovChainTest.train(null);
        });
    }

    @Test
    public void testOfAddingBigramEnd() {
        MarkovChain markovChainTest = new MarkovChain();
        markovChainTest.addBigram("final", MarkovChain.END_TOKEN);
        assertTrue(markovChainTest.chain.containsKey("final"));
        ProbabilityDistribution<String> pd = markovChainTest.chain.get("final");
        assertTrue(pd.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testCheckOfReset() {
        MarkovChain markovChainTest = new MarkovChain();
        markovChainTest.addBigram("disappears", MarkovChain.END_TOKEN);
        markovChainTest.reset("appears");
        assertTrue(markovChainTest.hasNext());
        assertEquals("appears", markovChainTest.next());
        assertFalse(markovChainTest.hasNext());
    }

    @Test
    public void testOfPossibleEmptyChainThrowsNoElementException() {
        MarkovChain markovChainTest = new MarkovChain();
        markovChainTest.addBigram("disappears", MarkovChain.END_TOKEN);
        markovChainTest.reset(MarkovChain.END_TOKEN);
        assertFalse(markovChainTest.hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            markovChainTest.next();
        });

    }

    @Test
    public void testAddBigramChecksIfNullAndAddsBigram() {
        MarkovChain markovChainTest = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> {
            markovChainTest.addBigram(null, "appears");
        });
    }

    @Test
    public void testNullConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MarkovChain(null);
        });
    }

}
