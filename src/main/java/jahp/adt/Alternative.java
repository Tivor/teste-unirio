// Abstract Data Type
package jahp.adt;


//imports

import java.io.Serializable;

//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.


/**
 * Alternative class
 *
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A>
 * @version July 21, 2003
 */
public class Alternative extends Activity implements Serializable, Cloneable {

    /**
     * Creates a new  <code>Alternative</code> instance.
     */
    public Alternative(String name) {
        this.name = name;
    }

    /**
     * <code>print</code> Returns a string representation of this Alternative, containing the String representation of each element.
     *
     * @return a <code>String</code> value
     */
    public String print() {
        String s = new String();
        s = s + "Name of this alternative                  : " + getName() + "\n";
        return s;
    }

    //************************************
    //
    //Method to calculate solutions
    //
    //
    //*************************************

//    /**
//     * <code>S</code> method here.
//     *
//     * @param Criterium c :  the index of the alternative
//     * @param Hierarchy h :  the reference hierarchy
//     * @return double  : the influence of the criterium on the alternative according to the hierarchy
//     * @throws IllegalArgumentException
//     */
//    public double S(Criterium c, Hierarchy h) {
//        if (c.equals(h.getGoal()))
//            throw new IllegalArgumentException("the influence of the goal on the alternative according to the hierarchy can not be calculated");
//        int index_alt = 0;
//        Vector alts = h.getAlternatives();
//        for (int i = 0; i < h.getAlternativesSize(); i++) {
//            Alternative alt = (Alternative) alts.get(i);
//            if (this.equals(alt)) index_alt = i;
//        }
//        return (c.Jstar(index_alt) * h.V(c) / h.Pi(index_alt));
//    }

}


  

