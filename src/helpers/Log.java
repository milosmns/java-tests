
package helpers;

/**
 * Helper class for quick terminal logging only for tests performed by this project.
 */
public class Log {

    /**
     * Writes detailed info to the default output about the loop performed. With this, you should use loops that iterate over a
     * {@link String} dataset so that you can provide a character count for the whole dataset as a parameter in this method.
     *
     * @param name Which loop was performed
     * @param elapsedNano How long did it take to complete (in nanoseconds)
     * @param charCount When looping through the dataset, collect character count for it and pass it in here
     * @see System#out
     * @see System#nanoTime()
     */
    public static void logLoopStats(String name, long elapsedNano, int charCount) {
        float timePerChar = (float) elapsedNano / (float) charCount;
        String part1 = String.format("%s: %,dns, character count: %,d. ", name, elapsedNano, charCount);
        String part2 = String.format("Time per character is %.3fns.", timePerChar);
        System.out.println(part1 + part2);
    }

    /**
     * Writes detailed info to the default output about the comparison performed. With this, you should use compared lists that contain a
     * {@link Cloneable} dataset so that you can provide a good {@link Object#equals(Object)} method. Another use case would be comparing
     * classes of dataset objects to see how well standard class comparison and inheritance operators work.
     *
     * @param name Which comparison was performed
     * @param elapsedNano How long did it take to complete (in nanoseconds)
     * @param equalities How many items in the list had equal classes or data
     * @param totalElements How many items did the whole list have
     * @see System#out
     * @see System#nanoTime()
     */
    public static void logComparisonStats(String name, long elapsedNano, int equalities, int totalElements) {
        float timesPerComparison = (float) elapsedNano / (float) totalElements;
        int percentEqual = (int) Math.floor(((float) equalities / (float) totalElements) * 100f);
        String part1 = String.format("%s: %,dns, equalities: %,d. ", name, elapsedNano, equalities);
        String part2 = String.format("Time per comparison is %.3fns. %d%% of the list was equal.", timesPerComparison, percentEqual);
        System.out.println(part1 + part2);
    }

}
