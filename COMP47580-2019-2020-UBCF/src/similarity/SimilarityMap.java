/**
 * This class computes and stores the pairwise similarities between user (or item) profiles.
 */

package similarity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import profile.Profile;
import similarity.metric.SimilarityMetric;

public class SimilarityMap 
{
	private Map<Integer,Profile> simMap; // stores profile-profile similarities

	/**
	 * constructor - creates a new SimilarityMap object
	 */
	public SimilarityMap()
	{
		simMap = new HashMap<Integer,Profile>();
	}
	
	/**
	 * constructor - creates a new SimilarityMap object
	 * @param profileMap
	 * @param metric
	 */
	public SimilarityMap(final Map<Integer,Profile> profileMap, final SimilarityMetric metric)
	{
		simMap = new HashMap<Integer,Profile>();
		
		// compute pairwise similarities between profiles
		for(Integer id1: profileMap.keySet())
			for(Integer id2: profileMap.keySet())
				if(id2 < id1)
				{
					double sim = metric.getSimilarity(profileMap.get(id1), profileMap.get(id2));
					if(sim > 0)
					{
						setSimilarity(id1, id2, sim);
						setSimilarity(id2, id1, sim);
					}
				}
	}

	/**
	 * @returns the numeric IDs of the profiles
	 */
	public Set<Integer> getIds()
	{
		return simMap.keySet();
	}
	
	/**
	 * @returns the similarity profile
	 * @param the numeric ID of the profile
	 */
	public Profile getSimilarities(Integer id)
	{
		return simMap.get(id);
	}
	
	/**
	 * @returns the similarity between two profiles
	 * @param the numeric ID of the first profile
	 * @param the numeric ID of the second profile
	 */
	public double getSimilarity(final Integer id1, final Integer id2)
	{
		if(simMap.containsKey(id1))
			return (simMap.get(id1).contains(id2) ? simMap.get(id1).getValue(id2).doubleValue() : 0);
		else 
			return 0;
	}

	/**
	 * adds the similarity between two profiles to the map
	 * @param the numeric ID of the first profile
	 * @param the numeric ID of the second profile
	 */
	public void setSimilarity(final Integer id1, final Integer id2, final double sim)
	{
		Profile profile = simMap.containsKey(id1) ? simMap.get(id1) : new Profile(id1);
		profile.addValue(id2, new Double(sim));
		simMap.put(id1, profile);
	}
	
	/**
	 * a string representation of all similarity values
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		
		for(Integer id: simMap.keySet())
			buf.append(simMap.get(id).toString());
	
		return buf.toString();
	}
}