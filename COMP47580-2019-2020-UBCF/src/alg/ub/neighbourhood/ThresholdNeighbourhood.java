package alg.ub.neighbourhood;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import profile.Profile;
import similarity.SimilarityMap;
import util.ScoredThingDsc;

public class ThresholdNeighbourhood extends Neighbourhood {
	private double threshold;
	public ThresholdNeighbourhood(double threshold)
	{
		super();
		this.threshold=threshold;
	}

	@Override
	public void computeNeighbourhoods(SimilarityMap simMap) 
		{
		for (Integer userId: simMap.getIds()) 
			{ // iterate over all users
			SortedSet<ScoredThingDsc> ss = new TreeSet<ScoredThingDsc>(); // for the current user, store all similarities in order of descending similarity in a sorted set

			Profile profile = simMap.getSimilarities(userId); // get the user similarity profile
			if (profile != null) 
				{
				for (Integer id: profile.getIds()) 
					{ 
						double sim = profile.getValue(id);	
							if (sim > this.threshold)
								ss.add(new ScoredThingDsc(sim, id));
					}
				
				// getting similar users 
				for (Iterator<ScoredThingDsc> iter = ss.iterator(); iter.hasNext(); ) 
				{
					ScoredThingDsc st = iter.next();
					Integer id = (Integer)st.thing;
					this.add(userId, id);
				}
			}
		}
	   }
}
