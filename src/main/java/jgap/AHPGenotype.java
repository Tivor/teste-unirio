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

import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.audit.IEvolutionMonitor;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Genotypes are fixed-length populations of chromosomes. As an instance of
 * a Genotype is evolved, all of its Chromosomes are also evolved. A Genotype
 * may be constructed normally via constructor, or the static
 * randomInitialGenotype() method can be used to generate a Genotype with a
 * randomized Chromosome population.
 * <p/>
 * Please note that among all created Genotype instances there may only be one
 * configuration, used by all Genotype instances.
 *
 * @author Neil Rotstan
 * @author Klaus Meffert
 * @since 1.0
 */
public class AHPGenotype extends Genotype implements Serializable, Runnable {

    /**
     * Constructs a new Genotype instance with the given array of
     * Chromosomes and the given active Configuration instance. Note
     * that the Configuration object must be in a valid state
     * when this method is invoked, or a InvalidconfigurationException
     * will be thrown.
     *
     * @param a_configuration the Configuration object to use
     * @param a_population    the Chromosome population to be managed by this
     *                        Genotype instance
     * @throws org.jgap.InvalidConfigurationException
     * @author Neil Rotstan
     * @author Klaus Meffert
     * @since 2.0
     */
    public AHPGenotype(Configuration a_configuration, Population a_population)
            throws InvalidConfigurationException {

        super(a_configuration, a_population);

    }

    /**
     * Convenience method that returns a newly constructed Genotype
     * instance configured according to the given Configuration instance.
     * The population of Chromosomes will be created according to the setup of
     * the sample Chromosome in the Configuration object, but the gene values
     * (alleles) will be set to random legal values.
     *
     * @param a_configuration the current active Configuration object
     * @return a newly constructed Genotype instance
     * @throws IllegalArgumentException      if the given Configuration object is null
     * @throws org.jgap.InvalidConfigurationException if the given Configuration
     *                                       instance is not in a valid state
     * @author Neil Rotstan
     * @author Klaus Meffert
     * @since 2.3
     */
    public static Genotype randomInitialGenotype(Configuration a_configuration) throws InvalidConfigurationException {
        if (a_configuration == null) {
            throw new IllegalArgumentException(
                    "The Configuration instance may not be null.");
        }
        a_configuration.lockSettings();
        // Create an array of chromosomes equal to the desired size in the
        // active Configuration and then populate that array with Chromosome
        // instances constructed according to the setup in the sample
        // Chromosome, but with random gene values (alleles). The Chromosome
        // class randomInitialChromosome() method will take care of that for
        // us.
        // ------------------------------------------------------------------
        int populationSize = a_configuration.getPopulationSize();
        Population pop = new Population(a_configuration, populationSize);
        // Do randomized initialization.
        // -----------------------------
        Genotype result = new AHPGenotype(a_configuration, pop);
        result.fillPopulation(populationSize);
        return result;
    }

    /**
     * Evolves this genotype until the given monitor asks to quit the evolution
     * cycle.
     *
     * @param a_monitor the monitor used to decide when to stop evolution
     * @return messages of the registered evolution monitor. May indicate why the
     * evolution was asked to be stopped. May be empty, depending on the
     * implementation of the used monitor
     * @author Klaus Meffert
     * @since 3.4.4
     */
    public List<String> evolve(IEvolutionMonitor a_monitor, long a_numberOfEvolutions) {
        a_monitor.start(getConfiguration());
        List<String> messages = new Vector();

        long iterations = 0;

        do {
//            System.out.println("ITER: " + iterations);
            getConfiguration();
            evolve();
            boolean goon = a_monitor.nextCycle(getPopulation(), messages);
            if (!goon) {
                break;
            }
        } while (iterations++ < a_numberOfEvolutions);
        return messages;
    }

}
