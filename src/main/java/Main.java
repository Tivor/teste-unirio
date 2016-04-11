import core.Executor;
import org.jgap.InvalidConfigurationException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {
    static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException, InvalidConfigurationException {

        for (int j = 1; j >= 1; j--) {

            for (int i = 0; i < 10; i++) {

                int fileIndex = 4;

                do {
                    String featuresFile = "input/Features" + fileIndex + ".dat";
                    String alternativesFile = "input/Alternatives" + fileIndex + ".dat";
                    Executor executor = new Executor(featuresFile, alternativesFile, j * 1000, false, i + "_" + j);
                    pool.execute(executor);
                    fileIndex++;
                } while (fileIndex <= 4);

            }
        }

        pool.shutdown();
    }
}