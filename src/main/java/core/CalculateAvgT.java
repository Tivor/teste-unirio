package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Igor on 06/04/2016.
 */
public class CalculateAvgT {

    public static void main(String[] args) throws IOException {

        File[] files = new File("output").listFiles();

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

                out.append(params[0] + "," + params[1] + "," + params[2] + ",");

                for (int j = 0; j < 4; j++) {
                    results[j] = results[j] / size;
                    out.append(String.valueOf(results[j]) + ",");
                }

                out.append(s + "\n");
                Files.write(Paths.get("plot/plot.out"), out.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                s = bufferedReader.readLine();
            }
        }
    }

}
