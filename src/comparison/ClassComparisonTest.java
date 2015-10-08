
package comparison;

import comparison.InheritanceObjectsComparisonTest.LevelOneClass;
import comparison.InheritanceObjectsComparisonTest.LevelTwoClass;

import helpers.ListGenerator;
import helpers.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A comparison test for {@link Class} objects. Iterating through 2 custom class datasets, counting how many equal values are there for each
 * comparison type. Comparison methods include class to class (by instance), class to class (using {@link Object#equals(Object)}, class to
 * class (by name comparison), using the {@code instanceof} operator, using {@link Class#isAssignableFrom(Class)}, etc.
 */
public class ClassComparisonTest {

    private static final int METHOD_IDENTICAL = 1;
    private static final int METHOD_EQUALS = 2;
    private static final int METHOD_EQUALS_NAME = 3;
    private static final int METHOD_INSTANCE_OF = 4;
    private static final int METHOD_ASSIGNABLE_FROM = 5;

    public static void main(String[] args) {
        // do tests with both classes, several times
        int testItems = 1000000;
        int testRounds = 3;
        for (int i = 1; i <= testRounds; i++) {
            long testTimeStart = System.currentTimeMillis();
            System.out.println("STARTING TEST [" + i + "]\n");
            workWith(testItems);
            System.out.println("\nTEST FINISHED IN " + (System.currentTimeMillis() - testTimeStart) + "ms\n");
        }
    }

    /**
     * Does the class comparison test, and logs the message for each test performed.
     *
     * @param listItems How many list items will the each list have
     */
    private static void workWith(int listItems) {
        try {
            // prepare lists for comparison: make 1/2 of the list with LevelOneClass objects, 1/2 with LevelTwoClass objects, and shuffle
            List<LevelOneClass> aList = ListGenerator.createCloneables(listItems / 2, LevelOneClass.class);
            List<LevelTwoClass> bList = ListGenerator.createCloneables(listItems / 2, LevelTwoClass.class);
            List<LevelOneClass> mergedList = new ArrayList<>();
            for (int i = 0; i < listItems; i++) {
                if (i < listItems / 2) {
                    mergedList.add(aList.get(i));
                } else {
                    mergedList.add(bList.get(i - listItems / 2));
                }
            }
            Collections.shuffle(mergedList);

            // noinspection UnnecessaryLocalVariable -> wrong in this case, really it's needed
            List<? extends LevelOneClass> list1 = mergedList;
            List<? extends LevelOneClass> list2 = new ArrayList<>(list1); // do a shallow copy, we compare classes
            // shuffle only one part (40%) of the second one to make it harder to equal 100%
            Collections.shuffle(list2.subList(0, (int) Math.floor(0.4f * list2.size())));

            int totalElements = list1.size();

            long startTime1 = System.nanoTime();
            int equalities1 = compareClassesInList(list1, list2, METHOD_IDENTICAL);
            long timeTaken1 = System.nanoTime() - startTime1;
            Log.logComparisonStats("Identity check with '=='", timeTaken1, equalities1, totalElements);

            long startTime2 = System.nanoTime();
            int equalities2 = compareClassesInList(list1, list2, METHOD_EQUALS);
            long timeTaken2 = System.nanoTime() - startTime2;
            Log.logComparisonStats("Class#equals() check", timeTaken2, equalities2, totalElements);

            long startTime3 = System.nanoTime();
            int equalities3 = compareClassesInList(list1, list2, METHOD_EQUALS_NAME);
            long timeTaken3 = System.nanoTime() - startTime3;
            Log.logComparisonStats("Class#getName()#equals() check", timeTaken3, equalities3, totalElements);

            long startTime4 = System.nanoTime();
            int equalities4 = compareClassesInList(list1, list2, METHOD_INSTANCE_OF);
            long timeTaken4 = System.nanoTime() - startTime4; // divide by 2?
            Log.logComparisonStats("Instance of check (both ways)", timeTaken4, equalities4, totalElements);

            long startTime5 = System.nanoTime();
            int equalities5 = compareClassesInList(list1, list2, METHOD_ASSIGNABLE_FROM);
            long timeTaken5 = System.nanoTime() - startTime5; // divide by 2?
            Log.logComparisonStats("Assignable from check (both ways)", timeTaken5, equalities5, totalElements);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("Test failed due to " + e.getMessage());
        }
    }

    /**
     * Compares classes of two lists' objects using different methods available.<br>
     * <b>Contract</b>: <u>Lists must contain an equal number of items.</u>
     *
     * @param listA First list for comparison
     * @param listB Second list for comparison
     * @param method Which method to use. Must be one of {@link #METHOD_IDENTICAL}, {@link #METHOD_EQUALS}, {@link #METHOD_EQUALS_NAME},
     *            {@link #METHOD_INSTANCE_OF}, {@link #METHOD_ASSIGNABLE_FROM}
     * @return How many items were equal in these two lists
     */
    private static int compareClassesInList(List<? extends LevelOneClass> listA, List<? extends LevelOneClass> listB, int method) {
        int equalTimes = 0;
        int listSize = listA.size();
        for (int i = 0; i < listSize; i++) {
            switch (method) {
                case METHOD_IDENTICAL:
                    equalTimes += identical(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_EQUALS:
                    equalTimes += equals(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_EQUALS_NAME:
                    equalTimes += equalsName(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_INSTANCE_OF:
                    // noinspection ConstantConditions
                    boolean bothA = listA.get(i) instanceof LevelOneClass && listB.get(i) instanceof LevelOneClass;
                    // noinspection ConstantConditions
                    boolean bothB = listA.get(i) instanceof LevelTwoClass && listB.get(i) instanceof LevelTwoClass;
                    equalTimes += (bothA || bothB) ? 1 : 0;
                    break;
                case METHOD_ASSIGNABLE_FROM:
                    boolean bothAA = LevelOneClass.class.isAssignableFrom(listA.get(i).getClass())
                            && LevelOneClass.class.isAssignableFrom(listB.get(i).getClass());
                    boolean bothAB = LevelTwoClass.class.isAssignableFrom(listA.get(i).getClass())
                            && LevelTwoClass.class.isAssignableFrom(listB.get(i).getClass());
                    equalTimes += (bothAA || bothAB) ? 1 : 0;
                    break;
            }
        }
        return equalTimes;
    }

    private static <T extends LevelOneClass> boolean identical(T first, T second) {
        return first.getClass() == second.getClass();
    }

    private static <T extends LevelOneClass> boolean equals(T first, T second) {
        return first.getClass().equals(second.getClass());
    }

    private static <T extends LevelOneClass> boolean equalsName(T first, T second) {
        return first.getClass().getName().equals(second.getClass().getName());
    }

}
