import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.*;
import model.NormalizedDiscountedCumulativeGain;
import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.audit.IEvolutionMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {


    private static final int MAX_ALLOWED_EVOLUTIONS = 9;
    public static final int RANDOM_CROMO = 0;

    public static void main(String[] args) throws IOException, InvalidConfigurationException {

        String featuresFile = "Features.dat";
        String alternativesFile = "Alternatives.dat";

        FileReader fileAlt = new FileReader(new File(alternativesFile));
        BufferedReader brAlt = new BufferedReader(fileAlt);
        String tempAlt = brAlt.readLine();

        Vector<Alternative> alternatives = readAlternatives(tempAlt);

        double[] originalRank = readOriginalRank(brAlt, alternatives);

        FileReader file = new FileReader(new File(featuresFile));
        BufferedReader brCriteria = new BufferedReader(file);

        String featuresStr = brCriteria.readLine();
        int chromosomeSize = featuresStr.split(",").length;

        String[] tempCrit = brCriteria.readLine().split(",");;

        double[][] individualRanks = readIndividualRanks(alternatives.size(), brCriteria, tempCrit.length);

        AHPConfigurator ahpConfigurator = new AHPConfigurator();
        ahpConfigurator.createConfiguration(chromosomeSize, originalRank);

        for (int i = 0; i < alternatives.size(); i++) {

            Vector<Alternative> crossValidationAlternatives = (Vector)alternatives.clone();
            crossValidationAlternatives.remove(i);

            Hierarchy h = new Hierarchy(crossValidationAlternatives);

            Vector<Criterium> criteria = readCriteria(tempCrit, h);
            h.getGoal().createPCM(criteria);

            ahpConfigurator.getFitnessFunction().setCrossValidationAlternative(i);
            ahpConfigurator.getFitnessFunction().setH(h);

            Genotype population = ahpConfigurator.createPopulation();
            populateAHP(brAlt, crossValidationAlternatives, brCriteria, criteria, true, population, i);
            System.out.println(Arrays.toString(originalRank));

            IEvolutionMonitor monitor = new FitnessValueMonitor(0.01d);

            ((AHPGenotype) population).evolve(monitor, MAX_ALLOWED_EVOLUTIONS);

            double[] bestAhpResult = printFittestResult(h, population, crossValidationAlternatives.size());

        }


    }

    private static void nDCG(double[] originalRank, Hierarchy h, int alternativesSize, double[][] individualRanks, double[] bestAhpResult) {
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

        System.out.println(gain.evaluate(bestAhpResult, originalRank, indexes));

        for (int i = 0; i < h.getGoal().getSonsSize(); i++) {

            double[] newAhpResult = new double[alternativesSize];
            for (int j = 0; j < alternativesSize; j++) {
                newAhpResult[j] = h.Pi(j, i);
            }

            System.out.println(gain.evaluate(newAhpResult, individualRanks[i], null));

        }
    }

    private static double[][] readIndividualRanks(int alternativesSize, BufferedReader brCriteria, int criteriaSize) throws IOException {
        double[][] individualRanks = new double[criteriaSize][alternativesSize];

        for (int i = 0; i < criteriaSize; i++) {
            String[] tempRatings = brCriteria.readLine().split(",");
            double sum = 0.0d;
            for (int j = 0; j < alternativesSize; j++) {
                double parseDouble = Double.parseDouble(tempRatings[j]);
                sum += parseDouble;
                individualRanks[i][j] = parseDouble;
            }

            for (int j = 0; j < alternativesSize; j++) {
                individualRanks[i][j] = individualRanks[i][j] / sum;
            }

        }

        return individualRanks;
    }

    private static double[] printFittestResult(Hierarchy h, Genotype population, int alternativesSize) {
        double[] newAhpResult;
        IChromosome a_subject = population.getFittestChromosome();
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

        newAhpResult = new double[alternativesSize];
        for (int j = 0; j < alternativesSize; j++) {
            newAhpResult[j] = h.Pi(j);
        }

        System.out.println(Arrays.toString(newAhpResult));

        return newAhpResult;
    }

    private static double[] readOriginalRank(BufferedReader brAlt, Vector<Alternative> alternatives) throws IOException {
        String tempAlt;
        tempAlt = brAlt.readLine();
        double[] originalData = new double[alternatives.size()];
        String[] originalDataStr = tempAlt.split(",");

        double sum = 0.0d;
        for (int i = 0; i < alternatives.size(); i++) {
            double parseDouble = Double.parseDouble(originalDataStr[i]);
            sum += parseDouble;
            originalData[i] = parseDouble;
        }

        for (int i = 0; i < alternatives.size(); i++) {
            originalData[i] = originalData[i] / sum;
        }

        return originalData;
    }

    private static void populateAHP(BufferedReader brAlt, Vector<Alternative> alternatives, BufferedReader brCrits, Vector<Criterium> criteria, boolean gaWeight, Genotype initialGenotype, int crossValidationAlternative) throws IOException {
        String tempCrit;

        int geneIndex = 0;

        for (Criterium criterium : criteria) {
            tempCrit = brCrits.readLine();

            String[] featuresStr = tempCrit.split(",");
            int length = featuresStr.length;
            Vector<Criterium> features = new Vector(length);
            double[] weights = new double[length];

            if (gaWeight) {
                setGaWeights(weights, initialGenotype, geneIndex);
                geneIndex += length;
            }

            for (int i = 0; i < length; i++) {
                createFeaturesWithAlternativesWeights(brAlt, alternatives, criterium, featuresStr[i], features, weights, gaWeight, i, crossValidationAlternative);
            }

            criterium.createPCM(features, weights);

        }
    }

    private static void setGaWeights(double[] weights, Genotype initialGenotype, int geneIndex) {

        IChromosome chromosome = initialGenotype.getPopulation().getChromosome(RANDOM_CROMO);

        for (int i = 0; i < weights.length; i++) {
            weights[i] = ((Double) chromosome.getGene(geneIndex + i).getAllele()).doubleValue();

        }
    }

    private static void createFeaturesWithAlternativesWeights(BufferedReader brAlt, Vector<Alternative> alternatives, Criterium criterium, String featureStr, Vector<Criterium> features, double[] weights, boolean gaWeight, int i, int crossValidationAlternative) throws IOException {

        if (!gaWeight) {
            String[] featureAndWeight = featureStr.split("@");
            featureStr = featureAndWeight[0];
            weights[i] = Double.parseDouble(featureAndWeight[1]);
        }

        Criterium feature = new Criterium(featureStr, true, criterium);
        features.add(feature);

        String tempAlt = brAlt.readLine();
        String[] alternativeWeightsStr = tempAlt.split(",");
        int lengthWeights = alternativeWeightsStr.length;
        double[] alternativeWeights = new double[lengthWeights - 1];

        int correctIndex = 0;
        for (int j = 0; j < lengthWeights; j++) {
            if (j != crossValidationAlternative) {
                alternativeWeights[correctIndex++] = Double.parseDouble(alternativeWeightsStr[j]);
            }
        }

        feature.createPCM(alternatives, alternativeWeights);
    }

    private static Vector<Alternative> readAlternatives(String tempAlt) {
        String[] altStr = tempAlt.split(",");
        int altLength = altStr.length;
        Vector<Alternative> alternatives = new Vector(altLength);
        for (int i = 0; i < altLength; i++) {
            Alternative alternative = new Alternative(altStr[i]);
            alternatives.add(alternative);
        }
        return alternatives;
    }

    private static Vector<Criterium> readCriteria(String[] critStr, Hierarchy h) {

        int critLength = critStr.length;

        Vector<Criterium> criteria = new Vector(critLength);
        for (int i = 0; i < critLength; i++) {
            Criterium criterium = new Criterium(critStr[i], false, h.getGoal());
            criteria.add(criterium);
        }

        return criteria;
    }

}
