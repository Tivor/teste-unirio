package core;

import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfigurator;
import metric.PrecisionAtK;
import metric.SpearmanCorrelation;
import model.NormalizedDiscountedCumulativeGain;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.jgap.Genotype;
import org.jgap.IChromosome;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

/**
 * Created by Igor on 02/02/2016.
 */
public class Printer {

    private double[][] individualRanks;
    private Path path;

    public void out(String text) {
        try {
            Files.write(path, text.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-9);
        }
    }

    public Printer(String fileName, double[][] individualRanks) {
        path = Paths.get("output/" + fileName + "_" + Thread.currentThread().getId());
        this.individualRanks = individualRanks;
    }

    public double[][] individualRanks() {
        return individualRanks;
    }

    public double[] printFittestResult(Hierarchy h, Genotype population) {
        double[] newAhpResult;
        IChromosome a_subject = population.getFittestChromosome();

        int geneIndex = 0;

        for (Criterium criterium : h.getGoal().getSons()) {

            int featureSize = criterium.getSonsSize();
            double[] weights = new double[featureSize];

            for (int i = 0; i < weights.length; i++) {
                weights[i] = getWeight(a_subject, geneIndex, i);
//                weights[i] = getFixedWeight(geneIndex, i);
            }

            geneIndex += featureSize;
            criterium.updatePCM(weights);
        }

//        System.out.println(h.print());

        newAhpResult = new double[h.getAlternativesSize()];
        for (int j = 0; j < h.getAlternativesSize(); j++) {
            newAhpResult[j] = h.PiFull(j);
        }

        System.out.println(Arrays.toString(newAhpResult));

        return newAhpResult;
    }

    private double getWeight(IChromosome a_subject, int geneIndex, int i) {
        return ((Integer) a_subject.getGene(geneIndex + i).getAllele()).doubleValue();
    }

    private double getFixedWeight(int geneIndex, int i) {

        double[] fixedWeights = {0.2, 0.6, 1.4, 2.4, 1.2};

        return fixedWeights[geneIndex + i];
    }

    public void printCompleteResult(AHPConfigurator ahpConfigurator, double[] bestAhpResultComplete) {
//        System.out.println(">>>BEST GA RESULT: " + ArrayUtils.toString(bestAhpResultComplete));
        double euclidean = new EuclideanDistance().compute(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
//        System.out.println("EUCLIDEAN: " + euclidean);
        double correlation = new SpearmanCorrelation().correlation(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
//        System.out.println("SPEARMANS: " + correlation);
        double precision = new PrecisionAtK().calculate(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
//        System.out.println("P@k: " + precision);
        double nDcg = new NormalizedDiscountedCumulativeGain().evaluate(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
//        System.out.println("nDCG: " + nDcg);
//        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        out(String.valueOf(euclidean) + "," + String.valueOf(correlation) + "," + String.valueOf(precision) + "," + String.valueOf(nDcg) + "\n");

    }

    public void printIndividualResult(int k, double[] newAhpResult) {
        System.out.println(">>>RESULT [" + k + "]: " + ArrayUtils.toString(newAhpResult));
        System.out.println(">>>EUCLIDEAN [" + k + "]: " + new EuclideanDistance().compute(newAhpResult, individualRanks[k]));
        System.out.println(">>>SPEARMANS [" + k + "]: " + new SpearmanCorrelation().correlation(newAhpResult, individualRanks[k]));
        System.out.println("P@k: " + new PrecisionAtK().calculate(newAhpResult, individualRanks[k]));
        System.out.println("nDCG: " + new NormalizedDiscountedCumulativeGain().evaluate(newAhpResult, individualRanks[k]));
        System.out.println("===========================");
    }

}
