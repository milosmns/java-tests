
package comparison;

import helpers.ListGenerator;
import helpers.Log;
import helpers.StringGenerator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A comparison test for objects. Iterating through 2 {@link String}-based datasets, counting how many equal values are there for each
 * comparison type. Comparison methods include {@link Object#equals(Object)} and {@link Object#hashCode()} overrides, default
 * implementations of these 2 methods, comparison by field name and data etc.
 */
public class InheritanceObjectsComparisonTest {

    private static final int METHOD_EQUALS = 1;
    private static final int METHOD_HASH = 2;
    private static final int METHOD_SUPER_EQUALS = 3;
    private static final int METHOD_SUPER_HASH = 4;
    private static final int METHOD_REFLECTION = 5;

    /**
     * A simple 2-{@link String} bundle that implements the {@link Cloneable} interface. This class overrides {@link Object#equals(Object)}
     * and {@link Object#hashCode()} to allow custom comparison. An object of this class is considered equal to another object if they have
     * the same bundle of {@link String}s, i.e. if both {@link #s1} and {@link #s2} equal other object's {@link #s1} and {@link #s2}.
     */
    public static class LevelOneClass implements Cloneable {

        /**
         * The first of 2 Strings in this bundle
         */
        protected String s1;
        /**
         * The second of 2 Strings in this bundle
         */
        protected String s2;

        public LevelOneClass() {
            super();
            s1 = StringGenerator.create();
            s2 = StringGenerator.create();
        }

        @Override
        @SuppressWarnings("RedundantStringConstructorCall")
        protected LevelOneClass clone() throws CloneNotSupportedException {
            try {
                return (LevelOneClass) super.clone();
            } catch (Exception e) {
                System.err.println("Can't native clone this due to " + e.getMessage());

                LevelOneClass clone = new LevelOneClass();
                clone.s1 = new String(this.s1);
                clone.s2 = new String(this.s2);
                return clone;
            }
        }

        public boolean superEquals(Object obj) {
            return super.equals(obj);
        }

        public int superHashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            try {
                return ((LevelOneClass) obj).s1.equals(this.s1) && ((LevelOneClass) obj).s2.equals(this.s2);
            } catch (Exception ignored) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return (s1.hashCode() * s2.hashCode()) % Integer.MAX_VALUE;
        }

    }

    /**
     * The second-level inheritance class. It contains everything from {@link InheritanceObjectsComparisonTest.LevelOneClass} but adds
     * another {@link String} {@link #s3} to the bundle. When comparing and calculating the hash code, this class uses {@link #s3} value in
     * combination with values of {@link #s1} and {@link #s2} from {@link InheritanceObjectsComparisonTest.LevelOneClass}.
     */
    public static class LevelTwoClass extends LevelOneClass {

        /**
         * Additional String in the extended bundle
         */
        protected String s3;

        public LevelTwoClass() {
            super();
            s3 = StringGenerator.create();
        }

        @Override
        @SuppressWarnings("RedundantStringConstructorCall")
        protected LevelTwoClass clone() throws CloneNotSupportedException {
            try {
                return (LevelTwoClass) super.clone();
            } catch (Exception e) {
                System.err.println("Can't native clone this due to " + e.getMessage());

                LevelTwoClass clone = new LevelTwoClass();
                clone.s1 = new String(this.s1);
                clone.s2 = new String(this.s2);
                clone.s3 = new String(this.s3);
                return clone;
            }
        }

        public boolean superEquals(Object obj) {
            return super.equals(obj);
        }

        public int superHashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            try {
                return super.equals(obj) && ((LevelTwoClass) obj).s3.equals(this.s3);
            } catch (Exception ignored) {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return (super.hashCode() * s3.hashCode()) % Integer.MAX_VALUE;
        }

    }

    public static void main(String[] args) {
        // do tests with both classes, several times
        int testItems = 1000000;
        for (int i = 1; i <= 5; i++) {
            long testTimeStart = System.currentTimeMillis();
            System.out.println("STARTING TEST [" + i + "] WITH " + LevelOneClass.class.getSimpleName() + "\n");
            workWith(LevelOneClass.class, testItems);
            System.out.println("TEST FINISHED IN " + (System.currentTimeMillis() - testTimeStart) + "ms");

            System.out.println();

            testTimeStart = System.currentTimeMillis();
            System.out.println("STARTING TEST [" + i + "] WITH " + LevelTwoClass.class.getSimpleName() + "\n");
            workWith(LevelTwoClass.class, testItems);
            System.out.println("TEST FINISHED IN " + (System.currentTimeMillis() - testTimeStart) + "ms");

            System.out.println();
        }
    }

    private static <T extends LevelOneClass> void workWith(Class<? extends T> clazz, int listItems) {
        try {
            // prepare lists for comparison
            List<T> list1 = ListGenerator.createCloneables(listItems, clazz);
            List<T> list2 = ListGenerator.deepCloneList(list1);
            // shuffle only one part (30%) of the second one to make it harder to equal 100%
            Collections.shuffle(list2.subList(0, (int) Math.floor(0.3f * list2.size())));
            int totalElements = list1.size();

            long startTime1 = System.nanoTime();
            int equalities1 = compareList(list1, list2, METHOD_EQUALS);
            long timeTaken1 = System.nanoTime() - startTime1;
            Log.logComparisonStats("Regular equals", timeTaken1, equalities1, totalElements);

            long startTime2 = System.nanoTime();
            int equalities2 = compareList(list1, list2, METHOD_HASH);
            long timeTaken2 = System.nanoTime() - startTime2;
            Log.logComparisonStats("Regular hash", timeTaken2, equalities2, totalElements);

            long startTime3 = System.nanoTime();
            int equalities3 = compareList(list1, list2, METHOD_SUPER_EQUALS);
            long timeTaken3 = System.nanoTime() - startTime3;
            Log.logComparisonStats("Super equals", timeTaken3, equalities3, totalElements);

            long startTime4 = System.nanoTime();
            int equalities4 = compareList(list1, list2, METHOD_SUPER_HASH);
            long timeTaken4 = System.nanoTime() - startTime4;
            Log.logComparisonStats("Super hash", timeTaken4, equalities4, totalElements);

            long startTime5 = System.nanoTime();
            int equalities5 = compareList(list1, list2, METHOD_REFLECTION);
            long timeTaken5 = System.nanoTime() - startTime5;
            Log.logComparisonStats("Reflection", timeTaken5, equalities5, totalElements);
        } catch (IllegalAccessException | InstantiationException e) {
            System.err.println("Test failed due to " + e.getMessage());
        }
    }

    private static <T extends LevelOneClass> int compareList(List<T> listA, List<T> listB, int method) {
        int equalTimes = 0;
        int listSize = listA.size();
        for (int i = 0; i < listSize; i++) {
            switch (method) {
                case METHOD_EQUALS:
                    equalTimes += equals(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_HASH:
                    equalTimes += equalsHash(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_SUPER_EQUALS:
                    equalTimes += equalsSuper(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_SUPER_HASH:
                    equalTimes += equalsHashSuper(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
                case METHOD_REFLECTION:
                    equalTimes += equalsReflection(listA.get(i), listB.get(i)) ? 1 : 0;
                    break;
            }
        }
        return equalTimes;
    }

    private static <T extends LevelOneClass> boolean equals(T a, T b) {
        return a.equals(b);
    }

    private static <T extends LevelOneClass> boolean equalsHash(T a, T b) {
        return a.hashCode() == b.hashCode();
    }

    private static <T extends LevelOneClass> boolean equalsSuper(T a, T b) {
        return a.superEquals(b);
    }

    private static <T extends LevelOneClass> boolean equalsHashSuper(T a, T b) {
        return a.superHashCode() == b.superHashCode();
    }

    private static <T extends LevelOneClass> boolean equalsReflection(T a, T b) {
        // compare each field individually
        List<Field> fields = getFields(a.getClass());
        int totalFields = fields.size();

        // noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < totalFields; i++) {
            fields.get(i).setAccessible(true);
            try {
                if (!fields.get(i).get(a).equals(fields.get(i).get(b))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                System.err.println("Can't compare due to " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    private static List<Field> getFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

}
