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
        int fileIndex = 0;
        do {
            String featuresFile = "input/Features" + fileIndex + ".dat";
            String alternativesFile = "input/Alternatives" + fileIndex + ".dat";
            Executor executor = new Executor(featuresFile, alternativesFile);
            pool.execute(executor);
            fileIndex++;
        } while (fileIndex <= 3);
        pool.shutdown();
    }
}