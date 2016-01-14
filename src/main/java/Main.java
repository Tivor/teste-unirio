import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfiguration;
import jgap.AHPGenotype;
import jgap.FitnessValueMonitor;
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


    private static final int MAX_ALLOWED_EVOLUTIONS = 9000;
    public static final int RANDOM_CROMO = 0;

    public static Genotype createChromosome(int chromosomeSize, double[] originalData, Hierarchy h) throws InvalidConfigurationException {
        Configuration conf = new AHPConfiguration(chromosomeSize, "ahp", "AHP", originalData, h);

        // Create random initial population of Chromosomes.
        // Here we try to read in a previous run via XMLManager.readFile(..)
        // for demonstration purpose only!
        // -----------------------------------------------------------------
        Genotype population;
        // Now we initialize the population randomly, anyway (as an example only)!
        // If you want to load previous results from file, remove the next line!
        // -----------------------------------------------------------------------
        population = AHPGenotype.randomInitialGenotype(conf);

        return population;

    }


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

        String tempCrit = brCriteria.readLine();

        Hierarchy h = new Hierarchy(alternatives);

        Vector<Criterium> criteria = readCriteria(tempCrit, h);
        h.getGoal().createPCM(criteria);

        //TODO: ainda falta ler!
        double[][] individualRanks = new double[criteria.size()][alternatives.size()];

        Genotype population = createChromosome(chromosomeSize, originalRank, h);

        populateAHP(brAlt, alternatives, brCriteria, criteria, true, population);

        int alternativesSize = h.getAlternativesSize();

        System.out.println(Arrays.toString(originalRank));
//        System.out.println(h.print());

        alternativesSize = h.getAlternativesSize();

        IEvolutionMonitor monitor = new FitnessValueMonitor(0.01d);

        ((AHPGenotype) population).evolve(monitor, MAX_ALLOWED_EVOLUTIONS);

        double[] bestAhpResult = printFittestResult(h, population, alternativesSize);

        NormalizedDiscountedCumulativeGain gain = new NormalizedDiscountedCumulativeGain(h.getAlternativesSize());
        System.out.println(gain.evaluate(bestAhpResult, originalRank));

        for (int i = 0; i < h.getGoal().getSonsSize(); i++) {

            double[] newAhpResult = new double[alternativesSize];
            for (int j = 0; j < alternativesSize; j++) {
                newAhpResult[j] = h.Pi(j, i);
            }

            System.out.println(gain.evaluate(newAhpResult, individualRanks[i]));

        }

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

    private static void populateAHP(BufferedReader brAlt, Vector<Alternative> alternatives, BufferedReader brCrits, Vector<Criterium> criteria, boolean gaWeight, Genotype initialGenotype) throws IOException {
        String tempCrit;

        int geneIndex = 0;

        for (Criterium criterium : criteria) {
            tempCrit = brCrits.readLine();

            String[] featuresStr = tempCrit.split("@");
            int length = featuresStr.length;
            Vector<Criterium> features = new Vector(length);
            double[] weights = new double[length];

            if (gaWeight) {
                setGaWeights(weights, initialGenotype, geneIndex);
                geneIndex += length;
            }

            for (int i = 0; i < length; i++) {
                createFeaturesWithAlternativesWeights(brAlt, alternatives, criterium, featuresStr[i], features, weights, gaWeight, i);
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

    private static void createFeaturesWithAlternativesWeights(BufferedReader brAlt, Vector<Alternative> alternatives, Criterium criterium, String featureStr, Vector<Criterium> features, double[] weights, boolean gaWeight, int i) throws IOException {
        String tempAlt;
        String[] featureAndWeight = featureStr.split(",");
        Criterium feature = new Criterium(featureAndWeight[0], true, criterium);
        features.add(feature);

        if (!gaWeight) weights[i] = Double.parseDouble(featureAndWeight[1]);

        tempAlt = brAlt.readLine();
        String[] alternativeWeightsStr = tempAlt.split(",");
        int lengthWeights = alternativeWeightsStr.length;
        double[] alternativeWeights = new double[lengthWeights];

        for (int j = 0; j < lengthWeights; j++) {
            alternativeWeights[j] = Double.parseDouble(alternativeWeightsStr[j]);
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

    private static Vector<Criterium> readCriteria(String tempCrit, Hierarchy h) {
        String[] critStr = tempCrit.split(",");
        int critLength = critStr.length;

        Vector<Criterium> criteria = new Vector(critLength);
        for (int i = 0; i < critLength; i++) {
            Criterium criterium = new Criterium(critStr[i], false, h.getGoal());
            criteria.add(criterium);
        }

        return criteria;
    }

}
