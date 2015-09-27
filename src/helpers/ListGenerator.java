
package helpers;

import java.util.ArrayList;
import java.util.List;

public class ListGenerator {

    /**
     * By default, all lists are this big.
     */
    public static final int DEFAULT_LIST_SIZE = 1000000;

    /**
     * Creates a full String list. This calls {@link #create(int)} with {@link #DEFAULT_LIST_SIZE} as Integer paramter.
     *
     * @return The newly created list
     */
    public static List<String> create() {
        return create(DEFAULT_LIST_SIZE);
    }

    /**
     * Creates a full String list.
     *
     * @param items How long should the list be
     * @return The newly created list
     */
    public static List<String> create(int items) {
        List<String> list = new ArrayList<>();

        for (int j = 0; j < 1000000; j++) {
            list.add("one");
            list.add("two");
            list.add("three");
            list.add("four");
            list.add("five");
        }

        return list;
    }

}
