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

import org.jgap.*;
import org.jgap.event.EventManager;
import org.jgap.impl.*;
import org.jgap.util.ICloneable;

/**
 * The DefaultConfiguration class simplifies the JGAP configuration
 * process by providing default configuration values for many of the
 * configuration settings. The following values must still be provided:
 * the sample Chromosome, population size, and desired fitness function.
 * All other settings may also be changed in the normal fashion for
 * those who wish to specify other custom values.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class AHPConfiguration extends Configuration implements ICloneable {

    /**
     * Constructs a new DefaultConfiguration instance with a number of
     * configuration settings set to default values. It is still necessary
     * to set the sample Chromosome, population size, and desired fitness
     * function. Other settings may optionally be altered as desired.
     *
     * @param a_id   unique id for the configuration within the current thread
     * @param a_name informative name of the configuration, may be null
     * @author Neil Rotstan
     * @author Klaus Meffert
     * @since 1.0
     */
    public AHPConfiguration(int chromosomeSize, String a_id, String a_name, double[][] originalIndividualData) {
        super(a_id, a_name);
        try {


            fill(chromosomeSize);

            // Set the fitness function we want to use, which is our
            // MinimizingMakeChangeFitnessFunction. We construct it with
            // the target amount of change passed in to this method.
            // ---------------------------------------------------------
            FitnessFunction myFunc = new EuclideanFitnessFunction(originalIndividualData);
            setFitnessFunction(myFunc);

        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(
                    "Fatal error: AHPConfiguration class could not use its "
                            + "own stock configuration values. This should never happen. "
                            + "Please report this as a bug to the JGAP team.", e);
        }
    }

    public AHPConfiguration(int chromosomeSize, String a_id, String a_name, double[] originalData) {
        super(a_id, a_name);
        try {


            fill(chromosomeSize);

            // Set the fitness function we want to use, which is our
            // MinimizingMakeChangeFitnessFunction. We construct it with
            // the target amount of change passed in to this method.
            // ---------------------------------------------------------
            FitnessFunction myFunc = new EuclideanFitnessFunction(originalData);
            setFitnessFunction(myFunc);

        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(
                    "Fatal error: AHPConfiguration class could not use its "
                            + "own stock configuration values. This should never happen. "
                            + "Please report this as a bug to the JGAP team.", e);
        }
    }

    private void fill(int chromosomeSize) throws InvalidConfigurationException {
        setBreeder(new GABreeder());
        setRandomGenerator(new GaussianRandomGenerator());
        setEventManager(new EventManager());
        BestChromosomesSelector bestChromsSelector = new BestChromosomesSelector(this, 0.90d);
        addNaturalSelector(bestChromsSelector, false);
        setMinimumPopSizePercent(0);
        setAlwaysCaculateFitness(false);
        //
        setSelectFromPrevGen(1.0d);
        setKeepPopulationSizeConstant(true);

        setFitnessEvaluator(new DefaultFitnessEvaluator());
        setChromosomePool(new ChromosomePool());
        addGeneticOperator(new CrossoverOperator(this, 0.35d, false, false));
        addGeneticOperator(new MutationOperator(this, 20));


        // Care that the fittest individual of the current population is
        // always taken to the next generation.
        // Consider: With that, the pop. size may exceed its original
        // size by one sometimes!
        // -------------------------------------------------------------
        setPreservFittestIndividual(true);

        // Now we need to tell the Configuration object how we want our
        // Chromosomes to be setup. We do that by actually creating a
        // sample Chromosome and then setting it on the Configuration
        // object. As mentioned earlier, we want our Chromosomes to each
        // have four genes, one for each of the coin types. We want the
        // values (alleles) of those genes to be integers, which represent
        // how many coins of that type we have. We therefore use the
        // IntegerGene class to represent each of the genes. That class
        // also lets us specify a lower and upper bound, which we set
        // to sensible values for each coin type.
        // --------------------------------------------------------------
        Gene[] sampleGenes = new Gene[chromosomeSize];

        for (int i = 0; i < chromosomeSize; i++) {
//                sampleGenes[i] = new DoubleGene(this, 0, Double.MAX_VALUE);
            sampleGenes[i] = new IntegerGene(this, 1, Integer.valueOf(9));
        }

        IChromosome sampleChromosome = new Chromosome(this, sampleGenes);
        setSampleChromosome(sampleChromosome);
        // Finally, we need to tell the Configuration object how many
        // Chromosomes we want in our population. The more Chromosomes,
        // the larger number of potential solutions (which is good for
        // finding the answer), but the longer it will take to evolve
        // the population (which could be seen as bad).
        // ------------------------------------------------------------
        setPopulationSize(50);
    }

    /**
     * @return deep clone of this instance
     * @author Klaus Meffert
     * @since 3.2
     */
    public Object clone() {
        return super.clone();
    }
}
