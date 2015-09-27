
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

}
