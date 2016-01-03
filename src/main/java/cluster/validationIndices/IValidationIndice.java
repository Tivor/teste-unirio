package cluster.validationIndices;

import cluster.input.Dataset;

/**
 * Created by Poliana on 29/12/2015.
 */
public interface IValidationIndice {

    public float calculateIndex(Dataset clusteredSet);
}
