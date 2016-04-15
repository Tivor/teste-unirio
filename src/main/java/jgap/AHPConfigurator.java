package jgap;

import org.jgap.Configuration;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;

/**
 * Created by Igor on 20/01/2016.
 */
public class AHPConfigurator {

    private Configuration configuration;
    private double[] originalRank;

    public AHPFitnessFunction getFitnessFunction() {
        return (AHPFitnessFunction)this.configuration.getFitnessFunction();
    }

    public double[] getOriginalRank() {
        return originalRank;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void createConfiguration(int chromosomeSize, double[] originalRank, double[][] originalIndividualData, String id)  {
        Configuration conf = new AHPConfiguration(chromosomeSize, id, id.toUpperCase(), originalIndividualData);
        this.configuration = conf;
        this.originalRank = originalRank;
        // Create random initial population of Chromosomes.
        // Here we try to read in a previous run via XMLManager.readFile(..)
        // for demonstration purpose only!
        // -----------------------------------------------------------------


    }

    public void createConfiguration(int chromosomeSize, double[] originalRank, double[] originalData, String id)  {
        Configuration conf = new AHPConfiguration(chromosomeSize, id, id.toUpperCase(), originalData);
        this.configuration = conf;
        this.originalRank = originalRank;
        // Create random initial population of Chromosomes.
        // Here we try to read in a previous run via XMLManager.readFile(..)
        // for demonstration purpose only!
        // -----------------------------------------------------------------


    }

    public Genotype createPopulation() throws InvalidConfigurationException {

        Genotype population;
        // Now we initialize the population randomly, anyway (as an example only)!
        // If you want to load previous results from file, remove the next line!
        // -----------------------------------------------------------------------
        population = Genotype.randomInitialGenotype(this.configuration);

        return population;

    }
}
