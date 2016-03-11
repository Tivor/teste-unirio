package core;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfigurator;
import metric.PrecisionAtK;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.jgap.Genotype;
import org.jgap.IChromosome;

import java.util.Arrays;

/**
 * Created by Igor on 02/02/2016.
 */
public class Printer {

    private double[][] individualRanks;

    public Printer(double[][] individualRanks) {
        this.individualRanks = individualRanks;
    }

    public double[][] individualRanks() {
        return individualRanks;
    }

    public double[] printFittestResult(Hierarchy h, Genotype population) {
        double[] newAhpResult;
        IChromosome a_subject = population.getFittestChromosome();

        System.out.println(a_subject);

        int geneIndex = 0;

        for (Criterium criterium : h.getGoal().getSons()) {

            int featureSize = criterium.getSonsSize();
            double[] weights = new double[featureSize];

            for (int i = 0; i < weights.length; i++) {
                weights[i] = ((Double) a_subject.getGene(geneIndex + i).getAllele()).doubleValue();
            }

            geneIndex += featureSize;
            criterium.updatePCM(weights);
        }

        System.out.println(h.print());

        newAhpResult = new double[h.getAlternativesSize()];
        for (int j = 0; j < h.getAlternativesSize(); j++) {
            newAhpResult[j] = h.Pi(j);
        }

        System.out.println(Arrays.toString(newAhpResult));

        return newAhpResult;
    }

    public void printCompleteResult(AHPConfigurator ahpConfigurator, double[] bestAhpResultComplete) {
//        System.out.println(">>>BEST GA RESULT: " + ArrayUtils.toString(bestAhpResultComplete));
        System.out.println("EUCLIDEAN: " + new EuclideanDistance().compute(bestAhpResultComplete, ahpConfigurator.getOriginalRank()));
        System.out.println("SPEARMANS: " + new SpearmansCorrelation().correlation(bestAhpResultComplete, ahpConfigurator.getOriginalRank()));
        System.out.println("P@k: " + new PrecisionAtK().calculate(bestAhpResultComplete, ahpConfigurator.getOriginalRank()));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    public void printIndividualResult(int k, double[] newAhpResult) {
//        System.out.println(">>>RESULT [" + k + "]: " + ArrayUtils.toString(newAhpResult));
        System.out.println(">>>EUCLIDEAN [" + k + "]: " + new EuclideanDistance().compute(newAhpResult, individualRanks[k]));
        System.out.println(">>>SPEARMANS [" + k + "]: " + new SpearmansCorrelation().correlation(newAhpResult, individualRanks[k]));
        System.out.println("P@k: " + new PrecisionAtK().calculate(newAhpResult, individualRanks[k]));
        System.out.println("===========================");
    }

}
