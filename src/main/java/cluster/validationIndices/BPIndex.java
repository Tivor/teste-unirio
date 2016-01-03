/**
 * 
 */
package cluster.validationIndices;

import cluster.input.Dataset;
import cluster.output.Cluster;
import cluster.util.CalculationUtil;

import java.util.Collection;
import java.util.Map;

/**
 * @author Markus
 *
 */
public class BPIndex {
	
	/**
	 * Takes a clustered Dataset with labels as input and calculates the
	 * Bipartite Index. The Better the clustering the closer this is to 1
	 * @param clusteredDataset
	 * @return
	 */
	public float calculateIndex(Dataset clusteredDataset){
		Map<Integer, Cluster> calculatedClusterMap = clusteredDataset.getClustermap();
		Collection<Cluster> calculatedClusters = calculatedClusterMap.values();
		
		Map<Integer, Cluster> labelMap = clusteredDataset.getLabelMap();
		Collection<Cluster> actualClusters = labelMap.values();

		int i = 0;
		int[][] confusionMatrix = new int[calculatedClusters.size()][actualClusters.size()];
		for (Cluster cc : calculatedClusters) {
			int j =0;
			for (Cluster ac : actualClusters) {
				
				int numOfAgreement = ac.countMutualElements(cc);
				confusionMatrix[i][j] = numOfAgreement;
				j++;
			}
			i++;
		}
		int[] maxAgreement = new int[confusionMatrix.length];
		int sumOfMaxAgreedElements =0;
		for (int k= 0; k< confusionMatrix.length ;k++){
			int[] row = confusionMatrix[k];
			maxAgreement[k] = CalculationUtil.getMaxElement(row);
			sumOfMaxAgreedElements += maxAgreement[k];
		}
		
		float result= (float)sumOfMaxAgreedElements/ clusteredDataset.size();
		assert result >= 0 && result <=1;
		return result;
	}

}
