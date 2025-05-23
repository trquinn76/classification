package io.github.trquinn76.classification.uk;

import java.util.Comparator;

/**
 * Utility functions and constants for the Classification scheme.
 */
public class Utils {

    public static final String UK = "UK";
    public static final String FIVE = "FIVE";
    public static final String REL_EU = "REL-EU";

    // Handling Instructions
    public static final String RECIPIENTS_ONLY = "RECIPIENTS ONLY";
    public static final String FOR_PUBLIC_RELEASE = "FOR PUBLIC RELEASE";
    public static final String USE_ONLY = "USE ONLY";
    public static final String HMG_USE_ONLY = "HMG USE ONLY";
    public static final String EMBARGOED = "EMBARGOED";

    // Descriptors
    public static final String PERSONAL_DATA = "PERSONAL DATA";
    public static final String LEGAL_PROFESSIONAL_PRIVILEGE = "LEGAL PROFESSIONAL PRIVILEGE";
    public static final String LEGAL = "LEGAL";
    public static final String MARKET_SENSITIVE = "MARKET SENSITIVE";
    public static final String COMMERCIAL = "COMMERCIAL";
    public static final String HR_MANAGEMENT = "HR/MANAGEMENT";

    /**
     * A {@link Comparator} which will sort 'UK' first, and then remaining values in
     * alphabetic order.
     */
    public static Comparator<String> UK_FIRST = new Comparator<>() {
        @Override
        public int compare(String alpha, String beta) {
            if (alpha == UK && beta != UK) {
                return -1;
            } else if (alpha != UK && beta == UK) {
                return 1;
            }
            return alpha.compareTo(beta);
        }
    };

    /**
     * A {@link Comparator} which will sort in alphabetical (actually lexicographic
     * order).
     */
    public static Comparator<String> ALPHABETICAL = new Comparator<>() {
        @Override
        public int compare(String alpha, String beta) {
            return alpha.compareTo(beta);
        }
    };

    /**
     * Determines if the given String ends with 'USE ONLY', but not 'HMG USE ONLY'.
     * 
     * @param str the String to test.
     * @return true iff the given String ends with 'USE ONLY', but not 'HMG USE
     *         ONLY'.
     */
    public static boolean endsInOrganisationUseOnly(String str) {
        return str.endsWith(Utils.USE_ONLY) && !str.endsWith(Utils.HMG_USE_ONLY);
    }

    private Utils() {
    }
}
