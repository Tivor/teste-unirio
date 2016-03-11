import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.Arrays;

/**
 * Created by Igor on 09/03/2016.
 */
public class TestRegression {


    public static void main(String[] args) {
        SimpleRegression sr = new SimpleRegression();

        double[] vl = {500000d, 1200344d, 2545450d};

        double sum = vl[0] + vl[1] + vl[2];

        System.out.println(Arrays.toString(vl));

        sr.addData(1, (vl[0] / sum) + 1);
        sr.addData(2, (vl[1] / sum) + 1);
        sr.addData(3, (vl[2] / sum) + 1);

        System.out.println(sr.predict(0));
        System.out.println((sr.predict(0) - 1) * sum);


        System.out.println(sr.predict(1));
        System.out.println(sr.predict(2));
        System.out.println(sr.predict(3));

    }




}
