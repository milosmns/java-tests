
package helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to generate all kinds of lists.
 */
public class ListGenerator {

    /**
     * By default, all lists are this big.
     */
    public static final int DEFAULT_LIST_SIZE = 1000000;

    /**
     * Creates a full String list. This calls {@link #createStrings(int)} with {@link #DEFAULT_LIST_SIZE} as parameter.
     *
     * @return The newly created list
     */
    public static List<String> createStrings() {
        return createStrings(DEFAULT_LIST_SIZE);
    }

    /**
     * Creates a full String list.
     *
     * @param items How long should the list be (times 5)
     * @return The newly created list
     */
    public static List<String> createStrings(int items) {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < items; i++) {
            list.add("one");
            list.add("two");
            list.add("three");
            list.add("four");
            list.add("five");
        }

        return list;
    }

    /**
     * Creates a full {@link Cloneable} list. This calls {@link #createCloneables(int, Class)} with {@link #DEFAULT_LIST_SIZE} as parameter.
     * 
     * @param clazz The exact class of the {@link Cloneable} you want to populate the list with
     * @param <T> A class which implements the {@link Cloneable} interface
     * @return The newly created list of {@link Cloneable}s
     * @throws IllegalAccessException When creating the {@link Cloneable} via its default (empty) constructor
     * @throws InstantiationException When creating the {@link Cloneable} via its default (empty) constructor
     */
    public static <T extends Cloneable> List<T> createCloneables(Class<? extends T> clazz)
            throws IllegalAccessException, InstantiationException {
        return createCloneables(DEFAULT_LIST_SIZE, clazz);
    }

    /**
     * Creates a full {@link Cloneable} list. <i>Note</i>: {@link Cloneable}s are created using the default (empty) constructor.
     * 
     * @param items How many items to create
     * @param clazz The exact class of the {@link Cloneable} you want to populate the list with
     * @param <T> A class which implements the {@link Cloneable} interface
     * @return The newly created list of {@link Cloneable}s
     * @throws InstantiationException When creating the {@link Cloneable} via its default (empty) constructor
     * @throws IllegalAccessException When creating the {@link Cloneable} via its default (empty) constructor
     */
    public static <T extends Cloneable> List<T> createCloneables(int items, Class<? extends T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            list.add(clazz.newInstance());
        }
        return list;
    }

    /**
     * Tries to clone the list of {@link Cloneable} objects. Since {@link Cloneable#clone()} IS public (interface makes it public), one
     * would think that it is accessible generically, but {@link Object#clone()} doesn't allow invoking the method as it is declared as
     * protected there. So the only way to do it is via reflection, which this method does.
     * 
     * @param original Which list to clone
     * @param <T> A class which implements the {@link Cloneable} interface
     * @return A fully cloned list, or an empty list if something goes wrong
     */
    public static <T extends Cloneable> List<T> deepCloneList(List<T> original) {
        if (original == null || original.size() < 1) {
            return new ArrayList<>();
        }

        try {
            int originalSize = original.size();
            Method cloneMethod = original.get(0).getClass().getDeclaredMethod("clone");
            cloneMethod.setAccessible(true);
            List<T> clonedList = new ArrayList<>();

            // noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < originalSize; i++) {
                // noinspection unchecked
                clonedList.add((T) cloneMethod.invoke(original.get(i)));
            }
            return clonedList;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.err.println("Couldn't clone list due to " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
