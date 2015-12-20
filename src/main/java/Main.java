import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        String featuresFile = "Features.dat";
        String alternativesFile = "Alternatives.dat";

        FileReader fileAlt = new FileReader(new File(alternativesFile));
        BufferedReader brAlt = new BufferedReader(fileAlt);
        String tempAlt = brAlt.readLine();

        Vector<Alternative> alternatives = readAlternatives(tempAlt);

        FileReader file = new FileReader(new File(featuresFile));
        BufferedReader brCriteria = new BufferedReader(file);
        String tempCrit = brCriteria.readLine();

        Hierarchy h = new Hierarchy(alternatives);

        Vector<Criterium> criteria = readCriteria(tempCrit, h);
        h.getGoal().createPCM(criteria.size());

        populateAHP(brAlt, alternatives, brCriteria, criteria);


//        int initCap = 10;
//        int alternativeToEvaluate = 0;
//
//        Vector<Alternative> allAlternatives = new Vector(initCap);
//
//        for (int i = 0; i < initCap; i++) {
//
//            Vector<Alternative> currentAlternatives = new Vector(initCap - 1);
//            currentAlternatives.addAll(allAlternatives);
//            currentAlternatives.remove(alternativeToEvaluate);
//
//            Hierarchy h = getHierarchyExample(currentAlternatives);
//
//
//            alternativeToEvaluate++;
//        }







    }

    private static void populateAHP(BufferedReader brAlt, Vector<Alternative> alternatives, BufferedReader brCrits, Vector<Criterium> criteria) throws IOException {
        String tempCrit;

        for (Criterium criterium : criteria) {
            tempCrit = brCrits.readLine();

            String[] featuresStr = tempCrit.split(",");
            int length = featuresStr.length;
            Vector<Criterium> features = new Vector(length);
            double[] weights = new double[length];
            for (int i = 0; i < length; i++) {
                createFeaturesWithAlternativesWeights(brAlt, alternatives, criterium, featuresStr[i], features, weights, i);
            }

            criterium.addCriteria(features, weights);

        }
    }

    private static void createFeaturesWithAlternativesWeights(BufferedReader brAlt, Vector<Alternative> alternatives, Criterium criterium, String s, Vector<Criterium> features, double[] weights, int i) throws IOException {
        String tempAlt;
        String[] featureAndWeight = s.split(",");
        Criterium feature = new Criterium(featureAndWeight[0], true, criterium);
        features.add(feature);
        weights[i] = Double.parseDouble(featureAndWeight[1]);

        tempAlt = brAlt.readLine();
        String[] alternativeWeightsStr = tempAlt.split(",");
        int lengthWeights = alternativeWeightsStr.length;
        double[] alternativeWeights = new double[lengthWeights];

        for (int j = 0; j < lengthWeights; j++) {
            alternativeWeights[j] = Double.parseDouble(alternativeWeightsStr[j]);
        }

        feature.addCriteria(alternatives, alternativeWeights);
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
