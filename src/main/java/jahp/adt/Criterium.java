// Abstract Data Type
package jahp.adt;

//imports

import java.io.Serializable;
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

    // The associated PairwiseComparisonMatrix
    private PairwiseComparisonMatrix p;

    /**
     *
     * As duas proximas variaveis resolvem o problema de feature zero-value
     *
     */

    private int pos;
    private int[][] featValues;
    public void setFeatValues(int[][] featValues) {
        this.featValues = featValues;
    }

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
    public double Jstar(int index) {
        if (isLowestLevel()) return J(index);
        double sum = 0.0;
        for (int i = 0; i < getSonsSize(); i++) {
            Criterium son = getSons().get(i);
            sum += son.Jstar(index) * p.getWeight(i);
        }
        return sum;
    }

    /**
     * <code>Jstar</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium
     */
    public double JstarFull(int index) {
        if (isLowestLevel()) return JFull(index);

        double sum = 0.0;
        for (int i = 0; i < getSonsSize(); i++) {
            Criterium son = getSons().get(i);
            sum += son.JstarFull(index) * p.getWeight(i);
        }
        return sum;
    }

    /**
     * <code>Jstar</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium
     */
    public double JstarFull(int index, int c) {
        if (isLowestLevel()) return JFull(index);

        double sum = 0.0;
        Criterium son = getSons().get(c);
        sum += son.JstarFull(index) * 1/*p.getWeight(c)*/;
        return sum;
    }

    /**
     * <code>Jstar</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium
     */
    public double Jstar(int index, int c) {
        if (isLowestLevel()) return J(index);
        Criterium son = getSons().get(c);
        return son.Jstar(index) * 1/*p.getWeight(c)*/;
    }

    /**
     * <code>J</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium in the lowest level
     * @throws IllegalArgumentException
     */
    public double J(int index) {

        int[][] actualFeatValues = getFather().getFather().featValues;

        int realIndex = actualFeatValues[this.pos][index];

        return (realIndex < 0) ? 0.0d : p.getWeight(realIndex);
    }

    /**
     * <code>J</code> method here.
     *
     * @param index the index of the alternative
     * @return double the global importance of the alternative according to the criterium in the lowest level
     * @throws IllegalArgumentException
     */
    public double JFull(int index) {

//        System.out.println("Para feature " + (this.pos + 1) + ", na alternativa " + index + " estou usando:");

        int[][] actualFeatValues = getFather().getFather().featValues;
        int realIndex = actualFeatValues[this.pos][index];

        if (realIndex == -1) {
//            System.out.println("zero pois realindex == -1");
            return 0.0d;
        } else {
            int dummyIndex = actualFeatValues[this.pos][actualFeatValues[this.pos].length - 1];

            if ((realIndex == -2) || (index == dummyIndex)) {
//                System.out.println("dummy = " + dummyIndex + " realindex == " + realIndex);
                return dummyIndex >= 0 ? p.getWeight(dummyIndex) : 0.0d;
            } else {
                if ((index < dummyIndex) || (dummyIndex == -3)) {
//                    System.out.println("realIndex = " + realIndex + ", pois dummyIndex == " + dummyIndex);
                    return p.getWeight(realIndex);
                } else {
                    try {
//                        System.out.println("realIndex + 1 = " + (realIndex + 1));
                        return p.getWeight(realIndex + 1);
                    }catch (ArrayIndexOutOfBoundsException e) {
//                        System.out.println(">>>>>>" + pos + "<<<<>>>>" + index);
                        return 0.0d;
                    }
                }
            }
        }
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
