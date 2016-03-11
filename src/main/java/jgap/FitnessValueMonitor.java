package jgap;

import org.jgap.*;
import org.jgap.audit.IEvolutionMonitor;

import java.util.List;

/**
 * Monitors the evolution and stops it if evolution does not make a progress
 * as desired.
 *
 * @author Igor
 */
public class FitnessValueMonitor implements IEvolutionMonitor {

    private double m_FitnessEnough;
    private double m_lastFitness = 0.0d;


    /**
     * Constructor.
     *
     * @param a_FitnessEnough number of fitness units the current best
     *                        solution evolved is better than the best solution from the previously check
     * @author Klaus Meffert
     * @since 3.4.4
     */
    public FitnessValueMonitor(double a_FitnessEnough) {
        m_FitnessEnough = a_FitnessEnough;
    }

    /**
     * Called after another evolution cycle has been executed.
     *
     * @param a_pop      the currently evolved population
     * @param a_messages the monitor can append messages here to indicate why
     *                   it asks evolution to stop
     * @return true: continue with the evolution; false: stop evolution
     * @author Klaus Meffert
     * @since 3.4.4
     */
    public boolean nextCycle(Population a_pop, List<String> a_messages) {

        // Let's verify the progress since our last check.
        // -----------------------------------------------
        IChromosome best = a_pop.determineFittestChromosome();
        if (best != null) {

            // Is the current best solution better than the previous one?
            // ----------------------------------------------------------
            double value = 1 / Math.abs(best.getFitnessValue());
            if (m_lastFitness != value) {
                m_lastFitness = value;
//                System.out.println(value);
            }

            if (value < m_FitnessEnough) {
                // Bad luck, not enough progress.
                // ------------------------------
                a_messages.add("Thats enough");
                return false;
            }

        }


        // No check needed yet.
        // --------------------
        return true;
    }

    /**
     * Called just before the evolution starts.
     *
     * @param a_config the configuration used
     * @author Igor
     * @since 3.4.4
     */
    public void start(Configuration a_config) {}
}
