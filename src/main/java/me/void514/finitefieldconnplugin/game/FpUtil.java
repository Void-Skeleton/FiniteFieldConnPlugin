package me.void514.finitefieldconnplugin.game;

import java.util.HashMap;
import java.util.Map;

public class FpUtil {
    public static final int INFINITY = Integer.MAX_VALUE;
    public static final int INAPPLICABLE = Integer.MIN_VALUE;

    public static int normalize(int prime, int representative) {
        if (representative == INAPPLICABLE || representative == INFINITY) return representative;
        int result = representative % prime;
        if (representative < 0 && result != 0) result += prime;
        return result;
    }

    public static boolean isCongruent(int prime, int val1, int val2) {
        return normalize(prime, val1 - val2) == 0;
    }

    private static class InversionCache {
        public static final Map<Integer, int[]> CACHE = new HashMap<>();
    }

    private static void findAllInverses(int prime, int[] array) {
        boolean[] foundInverse = new boolean[prime];
        for (int i = 1; i < prime; i ++) {
            if (foundInverse[i]) continue;
            for (int j = 1; j < prime; j ++) {
                if (foundInverse[j]) continue;
                if ((i * j) % prime == 1) {
                    foundInverse[i] = (foundInverse[j] = true);
                    array[i] = j;
                    array[j] = i;
                }
            }
        }
    }

    public static int invert(int prime, int val) {
        if (val == 0) return INAPPLICABLE;
        if (InversionCache.CACHE.containsKey(prime)) {
            return InversionCache.CACHE.get(prime)[normalize(prime, val)];
        } else {
            int[] inverses = new int[prime];
            findAllInverses(prime, inverses);
            InversionCache.CACHE.put(prime, inverses);
            return inverses[normalize(prime, val)];
        }
    }
}
