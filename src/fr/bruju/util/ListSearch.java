package fr.bruju.util;

import java.util.function.IntUnaryOperator;

public class ListSearch {
    public static int searchMaxValue(int max, IntUnaryOperator evaluation) {
        int best = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int i = 0 ; i != max ; i++) {
            int eval = evaluation.applyAsInt(i);

            if (best == -1 || eval > bestValue) {
                best = i;
                bestValue = eval;
            }
        }

        return best;
    }
}
