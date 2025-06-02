package io.github.trquinn76.classification.usa;

import java.util.Comparator;

public class Utils {
    
    public static final String USA = "USA";
    
    // Published NATO marks.
    public static final String BOHEMIA = "BOHEMIA";
    public static final String ATOMAL = "ATOMAL";
    
    // Published SCI Control Systems
    public static final String HCS = "HCS"; // HUMINT Control System
    public static final String RESERVE = "RESERVE";
    public static final String SPECIAL_INTELLIGENCE = "SI";
    public static final String TALENT_KEYHOLE = "TK";
    
    // Published SCI Compartments in HCS.
    public static final String OPERATIONS = "OPERATIONS";
    public static final String PRODUCT = "PRODUCT";
    
    // Published SCI Compartments in SI.
    public static final String ECRU = "ECRU";
    public static final String GAMMA = "GAMMA";
    public static final String NONBOOK = "NONBOOK";
    
    // Published SCI Compartments in TK.
    public static final String GEOCAP = "GEOCAP";
    public static final String BLUEFISH = "BLUEFISH";
    public static final String IDITAROD = "IDITAROD";
    public static final String KANDIK = "KANDIK";
    
    // SAP additional Markers
    public static final String HVSACO = "HVSACO";
    
    public static Comparator<String> USA_FIRST = new Comparator<>() {

        @Override
        public int compare(String alpha, String beta) {
            if (alpha == USA && beta != USA) {
                return -1;
            } else if (alpha != USA && beta == USA) {
                return 1;
            }
            int retval = alpha.length() - beta.length();
            if (retval == 0) {
                retval = alpha.compareTo(beta);
            }
            return retval;
        }
        
    };
    
    public static Comparator<String> ALPHABETIC = new Comparator<>() {

        @Override
        public int compare(String alpha, String beta) {
            int retval = alpha.length() - beta.length();
            if (retval == 0) {
                retval = alpha.compareTo(beta);
            }
            return retval;
        }
        
    };
    
    public static Comparator<String> ALPHANUMERIC = new Comparator<>() {

        // Hey look, AI code! :) It was actually helpful this time.
        @Override
        public int compare(String s1, String s2) {
            int i = 0, j = 0;
            while (i < s1.length() && j < s2.length()) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(j);

                if (Character.isDigit(c1) && Character.isDigit(c2)) {
                    int num1 = 0, num2 = 0;

                    while (i < s1.length() && Character.isDigit(s1.charAt(i))) {
                        num1 = num1 * 10 + (s1.charAt(i) - '0');
                        i++;
                    }
                    while (j < s2.length() && Character.isDigit(s2.charAt(j))) {
                        num2 = num2 * 10 + (s2.charAt(j) - '0');
                        j++;
                    }

                    if (num1 != num2) {
                        return Integer.compare(num1, num2);
                    }
                } else {
                    if (c1 != c2) {
                        return Character.compare(c1, c2);
                    }
                    i++;
                    j++;
                }
            }
            return Integer.compare(s1.length(), s2.length());
        }

        
    };
    
    private Utils() {}
}
