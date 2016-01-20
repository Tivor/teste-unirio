package jgap;

import jahp.adt.Hierarchy;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.commons.math3.util.FastMath;
import org.jgap.IChromosome;

/**
 * Created by igor.custodio on 06/01/2016.
 */
public class EuclideanFitnessFunction extends AHPFitnessFunction {

    public EuclideanFitnessFunction(double[] originalData) {

        super(originalData);

    }

    @Override
    protected double evaluate(IChromosome iChromosome) {

        double[] newAhpResult = runAHP(iChromosome);

        double distance = distance(this.original, newAhpResult, this.crossValidationAlternative);

        return distance == 0.0d ? Double.MAX_VALUE : 1/distance;

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
}
