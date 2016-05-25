package metric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by igor.custodio on 02/02/2016.
 */
public class PrecisionAtK {

    private List<LabelValue> ideal = new ArrayList();
    private List<LabelValue> calculated = new ArrayList();

    public double calculate(double[] calculatedRank, double[] idealRank) {

        for (int i = 0; i < idealRank.length; i++) {

            //O label pode ser o nome da alternativa no indice "i"
            ideal.add(new LabelValue("NOME: " + String.valueOf(i + 1), idealRank[i]));
            calculated.add(new LabelValue("NOME: " + String.valueOf(i + 1), calculatedRank[i]));

        }


        Collections.sort(calculated);

        double sum = 0.0d;
        int half = idealRank.length / 2 + ((idealRank.length % 2 == 0) ? 0 : 1);

//        for (int k = 1; k <= half; k++) {
//            double v =
                    return precisionAt(half);
//            sum += v;
//            System.out.println("P@"+ k + " =" + v);
//        }

//        return sum / half;

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
