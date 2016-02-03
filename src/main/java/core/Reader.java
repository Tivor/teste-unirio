package core;

import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Igor on 02/02/2016.
 */
public class Reader {

    public double[][] readIndividualRanks(int alternativesSize, BufferedReader brCriteria, int criteriaSize) throws IOException {
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

    public double[] readOriginalRank(BufferedReader brAlt, Vector<Alternative> alternatives) throws IOException {
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

    public Vector<Alternative> readAlternatives(String tempAlt) {
        String[] altStr = tempAlt.split(",");
        int altLength = altStr.length;
        Vector<Alternative> alternatives = new Vector(altLength);
        for (int i = 0; i < altLength; i++) {
            Alternative alternative = new Alternative(altStr[i]);
            alternatives.add(alternative);
        }
        return alternatives;
    }

    public void readCriteria(String[] critStr, Hierarchy h, Hierarchy hierarchyTest, Vector<Criterium> criteria, Vector<Criterium> criteriaTest, boolean testCreated) {

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
