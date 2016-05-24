package jgap;

import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import org.apache.commons.math3.stat.StatUtils;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.util.Arrays;

/**
 * Created by Poliana on 29/12/2015.
 */
public abstract class AHPFitnessFunction extends FitnessFunction {

    protected double[] original;

    protected double[][] individualOriginal;

    protected int crossValidationAlternative = -1;

    protected Hierarchy h;

    public AHPFitnessFunction(double[] originalData) {
        this.original = originalData;
    }

    public AHPFitnessFunction(double[][] individualOriginal) {
        this.individualOriginal = individualOriginal;
    }

    public void setCrossValidationAlternative(int crossValidationAlternative) {
        this.crossValidationAlternative = crossValidationAlternative;
    }

    public void setH(Hierarchy h) {
        this.h = h;
    }

    protected double[] runAHP(IChromosome a_subject) {
        int alternativesSize = populateAHP(a_subject);

        double[] newAhpResult = new double[alternativesSize];

        int correctIndex = 0;
        for (int j = 0; j <= alternativesSize; j++) {

            if (j != crossValidationAlternative) {
                double pi = h.Pi(j);
                newAhpResult[correctIndex++] = pi;
            }

        }

        double sum = StatUtils.sum(newAhpResult);
        correctIndex = 0;
        for (int j = 0; j <= alternativesSize; j++) {

            if (j != crossValidationAlternative) {
                newAhpResult[correctIndex] = newAhpResult[correctIndex++] / sum;
            }

        }

        return newAhpResult;

    }

    private int populateAHP(IChromosome a_subject) {
        int geneIndex = 0;
        for (Criterium criterium : h.getGoal().getSons()) {

            int featureSize = criterium.getSonsSize();
            double[] weights = new double[featureSize];

            for (int i = 0; i < weights.length; i++) {
                weights[i] = ((Integer) a_subject.getGene(geneIndex + i).getAllele()).doubleValue();
            }

            geneIndex += featureSize;
            criterium.updatePCM(weights);
        }

        return h.getAlternativesSize();
    }

    protected double[][] runCriteriaAHP(IChromosome a_subject, int crossValidationAlternative) {
        int alternativesSize = populateAHP(a_subject);
        int sonsSize = h.getGoal().getSonsSize();

        double[][] newFullAhpResult = new double[sonsSize][alternativesSize];

        for (int k = 0; k < sonsSize; k++) {

//            double interSum = 0.0d;
            int correctIndex = 0;
            for (int j = 0; j <= alternativesSize; j++) {

                if (j != crossValidationAlternative) {
                    double pi = h.Pi(j, k);
                    newFullAhpResult[k][correctIndex++] = pi;
//                    interSum += pi;
                }

            }

//            correctIndex = 0;
//            for (int j = 0; j <= alternativesSize; j++) {
//                if (j != crossValidationAlternative) {
//                    newFullAhpResult[k][correctIndex] = newFullAhpResult[k][correctIndex] / interSum;
//                    correctIndex++;
//                }
//            }

        }

        return newFullAhpResult;

    }

}
