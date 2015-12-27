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

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 * Sample fitness function for the MakeChange example.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class SpearmansFitnessFunction extends FitnessFunction {

    private double[] original;

    public void setOriginal(double[] originalData) {

        this.original = original;

    }

    private final double m_targetValue = 1.0d;



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
    public double evaluate(IChromosome a_subject) {

        double fitness = 0.0d;



        Gene[] genes = a_subject.getGenes();

        int length = genes.length;
        double[] generated = new double[length];

        for (int i = 0; i < length; i++) {

        }

        new SpearmansCorrelation().correlation(original,englishList);


        return fitness;
    }

}
