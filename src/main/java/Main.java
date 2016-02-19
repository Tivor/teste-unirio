import core.Executor;
import org.jgap.InvalidConfigurationException;

import java.io.IOException;

/**
 * Created by Poliana on 19/12/2015.
 */
public class Main {


    public static void main(String[] args) throws IOException, InvalidConfigurationException {

        boolean miss;
        int fileIndex = 3;

//        do {

            String featuresFile = "input/Features" + fileIndex + ".dat";
            String alternativesFile = "input/Alternatives" + fileIndex + ".dat";

            Executor executor = new Executor();

            miss = executor.execute(featuresFile, alternativesFile);
//            Configuration.reset();
//            fileIndex++;

//        } while (!miss);

    }

}
