// Abstract Data Type
package jahp.adt;


//imports

import java.io.Serializable;

//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.


/**
 * Activity class
 *
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A>
 * @version July 21, 2003
 */
public abstract class Activity implements Serializable, Cloneable {

    //ATTRIBUTS
    protected String name;

    //link with other Objects
    protected Criterium father;

    /**
     * Get the value of name.
     *
     * @return value of name.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Criterium getFather() {
        return father;
    }

    public void setFather(Criterium father) {
        this.father = father;
    }

}
