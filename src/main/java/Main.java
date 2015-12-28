import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfiguration;
import org.jgap.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {


    private static final int MAX_ALLOWED_EVOLUTIONS = 500;
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
        population = Genotype.randomInitialGenotype(conf);

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

        Genotype population = createChromosome(chromosomeSize, originalRank, h);

        populateAHP(brAlt, alternatives, brCriteria, criteria, true, population);

        /*
        LOOP:
        */

        int alternativesSize = h.getAlternativesSize();
        double[] newAhpResult = new double[alternativesSize];
        double[] rescaled = new double[alternativesSize];
        double[] ano_rescaled = new double[alternativesSize];

        int last = alternativesSize - 1;
        double majorPi = h.Pi(last);
        double domain0 = h.Pi(0) / majorPi;
        Rescale r = new Rescale(0, 1, originalRank[0], originalRank[last]);

        double sum = 0.0d;

        for (int i = 0; i < alternativesSize; i++) {

            sum += originalRank[i];

            newAhpResult[i] = h.Pi(i);
            rescaled[i] = r.rescale(newAhpResult[i]);
        }


        for (int i = 0; i < alternativesSize; i++) {

            ano_rescaled[i] = originalRank[i]/sum;
        }

        System.out.println(Arrays.toString(originalRank));
        System.out.println(Arrays.toString(newAhpResult));
        System.out.println(Arrays.toString(ano_rescaled));
//        System.out.println(h.print());


//        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
//            population.evolve();
//        }
//
//        alternativesSize = h.getAlternativesSize();
//        newAhpResult = new double[alternativesSize];
//        for (int i = 0; i < alternativesSize; i++) {
//            newAhpResult[i] = h.Pi(i)/h.Pi(alternativesSize - 1);
//        }
//
//        System.out.println(Arrays.toString(newAhpResult));




    }

    private static double[] readOriginalRank(BufferedReader brAlt, Vector<Alternative> alternatives) throws IOException {
        String tempAlt;
        tempAlt = brAlt.readLine();
        double[] originalData = new double[alternatives.size()];
        String[] originalDataStr = tempAlt.split(",");
        for (int i = 0; i < alternatives.size(); i++) {
            originalData[i] = Double.parseDouble(originalDataStr[i]);
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
