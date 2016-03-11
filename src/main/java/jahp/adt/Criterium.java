// Abstract Data Type
package jahp.adt;

//imports

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.


/**
 * Criterium class
 *
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A>
 * @version July 21, 2003
 */
public class Criterium extends Activity implements Serializable, Cloneable {

    private boolean goal;
    private boolean lowestLevel;

    private Vector sons;

    private int pos;

    // The associated PairwiseComparisonMatrix
    private PairwiseComparisonMatrix p;

    /**
     * Gets the value of p
     *
     * @return the value of p
     */
    public PairwiseComparisonMatrix getP() {
        return this.p;
    }

    public boolean isGoal() {
        return this.goal;
    }

    public boolean isLowestLevel() {
        return this.lowestLevel;
    }

    /**
     * Get the value of sons
     *
     * @return value of sons
     */
    public Vector<Criterium> getSons() {
        return (Vector<Criterium>) this.sons;
    }

    /**
     * Get the value of sons
     *
     * @return value of sons
     */
    public Vector<Alternative> getAlternatives() {
        return (Vector<Alternative>) this.sons;
    }

    /**
     * <code>print</code> Returns a string representation of this Criterium, containing the String representation of each element.
     * Useful to debug
     *
     * @return a <code>String</code> value
     */
    public String print() {
        String s = new String();

        s = s + "Name of this criterium                  : " + getName() + "\n";
        s = s + "Is this criterium in the lowest level ? : " + isLowestLevel() + "\n";
        s = s + "Is this criterium a goal ?              : " + isGoal() + "\n";
        if (!isGoal())
            s = s + "The name of his father                  : " + getFather().getName() + "\n";    //     getFather();
        s = s + "Number of sons                            : " + getSonsSize() + "\n";
        s = s + "PairwiseComparisonMatrix                  : " + "\n" + getP().toString();
        s = s + "sons                  : \n";

        Vector v = getSons();

        if (isLowestLevel()) { // a vector of alternatives
            for (Alternative alt : getAlternatives()) {
                s = s + alt.print();
            }
        } else {// a vector of criteria
            for (Criterium c : getSons()) {
                s = s + c.print();
            }

        }
        return s;
    }

    /**
     * <code>getSubcriteriumAt</code> Returns the ith subcriterium
     *
     * @param a <code>int</code> value : the index
     * @return a <code>Criterium</code> value
     * @throws IllegalArgumentException e "not found..."
     */
    public Criterium getSubcriteriumAt(int i) {
        if (this.isLowestLevel())
            throw new IllegalArgumentException("The ith subcriterium of a criterium in the least level can not be found");
        return getSons().elementAt(i);
    }

    /**
     * <code>getSubcriteriumAt</code> Returns the ith subcriterium
     *
     * @param a <code>Criterium</code> value
     * @return a <code>int</code> value : the index
     * @throws IllegalArgumentException e  "not found..."
     */
    public int getIndexOfSubcriterium(Criterium c) {
        if (this.isLowestLevel())
            throw new IllegalArgumentException("The ith subcriterium of a criterium in the least level can not be found");
        return getSons().indexOf(c);
    }

    /**
     * <code>getAlternativeAt</code> Returns the ith Alternative
     *
     * @param a <code>int</code> value : the index
     * @return a <code>Alternative</code> value
     * @throws IllegalArgumentException e  "nt found..."
     */
    public Alternative getAlternativeAt(int i) {
        if (!this.isLowestLevel())
            throw new IllegalArgumentException("The ith alternative of a criterium not in the least level can not be found");
        return getAlternatives().elementAt(i);
    }

    /**
     * <code>getAlternativeAt</code> Returns the ith subcriterium
     *
     * @param a <code>Alternative</code> value
     * @return a <code>int</code> value : the index
     * @throws IllegalArgumentException e "not found..."
     */
    public int getIndexOfAlternative(Alternative a) {
        if (this.isLowestLevel())
            throw new IllegalArgumentException("The ith alternative of a criterium not in the least level can not be found");
        return getAlternatives().indexOf(a);
    }


    /**
     * Creates a new  <code>Criterium</code> instance.
     */
    public Criterium(String name, boolean lowestLevel, Criterium father, int pos) {
        this.name = name;
        this.goal = false;
        this.lowestLevel = lowestLevel;
        this.father = father;
        this.pos = pos;
    }

    /**
     * Creates a new  <code>Criterium</code> instance.
     */
    public Criterium() {
        this.name = "Best choice";
        this.goal = true;
        this.lowestLevel = false;
        this.father = null;
    }

    public void createPCM(Vector<? extends Activity> criteria) {
        this.sons = criteria;
        this.p = new PairwiseComparisonMatrix(criteria.size());
    }

    /**
     * <code>addSubcriterium</code> method here.
     *
     * @param Criterium c which is the father
     * @param Criterium subc which should be added
     */
    public void createPCM(Vector<? extends Activity> criteria, double[] weights) {

        this.sons = criteria;
        this.p = new PairwiseComparisonMatrix(weights);

    }

    /**
     * <code>addSubcriterium</code> method here.
     *
     * @param Criterium c which is the father
     * @param Criterium subc which should be added
     */
    public void updatePCM(double[] weights) {
        this.p = new PairwiseComparisonMatrix(weights);
    }

    /**
     * <code>equals</code> method here.
     *
     * @param Criterium c
     * @return boolean
     */
    public boolean equals(Criterium c) {
        return name.equals(c.getName());
    }

    //************************************
    //
    //Method to calculate solutions
    //
    //
    //*************************************

    /**
     * <code>Jstar</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium
     */
    public double Jstar(int index, int c, boolean individual) {

        if (isLowestLevel()) {

            double[][] featValAlts = {{1,1,1},{1,1,1},{1,1,1},{1,1,1},{0,1,1}};

            try{
                return (featValAlts[this.pos][index] == 0) ? 0 : J(index, c);
            } catch (ArrayIndexOutOfBoundsException e) {
                return 0;
            }

        }

        if (individual) {
            Criterium son = getSons().get(c);
            return son.Jstar(index, c, individual) * p.getWeight(c);
        } else {
            double sum = 0.0;
            for (int i = 0; i < getSonsSize(); i++) {
                Criterium son = getSons().get(i);
                sum += son.Jstar(index, i, individual) * p.getWeight(i);
            }
            return sum;
        }

    }

    /**
     * <code>J</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium in the lowest level
     * @throws IllegalArgumentException
     */
    public double J(int index, int c) {
        if (!isLowestLevel())
            throw new IllegalArgumentException("J can not be calculated for a criterium which is not in the lowest level");
        return p.getWeight(index);
    }

    /**
     * <code>I</code> method here.
     *
     * @param Criterium c :  the index of the alternative
     * @return double  : the value of the alternative according to the criterium
     */
    public double I(Criterium c) {
        if (isLowestLevel()) return (0.0);
        for (int i = 0; i < getSonsSize(); i++) {
            Criterium son = getSons().get(i);
            if (son.equals(c)) return p.getWeight(i);
        }
        double sum = 0.0;
        for (int i = 0; i < getSonsSize(); i++) {
            Criterium son = getSons().get(i);
            sum += p.getWeight(i) * son.I(c);
        }
        return (sum);
    }

    public int getSonsSize() {
        return this.getSons().size();
    }
}
