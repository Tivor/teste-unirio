package model;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import org.apache.commons.math3.stat.ranking.NaturalRanking;
import org.apache.commons.math3.stat.ranking.TiesStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Normalized Discounted Cumulative Gain </p>
 * <p/>
 * This measure was introduced in Jarvelin, Kekalainen, "IR Evaluation Methods
 * for Retrieving Highly Relevant Documents" SIGIR 2001.  I copied the formula
 * from Vassilvitskii, "Using Web-Graph Distance for Relevance Feedback in Web
 * Search", SIGIR 2006.
 * <p/>
 * Score = N \sum_i (2^{r(i)} - 1) / \log(1 + i)
 * <p/>
 * Where N is such that the score cannot be greater than 1.  We compute this
 * by computing the DCG (unnormalized) of a perfect ranking.
 *
 * @author trevor, sjh
 */
public class NormalizedDiscountedCumulativeGain {

    public double evaluate(double[] documentJudgments, double[] idealJudgments) {

        NaturalRanking rankingAlgorithm = new NaturalRanking(TiesStrategy.SEQUENTIAL);

        double[] rankX = rankingAlgorithm.rank(documentJudgments);
        double[] rankY = rankingAlgorithm.rank(idealJudgments);

        // compute dcg:
        double dcg = computeDCG(rankX);

        // the normalizer represents the highest possible DCG score
        // that could possibly be attained.  we compute this by taking the relevance
        // judgments, ordering them by relevance value (highly relevant documents first)
        // then calling that the ranked list, and computing its DCG value.
        double normalizer = computeDCG(rankY);

        if (normalizer != 0) {
            return dcg / normalizer;
        }

        // if there are no relevant documents,
        // the average is artificially defined as zero, to mimic trec_eval
        // Really, the output is NaN, or the query should be ignored.
        return 0.0;
    }

    /**
     * Computes dcg @ documentsRetrieved
     */
    private double computeDCG(double[] gains) {
        double dcg = 0.0;
        for (int i = 0; i < gains.length; i++) {
            dcg += (Math.pow(2, gains[i]) - 1.0) / Math.log(i + 2);
        }
        return dcg;
    }
}