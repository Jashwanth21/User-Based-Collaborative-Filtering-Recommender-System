/* Class to implement PearsonSigWeightingMetric */
package similarity.metric;

import java.util.Set;

import profile.Profile;

public class PearsonSigWeightingMetric implements SimilarityMetric {
	private int threshold;
	public PearsonSigWeightingMetric(int threshold) {
		super();
		this.threshold = threshold;
	}	
	@Override
	public double getSimilarity(Profile p, Profile p1) 
	{
		SimilarityMetric metric = new PearsonMetric();
		double pearsonSimilarity = metric.getSimilarity(p, p1);
		Set<Integer> commonIds = p.getCommonIds(p1);
		double weight = (commonIds.size()<this.threshold)?(double) commonIds.size()/this.threshold : 1;
		return (pearsonSimilarity * weight);
	}

}
