package jgap;

import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

/**
 * Created by Poliana on 29/12/2015.
 */
public abstract class AHPFitnessFunction extends FitnessFunction {

    protected double[] original;

    protected int crossValidationAlternative = -1;

    protected Hierarchy h;

    public AHPFitnessFunction(double[] originalData) {

        this.original = originalData;

    }

    public void setCrossValidationAlternative(int crossValidationAlternative) {
        this.crossValidationAlternative = crossValidationAlternative;
    }

    public void setH(Hierarchy h) {
        this.h = h;
    }

    protected double[] runAHP(IChromosome a_subject) {
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

        int alternativesSize = h.getAlternativesSize();
        double[] newAhpResult = new double[alternativesSize];
        for (int i = 0; i < alternativesSize; i++) {
            newAhpResult[i] = h.Pi(i);
        }
        return newAhpResult;
    }

}
