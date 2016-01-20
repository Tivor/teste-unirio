import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfigurator;
import jgap.AHPGenotype;
import jgap.FitnessValueMonitor;
import model.NormalizedDiscountedCumulativeGain;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.audit.IEvolutionMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        System.out.println(Arrays.toString(originalRank));

        FileReader file = new FileReader(new File(featuresFile));
        BufferedReader brCriteria = new BufferedReader(file);

        String featuresStr = brCriteria.readLine();
        int chromosomeSize = featuresStr.split(",").length;

        String[] tempCrit = brCriteria.readLine().split(",");;

        double[][] individualRanks = readIndividualRanks(alternatives.size(), brCriteria, tempCrit.length);

        AHPConfigurator ahpConfigurator = new AHPConfigurator();
        ahpConfigurator.createConfiguration(chromosomeSize, originalRank);


        Map<String, String> mapeamento = new HashMap();
        Map<String, String> valoresFeatures = new HashMap();


        Hierarchy hierarchyTest = new Hierarchy(alternatives);
        boolean testCreated = false;

        for (int i = 0; i < alternatives.size(); i++) {

            executeCrossValidation(brAlt, alternatives, brCriteria, tempCrit, ahpConfigurator, i, hierarchyTest, testCreated, mapeamento, valoresFeatures);
            testCreated = true;

        }


    }

    private static void executeCrossValidation(BufferedReader brAlt, Vector<Alternative> alternatives, BufferedReader brCriteria, String[] tempCrit, AHPConfigurator ahpConfigurator, int i, Hierarchy hierarchyTest, boolean testCreated, Map<String, String> mapeamento, Map<String, String> valoresFeatures) throws InvalidConfigurationException, IOException {
        Vector<Alternative> crossValidationAlternatives = (Vector)alternatives.clone();
        crossValidationAlternatives.remove(i);

        Hierarchy h = new Hierarchy(crossValidationAlternatives);

        Vector<Criterium> criteria = new Vector<Criterium>(tempCrit.length);


        Vector<Criterium> criteriaTest = null;
        if (!testCreated) {
            criteriaTest = new Vector<Criterium>(tempCrit.length);
        }
        readCriteria(tempCrit, h, hierarchyTest, criteria, criteriaTest, testCreated);

        h.getGoal().createPCM(criteria);

        if (!testCreated) {
            hierarchyTest.getGoal().createPCM(criteriaTest);
        }

        ahpConfigurator.getFitnessFunction().setCrossValidationAlternative(i);
        ahpConfigurator.getFitnessFunction().setH(h);

        Genotype population = ahpConfigurator.createPopulation();
        populateAHP(brAlt, crossValidationAlternatives, alternatives, brCriteria, criteria, criteriaTest, testCreated, true, population, i, mapeamento, valoresFeatures);

        IEvolutionMonitor monitor = new FitnessValueMonitor(0.01d);
        ((AHPGenotype) population).evolve(monitor, MAX_ALLOWED_EVOLUTIONS);

        double[] bestAhpResult = printFittestResult(h, population);
        double[] bestAhpResultComplete = printFittestResult(hierarchyTest, population);
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

    private static double[] printFittestResult(Hierarchy h, Genotype population) {
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

        newAhpResult = new double[h.getAlternativesSize()];
        for (int j = 0; j < h.getAlternativesSize(); j++) {
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

    private static void populateAHP(BufferedReader brAlt, Vector<Alternative> alternatives, Vector<Alternative> allAlternatives, BufferedReader brCrits, Vector<Criterium> criteria, Vector<Criterium> criteriaTest, boolean testCreated, boolean gaWeight, Genotype initialGenotype, int crossValidationAlternative, Map<String, String> mapeamento, Map<String, String> valoresFeatures) throws IOException {
        String tempCrit;

        int geneIndex = 0;

        for (int critIndex = 0; critIndex < criteria.size(); critIndex++) {

            Criterium criterium = criteria.get(critIndex);

            if (testCreated) {
                tempCrit = mapeamento.get(criterium.getName());
            } else {
                tempCrit = brCrits.readLine();
                mapeamento.put(criterium.getName(), tempCrit);
            }

            String[] featuresStr = tempCrit.split(",");
            int length = featuresStr.length;
            Vector<Criterium> features = new Vector(length);
            Vector<Criterium> featuresTest = new Vector(length);

            double[] weights = new double[length];

            if (gaWeight) {
                setGaWeights(weights, initialGenotype, geneIndex);
                geneIndex += length;
            }

            for (int i = 0; i < length; i++) {
                Criterium criteriumTest = testCreated ? null : criteriaTest.get(critIndex);
                createFeaturesWithAlternativesWeights(brAlt, alternatives, allAlternatives, criteria.get(critIndex), criteriumTest, featuresStr[i], features, featuresTest, testCreated, weights, gaWeight, i, crossValidationAlternative, valoresFeatures);
            }

            criterium.createPCM(features, weights);

            if (!testCreated) {
                Criterium criteriumTest = criteriaTest.get(critIndex);
                criteriumTest.createPCM(featuresTest, weights);
            }

        }
    }

    private static void setGaWeights(double[] weights, Genotype initialGenotype, int geneIndex) {

        IChromosome chromosome = initialGenotype.getPopulation().getChromosome(RANDOM_CROMO);

        for (int i = 0; i < weights.length; i++) {
            weights[i] = ((Double) chromosome.getGene(geneIndex + i).getAllele()).doubleValue();

        }
    }

    private static void createFeaturesWithAlternativesWeights(BufferedReader brAlt, Vector<Alternative> alternatives,  Vector<Alternative> allAlternatives, Criterium criterium, Criterium criteriumTest, String featureStr, Vector<Criterium> features, Vector<Criterium> featuresTest, boolean testCreated, double[] weights, boolean gaWeight, int i, int crossValidationAlternative, Map<String, String> valoresFeatures) throws IOException {

        if (!gaWeight) {
            String[] featureAndWeight = featureStr.split("@");
            featureStr = featureAndWeight[0];
            weights[i] = Double.parseDouble(featureAndWeight[1]);
        }

        Criterium feature = new Criterium(featureStr, true, criterium);
        features.add(feature);

        String tempAlt;

        if (testCreated) {
            tempAlt = valoresFeatures.get(featureStr);
        } else {
            tempAlt = brAlt.readLine();
            valoresFeatures.put(featureStr, tempAlt);
        }


        String[] alternativeWeightsStr = tempAlt.split(",");
        int lengthWeights = alternativeWeightsStr.length;
        double[] alternativeWeights = new double[lengthWeights - 1];

        double[] allAlternativeWeights = new double[lengthWeights];

        int correctIndex = 0;
        for (int j = 0; j < lengthWeights; j++) {

            double v = Double.parseDouble(alternativeWeightsStr[j]);

            if (!testCreated) {
                allAlternativeWeights[j] = v;
            }

            if (j != crossValidationAlternative) {

                alternativeWeights[correctIndex++] = v;
            }
        }

        feature.createPCM(alternatives, alternativeWeights);

        if (!testCreated) {
            Criterium featureTest = new Criterium(featureStr, true, criteriumTest);
            featuresTest.add(featureTest);
            featureTest.createPCM(allAlternatives, allAlternativeWeights);
        }

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

    private static void readCriteria(String[] critStr, Hierarchy h, Hierarchy hierarchyTest, Vector<Criterium> criteria, Vector<Criterium> criteriaTest, boolean testCreated) {

        for (int i = 0; i < critStr.length; i++) {

            Criterium criterium = new Criterium(critStr[i], false, h.getGoal());
            criteria.add(criterium);

            if (!testCreated) {
                Criterium criteriumTest = new Criterium(critStr[i], false, hierarchyTest.getGoal());
                criteriaTest.add(criteriumTest);
            }
        }

    }

}
