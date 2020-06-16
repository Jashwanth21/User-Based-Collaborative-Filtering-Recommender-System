package alg.ub.predictor;

import java.util.Map;
import java.util.Set;

import alg.ub.neighbourhood.Neighbourhood;
import profile.Profile;
import similarity.SimilarityMap;

public class WeightedAveragePredictor implements Predictor {

	@Override
	public Double getPrediction(final Integer userId,final Integer itemId, final Map<Integer, Profile> userProfileMap,final Map<Integer, Profile> itemProfileMap,final Neighbourhood neighbourhood,final SimilarityMap simMap) {
		double num=0;
		double den=0;
		double weight;
		// get the neighbours for the user
				Set<Integer> neighbours = neighbourhood.getNeighbours(userId);
				
				if(neighbours==null) //return null if there are no neighbours 
					return null;

				for(Integer neighbour: neighbours) // iterate over each neighbour
				{
					Double rating = userProfileMap.get(neighbour).getValue(itemId); // get the neighbour's rating for the target item
					if(rating!=null)
					{
						weight = simMap.getSimilarity(userId, neighbour);
						num = num +(rating.doubleValue() * weight);
						den = den + Math.abs(weight);
					}
					
				}
				if(den > 0)
					return new Double(num / den);
				else
					return null;
	}

}
