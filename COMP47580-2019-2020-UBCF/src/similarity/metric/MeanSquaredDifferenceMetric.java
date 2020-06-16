/*Class to implement MeanSquaredDifference Metric*/
package similarity.metric;

import java.util.Set;

import profile.Profile;

public class MeanSquaredDifferenceMetric implements SimilarityMetric {
	private double minr, maxr;
	public MeanSquaredDifferenceMetric(double minr, double maxr)
	{
		super();
		this.minr = minr;
		this.maxr = maxr;
	}

	@Override
	public double getSimilarity(final Profile p1,final Profile p2) 
	{
		double MSD=0, diff=0;
		Set<Integer> common = p1.getCommonIds(p2);
		{
			for(Integer id: common)
			{
				 diff = p1.getValue(id)-p2.getValue(id);
				 MSD = MSD + Math.pow(diff, 2);
			}
	
		}
		 MSD = MSD/common.size();	
		 diff = this.maxr - this.minr;
		 return 1-(MSD/Math.pow(diff,2));
			
		// TODO Auto-generated method stub
		
	}

}
	
