package core;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Created by Igor on 06/04/2016.
 */
public class CalculateAvg {

    private static class Container {

        public SortedMap<Integer, List<Double>> valuesEuclideanQtdAlternatives = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesSpearmanQtdAlternatives = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesPrecisionQtdAlternatives = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesNDCGQtdAlternatives = new TreeMap();

        public SortedMap<Integer, List<Double>> valuesEuclideanQtdFeatures = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesSpearmanQtdFeatures = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesPrecisionQtdFeatures = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesNDCGQtdFeatures = new TreeMap();

        public SortedMap<Integer, List<Double>> valuesEuclideanQtdCriteria = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesSpearmanQtdCriteria = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesPrecisionQtdCriteria = new TreeMap();
        public SortedMap<Integer, List<Double>> valuesNDCGQtdCriteria = new TreeMap();

        public SortedMap<Double, Double> valuesEuclideanStdDev = new TreeMap();
        public SortedMap<Double, Double> valuesSpearmanStdDev = new TreeMap();
        public SortedMap<Double, Double> valuesPrecisionStdDev = new TreeMap();
        public SortedMap<Double, Double> valuesNDCGStdDev = new TreeMap();

    }


    public static void main(String[] args) throws IOException {

        File[] files = new File("output").listFiles();

        Container c = new Container();

        for (File file : files) {


            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String s = bufferedReader.readLine();

            while (s != null) {

                s = bufferedReader.readLine();

                StringBuffer out = new StringBuffer();

                String[] params = bufferedReader.readLine().split(",");

                int size = Integer.valueOf(params[0]).intValue();
                double[] results = new double[4];

                for (int i = 0; i < size; i++) {
                    String[] resultStr = bufferedReader.readLine().split(",");

                    for (int j = 0; j < 4; j++) {
                        results[j] += Double.valueOf(resultStr[j]).doubleValue();
                    }
                }

                out.append(params[0] + "\t" + params[1] + "\t" + params[2] + "\t");

                for (int j = 0; j < 4; j++) {
                    results[j] = results[j] / size;
                    out.append(String.valueOf(results[j]) + "\t");
                }

                addValue(c.valuesEuclideanQtdAlternatives, params[0], results[0]);
                addValue(c.valuesSpearmanQtdAlternatives, params[0], results[1]);
                addValue(c.valuesPrecisionQtdAlternatives, params[0], results[2]);
                addValue(c.valuesNDCGQtdAlternatives, params[0], results[3]);

                addValue(c.valuesEuclideanQtdFeatures, params[1], results[0]);
                addValue(c.valuesSpearmanQtdFeatures, params[1], results[1]);
                addValue(c.valuesPrecisionQtdFeatures, params[1], results[2]);
                addValue(c.valuesNDCGQtdFeatures, params[1], results[3]);

                addValue(c.valuesEuclideanQtdCriteria, params[2], results[0]);
                addValue(c.valuesSpearmanQtdCriteria, params[2], results[1]);
                addValue(c.valuesPrecisionQtdCriteria, params[2], results[2]);
                addValue(c.valuesNDCGQtdCriteria, params[2], results[3]);

                c.valuesEuclideanStdDev.put(Double.parseDouble(params[3]), results[0]);
                c.valuesSpearmanStdDev.put(Double.parseDouble(params[3]), results[1]);
                c.valuesPrecisionStdDev.put(Double.parseDouble(params[3]), results[2]);
                c.valuesNDCGStdDev.put(Double.parseDouble(params[3]), results[3]);

                out.append(s + "\n");
                Files.write(Paths.get("plot/plot.out"), out.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                s = bufferedReader.readLine();
            }
        }
        printValues(c.valuesEuclideanQtdAlternatives, c.valuesSpearmanQtdAlternatives, c.valuesPrecisionQtdAlternatives, c.valuesNDCGQtdAlternatives, "QtdAlternatives");
        printValues(c.valuesEuclideanQtdFeatures, c.valuesSpearmanQtdFeatures, c.valuesPrecisionQtdFeatures, c.valuesNDCGQtdFeatures, "QtdFeatures");
        printValues(c.valuesEuclideanQtdCriteria, c.valuesSpearmanQtdCriteria, c.valuesPrecisionQtdCriteria, c.valuesNDCGQtdCriteria, "QtdCriteria");

        for (Double key : c.valuesEuclideanStdDev.keySet()) {
            String str = key + "\t"
                    + c.valuesEuclideanStdDev.get(key) + "\t"
                    + c.valuesSpearmanStdDev.get(key) + "\t"
                    + c.valuesPrecisionStdDev.get(key) + "\t"
                    + c.valuesNDCGStdDev.get(key) + "\n";
            Files.write(Paths.get("plot/plotStdDev.out"), str .getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }

    }

    private static void printValues(SortedMap<Integer, List<Double>> valuesEuclidean, SortedMap<Integer, List<Double>> valuesSpearman, SortedMap<Integer, List<Double>> valuesPrecision, SortedMap<Integer, List<Double>> valuesNDCG, String plt) throws IOException {
        for (Integer key : valuesEuclidean.keySet()) {
            List<Double> doubles = valuesEuclidean.get(key);
            Double[] doublesPrimitive = doubles.toArray(new Double[doubles.size()]);
            double[] toPrimitive = ArrayUtils.toPrimitive(doublesPrimitive);
            double mean = new Mean().evaluate(toPrimitive, 0, toPrimitive.length);
            double evaluate = new StandardDeviation().evaluate(toPrimitive, mean);

            String value = key + "\t" + mean + "\t" + evaluate + "\t";

            doubles = valuesSpearman.get(key);
            doublesPrimitive = doubles.toArray(new Double[doubles.size()]);
            toPrimitive = ArrayUtils.toPrimitive(doublesPrimitive);
            mean = new Mean().evaluate(toPrimitive, 0, toPrimitive.length);
            evaluate = new StandardDeviation().evaluate(toPrimitive, mean);

            value += mean + "\t" + evaluate + "\t";

            doubles = valuesPrecision.get(key);
            doublesPrimitive = doubles.toArray(new Double[doubles.size()]);
            toPrimitive = ArrayUtils.toPrimitive(doublesPrimitive);
            mean = new Mean().evaluate(toPrimitive, 0, toPrimitive.length);
            evaluate = new StandardDeviation().evaluate(toPrimitive, mean);

            value += mean + "\t" + evaluate + "\t";

            doubles = valuesNDCG.get(key);
            doublesPrimitive = doubles.toArray(new Double[doubles.size()]);
            toPrimitive = ArrayUtils.toPrimitive(doublesPrimitive);
            mean = new Mean().evaluate(toPrimitive, 0, toPrimitive.length);
            evaluate = new StandardDeviation().evaluate(toPrimitive, mean);

            value += mean + "\t" + evaluate + "\n";

            Files.write(Paths.get("plot/plot" + plt + ".out"), value.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }

    private static void addValue(SortedMap<Integer, List<Double>> values, String param, double result) {
        if (!values.containsKey(new Integer(param))) {
            values.put(new Integer(param), new ArrayList<Double>());
        }
        values.get(new Integer(param)).add(new Double(result));
    }

}
