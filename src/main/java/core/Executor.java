package core;

import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import jgap.AHPConfigurator;
import jgap.AHPGenotype;
import jgap.FitnessValueMonitor;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.audit.IEvolutionMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Executor {

    private static final int MAX_ALLOWED_EVOLUTIONS = 1;

    private AHPConfigurator ahpConfigurator = new AHPConfigurator();
    private Reader reader = new Reader();
    private Printer printer;
    private AhpFiller ahpFiller = new AhpFiller();


    public boolean execute(String featuresFile, String alternativesFile) throws IOException, InvalidConfigurationException {
        File fileTest = new File(alternativesFile);

        if (fileTest.exists()) {

            FileReader fileAlt = new FileReader(fileTest);
            BufferedReader brAlt = new BufferedReader(fileAlt);
            String tempAlt = brAlt.readLine();

            Vector<Alternative> alternatives = reader.readAlternatives(tempAlt);

            double[] originalRank = reader.readOriginalRank(brAlt, alternatives);
            System.out.println(Arrays.toString(originalRank));

            FileReader file = new FileReader(new File(featuresFile));
            BufferedReader brCriteria = new BufferedReader(file);

            String featuresStr = brCriteria.readLine();
            int chromosomeSize = featuresStr.split(",").length;

            String[] tempCrit = brCriteria.readLine().split(",");

            printer = new Printer(reader.readIndividualRanks(alternatives.size(), brCriteria, tempCrit.length));

            double[][] originalIndividualData = printer.individualRanks();

//            originalRank = calculateAvg(originalIndividualData);
            ahpConfigurator.createConfiguration(chromosomeSize, originalRank, originalIndividualData);
//            ahpConfigurator.createConfiguration(chromosomeSize, originalRank);

            Map<String, String> mapeamento = new HashMap();
            Map<String, String> valoresFeatures = new HashMap();

            Hierarchy hierarchyTest = new Hierarchy(alternatives);
            boolean testCreated = false;

            for (int i = 0; i < alternatives.size(); i++) {

                System.out.println("------------------------------------------------------------------------------------------------------------------------");
                System.out.println("--------------------------------Produto: " + alternatives.get(i).getName() + "------------------------------------------");

                executeCrossValidation(brAlt, alternatives, brCriteria, tempCrit, ahpConfigurator, i, hierarchyTest, testCreated, mapeamento, valoresFeatures);
                testCreated = true;

            }

            return false;

        } else {
            return true;
        }
    }

    private double[] calculateAvg(double[][] originalIndividualData) {

        double[] result = new double[originalIndividualData[0].length];

        for (int i = 0; i < originalIndividualData[0].length; i++) {

            for (int k = 0; k < originalIndividualData.length; k++) {
                result[i] += originalIndividualData[k][i];
            }

            result[i] = result[i]/originalIndividualData.length;

        }

        return result;
    }

    public void executeCrossValidation(BufferedReader brAlt, Vector<Alternative> alternatives, BufferedReader brCriteria, String[] tempCrit, AHPConfigurator ahpConfigurator, int i, Hierarchy hierarchyTest, boolean testCreated, Map<String, String> mapeamento, Map<String, String> valoresFeatures) throws InvalidConfigurationException, IOException {
        Vector<Alternative> crossValidationAlternatives = (Vector) alternatives.clone();
        crossValidationAlternatives.remove(i);

        Hierarchy h = new Hierarchy(crossValidationAlternatives);

        Vector<Criterium> criteria = new Vector<Criterium>(tempCrit.length);

        Vector<Criterium> criteriaTest = null;
        if (!testCreated) {
            criteriaTest = new Vector<Criterium>(tempCrit.length);
        }
        reader.readCriteria(tempCrit, h, hierarchyTest, criteria, criteriaTest, testCreated);

        h.getGoal().createPCM(criteria);

        if (!testCreated) {
            hierarchyTest.getGoal().createPCM(criteriaTest);
        }

        ahpConfigurator.getFitnessFunction().setCrossValidationAlternative(i);
        ahpConfigurator.getFitnessFunction().setH(h);

        Genotype population = ahpConfigurator.createPopulation();
        ahpFiller.populateAHP(brAlt, crossValidationAlternatives, alternatives, brCriteria, criteria, criteriaTest, testCreated, true, population, i, mapeamento, valoresFeatures);

//        IEvolutionMonitor monitor = new FitnessValueMonitor(0.01d);
//        ((AHPGenotype) population).evolve(monitor, MAX_ALLOWED_EVOLUTIONS);
        population.evolve(MAX_ALLOWED_EVOLUTIONS);

//        System.out.println(h.print());
        double[] bestAhpResultComplete = printer.printFittestResult(hierarchyTest, population);

        printer.printCompleteResult(ahpConfigurator, bestAhpResultComplete);

        for (int k = 0; k < hierarchyTest.getGoal().getSonsSize(); k++) {

            int alternativesSize = hierarchyTest.getAlternativesSize();
            double[] newAhpResult = new double[alternativesSize];
            double interSum = 0.0d;
            for (int j = 0; j < alternativesSize; j++) {
                double pi = hierarchyTest.Pi(j, k);
                newAhpResult[j] = pi;
                interSum += pi;
            }

            for (int j = 0; j < alternativesSize; j++) {
                newAhpResult[j] = newAhpResult[j] / interSum;
            }

            printer.printIndividualResult(k, newAhpResult);

        }

    }

}