package metric;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.ranking.NaturalRanking;

/**
 * Created by Igor on 05/04/2016.
 */
public class SpearmanCorrelation {

    public double correlation(final double[] xArray, final double[] yArray) {

        double[] x = xArray;
        double[] y = yArray;

        NaturalRanking rankingAlgorithm = new NaturalRanking();

        return new PearsonsCorrelation().correlation(rankingAlgorithm.rank(x), rankingAlgorithm.rank(y));

    }

}
