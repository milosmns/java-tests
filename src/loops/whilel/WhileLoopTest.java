
package loops.whilel;

import helpers.ListGenerator;
import helpers.Log;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Simple looping test with <i>while</i> loops. Iterating through a {@link String} dataset, counting the number of characters for the whole
 * dataset in each looping test.
 */
public class WhileLoopTest {

    public static void main(String[] args) {
        List<String> list = ListGenerator.createStrings();

        long startTime1 = System.nanoTime();
        int count1 = loopWithI(list);
        long elapsed1 = System.nanoTime() - startTime1;

        long startTime2 = System.nanoTime();
        int count2 = loopWithISize(list);
        long elapsed2 = System.nanoTime() - startTime2;

        long startTime3 = System.nanoTime();
        int count3 = loopWithIterator(list);
        long elapsed3 = System.nanoTime() - startTime3;

        long startTime4 = System.nanoTime();
        int count4 = loopWithListIterator(list);
        long elapsed4 = System.nanoTime() - startTime4;

        Log.logLoopStats("Loop with I, counting size", elapsed1, count1);
        Log.logLoopStats("Loop with I, not counting size", elapsed2, count2);
        Log.logLoopStats("Loop with iterator", elapsed3, count3);
        Log.logLoopStats("Loop with list iterator", elapsed4, count4);
    }

    private static int loopWithI(List<String> list) {
        int i = 0;
        int charCount = 0;
        while (i < list.size()) {
            charCount += list.get(i).length();
            i++;
        }
        return charCount;
    }

    private static int loopWithISize(List<String> list) {
        int i = 0;
        int charCount = 0;
        int size = list.size();
        while (i < size) {
            charCount += list.get(i).length();
            i++;
        }
        return charCount;
    }

    private static int loopWithIterator(List<String> list) {
        Iterator<String> iterator = list.iterator();
        int charCount = 0;
        while (iterator.hasNext()) {
            charCount += iterator.next().length();
        }
        return charCount;
    }

    private static int loopWithListIterator(List<String> list) {
        ListIterator<String> listIterator = list.listIterator();
        int charCount = 0;
        while (listIterator.hasNext()) {
            charCount += listIterator.next().length();
        }
        return charCount;
    }

}
