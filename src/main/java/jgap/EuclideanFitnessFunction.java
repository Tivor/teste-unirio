package jgap;

import jahp.adt.Hierarchy;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.jgap.IChromosome;

/**
 * Created by igor.custodio on 06/01/2016.
 */
public class EuclideanFitnessFunction extends AHPFitnessFunction {

    public EuclideanFitnessFunction(double[] originalData, Hierarchy h) {

        super(originalData,h);

    }


    @Override
    protected double evaluate(IChromosome iChromosome) {

        double[] newAhpResult = runAHP(iChromosome);

        double distance = new EuclideanDistance().compute(this.original, newAhpResult);

        return distance == 0.0d ? Double.MAX_VALUE : 1/distance;

    }
}
