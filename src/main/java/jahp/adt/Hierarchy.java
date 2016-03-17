// Abstract Data Type
package jahp.adt;

//imports

import java.io.Serializable;
import java.util.Vector;

//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.


/**
 * Decision <code>Hierarchy</code> class
 *
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A>
 * @version March 24, 2003  final one
 */
public class Hierarchy implements Cloneable, Serializable {


    // ATTRBUTS
    private Criterium goal;
    private Vector<Alternative> alternatives;

    /**
     * Gets the value of goal
     *
     * @return the value of goal
     */
    public Criterium getGoal() {
        return this.goal;
    }

    /**
     * Gets the value of alternatives
     *
     * @return the value of alternatives
     */
    public Vector<Alternative> getAlternatives() {
        return this.alternatives;
    }

    /**
     * Gets the value of nb_alternatives
     *
     * @return the value of nb_alternatives
     */
    public int getAlternativesSize() {
        return this.getAlternatives().size();
    }

    /**
     * Creates a new  <code>Hierarchy</code> instance.
     * a minimal hierarchy is composed by 1 goal : "My goal" and 2 alternatives : "Alternative 1" and "Alternative 2"
     */
    public Hierarchy(Vector<Alternative> alternatives) {

        this.alternatives = alternatives;
        this.goal = new Criterium();

    }

    public void setAlternatives(Vector<Alternative> alternatives) {
        this.alternatives = alternatives;
    }

    /**
     * <code>print</code> Returns a string representation of this Hierarchy, containing the String representation of each elements to debug.
     *
     * @return a <code>String</code> value
     */
    public String print() {
        String s = new String();
        s = "Nb of alternatives          : " + getAlternativesSize() + "\n";
        s = s + "Alternatives of hierarchy : \n";
        Vector v = new Vector();
        v = getAlternatives();
        for (int i = 0; i < getAlternativesSize(); i++) {
            s = s + "Alternative " + i + "\n";
            Alternative alt = (Alternative) v.get(i);
            alt.print();
        }
        s = s + "Goal of the hierarchy       : \n" + getGoal().print();
        return s;
    }


    /**
     * <code>toString</code> Returns a short string representation of this Hierarchy
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        String s = new String();
        s = "Goal of the hierarchy       : \n" + getGoal().toString();
        s = s + "Nb of alternatives          : " + getAlternativesSize() + "\n";
        return s;
    }

    /**
     * <code>Pi</code> method here.
     *
     * @param i :  the index of the alternative
     * @return double  : the value of the alternative according to the hierarchy
     */
    public double Pi(int i) {
        return goal.Jstar(i);
    }

    /**
     * <code>Pi</code> method here.
     *
     * @param i :  the index of the alternative
     * @return double  : the value of the alternative according to the hierarchy
     */
    public double PiFull(int i) {
        return goal.JstarFull(i);
    }

    /**
     * <code>Pi</code> method here.
     *
     * @param i :  the index of the alternative
     * @return double  : the value of the alternative according to the hierarchy
     */
    public double Pi(int i, int c) {
        return goal.Jstar(i, c);
    }

    /**
     * <code>Pi</code> method here.
     *
     * @param i :  the index of the alternative
     * @return double  : the value of the alternative according to the hierarchy
     */
    public double PiFull(int i, int c) {
        return goal.JstarFull(i, c);
    }

    /**
     * <code>V</code> method here.
     *
     * @param Criterium c according to the hierarchy is evaluated
     * @return IllegalArgumentException
     */
    public double V(Criterium c) {
        if (goal.equals(c))
            throw new IllegalArgumentException("the value of the criterium according to the hierarchy can not be calculated");
        return (goal.I(c));
    }

}


  

