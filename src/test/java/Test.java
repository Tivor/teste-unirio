import org.apache.commons.math3.ml.distance.EuclideanDistance;

/**
 * Created by Igor on 19/01/2016.
 */
public class Test {

    public static void main(String[] args) {

        String[] vec1 = "0.09988748003778901,0.08666038452260601,0.09418653184274578,0.08495169977561293,0.09965300340236756,0.0759971494839122,0.08390362678005153,0.08258148036417759,0.07901808533742108,0.07871717350884633,0.06955077175871649,0.0648926131857536".split(",");
        String[] vec2 = "0.09464107400421612,0.09075779429712635,0.09020304005325641,0.08987018750693442,0.08953733496061245,0.0854321535559747,0.08465549761455674,0.08299123488294685,0.08021746366359703,0.07910795517585709,0.06712526350826582,0.06546100077665594".split(",");

        double[] val1 = new double[vec1.length];
        double[] val2 = new double[vec1.length];

        for (int i = 0; i < vec1.length; i++) {
            val1[i] = Double.valueOf(vec1[i]).doubleValue();
            val2[i] = Double.valueOf(vec2[i]).doubleValue();
        }

        System.out.println(new EuclideanDistance().compute(val1, val2));

    }


}
