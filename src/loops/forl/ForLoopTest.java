package loops.forl;

import java.util.ArrayList;
import java.util.List;

public class ForLoopTest {

    public static void main(String[] args) {
        ArrayList<String> ls = new ArrayList<>();

        for (int j = 0; j < 1000000; j++) {
            ls.add("one");
            ls.add("two");
            ls.add("three");
            ls.add("four");
            ls.add("five");
        }

        long startTime1 = System.nanoTime();
        int count1 = loop1(ls);
        long elapsed1 = System.nanoTime() - startTime1;

        long startTime2 = System.nanoTime();
        int count2 = loop2(ls);
        long elapsed2 = System.nanoTime() - startTime2;

        long startTime3 = System.nanoTime();
        int count3 = loop3(ls);
        long elapsed3 = System.nanoTime() - startTime3;

        System.out.print("Elapsed1: " + elapsed1 + " ns, item count: " + count1);
        System.out.println(". Time per item is " + ((float) elapsed1 / (float) count1) + " ns");
        System.out.print("Elapsed2: " + elapsed2 + " ns, item count: " + count2);
        System.out.println(". Time per item is " + ((float) elapsed2 / (float) count2) + " ns");
        System.out.print("Elapsed3: " + elapsed3 + " ns, item count: " + count3);
        System.out.println(". Time per item is " + ((float) elapsed3 / (float) count3) + " ns");
    }

    private static int loop1(List<String> list) {
        int charCount = 0;
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < list.size(); i++) {
            charCount += list.get(i).length();
        }
        return charCount;
    }

    private static int loop2(List<String> list) {
        int charCount = 0;
        int listCount = list.size();
        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < listCount; i++) {
            charCount += list.get(i).length();
        }
        return charCount;
    }

    private static int loop3(List<String> list) {
        int charCount = 0;
        for (String s : list) {
            charCount += s.length();
        }
        return charCount;
    }

}
