/**
 * 
 */
package cluster.validationIndices;

import cluster.input.Dataset;

/**
 * @author Markus
 *
 */
public class FowlkesMallowsIndex extends AbstractCountingPairsIndex implements IValidationIndice {
	
	public float calculateIndex(Dataset clusteredData) {
		this.countPairs(clusteredData);
		double fowlkesMallowsIndex =  a / Math.sqrt((a + b) * (a + c));
		return (float) fowlkesMallowsIndex;

	}

}
