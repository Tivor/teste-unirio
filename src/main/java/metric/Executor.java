package metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by igor.custodio on 02/02/2016.
 */
public class Executor {

    private List<LabelValue> ideal = new ArrayList();
    private List<LabelValue> calculated = new ArrayList();

    public double calculate() {

        double[] idealRank = {9.8d, 8.9d, 7.6d, 6.4d, 5.5d, 4.3d, 3.2d, 2.9d, 2.2d, 1.5d};
        double[] calculatedRank = {9.8d, 0d, 7.6d, 6.4d, 5.5d, 4.3d, 3.2d, 0d, 2.2d, 0d};

        for (int i = 0; i < idealRank.length; i++) {

            //O label pode ser o nome da alternativa no indice "i"
            ideal.add(new LabelValue("NOME: " + String.valueOf(i + 1), idealRank[i]));
            calculated.add(new LabelValue("NOME: " + String.valueOf(i + 1), calculatedRank[i]));

        }


        Collections.sort(calculated);

        double sum = 0.0d;
        for (int k = 1; k <= idealRank.length; k++) {
            double v = precisionAt(k);
            sum += v;
            System.out.println("P@"+ k + " =" + v);
        }

        return sum / idealRank.length;

    }

    private double precisionAt(int k) {

        int count = 0;

        List subList = ideal.subList(0, k);

        for (int i = 0; i < k; i++) {

            LabelValue labelValue = calculated.get(i);
            if (subList.contains(labelValue)) {
                count++;
            }

        }
        return (double)count/k;

    }

}
