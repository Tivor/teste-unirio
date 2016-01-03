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

import cluster.input.Dataset;
import cluster.input.FeatureVector;
import cluster.validationIndices.IValidationIndice;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

/**
 * Sample fitness function for the MakeChange example.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class ClusterIndexFitnessFunction extends AHPFitnessFunction {

    public ClusterIndexFitnessFunction(double[] originalData, Hierarchy h) {

        super(originalData, h);

    }

    private IValidationIndice validationAlgorithm;

    public void setValidationAlgorithm(IValidationIndice validationAlgorithm) {
        this.validationAlgorithm = validationAlgorithm;
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

        Dataset dataset = new Dataset();
        FeatureVector featureVector;

//        System.out.println(a_subject);

        for (int i = 0; i < original.length; i++) {

            String line = "" + ((int)((original[i]*20))) + " " + ((int)((newAhpResult[i]*20))) + " 1:" + newAhpResult[i];
//            System.out.println(line);

            String[] splitLine = line.split(" ");
            featureVector = new FeatureVector(splitLine,true);
            dataset.add(featureVector);
        }

//        System.out.println("------------------------------------------");

        float v = validationAlgorithm.calculateIndex(dataset);
//        System.out.println("===>" + v);
        return v;

    }



}
