
package helpers;

/**
 * Used to generate random sequences of characters.
 */
public class StringGenerator {

    /**
     * By default, all generated Strings are this big.
     */
    private static final int DEFAULT_STRING_LENGTH = 20;

    /**
     * This is used as the default alphabet when generating Strings.
     */
    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789";

    /**
     * Use this to change the default alphabet for generating Strings.
     * 
     * @param alphabet Which alphabet to use
     */
    public static void setAlphabet(String alphabet) {
        StringGenerator.alphabet = alphabet;
    }

    /**
     * Creates a new {@link String} from the default alphabet. This calls {@link #create(int)} with {@link #DEFAULT_STRING_LENGTH} as
     * parameter.
     * 
     * @return The newly created {@link String}
     */
    public static String create() {
        return create(DEFAULT_STRING_LENGTH);
    }

    /**
     * Creates a new {@link String} from the default alphabet.
     *
     * @param length How long should the {@link String} be
     * @return The newly created {@link String}
     */
    public static String create(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = alphabet.charAt((int) Math.floor(Math.random() * alphabet.length()));
            builder.append(c);
        }
        return builder.toString();
    }

}
