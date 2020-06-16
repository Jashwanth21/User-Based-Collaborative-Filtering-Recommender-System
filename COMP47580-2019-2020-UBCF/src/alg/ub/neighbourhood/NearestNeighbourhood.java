/**
 * A class that implements the "nearest neighbourhood" technique (user-based CF).
 */

package alg.ub.neighbourhood;

import similarity.SimilarityMap;
import util.ScoredThingDsc;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import profile.Profile;

public class NearestNeighbourhood extends Neighbourhood 
{
	private final int k; // the number of neighbours in the neighbourhood
	
	/**
	 * constructor - creates a new NearestNeighbourhood object
	 * @param k - the number of neighbours in the neighbourhood
	 */
	public NearestNeighbourhood(final int k)
	{
		super();
		this.k = k;
	}
	
	/**
	 * stores the neighbourhoods for all users in member Neighbour.neighbourhoodMap
	 * @param simMap - a map containing user-user similarities
	 */
	public void computeNeighbourhoods(final SimilarityMap simMap)
	{
		for (Integer userId: simMap.getIds()) { // iterate over all users
			SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); // for the current user, store all similarities in order of descending similarity in a sorted set

			Profile profile = simMap.getSimilarities(userId); // get the user similarity profile
			if (profile != null) {
				for (Integer id: profile.getIds()) { // iterate over each user in the profile
					double sim = profile.getValue(id);
					if (sim > 0)
						ss.add(new ScoredThingDsc(sim, id));
				}
			}
			
			// get the k most similar users (neighbours)
			int counter = 0;
			for (Iterator<ScoredThingDsc> iter = ss.iterator(); iter.hasNext() && counter < k; ) {
				ScoredThingDsc st = iter.next();
				Integer id = (Integer)st.thing;
				this.add(userId, id);
				counter++;
			}
		}	
	}
}
