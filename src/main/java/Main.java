import jahp.adt.Alternative;
import jahp.adt.Criterium;
import jahp.adt.Hierarchy;
import model.Product;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {

    public static void main(String[] args) {

        int initCap = 10;
        int alternativeToEvaluate = 0;

        List allAlternatives = new ArrayList(initCap);

        for (int i = 0; i < initCap; i++) {

            List currentAlternatives = new ArrayList(initCap - 1);
            currentAlternatives.addAll(allAlternatives);
            currentAlternatives.remove(alternativeToEvaluate);

            Hierarchy h = getHierarchyExample(currentAlternatives);


            alternativeToEvaluate++;
        }







    }

    private static Hierarchy getHierarchyExample(List<Alternative> alternatives) {
        // build a tested hierarchy H={G,A1,A2}
        Hierarchy h = new Hierarchy();
        //System.out.println("\t \t Hierarchy h:\n"+h.toString());

        //build and add a third  alternative A3
        Alternative alt=new Alternative();
        alt.setName("Alternative 3");
        //alt.setComment("The third alternative");
        try{
            alt.setUrl(new URL("http://messel.emse.fr/~mmorge/3alternative.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //System.out.println("\t \t Alternative alt:\n"+alt.toString());
        h.addAlternative(alt);
        //System.out.println("Hierarchy h + Alternative alt:\n"+h.toString());
        //build and add subcriteria C1,C2,C3,C4,C5,C7
        Criterium c1=new Criterium();
        //c1.setName("Criterium 1");
        //c1.setComment("The first criterium");
        try{
            c1.setUrl(new URL("http://messel.emse.fr/~mmorge/1criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(h.getGoal(),c1,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c2=new Criterium();
        c2.setName("Criterium 2");
        //c2.setComment("The second criterium");
        try{
            c2.setUrl(new URL("http://messel.emse.fr/~mmorge/2criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(h.getGoal(),c2,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c3=new Criterium();
        c3.setName("Criterium 3");
        //c3.setComment("The third criterium");
        try{
            c3.setUrl(new URL("http://messel.emse.fr/~mmorge/3criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(c1,c3,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c4=new Criterium();
        //c4.setName("Criterium 4");
        //c4.setComment("The fourth criterium");
        try{
            c4.setUrl(new URL("http://messel.emse.fr/~mmorge/4criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(c1,c4,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c5=new Criterium();
        c5.setName("Criterium 5");
        //c5.setComment("The fifth criterium");
        try{
            c5.setUrl(new URL("http://messel.emse.fr/~mmorge/5criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(c1,c5,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c6=new Criterium();
        c6.setName("Criterium 6");
        //c6.setComment("The sixth criterium");
        try{
            c6.setUrl(new URL("http://messel.emse.fr/~mmorge/6criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(c2,c6,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5+c6:\n"+h.toString());
        //System.out.println("************************************************************************************************\n");
        Criterium c7=new Criterium();
        c7.setName("Criterium 7");
        //c7.setComment("The seventh criterium");
        try{
            c7.setUrl(new URL("http://messel.emse.fr/~mmorge/7criterium.html"));}
        catch(MalformedURLException e){
            System.err.println("Error : MalformedURLException"+e);
            System.exit(-1);
        }
        //Every criterium should contain alternatives...
        //It's easier to addSubcriterium
        //and to calculate J(a_i|c_j); J* I I* \pi
        //I()
        h.addSubcriterium(c2,c7,h.getAlternatives(),h.getNb_alternatives());
        //h.addCriterium(c) ;
        //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5+c6+c7:\n"+h.toString());
        return h;

    }

}
