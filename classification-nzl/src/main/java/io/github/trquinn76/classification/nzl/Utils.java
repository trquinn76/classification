package io.github.trquinn76.classification.nzl;

import java.util.Comparator;
import java.util.Set;

import io.github.trquinn76.classification.nzl.model.Classification;

/**
 * Utility functions and constants for the Classification scheme.
 */
public class Utils {

    // Five Eyes Country Codes
    public static final String NZL = "NZL";
    public static final String AUS = "AUS";
    public static final String CAN = "CAN";
    public static final String GBR = "GBR";
    public static final String USA = "USA";
    /** Five Eyes Set */
    public static final Set<String> FIVE_EYES = Set.of(NZL, AUS, CAN, GBR, USA);
    
    /**
     * Provides the Set of {@link Classification}'s which are Policy and Privacy Classifications.
     * 
     * @return the Set of Policy and Privacy Classifications.
     */
    public static Set<Classification> policyAndPrivacyClassifications() {
        return Set.of(Classification.inConfidence(), Classification.sensitive());
    }

    /**
     * Provides the Set of {@link Classification}'s which are National Security Classifications.
     * 
     * @return the Set of National Security Classifications.
     */
    public static Set<Classification> nationalSecurityClassifications() {
        return Set.of(Classification.restricted(), Classification.confidential(), Classification.secret(),
                Classification.topSecret());
    }

    /**
     * A {@link Comparator} which will sort 'NZL' first, and then remaining values
     * in alphabetic order.
     */
    public static Comparator<String> NZL_FIRST = new Comparator<>() {
        @Override
        public int compare(String alpha, String beta) {
            if (alpha == NZL && beta != NZL) {
                return -1;
            } else if (alpha != NZL && beta == NZL) {
                return 1;
            }
            return alpha.compareTo(beta);
        }
    };

    /**
     * A {@link Comparator} which will sort with NZL first, followed by other the
     * Five Eyes countries in alphabetical order, and then remaining countries in
     * alphabetical order.
     */
    public static Comparator<String> FIVE_EYES_FIRST = new Comparator<>() {
        @Override
        public int compare(String alpha, String beta) {
            if (FIVE_EYES.contains(alpha) && !FIVE_EYES.contains(beta)) {
                return -1;
            } else if (!FIVE_EYES.contains(alpha) && FIVE_EYES.contains(beta)) {
                return 1;
            }
            return NZL_FIRST.compare(alpha, beta);
        }
    };

    private Utils() {
    }
}
