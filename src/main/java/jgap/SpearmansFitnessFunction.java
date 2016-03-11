/*
 * This file is part of JGAP.
 *
 * JGAP offers a dual license model containing the LGPL as well as the MPL.
 *
 * For licensing information please see the file license.txt included with JGAP
 * or have a look at the top of class org.jgap.Chromosome which representatively
 * includes the JGAP license policy applicable for any file delivered with JGAP.
 */
package jgap;

import jahp.adt.Hierarchy;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jgap.IChromosome;

/**
 * Sample fitness function for the MakeChange example.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class SpearmansFitnessFunction extends AHPFitnessFunction {

    public SpearmansFitnessFunction(double[][] originalIndividualData, Hierarchy h) {

        super(originalIndividualData);

    }

    /**
     * Determine the fitness of the given Chromosome instance. The higher the
     * return value, the more fit the instance. This method should always
     * return the same fitness value for two equivalent Chromosome instances.
     *
     * @param a_subject the Chromosome instance to evaluate
     * @return positive double reflecting the fitness rating of the given
     * Chromosome
     * @author Neil Rotstan, Klaus Meffert, John Serri
     * @since 2.0 (until 1.1: return type int)
     */
    @Override
    public double evaluate(IChromosome a_subject) {

        double[] newAhpResult = runAHP(a_subject);

//        double correlation = new SpearmansCorrelation().correlation(this.original, newAhpResult);
        double correlation = new PearsonsCorrelation().correlation(this.original, newAhpResult);
//        System.out.println(correlation);
        return correlation < 0.0d ? (correlation * -1) : correlation;

    }

}
