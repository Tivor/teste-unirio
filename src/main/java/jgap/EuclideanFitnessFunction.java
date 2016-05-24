package jgap;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.util.FastMath;
import org.jgap.IChromosome;

import java.util.*;

/**
 * Created by igor.custodio on 06/01/2016.
 */
public class EuclideanFitnessFunction extends AHPFitnessFunction {

    boolean extended = false;

    private List<double[][]> cacheIndividualOriginalCalculated = new ArrayList();

    private List<double[]> cacheOriginalCalculated = new ArrayList();

    EuclideanDistance euclideanDistance = new EuclideanDistance();

    public EuclideanFitnessFunction(double[] originalData) {

        super(originalData);

        for (int i = 0; i < originalData.length; i++) {
            cacheOriginalCalculated.add(clean(this.original, i));
        }

    }

    public EuclideanFitnessFunction(double[][] originalIndividualData) {
        super(originalIndividualData);

        for (int i = 0; i < originalIndividualData[0].length; i++) {
            cacheIndividualOriginalCalculated.add(clean(this.individualOriginal, i));
        }

        this.extended = true;
    }

    @Override
    protected double evaluate(IChromosome iChromosome) {
        return this.extended ? evaluateExt(iChromosome) : evaluateSimple(iChromosome);
    }

    private double evaluateSimple(IChromosome iChromosome) {

        double[] originalCleaned = cacheOriginalCalculated.get(this.crossValidationAlternative);

        double[] newAhpResult = runAHP(iChromosome);

        double sumTest = StatUtils.sum(newAhpResult);
        if (sumTest < 0.99999d || sumTest > 1.000001d) {
            System.err.println("------------------->" + Arrays.toString(newAhpResult));
            System.err.println("Sum>" + sumTest);
            System.err.println("<----------------------------------------------------");
            System.exit(-199);
        }

        double distance = euclideanDistance.compute(originalCleaned, newAhpResult);
        return distance == 0.0d ? Double.MAX_VALUE : 1/distance;
    }

    private double[][] clean(double[][] individualOriginal, int index) {

        int sonsSize = individualOriginal.length;
        int alternativesSize = individualOriginal[0].length - 1;
        double[][] result = new double[sonsSize][alternativesSize];

        for (int i = 0; i < sonsSize; i++) {
            double sum = 0.0d;
            int correctIndex = 0;
            for (int j = 0; j <= alternativesSize; j++) {

                if (j != index) {
                    sum += individualOriginal[i][j];
                    result[i][correctIndex] = individualOriginal[i][j];
                    correctIndex++;
                }
            }

            for (int j = 0; j < alternativesSize; j++) {
                result[i][j] = result[i][j] / sum;
            }

            double sumTest = StatUtils.sum(result[i]);
            if (sumTest < 0.99999d || sumTest > 1.000001d) {
                System.err.println("------------------->" + Arrays.toString(result[i]));
                System.err.println("Sum>" + sumTest);
                System.err.println("<----------------------------------------------------");
                System.exit(-299);
            }

        }

        return result;
    }

    private double[] clean(double[] individualOriginal, int index) {

        double[] result = new double[individualOriginal.length-1];

        double sum = 0.0d;
        int correctIndex = 0;
        for (int j = 0; j <= result.length; j++) {

            if (j != index) {
                sum += individualOriginal[j];
                result[correctIndex] = individualOriginal[j];
                correctIndex++;
            }
        }

        for (int j = 0; j < result.length; j++) {
            result[j] = result[j] / sum;
        }

        double sumTest = StatUtils.sum(result);
        if (sumTest < 0.99999d || sumTest > 1.000001d) {
            System.err.println("------------------->" + Arrays.toString(result));
            System.err.println("Sum>" + sumTest);
            System.err.println("<----------------------------------------------------");
            System.exit(-399);
        }

        return result;
    }

    private double evaluateExt(IChromosome iChromosome) {

        double[][] newFullAhpResult = runCriteriaAHP(iChromosome, this.crossValidationAlternative);

        double[][] individualOriginalCleaned = cacheIndividualOriginalCalculated.get(this.crossValidationAlternative);

        double sum = 0.0d;

        int sonsSize = h.getGoal().getSonsSize();
        double[] distance = new double[sonsSize];

        for (int i = 0; i < sonsSize; i++) {
            /*double*/
            distance[i] = euclideanDistance.compute(individualOriginalCleaned[i], newFullAhpResult[i]);
            sum += distance[i];
        }

//        return distance(distance);
        double v = 1 / sum;
        return v;

    }


//    public static double distance(double[] p1) {
//
//        double sum = 0.0d;
//
//        for(int i = 0; i < p1.length; ++i) {
//            double dp = p1[i] - 0.01d;
//            sum += dp * dp;
//        }
//
//        double sqrt = FastMath.sqrt(sum);
//        return sqrt == 0.0d ? Double.MAX_VALUE : 1 / sqrt;
//    }
}
