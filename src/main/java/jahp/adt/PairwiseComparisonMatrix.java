// Abstract Data Type
package jahp.adt;


//imports

import jahp.utils.Jama.EigenvalueDecomposition;
import jahp.utils.Jama.Matrix;
import org.apache.commons.math3.stat.StatUtils;

import java.io.Serializable;
import java.util.Arrays;

//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.


/**
 * PairwiseComparisonMatrix class
 *
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A>
 * @version Fev 13, 2003
 */
public class PairwiseComparisonMatrix implements Serializable, Cloneable {


    // ATTRIBUTS
    public static double EXTREMELY = 9.0;
    public static double BETWEEN_EXTREMELY_AND_VERY_STRONGLY = 8.0;
    public static double VERY_STRONGLY = 7.0;
    public static double BETWEEN_VERY_VERY_STRONGLY_AND_STRONGLY = 6.0;
    public static double STRONGLY = 5.0;
    public static double BETWEEN_STRONGLY_AND_SLIGHTLY = 4.0;
    public static double SLIGHTLY = 3.0;
    public static double BETWEEN_SLIGHTLY_EQUALLY = 2.0;
    public static double EQUALLY = 1.0;

    private Matrix A;
    private int size;

    private Matrix weightCache = null;

    /**
     * Set the value of the pairwise comparison aij between w_i and w_j
     *
     * @param i     index of the dominant activity w_i
     * @param j     index of the dominated activity w_j
     * @param value of w_i/w_j
     * @throws IllegalArgumentException
     */
    public void set(int i, int j, double value) {
        if (i >= getSize() && j >= getSize())
            throw new IllegalArgumentException("index of a single element should be like 0<=i,j<size");
        if (i == j && value != EQUALLY)
            throw new IllegalArgumentException("Elements in the diagonal of the Pairwise Comparison Matrix shoulb be EQUALLY.");
        A.set(i, j, value);
        A.set(j, i, 1.0 / value);
    }

    /**
     * Set the value of the pairwise comparison between aij=w_i
     *
     * @param i index of the dominant activity W_i
     * @param j index of the dominated activity W_j
     * @return value of W_i/W_j
     * @throws IllegalArgumentException "Out of bounded..."
     */
    public double get(int i, int j) {
        if (i >= getSize() && j >= getSize())
            throw new IllegalArgumentException("index of a single element should be like 0<=i,j<size");
        return A.get(i, j);
    }

    /**
     * Gets the value of a
     *
     * @return the value of a
     */
    public Matrix getA() {
        return this.A;
    }

    /**
     * Sets the value of a
     *
     * @param argA Value to assign to this.a
     */
    public void setA(Matrix argA) {
        this.A = argA;
    }

    /**
     * Gets the value of size
     *
     * @return the value of size
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Sets the value of size
     *
     * @param argSize Value to assign to this.size
     */
    public void setSize(int argSize) {
        this.size = argSize;
    }


    /**
     * Creates a new  <code>PairwiseComparisonMatrix</code> instance.
     */
    public PairwiseComparisonMatrix(int size) {
        this.size = size;
        InitMatrix();
    }

    /**
     * Creates a new  <code>PairwiseComparisonMatrix</code> instance.
     */
    public PairwiseComparisonMatrix(double[] weights) {
        this.size = weights.length;
        InitMatrix(weights);
    }

    /**
     * Init a new  <code>Matrix</code> instance.
     */
    private void InitMatrix() {
        A = new Matrix(size, size, 1.0);
    }

    /**
     * Init a new  <code>Matrix</code> instance.
     */
    private void InitMatrix(double[] weights) {
        A = new Matrix(size, weights);
    }

    /**
     * Print the  <code>Matrix</code>
     */
    public void print() {
        A.print(this.size, this.size);
    }


