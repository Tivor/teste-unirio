package jgap;

import jahp.adt.Hierarchy;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.FastMath;
import org.jgap.IChromosome;

/**
 * Created by igor.custodio on 06/01/2016.
 */
public class EuclideanFitnessFunction extends AHPFitnessFunction {

    boolean extended = false;

    public EuclideanFitnessFunction(double[] originalData) {

        super(originalData);

    }

    public EuclideanFitnessFunction(double[][] originalIndividualData) {
        super(originalIndividualData);
        this.extended = true;
    }

    @Override
    protected double evaluate(IChromosome iChromosome) {
        return this.extended ? evaluateExt(iChromosome) : evaluateSimple(iChromosome);
    }

    private double evaluateSimple(IChromosome iChromosome) {

        double[] newAhpResult = runAHP(iChromosome);
        double distance = distance(this.original, newAhpResult, this.crossValidationAlternative);
        return distance == 0.0d ? Double.MAX_VALUE : 1/distance;
    }

    private double evaluateExt(IChromosome iChromosome) {

        double[][] newFullAhpResult = runCriteriaAHP(iChromosome);

        double sum = 0.0d;

        int sonsSize = h.getGoal().getSonsSize();
        double[] distance = new double[sonsSize];

        for (int i = 0; i < sonsSize; i++) {
            /*double*/distance[i] = distance(this.individualOriginal[i], newFullAhpResult[i], this.crossValidationAlternative);
            sum += distance[i];
        }

//        return distance(d
// istance);
        double v = 1 / sum;
        return v;

    }


    public static double distance(double[] p1, double[] p2,int crossValidationAlternative) {
        double sum = 0.0D;

        int correctIndex = 0;
        for(int i = 0; i < p1.length; ++i) {

            if (i != crossValidationAlternative) {
                double dp = p1[i] - p2[correctIndex++];
                sum += dp * dp;
            }
        }

        return FastMath.sqrt(sum);
    }

    public static double distance(double[] p1) {

        double sum = 0.0d;

        for(int i = 0; i < p1.length; ++i) {
            double dp = p1[i] - 0.01d;
            sum += dp * dp;
        }

        double sqrt = FastMath.sqrt(sum);
        return sqrt == 0.0d ? Double.MAX_VALUE : 1 / sqrt;
    }
}
