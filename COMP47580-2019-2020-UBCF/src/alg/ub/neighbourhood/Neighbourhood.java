/**
 * An abstract class to compute the neighbourhood for all users (user-based CF).
 */

package alg.ub.neighbourhood;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import similarity.SimilarityMap;

public abstract class Neighbourhood 
{
	private Map<Integer,Set<Integer>> neighbourhoodMap; // stores the neighbourhood users for each user in a set
	
	/**
	 * constructor - creates a new Meighbourhood object
	 */
	public Neighbourhood() {
		neighbourhoodMap = new HashMap<Integer,Set<Integer>>();
	}
	
	/**
	 * returns the neighbours for id
	 * @param id - the numeric ID of the user
	 */
	public Set<Integer> getNeighbours(final Integer id) {
		return neighbourhoodMap.get(id);
	}
	
	/**
	 * @returns true if id2 is a neighbour of id1
	 * @param id1 - the numeric ID of the first user
	 * @param id2 - the numeric ID of the second user
	 */
	public boolean isNeighbour(final Integer id1, final Integer id2) {
		if (neighbourhoodMap.containsKey(id1))
			return neighbourhoodMap.get(id1).contains(id2);
		else
			return false;
	}
	
	/**
	 * adds id2 as a neighbour of id1
	 * @param id1 - the numeric ID of the first user
	 * @param id2 - the numeric ID of the second user
	 */
	public void add(final Integer id1, final Integer id2) {
		Set<Integer> set = neighbourhoodMap.containsKey(id1) ? neighbourhoodMap.get(id1) : new HashSet<Integer>();
		set.add(id2);
		neighbourhoodMap.put(id1, set);
	}
	
	/**
	 * stores the neighbourhoods for all users in member neighbourhoodMap - must be called before isNeighbour(Integer,Integer).
	 * @param simMap - a map containing user-user similarities
	 */
	public abstract void computeNeighbourhoods(final SimilarityMap simMap);
}
