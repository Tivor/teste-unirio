package core;

import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfigurator;
import jsc.datastructures.PairedData;
import metric.PrecisionAtK;
import metric.SpearmanCorrelation;
import model.NormalizedDiscountedCumulativeGain;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.StatUtils;
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
    private Path errPath;

    public void out(String text) {
        try {
            Files.write(path, text.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-9);
        }
    }

    public void err(String text) {
        try {
            Files.write(errPath, text.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Printer(String fileName, double[][] individualRanks) {
        path = Paths.get("output/" + fileName + "_" + Thread.currentThread().getId());
        errPath = Paths.get("err/" + fileName + "_" + Thread.currentThread().getId());
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
//            System.out.println("------------------->" + Arrays.toString(weights));
        }

//        System.out.println(h.print());

        newAhpResult = new double[h.getAlternativesSize()];
        for (int j = 0; j < h.getAlternativesSize(); j++) {
            newAhpResult[j] = h.PiFull(j);
        }

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

        double sumTest = StatUtils.sum(bestAhpResultComplete);
        if (sumTest < 0.98d || sumTest > 1.1d) {
            err("BEST RESULT: sumTest < 0.98d || sumTest > 1.1d");
            err("bestAhpResultComplete SUM_TEST>" + sumTest);
            err("------------------->" + Arrays.toString(bestAhpResultComplete));
        }

        sumTest = StatUtils.sum(ahpConfigurator.getOriginalRank());
        if (sumTest < 0.99999d || sumTest > 1.1d) {
            err("ORIGINAL: sumTest < 0.98d || sumTest > 1.1d");
            err("bestAhpResultComplete SUM_TEST>" + sumTest);
            err("------------------->" + Arrays.toString(ahpConfigurator.getOriginalRank()));
        }

//        System.out.println(">>>BEST GA RESULT: " + ArrayUtils.toString(bestAhpResultComplete));

//        System.out.println("EUCLIDEAN: " + euclidean);
        double euclidean = new EuclideanDistance().compute(bestAhpResultComplete, ahpConfigurator.getOriginalRank());

//        System.out.println("SPEARMANS: " + correlation);
//        double correlation = new SpearmanCorrelation().correlation(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
        PairedData pd = new PairedData(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
        jsc.correlation.SpearmanCorrelation spearmanCorrelation = new jsc.correlation.SpearmanCorrelation(pd);
        double correlation = spearmanCorrelation.getR();
        if (correlation > 1.0d) {
            err("correlation > 1.0d");
            err("--------------------------------------------" + correlation);
            err("ORIGINAL--------->" + Arrays.toString(ahpConfigurator.getOriginalRank()));
            err("BEST RESULT------>" + Arrays.toString(bestAhpResultComplete));
        }

        double pvalue = spearmanCorrelation.getSP();

//        System.out.println("P@k: " + precision);
        double precision = new PrecisionAtK().calculate(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
        if (precision > 1.0d) {
            err("precision > 1.0d");
            err("--------------------------------------------" + precision);
            err("ORIGINAL--------->" + Arrays.toString(ahpConfigurator.getOriginalRank()));
            err("BEST RESULT------>" + Arrays.toString(bestAhpResultComplete));
        }

//        System.out.println("nDCG: " + nDcg);
        double nDcg = new NormalizedDiscountedCumulativeGain().evaluate(bestAhpResultComplete, ahpConfigurator.getOriginalRank());
        if (nDcg > 1.0d) {
            err("nDcg > 1.0d");
            err("--------------------------------------------" + nDcg);
            err("ORIGINAL--------->" + Arrays.toString(ahpConfigurator.getOriginalRank()));
            err("BEST RESULT------>" + Arrays.toString(bestAhpResultComplete));
        }
//        System.out.println("END PRINT");

        out(String.valueOf(euclidean) + "," + String.valueOf(correlation) + "," + String.valueOf(precision) + "," + String.valueOf(nDcg) + "," + String.valueOf(pvalue) + "\n");

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
