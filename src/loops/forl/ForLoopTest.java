
package loops.forl;

import helpers.ListGenerator;
import helpers.Log;

import java.util.List;

/**
 * Simple looping test with <i>for</i> loops. Iterating through a {@link String} dataset, counting the number of characters for the whole
 * dataset in each looping test.
 */
public class ForLoopTest {

    public static void main(String[] args) {
        List<String> list = ListGenerator.createStrings();

        long startTime1 = System.nanoTime();
        int count1 = loopWithI(list);
        long elapsed1 = System.nanoTime() - startTime1;

        long startTime2 = System.nanoTime();
        int count2 = loopWithISize(list);
        long elapsed2 = System.nanoTime() - startTime2;

        long startTime3 = System.nanoTime();
        int count3 = loopForEach(list);
        long elapsed3 = System.nanoTime() - startTime3;

        Log.logLoopStats("Loop with I, counting size", elapsed1, count1);
        Log.logLoopStats("Loop with I, not counting size", elapsed2, count2);
        Log.logLoopStats("For-each loop", elapsed3, count3);
    }

    private static int loopWithI(List<String> list) {
        int charCount = 0;
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < list.size(); i++) {
            charCount += list.get(i).length();
        }
        return charCount;
    }

    private static int loopWithISize(List<String> list) {
        int charCount = 0;
        int listCount = list.size();
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < listCount; i++) {
            charCount += list.get(i).length();
        }
        return charCount;
    }

    private static int loopForEach(List<String> list) {
        int charCount = 0;
        for (String s : list) {
            charCount += s.length();
        }
        return charCount;
    }

}
