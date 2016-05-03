import core.Executor;
import org.jgap.InvalidConfigurationException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {
    public static final int EVOLUTION_RATIO = 1;
    public static final int REPETICOES = 1;
    public static final int PRIMEIRO_INPUT = 51;
    public static final int END = PRIMEIRO_INPUT;
    static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException, InvalidConfigurationException {

        for (int j = EVOLUTION_RATIO; j >= EVOLUTION_RATIO; j--) {

            for (int i = 0; i < REPETICOES; i++) {

                int fileIndex = PRIMEIRO_INPUT;

                do {
                    String featuresFile = "input/Features" + fileIndex + ".dat";
                    String alternativesFile = "input/Alternatives" + fileIndex + ".dat";
                    Executor executor = new Executor(featuresFile, alternativesFile, j * 1000, false, i + "_" + j);
                    pool.execute(executor);
                    fileIndex++;
                } while (fileIndex <= END);

            }
        }

        pool.shutdown();
    }
}