    /**
     * Get the value of the InconsistencyRatio
     *
     * @return double
     */
    public double getInconsistencyIndex() {
        //n!=1
        if (getSize() <= 1) return 0.0;
        return (getMaxEigenValue() - getSize()) / (getSize() - 1.0);
    }


    /**
     * Get the value of the InconsistencyRatio
     *
     * @return double
     */
    public double getRandomInconsistency() {
        switch (size) {
            case 0:
                return 0.00;
            case 1:
                return 0.00;
            case 2:
                return 0.00;
            case 3:
                return 0.58;
            case 4:
                return 0.90;
            case 5:
                return 1.12;
            case 6:
                return 1.24;
            case 7:
                return 1.32;
            case 8:
                return 1.41;
            case 9:
                return 1.45;
            case 10:
                return 1.49;
            default:
                return 1.5; //take care
        }

    }

    /**
     * Get the value of the InconsistencyRatio
     *
     * @return double
     */
    public double getInconsistencyRatio() {
        if (getSize() <= 2) return 0.0;
        return getInconsistencyIndex() / getRandomInconsistency();
    }


    /**
     * Check the consistency of the <code>PairwiseComparisonMatrix</code>
     *
     * @return boolean value
     */
    public boolean isConsistency() {
        if (getInconsistencyRatio() <= 0.1) return true;
        else return false;
    }


    /**
     * Get the value of the <code>max_eigen_value</code> of the matrix A.
     *
     * @return the max_eigen_value of the matrix A
     */

    public double getMaxEigenValue() {
        EigenvalueDecomposition Eig = new EigenvalueDecomposition(A);
        double[] values = Eig.getRealEigenvalues();
        double max = 0.0;

        for (int i = 0; i < this.size; i++) {
            if (values[i] >= max) max = values[i];
        }

        //System.out.println("The max eigenvalue : " + max);
        return max;

    }


    /**
     * Get the vector of weights of the <code>PairwiseComparisonMatrix</code>.
     *
     * @return Matrix
     */
    public Matrix createWeightMatrix() {


        Matrix W = new Matrix(getSize(), 1, 1.0);
        double sum = 0.00;

        for (int i = 0; i < getSize(); i++) {
            sum = 0.00;
            for (int j = 0; j < getSize(); j++) {
                sum += A.get(i, j);
            }
            try {
                W.set(i, 0, sum);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error in setting W : ArrayIndexOutOfBoundsException" + e);
            }

        }
        //normalization vector
        sum = 0.00;
        for (int i = 0; i < getSize(); i++) {
            try {
                sum += W.get(i, 0);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error in setting W : ArrayIndexOutOfBoundsException" + e);
            }
        }

        if (sum < 0.99999d) {
            System.err.println("Sum------------------------------------------->" + sum);
            System.err.println("<----------------------------------------------------");
            System.exit(-100);
        }

        for (int i = 0; i < getSize(); i++) {

            try {
                W.set(i, 0, W.get(i, 0) / sum);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Error in setting W : ArrayIndexOutOfBoundsException" + e);
            }
        }

        return W;

    }

    /**
     * Get the i element of the vector of weights of the <code>PairwiseComparisonMatrix</code>.
     *
     * @param int index
     * @return double value
     */
    public double getWeight(int i) {

        if (this.weightCache == null) {
            this.weightCache = createWeightMatrix();
        }

        return this.weightCache.get(i, 0);
    }

    /**
     * <code>toString</code> Returns a string representation of this PairwiseComparisonMatrix, containing the String representation of each weight.
     *
     * @return a <code>String</code> value
     */
    public String toString() {
        String s = new String();
        s = "Matrix : \n";
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                s += " " + get(i, j) + " ";
            }
            s += "\n";
        }
        s += "Weights                   : \n";
        for (int i = 0; i < getSize(); i++) s += " " + getWeight(i) + " ";
        s = s + "\n";
//        s = s + "Inconsistency Ratio      : " + getInconsistencyRatio() + "\n";
        return s;
    }

}


  

