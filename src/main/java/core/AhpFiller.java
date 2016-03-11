package core;

import jahp.adt.Alternative;
import jahp.adt.Criterium;
import org.jgap.Genotype;
import org.jgap.IChromosome;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Igor on 02/02/2016.
 */
public class AhpFiller {

    public static final int RANDOM_CROMO = 0;

    public void populateAHP(BufferedReader brAlt, Vector<Alternative> alternatives, Vector<Alternative> allAlternatives, BufferedReader brCrits, Vector<Criterium> criteria, Vector<Criterium> criteriaTest, boolean testCreated, boolean gaWeight, Genotype initialGenotype, int crossValidationAlternative, Map<String, String> mapeamento, Map<String, String> valoresFeatures) throws IOException {
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

    public void setGaWeights(double[] weights, Genotype initialGenotype, int geneIndex) {

        IChromosome chromosome = initialGenotype.getPopulation().getChromosome(RANDOM_CROMO);

        for (int i = 0; i < weights.length; i++) {
            weights[i] = ((Double) chromosome.getGene(geneIndex + i).getAllele()).doubleValue();

        }
    }

    public void createFeaturesWithAlternativesWeights(BufferedReader brAlt, Vector<Alternative> alternatives,  Vector<Alternative> allAlternatives, Criterium criterium, Criterium criteriumTest, String featureStr, Vector<Criterium> features, Vector<Criterium> featuresTest, boolean testCreated, double[] weights, boolean gaWeight, int i, int crossValidationAlternative, Map<String, String> valoresFeatures) throws IOException {

        if (!gaWeight) {
            String[] featureAndWeight = featureStr.split("@");
            featureStr = featureAndWeight[0];
            weights[i] = Double.parseDouble(featureAndWeight[1]);
        }

        Criterium feature = new Criterium(featureStr, true, criterium, i);
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


        Vector<Alternative> alternativesClean = (Vector) alternatives.clone();
        Vector<Alternative> allAlternativesClean = (Vector) allAlternatives.clone();

        int countZeros = 0;
        int countZerosAll = 0;
        double[] parsedWeights = new double[lengthWeights];
        for (int j = 0; j < lengthWeights; j++) {
            parsedWeights[j] = Double.parseDouble(alternativeWeightsStr[j]);
            if (parsedWeights[j] == 0.0d) {
                countZerosAll++;
                if (j != crossValidationAlternative) countZeros++;
            }
        }

        double[] alternativeWeights = new double[lengthWeights - 1 - countZeros];
        double[] allAlternativeWeights = new double[lengthWeights - countZerosAll];

        int correctIndex = 0;
        int correctIndexAll = 0;
        for (int j = 0; j < lengthWeights; j++) {

            if (parsedWeights[j] > 0.0d) {
                if (!testCreated) {
                    allAlternativeWeights[correctIndexAll++] = parsedWeights[j];
                }

                if (j != crossValidationAlternative) {

                    alternativeWeights[correctIndex++] = parsedWeights[j];
                }
            } else {
                if (j != crossValidationAlternative) {
                    alternativesClean.remove(correctIndex);
                }

                allAlternativesClean.remove(correctIndexAll);
            }
        }

        feature.createPCM(alternativesClean, alternativeWeights);

        if (!testCreated) {
            Criterium featureTest = new Criterium(featureStr, true, criteriumTest, i);
            featuresTest.add(featureTest);
            featureTest.createPCM(allAlternativesClean, allAlternativeWeights);
        }

    }

}
