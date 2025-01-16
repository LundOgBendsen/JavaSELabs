package dk.logb.javase.labs.sayings;

import java.util.ArrayList;
import java.util.List;

public class Utilities {


    public static int levenshteinDistance(String s1, String s2) {
        // handle nulls, just in case
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int len1 = s1.length();
        int len2 = s2.length();

        // Create a 2D-array (matrix) for partial distances
        int[][] dp = new int[len1 + 1][len2 + 1];

        // init first column (comparison with empty string)
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;  // cost of deletions
        }
        // init first row
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;  // cost j inserts
        }

        // fill rest of table
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                // No extra cost if chars are equal, otherwise it costs 1
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                // dp[i][j] is the minimum of (delete, insert, replace)
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,  // delete
                                dp[i][j - 1] + 1), // insert
                        dp[i - 1][j - 1] + cost      // replace
                );
            }
        }

        // Nederste højre hjørne i matrixen er Levenshtein-afstanden
        return dp[len1][len2];
    }

    public static List<String> tokenize(String input) {
        // Split på alle tegn, som ikke er bogstaver (uanset sprog)
        // Brug 'split("\\P{L}+")' for at opdele i ord
        String[] rawTokens = input.split("\\P{L}+");

        List<String> tokenList = new ArrayList<>();
        for (String token : rawTokens) {
            // Filtrér tomme tokens fra (kan opstå ved split i starten/slutningen)
            if (!token.isEmpty()) {
                tokenList.add(token);
            }
        }

        return tokenList;
    }

}
