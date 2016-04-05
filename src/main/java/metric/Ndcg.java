package metric;

import jahp.adt.Hierarchy;
import model.NormalizedDiscountedCumulativeGain;

import java.util.Arrays;

/**
 * Created by Igor on 02/02/2016.
 */
public class Ndcg {

    private static void calculate(double[] originalRank, Hierarchy h, int alternativesSize, double[][] individualRanks, double[] bestAhpResult) {
        NormalizedDiscountedCumulativeGain gain = new NormalizedDiscountedCumulativeGain();

        ArrayIndexComparator comparator = new ArrayIndexComparator(originalRank);
        Integer[] indexes = comparator.createIndexArray();
        Arrays.sort(indexes, comparator);

        double sum = 0.0;
        for (int i = 0; i < originalRank.length; i++) sum += originalRank[i];
        System.out.println(sum);

        sum = 0.0;
        for (int i = 0; i < bestAhpResult.length; i++) sum += bestAhpResult[i];
        System.out.println(sum);

        System.out.println(gain.evaluate(bestAhpResult, originalRank));

        for (int i = 0; i < h.getGoal().getSonsSize(); i++) {

            double[] newAhpResult = new double[alternativesSize];
            for (int j = 0; j < alternativesSize; j++) {
                newAhpResult[j] = h.Pi(j, i);
            }

            System.out.println(gain.evaluate(newAhpResult, individualRanks[i]));

        }
    }

}
