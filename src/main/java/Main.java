import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfiguration;
import org.jgap.*;

import java.io.*;
import java.util.Vector;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {


    private static final int MAX_ALLOWED_EVOLUTIONS = 5000;

    public static Genotype createChromosome(int chromosomeSize) throws InvalidConfigurationException {
        Configuration conf = new AHPConfiguration(chromosomeSize, "ahp", "AHP");

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

        FileReader file = new FileReader(new File(featuresFile));
        BufferedReader brCriteria = new BufferedReader(file);

        String featuresStr = brCriteria.readLine();
        int chromosomeSize = featuresStr.split(",").length;

        Genotype initialGenotype = createChromosome(chromosomeSize);

        String tempCrit = brCriteria.readLine();

        Hierarchy h = new Hierarchy(alternatives);

        Vector<Criterium> criteria = readCriteria(tempCrit, h);
        h.getGoal().createPCM(criteria);

        populateAHP(brAlt, alternatives, brCriteria, criteria, true, initialGenotype);

        /*
        LOOP:

        //        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
//            population.evolve();
//        }
//
//        IChromosome fittestChromosome = population.getFittestChromosome();
//        Gene[] genes = fittestChromosome.getGenes();


         */


        int best = h.bestAlternative();

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
                setRandomGaWeights(weights, initialGenotype, geneIndex);
                geneIndex += length;
            }

            for (int i = 0; i < length; i++) {
                createFeaturesWithAlternativesWeights(brAlt, alternatives, criterium, featuresStr[i], features, weights, gaWeight, i);
            }

            criterium.createPCM(features, weights);

        }
    }

    private static void setRandomGaWeights(double[] weights, Genotype initialGenotype, int geneIndex) {

        IChromosome chromosome = initialGenotype.getFittestChromosome();

        for(int i = 0; i< weights.length; i++) {
            weights[i] = ((Double)chromosome.getGene(geneIndex + i).getAllele()).doubleValue();

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
